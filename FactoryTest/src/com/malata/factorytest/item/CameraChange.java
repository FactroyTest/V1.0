package com.malata.factorytest.item;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.malata.factorytest.R;



/**
 * @author chehongbin
 *存在bug
 */
public class CameraChange extends AbsHardware   {
	
	private Context context;//上下文
	private Camera mCamera;//申明相机
	private SurfaceHolder holder;
	private Button btnShutter;//快门
	private Button btnChange;//返回，切换前后摄像头
	private TextView mainCameraTextView,sencondCameratTextView,flashCameratTextView;
	private String filepath = "";//照片保存路径
	private int cameraPosition = 1;//0代表前置摄像头，1代表后置摄像头
	private CameraPreview mPreview;
	boolean is_mainCamera = true;
	boolean isTakePicture = true;
	
	@Override
	public void onCreate() {
		super.onCreate();		
		// 检查设备是否支持摄像头
		if(checkCameraHardware(context) == false) {
			Toast.makeText(context, "该设备不支持摄像头功能", Toast.LENGTH_LONG).show();
			return;
		}		
	}
	// 检查设备是否提供摄像头
	private boolean checkCameraHardware(Context context) { 
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){ 
	        // 摄像头存在 
	        return true; 
	    } else { 
	        // 摄像头不存在 
	        return false; 
	    } 
	}		
	
	public CameraChange(String text, Boolean visible) {
		super(text, visible);
	}
	
	@Override
	public TestResult test() {
		return null;
	}

	//uppressWarnings("deprecation")
	@Override
	public View getView(Context context) {
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.camera, null);
		
		// 创建Camera实例 
		mCamera = getCameraInstance(); 
        setCameraParams(mCamera);
        // 创建Preview view并将其设为activity中的内容
        mPreview = new CameraPreview(context, mCamera); 
        FrameLayout preview = (FrameLayout) view.findViewById(R.id.camera_preview); 
        preview.addView(mPreview); 
		//surface = (SurfaceView) view.findViewById(R.id.camera_preview); // 实例化拍照界面组件 
		btnShutter =(Button) view.findViewById(R.id.camera_finish);
		btnChange = (Button) view.findViewById(R.id.camera_change);
		mainCameraTextView = (TextView) view.findViewById(R.id.camera_text);
		sencondCameratTextView = (TextView) view.findViewById(R.id.camera_text1);
		flashCameratTextView = (TextView) view.findViewById(R.id.camera_text2);

		//设置监听
        btnChange.setOnClickListener(listener);
        btnShutter.setOnClickListener(listener);
        Toast.makeText(context, "点击主副按钮，可切换前后摄像头！", Toast.LENGTH_LONG).show();
		return view;
	}
	
	//响应点击事件
    OnClickListener listener = new OnClickListener() {
        @SuppressWarnings("deprecation")
		@SuppressLint("NewApi")
		@Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.camera_change:
                //切换前后摄像头
                int cameraCount = 0;
                CameraInfo cameraInfo = new CameraInfo();
                cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
                for(int i = 0; i < cameraCount; i++) {
                    Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
                    if(cameraPosition == 1) {
                        //现在是后置，变更为前置
                        if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置  
                        	mCamera.stopPreview();//停掉原来摄像头的预览
                            mCamera.release();//释放资源
                            mCamera = null;//取消原来摄像头
                            mCamera = Camera.open(i);//打开当前选中的摄像头
                            try {
                            	setCameraParams(mCamera);
                            	mCamera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            mCamera.startPreview();//开始预览
                            cameraPosition = 0;
                            btnChange.setText("前置");
                            is_mainCamera = false;
                            break;
                        }
                    } else {
                        //现在是前置， 变更为后置
                        if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置  
                        	mCamera.stopPreview();//停掉原来摄像头的预览
                        	mCamera.release();//释放资源
                        	mCamera = null;//取消原来摄像头
                        	mCamera = Camera.open(i);//打开当前选中的摄像头
                            try {
                            	setCameraParams(mCamera);
                            	mCamera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            mCamera.startPreview();//开始预览
                            cameraPosition = 1;
                            btnChange.setText("后置");
                            is_mainCamera = true;
                            break;
                        }
                    }
                }
                break;
             //快门
            case R.id.camera_finish:
            	mCamera.autoFocus(new AutoFocusCallback() {
                	//自动对焦  先对焦后拍照
					@Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if(success && camera != null) {
                			Parameters params = camera.getParameters();
                        	//设置参数，并拍照
                            params.setPictureFormat(PixelFormat.JPEG);//图片格式
                            params.setPreviewSize(800, 480);//图片大小
                            //开闪光
                            params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                            flashCameratTextView.setText("闪光完成");
                            flashCameratTextView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_sucess));
                        		Log.i("Camera", "闪光灯：---------ok");
                            camera.setParameters(params);//将参数设置到我的camera
                        	camera.takePicture(null, null, jpegCallback);//将拍摄到的照片给自定义的对象
                        	if (jpegCallback == null) {
                        		isTakePicture = false;
							}
						}
                     }
                  });
            	//休息一会儿再处理
            	try {
					Thread.sleep(1*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            	 if(cameraPosition == 1) {
            		 if( isTakePicture == true) {
            			mainCameraTextView.setText("后置拍照完成");
                       	mainCameraTextView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_sucess));
                       	Log.i("Camera", "后摄像头---------ok");
            		 } else {
	                	mainCameraTextView.setText("后置拍照失败");
	                    mainCameraTextView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_fail));
	                    Log.i("Camera", "后摄像头---------fail");
            	 	 }
            	 } else if (cameraPosition == 0){
            		 if(isTakePicture == true) {
            			sencondCameratTextView.setText("前置预览完成");
                        sencondCameratTextView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_sucess));
                        Log.i("Camera", "前置摄像头---------ok");
            		 }else {
            			 sencondCameratTextView.setText("前置拍照失败");
                         sencondCameratTextView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_fail));
                         Log.i("Camera", "前置摄像头---------fail");
            		 }
            		
            	 }
                break;
            }
        }
    };

    
    // 设置摄像头参数
    protected void setCameraParams(Camera camera)
    {
    	camera.setDisplayOrientation(90);   
    	Camera.Parameters params = camera.getParameters();
    	params.setRotation(90);
    	camera.setParameters(params);
    }
    
    // 安全获取Camera对象实例的方法 
 	public static Camera getCameraInstance(){ 
 	    Camera c = null; 
 	    try { 
 	        c = Camera.open(); // 试图获取Camera实例
 	    } 
 	    catch (Exception e){ 
 	        // 摄像头不可用（正被占用或不存在）
 	    } 
 	    return c; // 不可用则返回null
 	}
 
    
  //创建jpeg图片回调数据对象
	PictureCallback jpegCallback = new PictureCallback() {
        @SuppressLint({ "SdCardPath", "SimpleDateFormat" })
		@Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                //自定义文件保存路径  以拍摄时间区分命名
                filepath = "/sdcard/MyPictures/"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".jpg";
                File file = new File(filepath);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩的流里面
                bos.flush();// 刷新此缓冲区的输出流
                bos.close();// 关闭此输出流并释放与此流有关的所有系统资源
                camera.stopPreview();//关闭预览 处理数据
                camera.startPreview();//数据处理完后继续开始预览
                bitmap.recycle();//回收bitmap空间
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    
	// 基本的摄像头预览类
	public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback { 
	    private Camera mCamera; 
	    public CameraPreview(Context context, Camera camera) { 
	        super(context); 
	        mCamera = camera; 
	        /// 基本的摄像头预览
			// 安装一个SurfaceHolder.Callback，
	        // 这样创建和销毁底层surface时能够获得通知
			holder= getHolder();//获得句柄
			holder.addCallback(this);//添加回调
			//surfaceview不维护自己的缓冲区，等待屏幕渲染引擎将内容推送到用户面前
			holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    } 

	    @Override
	    public void surfaceChanged(SurfaceHolder holder, 
	    		int format, int width, int height) {
	        if (holder.getSurface() == null){ 
	          // 预览surface不存在
	          return; 
	        } 
	       if(mCamera !=null ) {
	        	try {		
	        		
	        		mCamera.stopPreview();
	        		mCamera.setPreviewDisplay(holder); 
		            mCamera.setDisplayOrientation(90);
	        		// 打开预览画面   
	                mCamera.startPreview(); 
				} catch (Exception e) {
					 e.printStackTrace();  
				}
	       }

	    }
		
	    @Override
	    public void surfaceCreated(SurfaceHolder holder) {
	    	//当surfaceview创建时开启相机
	        if(mCamera == null) {
	        	mCamera = Camera.open();
	            try {
	            	//通过surfaceview显示取景画面
	            	mCamera.setPreviewDisplay(holder);
	            	//开始预览
	            	mCamera.startPreview();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
			
	  @Override
	public void surfaceDestroyed(SurfaceHolder holder) {
        //在activity中释放资源
	}
  
	    public void setCamera(Camera camera) {
	    	try {
	    		mCamera = camera;
	    		camera.setPreviewDisplay(holder);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}
	
	// 为其它应用释放摄像头
	private void releaseCamera() { 
        if (mCamera != null) { 
            mCamera.release();        
            mCamera = null; 
        } 
    } 
	
	@Override
	public void onDestory() {
		super.onDestory();
    	mCamera.setPreviewCallback(null);
    	mCamera.stopPreview();
    	releaseCamera();
    	mCamera = null;
    	Log.i("CameraPreview", "sufaceDestroyed---------camera release");
	}
	
}

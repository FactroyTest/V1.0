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
 *����bug
 */
public class CameraChange extends AbsHardware   {
	
	private Context context;//������
	private Camera mCamera;//�������
	private SurfaceHolder holder;
	private Button btnShutter;//����
	private Button btnChange;//���أ��л�ǰ������ͷ
	private TextView mainCameraTextView,sencondCameratTextView,flashCameratTextView;
	private String filepath = "";//��Ƭ����·��
	private int cameraPosition = 1;//0����ǰ������ͷ��1�����������ͷ
	private CameraPreview mPreview;
	boolean is_mainCamera = true;
	boolean isTakePicture = true;
	
	@Override
	public void onCreate() {
		super.onCreate();		
		// ����豸�Ƿ�֧������ͷ
		if(checkCameraHardware(context) == false) {
			Toast.makeText(context, "���豸��֧������ͷ����", Toast.LENGTH_LONG).show();
			return;
		}		
	}
	// ����豸�Ƿ��ṩ����ͷ
	private boolean checkCameraHardware(Context context) { 
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){ 
	        // ����ͷ���� 
	        return true; 
	    } else { 
	        // ����ͷ������ 
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
		
		// ����Cameraʵ�� 
		mCamera = getCameraInstance(); 
        setCameraParams(mCamera);
        // ����Preview view��������Ϊactivity�е�����
        mPreview = new CameraPreview(context, mCamera); 
        FrameLayout preview = (FrameLayout) view.findViewById(R.id.camera_preview); 
        preview.addView(mPreview); 
		//surface = (SurfaceView) view.findViewById(R.id.camera_preview); // ʵ�������ս������ 
		btnShutter =(Button) view.findViewById(R.id.camera_finish);
		btnChange = (Button) view.findViewById(R.id.camera_change);
		mainCameraTextView = (TextView) view.findViewById(R.id.camera_text);
		sencondCameratTextView = (TextView) view.findViewById(R.id.camera_text1);
		flashCameratTextView = (TextView) view.findViewById(R.id.camera_text2);

		//���ü���
        btnChange.setOnClickListener(listener);
        btnShutter.setOnClickListener(listener);
        Toast.makeText(context, "���������ť�����л�ǰ������ͷ��", Toast.LENGTH_LONG).show();
		return view;
	}
	
	//��Ӧ����¼�
    OnClickListener listener = new OnClickListener() {
        @SuppressWarnings("deprecation")
		@SuppressLint("NewApi")
		@Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.camera_change:
                //�л�ǰ������ͷ
                int cameraCount = 0;
                CameraInfo cameraInfo = new CameraInfo();
                cameraCount = Camera.getNumberOfCameras();//�õ�����ͷ�ĸ���
                for(int i = 0; i < cameraCount; i++) {
                    Camera.getCameraInfo(i, cameraInfo);//�õ�ÿһ������ͷ����Ϣ
                    if(cameraPosition == 1) {
                        //�����Ǻ��ã����Ϊǰ��
                        if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_FRONT) {//��������ͷ�ķ�λ��CAMERA_FACING_FRONTǰ��      CAMERA_FACING_BACK����  
                        	mCamera.stopPreview();//ͣ��ԭ������ͷ��Ԥ��
                            mCamera.release();//�ͷ���Դ
                            mCamera = null;//ȡ��ԭ������ͷ
                            mCamera = Camera.open(i);//�򿪵�ǰѡ�е�����ͷ
                            try {
                            	setCameraParams(mCamera);
                            	mCamera.setPreviewDisplay(holder);//ͨ��surfaceview��ʾȡ������
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            mCamera.startPreview();//��ʼԤ��
                            cameraPosition = 0;
                            btnChange.setText("ǰ��");
                            is_mainCamera = false;
                            break;
                        }
                    } else {
                        //������ǰ�ã� ���Ϊ����
                        if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_BACK) {//��������ͷ�ķ�λ��CAMERA_FACING_FRONTǰ��      CAMERA_FACING_BACK����  
                        	mCamera.stopPreview();//ͣ��ԭ������ͷ��Ԥ��
                        	mCamera.release();//�ͷ���Դ
                        	mCamera = null;//ȡ��ԭ������ͷ
                        	mCamera = Camera.open(i);//�򿪵�ǰѡ�е�����ͷ
                            try {
                            	setCameraParams(mCamera);
                            	mCamera.setPreviewDisplay(holder);//ͨ��surfaceview��ʾȡ������
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            mCamera.startPreview();//��ʼԤ��
                            cameraPosition = 1;
                            btnChange.setText("����");
                            is_mainCamera = true;
                            break;
                        }
                    }
                }
                break;
             //����
            case R.id.camera_finish:
            	mCamera.autoFocus(new AutoFocusCallback() {
                	//�Զ��Խ�  �ȶԽ�������
					@Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if(success && camera != null) {
                			Parameters params = camera.getParameters();
                        	//���ò�����������
                            params.setPictureFormat(PixelFormat.JPEG);//ͼƬ��ʽ
                            params.setPreviewSize(800, 480);//ͼƬ��С
                            //������
                            params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                            flashCameratTextView.setText("�������");
                            flashCameratTextView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_sucess));
                        		Log.i("Camera", "����ƣ�---------ok");
                            camera.setParameters(params);//���������õ��ҵ�camera
                        	camera.takePicture(null, null, jpegCallback);//�����㵽����Ƭ���Զ���Ķ���
                        	if (jpegCallback == null) {
                        		isTakePicture = false;
							}
						}
                     }
                  });
            	//��Ϣһ����ٴ���
            	try {
					Thread.sleep(1*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            	 if(cameraPosition == 1) {
            		 if( isTakePicture == true) {
            			mainCameraTextView.setText("�����������");
                       	mainCameraTextView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_sucess));
                       	Log.i("Camera", "������ͷ---------ok");
            		 } else {
	                	mainCameraTextView.setText("��������ʧ��");
	                    mainCameraTextView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_fail));
	                    Log.i("Camera", "������ͷ---------fail");
            	 	 }
            	 } else if (cameraPosition == 0){
            		 if(isTakePicture == true) {
            			sencondCameratTextView.setText("ǰ��Ԥ�����");
                        sencondCameratTextView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_sucess));
                        Log.i("Camera", "ǰ������ͷ---------ok");
            		 }else {
            			 sencondCameratTextView.setText("ǰ������ʧ��");
                         sencondCameratTextView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_fail));
                         Log.i("Camera", "ǰ������ͷ---------fail");
            		 }
            		
            	 }
                break;
            }
        }
    };

    
    // ��������ͷ����
    protected void setCameraParams(Camera camera)
    {
    	camera.setDisplayOrientation(90);   
    	Camera.Parameters params = camera.getParameters();
    	params.setRotation(90);
    	camera.setParameters(params);
    }
    
    // ��ȫ��ȡCamera����ʵ���ķ��� 
 	public static Camera getCameraInstance(){ 
 	    Camera c = null; 
 	    try { 
 	        c = Camera.open(); // ��ͼ��ȡCameraʵ��
 	    } 
 	    catch (Exception e){ 
 	        // ����ͷ�����ã�����ռ�û򲻴��ڣ�
 	    } 
 	    return c; // �������򷵻�null
 	}
 
    
  //����jpegͼƬ�ص����ݶ���
	PictureCallback jpegCallback = new PictureCallback() {
        @SuppressLint({ "SdCardPath", "SimpleDateFormat" })
		@Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                //�Զ����ļ�����·��  ������ʱ����������
                filepath = "/sdcard/MyPictures/"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".jpg";
                File file = new File(filepath);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//��ͼƬѹ����������
                bos.flush();// ˢ�´˻������������
                bos.close();// �رմ���������ͷ�������йص�����ϵͳ��Դ
                camera.stopPreview();//�ر�Ԥ�� ��������
                camera.startPreview();//���ݴ�����������ʼԤ��
                bitmap.recycle();//����bitmap�ռ�
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    
	// ����������ͷԤ����
	public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback { 
	    private Camera mCamera; 
	    public CameraPreview(Context context, Camera camera) { 
	        super(context); 
	        mCamera = camera; 
	        /// ����������ͷԤ��
			// ��װһ��SurfaceHolder.Callback��
	        // �������������ٵײ�surfaceʱ�ܹ����֪ͨ
			holder= getHolder();//��þ��
			holder.addCallback(this);//��ӻص�
			//surfaceview��ά���Լ��Ļ��������ȴ���Ļ��Ⱦ���潫�������͵��û���ǰ
			holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    } 

	    @Override
	    public void surfaceChanged(SurfaceHolder holder, 
	    		int format, int width, int height) {
	        if (holder.getSurface() == null){ 
	          // Ԥ��surface������
	          return; 
	        } 
	       if(mCamera !=null ) {
	        	try {		
	        		
	        		mCamera.stopPreview();
	        		mCamera.setPreviewDisplay(holder); 
		            mCamera.setDisplayOrientation(90);
	        		// ��Ԥ������   
	                mCamera.startPreview(); 
				} catch (Exception e) {
					 e.printStackTrace();  
				}
	       }

	    }
		
	    @Override
	    public void surfaceCreated(SurfaceHolder holder) {
	    	//��surfaceview����ʱ�������
	        if(mCamera == null) {
	        	mCamera = Camera.open();
	            try {
	            	//ͨ��surfaceview��ʾȡ������
	            	mCamera.setPreviewDisplay(holder);
	            	//��ʼԤ��
	            	mCamera.startPreview();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
			
	  @Override
	public void surfaceDestroyed(SurfaceHolder holder) {
        //��activity���ͷ���Դ
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
	
	// Ϊ����Ӧ���ͷ�����ͷ
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

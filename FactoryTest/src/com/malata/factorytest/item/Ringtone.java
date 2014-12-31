package com.malata.factorytest.item;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.malata.factorytest.R;

public class Ringtone  extends AbsHardware{
	private Context context;
	Button MIChead,mainButton,secondButton;
	MediaRecorder recorder;//录音
	MediaPlayer   media;//音频
	AudioManager audioManager;
	File file;
	boolean isrecorder=false;
	boolean ismainMic = true;
	private boolean isHeadsetConnect;
	//private MyThread myThread =new MyThread();
	
	public Ringtone(String text, Boolean visible) {
		super(text, visible);
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		isHeadsetConnect = audioManager.isWiredHeadsetOn();
		if(isHeadsetConnect){
			Toast.makeText(context.getApplicationContext(), "请拔出耳机进行测试！", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public View getView(Context context) {
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.ringtone, null);
		
		 MIChead=(Button) view.findViewById(R.id.Mic);
		 mainButton = (Button) view.findViewById(R.id.button_main);
		 secondButton = (Button) view.findViewById(R.id.button_second);
		//设置监听
		 mainButton.setOnClickListener(listener);
		 secondButton.setOnClickListener(listener);
		 MIChead.setText("主MIC测试/请说话");
		 Toast.makeText(context, "请点击测试按钮，开始测试！", Toast.LENGTH_LONG).show();
		 MIChead.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//主麦
					if(MIChead.getText().toString().equals("主MIC测试/请说话")&& ismainMic)	{
						
						startRecord();
						return;
					}
					//副麦
					if (MIChead.getText().toString().equals("副MIC测试/请说话")&&!ismainMic) {
						
						startRecord();
						return;
					}
					if (MIChead.getText().toString().equals("停止说话并播放"))
					{
						stopRecord();
						return ;
					}
					if(MIChead.getText().toString().equals("停止播放")) {	
						stopPlay();
						return ;
					}
				}
			});
		 
		return view;
	}
	
	//响应点击事件 主副麦克风
    OnClickListener listener = new OnClickListener() {
		@Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.button_main:
            	setMic();
            	break;
            case R.id.button_second:
            	setMic();
            	break;
            }
        }
    };
	
	@Override
	public TestResult test() {
		return null;
	}
	
	@Override
	public void onDestory() {
		
		super.onDestory();
		if (media!=null) {
			media.stop();
		}
		//myThread.stop();
		
		
		
	}

	@SuppressWarnings("deprecation")
	public void setMic(){
		if(ismainMic) {
    		mainButton.setText("关闭");
    		mainButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_fail));
    		secondButton.setText("打开");
    		secondButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_sucess));
    		ismainMic = false;
    		MIChead.setText("副MIC测试/请说话");
    	}else {
    		mainButton.setText("打开");
    		mainButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_sucess));
    		secondButton.setText("关闭");
    		secondButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_fail));
    		ismainMic = true;
    		MIChead.setText("主MIC测试/请说话");
		}
	}
	
	//开始录音
	@SuppressWarnings("deprecation")
	void startRecord() {
		Log.d("是否进入录音", "进入");
		try {
			if(recorder==null) {
				//实例化个MediaRecorder对象
				recorder=new MediaRecorder();
			}
			
			//指定AudioSource 为MIC(Microphone audio source ),这是最长用的 
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC); 
			//myThread.run();
			//指定OutputFormat,我们选择3gp格式  
            //THREE_GPP:录制后文件是一个3gp文件，支持音频和视频录制  
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			//指定Audio编码方式，目前只有AMR_NB格式 
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			//保存录音文件	 
			file=new File(Environment.getExternalStorageDirectory()+"/luyin.mp3");
			if(file.exists()) {
				file.delete();
			}
			recorder.setOutputFile(Environment.getExternalStorageDirectory()+"/luyin.mp3");
			//开始录制
			recorder.prepare();
			recorder.start();
			MIChead.setText("停止说话并播放");
			setDisabled();
			 if (setDisabled()) {
				 mainButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_disable));
				 secondButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_disable));
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//停止录制 开始播放
	void stopRecord() {
		recorder.stop();
		Toast.makeText(context, "请从听筒听声！", Toast.LENGTH_LONG).show();
		try {
			Thread.sleep(1*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized (file) {
			if(media==null) {
				media=new MediaPlayer();
			}
			// 把模式调成听筒放音模式
			audioManager.setMode(AudioManager.MODE_IN_CALL);
			try {
				//开始循环播放
				media.setLooping(true);
				media.setDataSource(Environment.getExternalStorageDirectory()+"/luyin.mp3");
				media.prepare();
				media.start();
				MIChead.setText("停止播放");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//停止播放
	@SuppressWarnings("deprecation")
	void stopPlay() {
		//停止播放
		media.stop();
		media.reset();
		setEnabled();
		if (ismainMic) {
			MIChead.setText("主MIC测试/请说话");
			if (setEnabled()) {
				 mainButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_sucess));
				 secondButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_fail));
			}
		}else {
			MIChead.setText("副MIC测试/请说话");
			if (setEnabled()) {
				 mainButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_fail));
				 secondButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_sucess));
			}
		}
	}
	 boolean setEnabled() {
			 mainButton.setEnabled(true);
		     secondButton.setEnabled(true);
			 return true;
	 }
	 
	 boolean setDisabled() {
			 mainButton.setEnabled(false);
		     secondButton.setEnabled(false);
			 return true;
	 }
	 
/*	 class MyThread extends Thread {
		  @SuppressLint("NewApi")
		public void run() {	    
		    try {
		      Thread.sleep(1*1000L);	     
		    }
		    catch (InterruptedException localInterruptedException) {
		    	localInterruptedException.printStackTrace();
		    }
		    
		    if(true){
		        if (ismainMic){
		        	//1，2，3分别代码main mic，耳机mic，sub mic(dual mic项目才有效)	        	
		        	Ringtone.this.audioManager.setParameters("SET_LOOPBACK_TYPE=1");
		        	Log.d("主麦测试", "现在是主麦");
		        } else if (!ismainMic){
		        	//1，2，3分别代码main mic，耳机mic，sub mic(dual mic项目才有效)	        	
		        	Ringtone.this.audioManager.setParameters("SET_LOOPBACK_TYPE=3");
		        	Log.d("副麦测试", "现在是副麦");
		        }
		      }
		      return;
		  }
	}*/
}




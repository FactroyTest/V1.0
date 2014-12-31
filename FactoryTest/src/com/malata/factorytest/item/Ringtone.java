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
	MediaRecorder recorder;//¼��
	MediaPlayer   media;//��Ƶ
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
			Toast.makeText(context.getApplicationContext(), "��γ��������в��ԣ�", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public View getView(Context context) {
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.ringtone, null);
		
		 MIChead=(Button) view.findViewById(R.id.Mic);
		 mainButton = (Button) view.findViewById(R.id.button_main);
		 secondButton = (Button) view.findViewById(R.id.button_second);
		//���ü���
		 mainButton.setOnClickListener(listener);
		 secondButton.setOnClickListener(listener);
		 MIChead.setText("��MIC����/��˵��");
		 Toast.makeText(context, "�������԰�ť����ʼ���ԣ�", Toast.LENGTH_LONG).show();
		 MIChead.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//����
					if(MIChead.getText().toString().equals("��MIC����/��˵��")&& ismainMic)	{
						
						startRecord();
						return;
					}
					//����
					if (MIChead.getText().toString().equals("��MIC����/��˵��")&&!ismainMic) {
						
						startRecord();
						return;
					}
					if (MIChead.getText().toString().equals("ֹͣ˵��������"))
					{
						stopRecord();
						return ;
					}
					if(MIChead.getText().toString().equals("ֹͣ����")) {	
						stopPlay();
						return ;
					}
				}
			});
		 
		return view;
	}
	
	//��Ӧ����¼� ������˷�
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
    		mainButton.setText("�ر�");
    		mainButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_fail));
    		secondButton.setText("��");
    		secondButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_sucess));
    		ismainMic = false;
    		MIChead.setText("��MIC����/��˵��");
    	}else {
    		mainButton.setText("��");
    		mainButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_sucess));
    		secondButton.setText("�ر�");
    		secondButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_fail));
    		ismainMic = true;
    		MIChead.setText("��MIC����/��˵��");
		}
	}
	
	//��ʼ¼��
	@SuppressWarnings("deprecation")
	void startRecord() {
		Log.d("�Ƿ����¼��", "����");
		try {
			if(recorder==null) {
				//ʵ������MediaRecorder����
				recorder=new MediaRecorder();
			}
			
			//ָ��AudioSource ΪMIC(Microphone audio source ),������õ� 
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC); 
			//myThread.run();
			//ָ��OutputFormat,����ѡ��3gp��ʽ  
            //THREE_GPP:¼�ƺ��ļ���һ��3gp�ļ���֧����Ƶ����Ƶ¼��  
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			//ָ��Audio���뷽ʽ��Ŀǰֻ��AMR_NB��ʽ 
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			//����¼���ļ�	 
			file=new File(Environment.getExternalStorageDirectory()+"/luyin.mp3");
			if(file.exists()) {
				file.delete();
			}
			recorder.setOutputFile(Environment.getExternalStorageDirectory()+"/luyin.mp3");
			//��ʼ¼��
			recorder.prepare();
			recorder.start();
			MIChead.setText("ֹͣ˵��������");
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
	
	//ֹͣ¼�� ��ʼ����
	void stopRecord() {
		recorder.stop();
		Toast.makeText(context, "�����Ͳ������", Toast.LENGTH_LONG).show();
		try {
			Thread.sleep(1*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized (file) {
			if(media==null) {
				media=new MediaPlayer();
			}
			// ��ģʽ������Ͳ����ģʽ
			audioManager.setMode(AudioManager.MODE_IN_CALL);
			try {
				//��ʼѭ������
				media.setLooping(true);
				media.setDataSource(Environment.getExternalStorageDirectory()+"/luyin.mp3");
				media.prepare();
				media.start();
				MIChead.setText("ֹͣ����");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//ֹͣ����
	@SuppressWarnings("deprecation")
	void stopPlay() {
		//ֹͣ����
		media.stop();
		media.reset();
		setEnabled();
		if (ismainMic) {
			MIChead.setText("��MIC����/��˵��");
			if (setEnabled()) {
				 mainButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_sucess));
				 secondButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_fail));
			}
		}else {
			MIChead.setText("��MIC����/��˵��");
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
		        	//1��2��3�ֱ����main mic������mic��sub mic(dual mic��Ŀ����Ч)	        	
		        	Ringtone.this.audioManager.setParameters("SET_LOOPBACK_TYPE=1");
		        	Log.d("�������", "����������");
		        } else if (!ismainMic){
		        	//1��2��3�ֱ����main mic������mic��sub mic(dual mic��Ŀ����Ч)	        	
		        	Ringtone.this.audioManager.setParameters("SET_LOOPBACK_TYPE=3");
		        	Log.d("�������", "�����Ǹ���");
		        }
		      }
		      return;
		  }
	}*/
}




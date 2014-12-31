package com.malata.factorytest.item;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.malata.factorytest.R;

public class Headset extends AbsHardware {
	
	private Context context;
	
	private Button headsetSpeak;
	private TextView headsetstae,headsetleft,headsetstop,headsetright;
	 boolean  isRecorder=false;
	 MediaRecorder recorder;
	 MediaPlayer media;
	 private AudioRecord audioRecord;
	 private AudioTrack audioTrack;
	 boolean isNormal = false;
	 AudioManager  audiomanager;
	 
	 @Override
	public void onCreate() {
		super.onCreate();
		//����������,������
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
		context.registerReceiver(headSetReceiver, ifilter);
		Intent headStatus = context.registerReceiver(headSetReceiver, ifilter);
	}
	 
	

	public Headset(String text, Boolean visible) {
		super(text, visible);
	}

	@Override
	public TestResult test() {
		return null;
	}

	@Override
	public View getView(Context context) {
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.headset, null);
	     headsetSpeak = (Button) view.findViewById(R.id.headset_speak);
	     headsetstae = (TextView) view.findViewById(R.id.headset_stae);
	     headsetleft = (TextView) view.findViewById(R.id.left_headset);
	     headsetstop = (TextView) view.findViewById(R.id.stop_headset);
	     headsetright = (TextView) view.findViewById(R.id.right_headset);
	     
	     
	     
	     headsetSpeak.setOnClickListener(listener);
	     
	     headsetSpeak.setEnabled(false);
		 return view;
	}
	
	OnClickListener listener = new OnClickListener() {
		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
            case R.id.headset_speak:
            	if (!isNormal) {
            		headsetSpeak.setText("��������");
            		headsetSpeak.setBackgroundDrawable(v.getResources().getDrawable(R.drawable.btn_fm_sucess));
            		isNormal = true;
				}else {
					headsetSpeak.setText("��������");
            		headsetSpeak.setBackgroundDrawable(v.getResources().getDrawable(R.drawable.btn_fm_fail));
            		isNormal =false;
				}
            	break;
            	
          
            }
		}
	};
	

	
	
	//��ȡ����
	@SuppressWarnings("deprecation")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		//�����ϼ�
		case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
			Log.v("headset DOWN", "DOWN OK!");
			headsetleft.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.left_press));
			headsetright.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.right_up));
			headsetstop.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.stop_up));
			break;
		//�����������м䰴��
		case KeyEvent.KEYCODE_MEDIA_PAUSE:
		case KeyEvent.KEYCODE_HEADSETHOOK:
			headsetleft.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.left_up));
			headsetstop.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.stop_press));
			headsetright.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.right_up));
			break;
		//�������������ǵ��¼�	
		case KeyEvent.KEYCODE_MEDIA_NEXT:
			headsetleft.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.left_up));
			headsetright.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.right_press));
			headsetstop.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.stop_up));
			break;
		default:
			break;
		}
		return false;
	}
	
	
	//ע���ڲ� ������
	BroadcastReceiver headSetReceiver = new BroadcastReceiver() {
		@SuppressWarnings("deprecation")
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("Headset","into headsetreceiver!");  
             String action = intent.getAction();  
             if (action.equals(Intent.ACTION_HEADSET_PLUG)) {  
                 if(intent.getIntExtra("state", 0) == 1){  
	                  Log.d("Headset","this is headphone plugged");  
	                  headsetstae.setText("������");
	                  headsetstae.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_sucess));
	                  Toast.makeText(context, "�뿪ʼ��⣬���Զ���˵����", Toast.LENGTH_LONG).show();
	                  RecordThread recordThread = new RecordThread();
	                  recordThread.start();
	                  headsetSpeak.setEnabled(true);
                 }else{  
	                  Log.d("Headset","this is headphone unplugged");  
	                  headsetstae.setText("δ����"); 
	                  headsetstae.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_fail));
	                  Toast.makeText(context, "�����������в��ԣ�", Toast.LENGTH_LONG).show();
	                  headsetSpeak.setText("�Ƿ�����");
	                  headsetSpeak.setEnabled(false);
	                  headsetSpeak.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_fail));
                 }  
             }
		}
	};

	
	
	//ֱ�Ӳ�����˷��ռ���������
    class RecordThread extends Thread{
    	static final int frequency = 44100;
    	@SuppressWarnings("deprecation")
		static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    	static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
		@Override
		public void run() {
			int recBufSize = AudioRecord.getMinBufferSize(frequency,
					channelConfiguration, audioEncoding)*2;
			int plyBufSize = AudioTrack.getMinBufferSize(frequency,
					channelConfiguration, audioEncoding)*2;
			
			 audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
					channelConfiguration, audioEncoding, recBufSize);

			 audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency,
					channelConfiguration, audioEncoding, plyBufSize, AudioTrack.MODE_STREAM);
			
			byte[] recBuf = new byte[recBufSize];
			audioRecord.startRecording();
			audioTrack.play();
			while(true){
				int readLen = audioRecord.read(recBuf, 0, recBufSize);
				audioTrack.write(recBuf, 0, readLen);
			}
			
		}
    }
	
	@Override
	public void onDestory() {
		super.onDestory();
		audioTrack.stop();
		audioRecord.stop();
		//�˳�������ע��
		context.unregisterReceiver(headSetReceiver);
		
	}
}

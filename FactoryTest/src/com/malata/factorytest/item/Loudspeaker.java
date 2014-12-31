package com.malata.factorytest.item;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.malata.factorytest.R;

public class Loudspeaker extends AbsHardware {
	private Context context;
	private Button loudButton,earbButton,loudButton2;
	private TextView headsetstade;
	AudioManager audioManager;
	MediaPlayer mMediaPlayer=null;
	boolean isloudspeaker = false; 
	boolean isearspeaker = false;
	boolean isNormal = false;
	boolean isheadsetNormal = false;
	boolean isLoudNormal = false;
	
	@Override
	public void onCreate() {
		
		super.onCreate();
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
		context.registerReceiver(headSetReceiver, ifilter);
		Intent headstatus = context.registerReceiver(headSetReceiver, ifilter);
	}

	public Loudspeaker(String text, Boolean visible) {
		super(text, visible);
	}

	@Override
	public TestResult test() {
		return null;
	}

	@Override
	public View getView(Context context) {
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.loudspeaker, null);
		
		loudButton = (Button) view.findViewById(R.id.loudbutton);
		earbButton = (Button) view.findViewById(R.id.earbutton);
		loudButton2 = (Button) view.findViewById(R.id.button_loudtip);
		headsetstade = (TextView) view.findViewById(R.id.headset_states);
		
		//外放正常事件
		 loudButton.setOnClickListener(new View.OnClickListener() {
				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View v) {
					//喇叭播放
					audioManager.setMode(AudioManager.MODE_NORMAL); 
					isloudspeaker = true;
					if (!isNormal) {
						loudButton.setText("外放正常");
						loudButton.setBackgroundDrawable(v.getResources().getDrawable(R.drawable.btn_fm_sucess));
						isNormal = true;
					}else {
						loudButton.setText("外放异常");
						loudButton.setBackgroundDrawable(v.getResources().getDrawable(R.drawable.btn_fm_fail));
						isNormal = false;
					}
				}
		});
		 //耳机事件
		 earbButton.setOnClickListener(new View.OnClickListener() {
				@SuppressWarnings("deprecation")
				@Override
				public void onClick(View v) {
					isearspeaker = true ;
					isloudspeaker = false;
					if (!isheadsetNormal) {
						earbButton.setText("耳机正常");
						earbButton.setBackgroundDrawable(v.getResources().getDrawable(R.drawable.btn_fm_sucess));
						isheadsetNormal = true;
					}else {
						earbButton.setText("耳机异常");
						earbButton.setBackgroundDrawable(v.getResources().getDrawable(R.drawable.btn_fm_fail));
						isheadsetNormal = false;
					}
					
				}
			});
		 
		 loudButton2.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				isloudspeaker = false;
				isearspeaker = true;
				if (!isLoudNormal) {
					loudButton2.setText("外放无声");
					loudButton2.setBackgroundDrawable(v.getResources().getDrawable(R.drawable.btn_fm_sucess));
					isLoudNormal = true;
				}else {
					loudButton2.setText("外放有声");
					loudButton2.setBackgroundDrawable(v.getResources().getDrawable(R.drawable.btn_fm_fail));
					isLoudNormal = false;
				}
				
			}
		});
		playmusic();
		 
		return view;
	}
	//读取文件
	public void writeRFile(int RID,String filename) {
		   try {
			   InputStream is = context.getResources().openRawResource(RID);
			   FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() +"/"+filename); 
			   byte[] buffer = new byte[8192];
			   int count = 0;
			   while((count=is.read(buffer)) > 0) {
				   fos.write(buffer, 0, count); 
			   }
			   fos.close();
			   is.close(); 
			  } 
		   catch(Exception e) {
			   e.printStackTrace(); } 
	}
	
	//播放歌曲
	public void playmusic() {
		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		// 把模式调成喇叭放音模式
		 audioManager.setMode(AudioManager.MODE_NORMAL);
		 //这里的资源文件要求要小点的文件，要注意
		try {
			 mMediaPlayer = MediaPlayer.create(context, R.raw.fukua);
			 mMediaPlayer.setLooping(true); 
			 mMediaPlayer.start();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} 
	}
	
	//注册内部 接收器
		BroadcastReceiver headSetReceiver = new BroadcastReceiver() {
			@SuppressLint("ShowToast")
			@SuppressWarnings("deprecation")
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.d("Headset","into headsetreceiver!");  
	             String action = intent.getAction();  
	             if (action.equals(Intent.ACTION_HEADSET_PLUG)) {  
	                 if(intent.getIntExtra("state", 0) == 1){  
	                  Log.d("Headset","this is headphone plugged");  
	                  headsetstade.setText("已连接");
	                  headsetstade.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_sucess));
	                  earbButton.setEnabled(true);
	                  loudButton2.setEnabled(true);
	                  loudButton.setEnabled(false);
	                  Toast.makeText(context, "请点击按钮，判断是否正常与无声！", Toast.LENGTH_LONG).show();
	                 }else{  
	                  Log.d("Headset","this is headphone unplugged");  
	                  headsetstade.setText("未连接"); 
	                  Toast.makeText(context, "外放正常时，请插入耳机进行测试！", 2000).show();
	                  Toast.makeText(context, "请点击按钮，判断外放是否正常！", Toast.LENGTH_LONG).show();
	                  headsetstade.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_fm_fail));
	                  earbButton.setEnabled(false);
	                  loudButton2.setEnabled(false);
	                  loudButton.setEnabled(true);
	                 }  
	             }
			}
		};
	
	@Override
	public void onDestory() {
		mMediaPlayer.stop();
		super.onDestory();
		//退出时候销毁注册
		context.unregisterReceiver(headSetReceiver);
	}
}

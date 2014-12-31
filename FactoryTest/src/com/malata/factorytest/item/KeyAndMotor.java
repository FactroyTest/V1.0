package com.malata.factorytest.item;

import com.malata.factorytest.ItemTestActivity;
import com.malata.factorytest.R;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class KeyAndMotor extends AbsHardware{
	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; //需要自己定义标志
	Context context;
	private Button bt_Volume_Up;
	private Button bt_Volume_Down;
	private Button bt_Power;
	private Button bt_Menu;
	private Button bt_Home;
	private Button bt_Back;
	private int []flag = new int[]{0,0,0,0,0,0};
	long []pattern = new long[]{500,500,500,500,500,500,500,500,500,500,500};
	public KeyAndMotor(String text, Boolean visible) {
		super(text, visible);
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		ItemTestActivity.itemActivity.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
		super.onCreate();
	}
	
	
	@Override
	public TestResult test() {
		// TODO Auto-generated method stub
		
		TipHelper tipHelper = new TipHelper(ItemTestActivity.itemActivity);
		tipHelper.vibrate(pattern,false);
		return null;
	}
	
	
	@Override
	public View getView(Context context) {
		// TODO Auto-generated method stub\
		this.context = context;
		LayoutInflater factory = LayoutInflater.from(context);
		View view  = factory.inflate(R.layout.key_motor,null);
		bt_Back = (Button) view.findViewById(R.id.back);
		bt_Home = (Button) view.findViewById(R.id.home);
		bt_Menu = (Button) view.findViewById(R.id.menu);
		bt_Power = (Button) view.findViewById(R.id.power);
		bt_Volume_Down = (Button) view.findViewById(R.id.volume_down);
		bt_Volume_Up = (Button) view.findViewById(R.id.volume_Up);
		bt_Back.setBackgroundColor(Color.RED);
		bt_Home.setBackgroundColor(Color.RED);
		bt_Menu.setBackgroundColor(Color.RED);
		bt_Power.setBackgroundColor(Color.RED);
		bt_Volume_Down.setBackgroundColor(Color.RED);
		bt_Volume_Up.setBackgroundColor(Color.RED);
		//ItemTestActivity.itemActivity.setButtomBarVisiblity(false);
		
		return view;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0) {
			bt_Back.setBackgroundColor(Color.GREEN);
			return true;
		}else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN&&event.getRepeatCount()==0) {
			bt_Volume_Down.setBackgroundColor(Color.GREEN);
			return true;
		}else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP&&event.getRepeatCount()==0) {
			bt_Volume_Up.setBackgroundColor(Color.GREEN);
			return true;
		}else if (keyCode == KeyEvent.KEYCODE_MENU&&event.getRepeatCount()==0) {
			bt_Menu.setBackgroundColor(Color.GREEN);
			return true;
		}else if (keyCode == KeyEvent.KEYCODE_HOME&&event.getRepeatCount()==0) {
			bt_Home.setBackgroundColor(Color.GREEN);
			return true;
		}else if (keyCode == KeyEvent.KEYCODE_POWER) {
			android.os.Process.killProcess(android.os.Process.myPid());
			bt_Power.setBackgroundColor(Color.GREEN);
			return super.onKeyDown(keyCode, event); 
		}else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	
	/**
	 * @author peisaisai
	 * 震动器类
	 */
	private  class TipHelper{
		Activity activity;
		public TipHelper(ItemTestActivity itemActivity) {
			// TODO Auto-generated constructor stub
			this.activity = itemActivity;
		}
		public  void vibrate(long milliseconds){
			Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
			vib.vibrate(milliseconds);
		}
		public void vibrate(long []pattern,boolean isRepeat){
			Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
			vib.vibrate(pattern, isRepeat ? 1 : -1);
		}
	}
	
	private class MyBroadCastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String reason = intent.getStringExtra("reason");
			if (intent.getAction().equals(intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
				Log.i("pss", "Intent.ACTION_CLOSE_SYSTEM_DIALOGS : " + intent.getStringExtra("reason"));
				
			}
		}
		
	}
	
	
	

}

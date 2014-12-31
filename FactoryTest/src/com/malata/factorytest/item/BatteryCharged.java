package com.malata.factorytest.item;

import java.lang.ref.WeakReference;
import android.R.raw;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiConfiguration.Status;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.Preference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.malata.factorytest.ItemTestActivity;
import com.malata.factorytest.R;

public class BatteryCharged extends AbsHardware {
	
	
	private Context context;
	private TextView batterychargedTextView;
	private TextView batteryvoltageTextView;
	private TextView batterycurrentTextView;
	private TextView batterylevelTextView;
	private TextView batterytempTextView;
	private TextView batteryhealthTextView;
	private TextView batteryluggedTextView,uptimeTextView;
	private Message msg;
	private Bundle b;
	boolean isableClick = false;
    private static final int EVENT_UPDATE_STATS = 500;
	//创建时注册
	@Override
	public void onCreate() {		
		super.onCreate();
		//创建过滤器
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		context.registerReceiver(mReceiver, ifilter);
		Intent batteryStatus = context.registerReceiver(mReceiver, ifilter);
		
		Handler mHandler = new MyHandler(this);
		mHandler.sendEmptyMessage(EVENT_UPDATE_STATS);

		
	}
	/*//设置 通过按键可用
	public void ablePassButtonClick() {
		msg = new Message();
		b = new Bundle();
		b.putBoolean("avisible", true);
		b.putInt("id", ItemTestActivity.BUTTON_PASS);
		msg.what = ItemTestActivity.BUTTON_AVAILABLE;
		msg.setData(b);
		ItemTestActivity.itemActivity.handler.sendMessage(msg);
	}
	
	//设置 通过按键不可用
	public void unablePassButtonClick() {
		msg = new Message();
		b = new Bundle();
		b.putBoolean("avisible", false);
		b.putInt("id", ItemTestActivity.BUTTON_PASS);
		msg.what = ItemTestActivity.BUTTON_AVAILABLE;
		msg.setData(b);
		ItemTestActivity.itemActivity.handler.sendMessage(msg);
	}
	
	*/
	
	//销毁时取消注册
	@Override
	public void onDestory() {		
		super.onDestory();
		context.unregisterReceiver(mReceiver); 
	}
	

	public BatteryCharged(String text, Boolean visible) {
		super(text, visible);
	}

	@Override
	public TestResult test() {
		return getResult();
	}
	
	void updateTimes() {
	        long at = SystemClock.uptimeMillis() / 1000;
	        long ut = SystemClock.elapsedRealtime() / 1000;

	        if (ut == 0) {
	            ut = 1;
	        }

	        uptimeTextView.setText(convert(ut));
	    }
	 private String convert(long t) {
	        int s = (int)(t % 60);
	        int m = (int)((t / 60) % 60);
	        int h = (int)((t / 3600));

	        return h + ":" + pad(m) + ":" + pad(s);
	    }
	 
	   private String pad(int n) {
	        if (n >= 10) {
	            return String.valueOf(n);
	        } else {
	            return "0" + String.valueOf(n);
	        }
	    }
	
	

	@Override
	public View getView(Context context) {
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.batterycharged, null);
		batterychargedTextView = (TextView) view.findViewById(R.id.textview_batteryCharging);
		batteryvoltageTextView = (TextView) view.findViewById(R.id._textview_chargingVoltage);
		batterycurrentTextView = (TextView) view.findViewById(R.id._textview_chargingCurrent);
		batterylevelTextView = (TextView) view.findViewById(R.id._textview_charginglevel);
		batterytempTextView = (TextView) view.findViewById(R.id._textview_chargingtemp);
		batteryhealthTextView = (TextView) view.findViewById(R.id._textview_charginghealth);
		batteryluggedTextView = (TextView) view.findViewById(R.id._textview_chargingplugged);
		uptimeTextView = (TextView) view.findViewById(R.id.uptime_text);
		return view;
	}
	
	
	//注册内部 接收器
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			//电池健康 
			int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
				Log.i("test", "health: " + health);
			boolean HEALTH_COLD = health == BatteryManager.BATTERY_HEALTH_COLD;
			boolean HEALTH_DEAD = health == BatteryManager.BATTERY_HEALTH_DEAD;
			boolean HEALTH_GOOD = health == BatteryManager.BATTERY_HEALTH_GOOD;
			boolean HEALTH_OVER_VOLTAGE = health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE;
			boolean HEALTH_OVERHEAT = health == BatteryManager.BATTERY_HEALTH_OVERHEAT;
			boolean HEALTH_UNKNOWN = health == BatteryManager.BATTERY_HEALTH_UNKNOWN;
			boolean HEALTH_UNSPECIFIED_FAILURE = health == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE;
			if (HEALTH_GOOD) {
				
				batteryhealthTextView.setText("电池状态良好。");
			}else if (HEALTH_COLD) {
				
				batteryhealthTextView.setText("电池温度过低。");
			}else if (HEALTH_DEAD) {
				
				batteryhealthTextView.setText("电池没电。");
			}else if (HEALTH_OVER_VOLTAGE) {
				
				batteryhealthTextView.setText("电池过压。");
			}else if (HEALTH_OVERHEAT) {
				
				batteryhealthTextView.setText("电池温度过热。");
			}else if (HEALTH_UNKNOWN) {
				
				batteryhealthTextView.setText("电池状态未知。");
			}else if (HEALTH_UNSPECIFIED_FAILURE) {
				
				batteryhealthTextView.setText("未知错误！");
			}
			
			//电池电压
			int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
				Log.i("test", "voltage: " + voltage);
				float batteryvoltage = voltage/1000;
				batteryvoltageTextView.setText(batteryvoltage+"V");
				
			//电池温度
			int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
				Log.i("test", "BATTERY_TEMPERATURE: " + temperature);
				float batterytemperatyre = temperature/10;
				batterytempTextView.setText(batterytemperatyre+"°c");
				
			//当前状态
			int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
				Log.i("test", "BATTERY_STATUS: " + status);
			boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
					status == BatteryManager.BATTERY_STATUS_FULL;
			if(isCharging) {
				batterychargedTextView.setBackgroundColor(Color.GREEN);
				batterychargedTextView.setText("正在充电...");
				isableClick = true;
				Log.i("isableClick", "可用点击");
				
				//设置通过按键可用
				//ablePassButtonClick();
			}else {
				batterychargedTextView.setBackgroundColor(Color.RED);
				batterychargedTextView.setText("未连接...");
				isableClick = false;
				//Log.i("isableClick", "不可以点击");
				//unablePassButtonClick();
			}	
			//当前电量
			int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			//电量最大值
			int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
				Log.i("test", "BATTERY_LEVEL: " + level);
			float batteryPct = level / (float)scale;
				Log.i("test", "BATTERY_batteryPct: " + batteryPct);
				batterylevelTextView.setText("当前电量 "+batteryPct*100+"%");
				
			//电池充电方式
			int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
				Log.i("test", "BATTERY_PLUGGED: " + chargePlug);
			boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
			boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
			if (usbCharge) {
				
				batteryluggedTextView.setText("USB充电器充电中...");
			}else if (acCharge) {
				
				batteryluggedTextView.setText("AC充电器充电中...");
			}else {
				batteryluggedTextView.setText("手机未充电...");
			}
						
		}
	};
	
	
	 @SuppressLint("HandlerLeak")
	private  class MyHandler extends Handler {
	        public MyHandler(BatteryCharged batteryCharged) {
	            new WeakReference<BatteryCharged>(batteryCharged);
	        }
	        @Override
	        public void handleMessage(Message msg) {
	        	//BatteryCharged batteryManager1 = mStatus.get();
	            switch (msg.what) {
	                case EVENT_UPDATE_STATS:
	                   updateTimes();
	                    sendEmptyMessageDelayed(EVENT_UPDATE_STATS, 1000);
	                    break;
	            }
	        }
	 }
}	    
	
	



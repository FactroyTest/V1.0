/*package com.malata.factorytest.item;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.malata.factorytest.ItemTestActivity;
import com.malata.factorytest.R;


public class Gyroscope extends AbsHardware {
	private final String INIT_FAILED = "初始化失败！";
	private final int UPDATE_GYRO = 'g'+'y'+'r'+'o';
	private boolean gyroOpen;	
	private TextView tv_gyro;
	private String gyro_data = "";
	private Handler handler = null;
	private boolean isAlive;
	private Sensor mSensor;
	private SensorManager mSensorManager;
	private GyroscopeListener listener = new GyroscopeListener();
	public Gyroscope(String text, Boolean visible) {
		super(text, visible);
	}

	@Override
	public TestResult test() {
		if(gyroOpen) {
			Log.i("hah", "open success");
			new Thread(new UpdateGyroThread()).start();
		} else { // 设备打开失败
			setResult(TestResult.Fail);
			Log.i("hah", "open failed");
			return TestResult.Fail;
		}
		return null;
	}
	

	@Override
	public View getView(Context context) {
		init();
		View v = null;
		v = LayoutInflater.from(context).inflate(R.layout.sensor_show, null);
		tv_gyro = (TextView) v.findViewById(R.id.tv_gyro);
		if(!gyroOpen) {
			tv_gyro.setText(INIT_FAILED);
		} 
		return v;
	}
		
	private void init() {
		gyroOpen = initGyro();
		isAlive = true;
		handler = new Handler(ItemTestActivity.itemActivity.getMainLooper()){
			public void handleMessage(Message msg) {
				switch(msg.what) {
				case UPDATE_GYRO:
					tv_gyro.setText((String)msg.obj);
					break;
				}
			}
		};
	}

	public native boolean initGyro();
	
	public native boolean closeGyro();
	
	public native String updateGyro();
		
	static {
        System.loadLibrary("Gyroscopejni");
    }
	
	private class UpdateGyroThread implements Runnable {

		@Override
		public void run() {
			Message msg = null;
			while(true) {
				if(!isAlive) {
					break;
				}
				gyro_data = updateGyro();
				msg = handler.obtainMessage(UPDATE_GYRO, gyro_data);
				handler.sendMessage(msg);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	@Override
	public void onDestory() {
		// 界面销毁的时候停止刷新线程
		isAlive = false;
		closeGyro();
		super.onDestory();
	}
}
*/
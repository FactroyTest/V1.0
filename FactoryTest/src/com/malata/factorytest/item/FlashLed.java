package com.malata.factorytest.item;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.malata.factorytest.R;

public class FlashLed extends AbsHardware {
	private View view;
	private Button flashButton;
	private Camera camera = null;
	private Parameters parameters = null;
	public FlashLed(String text, Boolean visible) {
		super(text, visible);
	}

	@Override
	public TestResult test() {
		return null;
	}

	@Override
	public View getView(Context context) {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		view = layoutInflater.inflate(R.layout.flashled, null);
		flashButton = (Button) view.findViewById(R.id.flash_button);
		flashButton.setOnClickListener(listener);
		return view;
		
	}
	
	OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//打开
			camera = Camera.open();
			 parameters = camera.getParameters();  
             parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);//开启   
             camera.setParameters(parameters);  
			
           /*  try {
				Thread.sleep(1*1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
          //直接关闭   
             parameters.setFlashMode(Parameters.FLASH_MODE_OFF);//关闭   
                camera.setParameters(parameters);  
                  camera.release();
		}
	};

	public void onDestory() {
		
		super.onDestory();
		if (camera!=null) {
			camera.release();
		}
		
	};
}

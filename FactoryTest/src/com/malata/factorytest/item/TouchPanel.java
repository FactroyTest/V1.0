package com.malata.factorytest.item;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.malata.factorytest.ItemTestActivity;
import com.malata.factorytest.TouchPanelView;
/**
 * ¥•√˛∆¡≤‚ ‘
 *
 */
public class TouchPanel extends AbsHardware {
	public static TouchPanel panel;
	public TouchPanel(String text, Boolean visible) {
		super(text, visible);
	}

	@Override
	public TestResult test() {
		
		return TestResult.Fail;
	}

	@Override
	public View getView(Context context) {
		panel = this;
		Message msg = new Message();
		Bundle b = new Bundle();
		b.putBoolean("visible", false);
		msg.what = ItemTestActivity.BTN_BAR_AVAILABLE;
		msg.setData(b);
		ItemTestActivity.itemActivity.handler.sendMessage(msg);
		ItemTestActivity.itemActivity.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		ItemTestActivity.itemActivity.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		View v = new TouchPanelView(context);
		return v;
	}

}

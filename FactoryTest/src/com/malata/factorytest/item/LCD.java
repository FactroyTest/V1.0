package com.malata.factorytest.item;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.malata.factorytest.ItemTestActivity;
import com.malata.factorytest.R;

public class LCD extends AbsHardware {

	private ImageView imageView;
	private View view;
	private int imgNum;
	private boolean visible = true;

	public LCD(String text, Boolean visible) {
		super(text, visible);
	}

	@Override
	public TestResult test() {
		return null;
	}

	@Override
	public View getView(Context context) {
		imgNum = 3;
		setVisible();
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		view = layoutInflater.inflate(R.layout.item_lcd, null);
		imageView = (ImageView) view.findViewById(R.id.img_lcd);
		// imageView.setBackgroundResource(R.drawable.ic_launcher);
		view.setBackgroundColor(Color.BLUE);
		imageView.setOnTouchListener(new ImgOnTouchListener());
		return view;
	}

	public void setVisible() {
		Bundle b = new Bundle();
		b.putBoolean("visible", false);
		Message m = new Message();
		m.what = ItemTestActivity.BTN_BAR_AVAILABLE;
		m.setData(b);
		ItemTestActivity.itemActivity
				.requestWindowFeature(Window.FEATURE_NO_TITLE);
		ItemTestActivity.itemActivity.handler.sendMessage(m);
		ItemTestActivity.itemActivity.getWindow().requestFeature(
				Window.FEATURE_NO_TITLE);

		ItemTestActivity.itemActivity.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

	}

	private class ImgOnTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				imageView.setVisibility(0);
				Log.i("aa", "sss");
				if (imgNum == 3) {
					view.setBackgroundColor(Color.RED);
					imgNum--;
				} else if (imgNum == 2) {
					view.setBackgroundColor(Color.GREEN);
					imgNum--;
				} else if (imgNum == 1) {
					view.setBackgroundColor(Color.BLACK);
					imgNum--;
					Bundle b = new Bundle();
					b.putBoolean("visible", true);
					Message m = new Message();
					m.what = ItemTestActivity.BTN_BAR_AVAILABLE;
					m.setData(b);
				} else if (imgNum == 0) {
					
					
				}

				break;

			default:
				break;
			}

			return true;
		}

	}

}

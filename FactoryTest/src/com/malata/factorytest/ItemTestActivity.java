package com.malata.factorytest;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.malata.factorytest.item.AbsHardware;
import com.malata.factorytest.item.AbsHardware.TestResult;
/**
 * ������Խ���
 *
 */
public class ItemTestActivity extends Activity {
	//��ǰ���ԵĲ���
	private AbsHardware hw;
	//HandlerThread t;
	
	private RelativeLayout contentView;
	/**
	 * ��ǰ���ʵ����������ͼ���ÿ�����ͼ
	 */
	public static ItemTestActivity itemActivity;
	/**
	 * ͨ������ ID
	 */
	public static final int BUTTON_PASS = R.id.btn_pass;
	/**
	 * ʧ�ܰ��� ID
	 */
	public static final int BUTTON_FAIL = R.id.btn_fail;
	/**
	 * ���ð�ť�Ƿ�ɵ����Ϣ
	 */
	public static final int BUTTON_AVAILABLE = R.id.btn_fail+R.id.btn_pass;
	/**
	 * ���ð�ť���Ƿ�ɼ���Ϣ
	 */
	public static final int BTN_BAR_AVAILABLE = R.id.pass_fail_bar;
	/**
	 *  ����Button�Ƿ������Ϣ��msg.what = BUTTON_AVAILABLE, 
	 *  	����ΪBundle�����boolean������,keyΪ"available"��һ��keyΪ"id"�İ���ID�� BUTTON_PASS��BUTTON_FAIL <p>
	 *  ����ButtonBar�Ƿ�ɼ���Ϣ��msg.what = BTN_BAR_AVAILABLE, 
	 *  	����ΪBundle�����boolean������,keyΪ"visible"
	 */
	public Handler handler = new Handler() {
		
		public void handleMessage(android.os.Message msg) {
			Bundle data;
			switch(msg.what) {
			case BUTTON_AVAILABLE:
				data = msg.getData();
				setButtonAvailable(data.getInt("id"), data.getBoolean("available"));
				break;
			case BTN_BAR_AVAILABLE:
				data = msg.getData();
				setButtomBarVisiblity(data.getBoolean("visible"));
				break;
			}
		};
	};
	
	
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		itemActivity = this;
		
		/*t=new HandlerThread("theard");
	    t.start();*/
		/*setContentView(R.layout.activity_item_test);
		//�����һ���洫�ݹ�������Ϣ
		hw = MainActivity.hardwares.get(getIntent().getIntExtra("position", 0));
		//�õ���ͼView����ӵ���ͼ��
		View view = hw.getView(this);
		contentView = (RelativeLayout) findViewById(R.id.rl_content);
		if(view != null) {
			RelativeLayout.LayoutParams lp = new RelativeLayout.
					LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			contentView.addView(view, lp);
		} 
		hw.onCreate();
		hw.test();*/
		RelativeLayout layout = (RelativeLayout)LayoutInflater.from(this).inflate(R.layout.activity_item_test, null);
		hw = MainActivity.hardwares.get(getIntent().getIntExtra("position", 0));
		View subview = hw.getView(this);
		contentView = (RelativeLayout) layout.findViewById(R.id.rl_content);
		if(subview != null) {
			RelativeLayout.LayoutParams lp = new RelativeLayout.
					LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			contentView.addView(subview, lp);
		} 
		setContentView(layout);
		hw.onCreate();
		hw.test();
	}
	
	/**
	 * ���ý����PASS��FAIL��ť�Ƿ����
	 * @param id ��ť��id�����ܲ���  ItemTestActivity.BUTTON_PASS ��  ItemTestActivity.BUTTON_FAIL
	 * @param available tureΪ���ã�falseΪ������
	 */
	public void setButtonAvailable(int id, boolean available) {
		((Button)this.findViewById(id)).setClickable(available);
		if(available) {
			
		}else {
			((Button)this.findViewById(id)).setBackgroundColor(Color.GRAY);
		}
			
	}
	
	/**
	 * ���õײ�ͨ��/ʧ�ܰ�ť ���Ƿ�ɼ�
	 * @param visible
	 */
	public void setButtomBarVisiblity(boolean visible) {
		LinearLayout bar = (LinearLayout)this.findViewById(R.id.pass_fail_bar);
		if(visible) {
			bar.setVisibility(View.VISIBLE);
		} else {
			bar.setVisibility(View.GONE);
		}
	}
	
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btn_pass:
			hw.setResult(TestResult.Pass);
			finish();
			break;
		case R.id.btn_fail:
			hw.setResult(TestResult.Fail);
			finish();
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(!hw.onKeyDown(keyCode, event)) {
			if(keyCode == KeyEvent.KEYCODE_BACK) {
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}
	
	@Override
	protected void onStart() {
		hw.onStart();
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		hw.onResume();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		hw.onPause();
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		hw.onStop();
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		hw.onDestory();
		super.onDestroy();
	}

}

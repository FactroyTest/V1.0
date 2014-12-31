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
 * 单项测试界面
 *
 */
public class ItemTestActivity extends Activity {
	//当前测试的部件
	private AbsHardware hw;
	//HandlerThread t;
	
	private RelativeLayout contentView;
	/**
	 * 当前类的实例，供子视图调用控制视图
	 */
	public static ItemTestActivity itemActivity;
	/**
	 * 通过按键 ID
	 */
	public static final int BUTTON_PASS = R.id.btn_pass;
	/**
	 * 失败按键 ID
	 */
	public static final int BUTTON_FAIL = R.id.btn_fail;
	/**
	 * 设置按钮是否可点击消息
	 */
	public static final int BUTTON_AVAILABLE = R.id.btn_fail+R.id.btn_pass;
	/**
	 * 设置按钮条是否可见消息
	 */
	public static final int BTN_BAR_AVAILABLE = R.id.pass_fail_bar;
	/**
	 *  接收Button是否可用消息：msg.what = BUTTON_AVAILABLE, 
	 *  	数据为Bundle，存放boolean型数据,key为"available"和一个key为"id"的按键ID： BUTTON_PASS、BUTTON_FAIL <p>
	 *  接收ButtonBar是否可见消息：msg.what = BTN_BAR_AVAILABLE, 
	 *  	数据为Bundle，存放boolean型数据,key为"visible"
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
		//获得上一界面传递过来的信息
		hw = MainActivity.hardwares.get(getIntent().getIntExtra("position", 0));
		//得到视图View并添加到视图中
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
	 * 设置界面的PASS或FAIL按钮是否可用
	 * @param id 按钮的id，接受参数  ItemTestActivity.BUTTON_PASS 或  ItemTestActivity.BUTTON_FAIL
	 * @param available ture为可用，false为不可用
	 */
	public void setButtonAvailable(int id, boolean available) {
		((Button)this.findViewById(id)).setClickable(available);
		if(available) {
			
		}else {
			((Button)this.findViewById(id)).setBackgroundColor(Color.GRAY);
		}
			
	}
	
	/**
	 * 设置底部通过/失败按钮 条是否可见
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

package com.malata.factorytest.item;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
/**
 * <b>手机部件的抽象类：</b>
 * 	文本内容<p>
 * 	是否可见<p>
 * 	测试结果<p>
 *	进行测试<p>	
 *	获得视图View<p>
 *	
 */
public abstract class AbsHardware {
	/**
	 * 测试结果：通过、失败、未检测
	 * @author laiyang
	 *
	 */
	public enum TestResult {Pass,Fail,UnCheck};
	/**
	 * 文本内容
	 */
	public String text;
	/**
	 * 是否显示
	 */
	private boolean visible;
	/**
	 * 上下文Context
	 */
	private Context context;
	/**
	 * 测试是否通过
	 */
	private TestResult result = TestResult.UnCheck;
	/**
	 * 获得测试结果
	 * @return
	 */
	public TestResult getResult() {
		return result;
	}
	/**
	 * 设置测试结果
	 * @param result
	 */
	public void setResult(TestResult result) {
		this.result = result;
	}

	public AbsHardware(String text, Boolean visible) {
		this.text = text;
		this.visible = visible;
	}
	
	public AbsHardware(String text, Boolean visible, Context context) {
		this(text, visible);
		this.context = context;
	}
	
	/**
	 * 调用该方法进行测试
	 */
	public abstract TestResult test();

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}	
	/**
	 * 通过调用此方法，获得界面View
	 * @param context
	 * @return
	 */
	public abstract View getView(Context context);
	/**
	 * 获得上下文
	 * @return
	 */
	public Context getContext() {
		return context;
	}
	
	public void onStart() {
		
	}
	
	public void onPause() {
		
	}
	
	public void onResume() {
		
	}
	
	public void onStop() {
		
	}
	
	public void onDestory() {
		
	}
	
	public void onCreate() {
		
	}
	/**
	 * 默认返回false ,如果不希望按键事件继续传递，则返回true
	 * @param keyCode
	 * @param event
	 * @return
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		return false;
	}
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return false;
	}
	
	 public void keyReleased(int keyCode) {
		 
		 
	    } 
	
	
	
}

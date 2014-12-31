package com.malata.factorytest.item;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
/**
 * <b>�ֻ������ĳ����ࣺ</b>
 * 	�ı�����<p>
 * 	�Ƿ�ɼ�<p>
 * 	���Խ��<p>
 *	���в���<p>	
 *	�����ͼView<p>
 *	
 */
public abstract class AbsHardware {
	/**
	 * ���Խ����ͨ����ʧ�ܡ�δ���
	 * @author laiyang
	 *
	 */
	public enum TestResult {Pass,Fail,UnCheck};
	/**
	 * �ı�����
	 */
	public String text;
	/**
	 * �Ƿ���ʾ
	 */
	private boolean visible;
	/**
	 * ������Context
	 */
	private Context context;
	/**
	 * �����Ƿ�ͨ��
	 */
	private TestResult result = TestResult.UnCheck;
	/**
	 * ��ò��Խ��
	 * @return
	 */
	public TestResult getResult() {
		return result;
	}
	/**
	 * ���ò��Խ��
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
	 * ���ø÷������в���
	 */
	public abstract TestResult test();

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}	
	/**
	 * ͨ�����ô˷�������ý���View
	 * @param context
	 * @return
	 */
	public abstract View getView(Context context);
	/**
	 * ���������
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
	 * Ĭ�Ϸ���false ,�����ϣ�������¼��������ݣ��򷵻�true
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

package com.malata.factorytest;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.malata.factorytest.item.AbsHardware;
import com.malata.factorytest.item.AbsHardware.TestResult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	/**
	 * ����-�Զ����ԣ���������Զ�����
	 */
	Button bt_auto_test;
	
	Button bt_manual_test;
	
	Button bt_test_report;
	
	Button bt_reboot;
	
	public static ArrayList<AbsHardware> hardwares = new ArrayList<AbsHardware>();
	/**
	 * ����������
	 */
	String[] hwTexts;
	/**
	 * �������������
	 */
	String[] hwClassName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//����Ӧ��ʱ����ʼ�����в�����Ķ���
	
		init();
	}
	
	public void onClick(View v) {
		Intent i = null;
		switch(v.getId()) {
		case R.id.bt_auto_test:
			i = new Intent(this, AutoTestActivity.class);
			break;
		case R.id.bt_manual_test:
			i = new Intent(this, ManualTestActivity.class);
			break;
		case R.id.bt_test_report:
			i = new Intent(this, TestReportActivity.class);
			break;
		case R.id.bt_reboot:
			i = new Intent(this, RebootActivity.class);
			break;
		}
		startActivity(i);
	}
	
	private void init() {
		// ��arrays�л�ò������Ͳ����������
		hwTexts = getResources().getStringArray(R.array.testitems);
		hwClassName = getResources().getStringArray(R.array.testClassNames);
		hardwares.clear();
		for(int i = 0;i <hwClassName.length; i++) {
			hardwares.add(createObj(hwClassName[i], hwTexts[i], true));
		}
	}
	/**
	 * ������������Ķ���
	 * 	ʹ�÷�����ƴ�������
	 * @param className ��������Ҫ���������������������arrays.xml�ж���
	 * @param text ������
	 * @param visible �Ƿ�ɼ�
	 * @return
	 */
	public AbsHardware createObj(String className,String text, boolean visible) {
		AbsHardware hw = null;
		try {
			Object o = (AbsHardware) Class.forName(className)
					.getConstructor(String.class,Boolean.class)
					.newInstance(text,visible);
			if(o instanceof AbsHardware) {
				hw = (AbsHardware) o;
			}
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		if(hw == null) {
			throw new NullPointerException("����AbsHardware�����");
		}
		return hw;
	}
	/**
	 * �������в������Խ��Ϊ��δ����
	 */
	public void clearResult() {
		for(int i = 0; i < hardwares.size(); i++) {
			hardwares.get(i).setResult(TestResult.UnCheck);
		}
	}
}

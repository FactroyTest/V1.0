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
	 * 按键-自动测试，点击启动自动测试
	 */
	Button bt_auto_test;
	
	Button bt_manual_test;
	
	Button bt_test_report;
	
	Button bt_reboot;
	
	public static ArrayList<AbsHardware> hardwares = new ArrayList<AbsHardware>();
	/**
	 * 各部件名字
	 */
	String[] hwTexts;
	/**
	 * 各部件类的名字
	 */
	String[] hwClassName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//启动应用时，初始化所有部件类的对象
	
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
		// 从arrays中获得部件名和部件类的名字
		hwTexts = getResources().getStringArray(R.array.testitems);
		hwClassName = getResources().getStringArray(R.array.testClassNames);
		hardwares.clear();
		for(int i = 0;i <hwClassName.length; i++) {
			hardwares.add(createObj(hwClassName[i], hwTexts[i], true));
		}
	}
	/**
	 * 创建各部件类的对象
	 * 	使用反射机制创建对象
	 * @param className 类名：所要创建对象的类名，该类在arrays.xml中定义
	 * @param text 部件名
	 * @param visible 是否可见
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
			throw new NullPointerException("创建AbsHardware类出错！");
		}
		return hw;
	}
	/**
	 * 设置所有部件测试结果为：未测试
	 */
	public void clearResult() {
		for(int i = 0; i < hardwares.size(); i++) {
			hardwares.get(i).setResult(TestResult.UnCheck);
		}
	}
}

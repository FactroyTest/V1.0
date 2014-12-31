package com.malata.factorytest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
/**
 * ≤‚ ‘±®∏Ê¿‡
 *
 */
public class TestReportActivity extends Activity {
	
	GridView gridView;
	
	TestItemAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_report);
		
		gridView = (GridView)findViewById(R.id.girdView);
		adapter = new TestItemAdapter(this);
		gridView.setAdapter(adapter);
	}
}

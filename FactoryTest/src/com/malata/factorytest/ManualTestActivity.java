package com.malata.factorytest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
/**
 *  ÷∂Ø≤‚ ‘
 *
 */
public class ManualTestActivity extends Activity {
	
	GridView gridView;
	
	TestItemAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manual_test);
		
		gridView = (GridView)findViewById(R.id.girdView);
		adapter = new TestItemAdapter(this);
		gridView.setAdapter(adapter);
	}
	
	@Override
	protected void onResume() {
		gridView.invalidateViews();
		super.onResume();
	}
}

package com.malata.factorytest;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;

public class TestItemAdapter implements ListAdapter {

	private Context context;
	
	private int count;
	
	public TestItemAdapter(Context context) {
		this.context = context;
		count = MainActivity.hardwares.size();
	}
	
	@Override
	public int getCount() {
		return count;
	}

	@Override
	public Object getItem(int position) {
		return MainActivity.hardwares.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public int getItemViewType(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Button bt = null;
		if(convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.gridview_item, null);
			bt = (Button)convertView.findViewById(R.id.bt_item);
			convertView.setTag(bt);
		} else {
			bt = (Button)convertView.getTag();
		}
		bt.setText(MainActivity.hardwares.get(position).text);
		bt.setOnClickListener(new OnClickListener(position));
		
		switch(MainActivity.hardwares.get(position).getResult()) {
		case Pass:
			bt.setBackgroundColor(Color.GREEN);
			break;
		case Fail:
			bt.setBackgroundColor(Color.RED);
			break;
		case UnCheck:
		default:
			break;
		}
		return convertView;
	}
	
	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver arg0) {

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver arg0) {

	}
	
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int arg0) {
		return false;
	}
	/**
	 * 按键的监听器，点击后传递当前点击的按钮所代表部件在部件数组中所在的位置
	 *
	 */
	private class OnClickListener implements View.OnClickListener {
		private int position;
		public OnClickListener(int position) {
			this.position = position;
		}
		
		@Override
		public void onClick(View v) {
			Intent i = new Intent(context, ItemTestActivity.class);
			i.putExtra("position", position);
			context.startActivity(i);
		}
	}
}


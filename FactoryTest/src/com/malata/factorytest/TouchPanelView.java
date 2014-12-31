package com.malata.factorytest;

import java.util.ArrayList;

import com.malata.factorytest.item.AbsHardware.TestResult;
import com.malata.factorytest.item.TouchPanel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TouchPanelView extends View implements View.OnTouchListener {
	// 屏幕宽高
	int width;
	int height;
	int step = 100;
	// 当前触摸点坐标
	float touchX;
	float touchY;
	// 前一个触摸点坐标
	float preX;
	float preY;
	// 当前触摸点的总量
	int currentTch;
	int touchSum;
	// 当前为第几张图
	int process; 
	private Canvas bCanvas;
	private Bitmap bitmap;
	private Paint p;
	private Paint pLine;
	private ArrayList<CustomRect> rects;
	//交叉线图形集合
	private ArrayList<Trapezoid> trapezoids;
	public void init() {
		setOnTouchListener(this);
		width = getResources().getDisplayMetrics().widthPixels;
		height = getResources().getDisplayMetrics().heightPixels;
		step = width/16;
		bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bCanvas = new Canvas();
		p = new Paint();
		p.setAntiAlias(true);
		p.setStrokeWidth(8f);
		p.setColor(Color.WHITE);
		p.setStyle(Style.STROKE);
		
		pLine = new Paint();
		pLine.setAntiAlias(true);
		pLine.setStrokeWidth(4f);
		pLine.setStyle(Style.STROKE);
		
		bCanvas.setBitmap(bitmap);
		
		initMap();
		process = 1;
		touchSum = rects.size();
	}
	private void initMap() {
		rects = new ArrayList<CustomRect>();
		int numW = width/step;
		int numH = height/step;
		int residueH = height%step;
		int residueW = width%step;
		for(int i = 0; i <= numW-1;i++) {
			for(int j = 0; j <= numH-1; j++) {
				if(i == 0 || j == 0 || i == numW-1 || j == numH-1) {
					if(i == numW-1 && j == numH-1) {
						rects.add(new CustomRect(new Rect(i*step, j*step, (i+1)*step+residueW, (j+1)*step+residueH), false));
						continue;
					}
					if(j == numH-1) {
						rects.add(new CustomRect(new Rect(i*step, j*step, (i+1)*step, (j+1)*step+residueH), false));
						continue;
					}
					if(i == numW - 1){
						rects.add(new CustomRect(new Rect(i*step, j*step, (i+1)*step+residueW, (j+1)*step), false));
						continue;
					}	
					rects.add(new CustomRect(new Rect(i*step, j*step, (i+1)*step, (j+1)*step), false));
				}
			}
		} 
		pLine.setColor(Color.RED);
		double sideSum = Math.sqrt(width*width+height*height);
		double angle = Math.acos(height/sideSum);
		double side = step / Math.cos(angle);
		int wSum = (int) (sideSum/side);
		int hSum = (int) (height / step);

		trapezoids = new ArrayList<Trapezoid>();
		Trapezoid t = null;
		trapezoids.add(new Trapezoid(-step,0,-step,step,(int)(Math.sin(angle)*side),0,
				(int)side, step,false));
		t = new Trapezoid((int)(wSum*(Math.sin(angle)*side)),0,
				(int)((wSum-1)*(Math.sin(angle)*side)),step,
				width+step, 0,
				width+step,step,false);
		trapezoids.add(t);
		t = new Trapezoid(0,step*hSum,
				(int)(-1*(Math.sin(angle)*side)),step*(hSum+1),
				(int)(0+side), step*hSum,
				(int)(-1*(Math.sin(angle)*side)+side),step*(hSum+1),false);
		trapezoids.add(t);
		t = new Trapezoid((int)((wSum-1)*(Math.sin(angle)*side)),step*hSum,
				(int)((wSum)*(Math.sin(angle)*side)),step*(hSum+1),
				(int)((wSum-1)*(Math.sin(angle)*side)+side), step*hSum,
				(int)((wSum)*(Math.sin(angle)*side)+side),step*(hSum+1),false);
		trapezoids.add(t);
		int temp1 = 1;
		int temp2 = 1;
		int temp3 = 1;
		int temp4 = hSum-1;
		for(int w = 0; w < wSum; w++ ) {
			for(int h = 0; h < hSum; h++) {
				if((w == 0 && h == 0) || (h == 0 && w == wSum - 1)) {
						continue;
				} 
			 	if(w == temp1 && h == temp2) {
			 		t = new Trapezoid((int)((w-1)*Math.sin(angle)*side),(int)(step*h),
							(int)(w*Math.sin(angle)*side),(int)(step*(h+1)),
							(int)(Math.sin(angle)*side+w*Math.sin(angle)*side),(int)(step*h),
							(int)(side + w*Math.sin(angle)*side), step*(h+1),
							false);
			 		temp1++;
					temp2++;
					trapezoids.add(t);
					t = null;
				}
				if(w == temp3 && h == temp4) {
					t = new Trapezoid((int)(w*(Math.sin(angle)*side)),step*h,
							(int)((w-1)*(Math.sin(angle)*side)),step*(h+1),
							(int)(w*(Math.sin(angle)*side)+side), step*h,
							(int)((w-1)*(Math.sin(angle)*side)+side),step*(h+1)
							,false);
			 		temp3++;
					temp4--;
					trapezoids.add(t);
					t = null;
				}
			}
			
		}
	}
	
	
	public TouchPanelView(Context context) {
		super(context);
		init();
	}

	public TouchPanelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TouchPanelView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width,  
                View.MeasureSpec.EXACTLY);  
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height,  
                View.MeasureSpec.EXACTLY);  
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		drawTouchPanel(canvas);
		if(preX == touchX && preY == touchY) {
			bCanvas.drawPoint(preX, preY, p);
		} else {
			bCanvas.drawLine(preX, preY, touchX, touchY, p);
		}
		canvas.drawBitmap(bitmap, 0, 0, p);
		super.onDraw(canvas);
	}

	private void drawTouchPanel(Canvas canvas) {
		if(process == 1) {
			for(CustomRect rect : rects) {
				rect.draw(canvas, pLine);
			}
		} else {
			for(Trapezoid t : trapezoids) {
				t.draw(canvas, pLine);
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		preX = touchX;
		preY = touchY;
		touchX = event.getX();
		touchY = event.getY();
		switch(action) {
		case MotionEvent.ACTION_DOWN:
			preX = touchX;
			preY = touchY;
//			isTouch();
//			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			isTouch();
			invalidate();
			break;
		}
		return true;
	}
	
	private void isTouch() {
		if(process == 1) {
			for(CustomRect r:rects) {
				if(r.isTouch) { //如果已点击，跳过
					continue;
				}
				if(r.inSide((int)touchX, (int)touchY)) {
					r.isTouch = true;
					currentTch++;
				}
			}
		} else {
			for(Trapezoid t:trapezoids) {
				if(t.isTouch) { //如果已点击，跳过
					continue;
				}
				if(t.inSide((int)touchX, (int)touchY)) {
					t.isTouch = true;
					currentTch++;
				}
			}
		}
		if(currentTch == touchSum) {
			if(process ==  1) {
				process++;
				currentTch = 0;
				touchSum = trapezoids.size();
				if(!bitmap.isRecycled()) {
					bitmap.recycle();
					bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
					bCanvas.setBitmap(bitmap);
				}
			} else {
				Log.i("hah", "通过");
				TouchPanel.panel.setResult(TestResult.Pass);
				ItemTestActivity.itemActivity.finish();
			}
		}
		Log.i("hah", "current:"+currentTch+"  touchSum:"+touchSum);
	}
	/**
	 * 梯形
	 */
	class Trapezoid {
		int x1;
		int y1;
		int x2;
		int y2;
		int x3;
		int y3;
		int x4;
		int y4;
		int top;
		int right;
		int left;
		int buttom;
		boolean isTouch;
		
		public Trapezoid(int x1, int y1, int x2, int y2, int x3, int y3,
				int x4, int y4, boolean isTouch) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.x3 = x3;
			this.y3 = y3;
			this.x4 = x4;
			this.y4 = y4;
			if(x1 - x2 == 0) {
				left = 0; //两点斜率不存在，需要特殊判定，此处置0
			} else {
				left = (int)((y1-y2)/(x1-x2));
			}
			if(x3 - x4 == 0) {
				right = 0;//两点斜率不存在，需要特殊判定，此处置0
			} else {
				right = (int)((y3-y4)/(x3-x4));
			}
			
			this.isTouch = isTouch;
		}
		public void draw(Canvas canvas, Paint paint) {
			
			if(isTouch) {
				paint.setColor(Color.GREEN);
			} else {
				paint.setColor(Color.RED);
			}
			canvas.drawLine(x1, y1, x2, y2, paint);
			canvas.drawLine(x1, y1, x3, y3, paint);
			canvas.drawLine(x3, y3, x4, y4, paint);
			canvas.drawLine(x2, y2, x4, y4, paint);
		}
		
		/**
		 * 传入的坐标点是否在当前矩形之内
		 * @param x
		 * @param y
		 * @return
		 */
		public boolean inSide(int x, int y) {
			// 不在四个点坐标之内
			if((x > x3 && x > x4)||(x < x1 && x < x2) || (y > y2 && y > y4) || (y < y1 && y < y3)) {
				return false;
			}
			// 四个点之内
			if((x <= x3 && x <= x4)&&(x >= x1 && x >= x2) && (y >= y1 && y <= y2)) {
				return true;
			}
			if(x - x2 == 0) {
				if(left > 0) {
					if(y < y2) {
						return false;
					} 
					return true;
				} 
				if(left < 0) {
					if(y > y2) {
						return false;
					} 
					return true;
				}
			}
			if(x - x4 == 0) {
				if(right > 0) {
					if(y < y4) {
						return true;
					} 
					return false;
				} 
				if(right < 0) {
					if(y == y4) {
						return true;
					} 
					return false;
				}
			}
			//在左斜边之外
			int temp = (int)((y-y2)/(x-x2));
			if(x <= x1 || x <= x2) {
				if((left > 0 && temp > left) || (left < 0 && temp < left)) {
					return false;
				}
			}
//			if((left > 0 && temp > left) || (left < 0 && temp < left)) {
//				return false;
//			}
			temp = (int)((y-y4)/(x-x4));
			//在右斜边之外 
			if(x >= x3 || x >= x4) {
				if((right > 0 && temp < right) || (right < 0 && temp > right) ) {
					return false;
				}
			} 
			return true;
		}
	}
	/**
	 * 自定义矩形
	 *
	 */
	class CustomRect {
		Rect rect;
		boolean isTouch;
		
		public CustomRect(Rect rect, boolean isTouch) {
			this.rect = rect;
			this.isTouch = isTouch;
		}
		
		public void draw(Canvas canvas, Paint paint) {
			if(isTouch) {
				paint.setColor(Color.GREEN);
			} else {
				paint.setColor(Color.RED);
			}
			canvas.drawRect(rect, paint);
		}
		/**
		 * 传入的坐标点是否在当前矩形之内
		 * @param x
		 * @param y
		 * @return
		 */
		public boolean inSide(int x, int y) {
			return rect.contains(x, y);
		}
	}
}


package edu.wm.cs.cs301.xianglupeng.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;


/**
 * Add functionality for double buffering to an AWT Panel class.
 * Used for drawing a maze.
 * 
 * @author Peter Kemper
 *
 */
public class MazePanel extends View {

	private GeneratingActivity generatingActivity;
	private PlayManuallyActivity manuallyActivity;
	private Context context;
	private AttributeSet attributeSet;//

	private Canvas canvas;
	private Bitmap bitmap;
	private Paint paint;
	private Path path;

	private int color;
	private int width;
	private int height;


	/**
	 * Constructor. Object is not focusable.
	 */

	public MazePanel(Context context) {
		super(context);
		this.context = context;

		bitmap = Bitmap.createBitmap(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		paint = new Paint();
	}

	public MazePanel(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		this.context = context;
		this.attributeSet = attributeSet;

		bitmap = Bitmap.createBitmap(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		paint = new Paint();
	}


	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(bitmap, 0, 0, paint);
		invalidate();
		//myTestImage(canvas);

	}

	@Override
	public void onMeasure(int width, int height) {
		super.onMeasure(width, height);
		this.setMeasuredDimension(width, height);
	}



	public void update() {
		invalidate();

	}

	void myTestImage(Canvas c){

		canvas = c;

		setColor(Color.YELLOW);
		addFilledRectangle(100,100,100,100);
		setColor(Color.RED);
		addFilledOval(400,400,100,100);
		setColor(Color.GREEN);
		addFilledOval(600,200,100,100);

		setColor(Color.BLUE);

		int[] intArrayx = new int[]{ 600,700,800,900};
		int[] intArrayy = new int[]{ 600,900,900,600};
		addPolygon(intArrayx,intArrayy,4);

		update();

	}


	public void setColor(int color) {
		this.color = color;
		paint.setColor(color);
	}


	public int getColor() {

		return color;
	}



	public void addFilledRectangle(int x, int y, int width, int height) {

		Rect rec = new Rect(x, y, x + width, y + height);
		canvas.drawRect(rec, paint);
	}



	public void addPolygon(int[] xPoints, int[] yPoints, int nPoints) {

		Path path = new Path();
		path.reset();
		path.moveTo(xPoints[0], yPoints[0]);
		for (int i = 1; i < nPoints; i++){
			path.lineTo(xPoints[i], yPoints[i]);
		}
		path.lineTo(xPoints[0], yPoints[0]);
		canvas.drawPath(path, paint);


	}


	public void addLine(int startX, int startY, int endX, int endY) {
		canvas.drawLine(startX, startY, endX, endY, paint);
	}


	public void addFilledOval(int x, int y, int width, int height) {
		RectF oval = new RectF(x, y, x + width, y + height);
		canvas.drawOval(oval, paint);
	}



	public void setGeneratingActivity(GeneratingActivity generatingActivity){
		this.generatingActivity = generatingActivity;
	}


	public void Start(){

		if (generatingActivity == null){
			Log.v("MazePanel: ","Error!!!!!");
			System.exit(0);
		}
		generatingActivity.start(this);
	}


//	public int getWallColor(int distance, int cc, int extensionX) {
//		// TODO Auto-generated method stub
//		final int d = distance / 4;
//		// mod used to limit the number of colors to 6
//		final int part1 = distance & 7;
//		final int add = (extensionX != 0) ? 1 : 0;
//		final int rgbValue = ((part1 + 2 + add) * 70) / 8 + 80;
//
//		final int RGB_DEF = 20;
//		final int RGB_DEF_GREEN = 60;
//
//		//System.out.println("Initcolor rgb: " + rgbValue);
//		switch (((d >> 3) ^ cc) % 6) {
//			case 0:
//				color = Color.GREEN;
//				break;
//			case 1:
//				color = Color.YELLOW;
//				break;
//			case 2:
//				color = Color.RED;
//				break;
//			case 3:
//				color = Color.GRAY;
//				break;
//			case 4:
//				color = Color.BLUE;
//				break;
//			case 5:
//				color = Color.WHITE;
//				break;
//			default:
//				color = Color.BLACK;
//				break;
//		}
//
//		return color;
//
//	}



}

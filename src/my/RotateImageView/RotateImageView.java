/**********************************************************
 * 2011. 07. 26(최종수정일:2011. 08. 24)
 * 박진우
 * 룰렛View, default 분할 갯수 = 8;
 **********************************************************/

package my.RotateImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class RotateImageView extends ImageView{
	private final static float DEGREE360 = 360;
	private final static float DEGREE180 = 180;
	
	private Matrix matrix = new Matrix();
	private float x;	//view중심 x좌표
	private float y;	//view중심 y좌표
	private double radi;	//radian값
	private float degree = 0;
	private float fixDegree = 0;
	
	private int maxRoulette = 8;	//룰렛분할수 default value = 8
	private float[] rouletteDegree;
	private int currentIndex = 0;	//현재
	
	private Bitmap bmp;
	
	private OnChangedListener listener;
	
	public interface OnChangedListener {
		void onChanged(RotateImageView view, int index);
	}
	
	public RotateImageView(Context context) {
		this(context, null);
	}
	
	public RotateImageView(Context context, AttributeSet attrs) 
	{
		this(context, attrs, 0);
	}
	
	public RotateImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs,defStyle);
		setFocusable(true);
		setFocusableInTouchMode(true);
		
		setMaxRoulette(maxRoulette);
	}
	
	public void setOnChangedListener(OnChangedListener listener){
		this.listener = listener;
	}
	
	@Override
	public void setBackgroundResource(int resid) {
		super.setBackgroundResource(resid);
		
		BitmapFactory.Options option = new BitmapFactory.Options();
		bmp = BitmapFactory.decodeResource( getResources(), resid, option );
		x = option.outWidth / 2;
		y = option.outHeight / 2;
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		
		BitmapFactory.Options option = new BitmapFactory.Options();
		bmp = BitmapFactory.decodeResource( getResources(), resId, option );
		x = option.outWidth / 2;
		y = option.outHeight / 2;
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i("RotateImageView", "touch");
		
		//현재 터치된 좌표
		float x1 = event.getX();
		float y1 = event.getY();
		
		//터치 시작할때 한번 감지(현재 룰렛의 회전정도를 이용 보정할 fixDegree계산)
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			Log.i("ACTION", "DOWN");
			radi = Math.atan2( y1-y, x1-x );	
			fixDegree = (float) Math.toDegrees(radi);
			if( fixDegree < 0 ) fixDegree = DEGREE180 + ( DEGREE180 + fixDegree );
		}
			
		//보정값(fixDegree)를 이용 화면에 표시할 룰렛 회전 degree계산
		radi = Math.atan2( y1-y, x1-x );
		degree = (float) Math.toDegrees(radi);
		if( degree < 0 ) degree = DEGREE180 + ( DEGREE180 + degree );
		degree = ( DEGREE360 - degree ) - ( DEGREE360 - degree - rouletteDegree[currentIndex] ) + ( degree - fixDegree );
		
		//터치를 중단할때 가장 가까운 부분으로 판 배치
		if(event.getAction() == MotionEvent.ACTION_UP){
			float def = 0;
			float defMin = DEGREE360;
			currentIndex = 0;
			
//			float startDegree = degree;
			
			degree %= DEGREE360;
			if(degree<0) degree = DEGREE360+degree;
			for(int i=0; i<maxRoulette; i++){
				def = Math.abs(degree - rouletteDegree[i]);
				if(defMin>def){
					defMin = def;
					currentIndex = i;
				}
			}
			if(def<(Math.abs(degree - DEGREE360))){
				degree = rouletteDegree[currentIndex];
			}else{
				currentIndex = 0;
				degree = rouletteDegree[currentIndex];
			}
			
        	Log.i("ACTION", "UP");
        	
        	if ( listener != null ){
        		listener.onChanged(this, currentIndex);
        	}
        	
        }     
				
		//계산된 degree만큼 회전
		Log.i("RotateImageView", "Degree = "+degree);
        matrix.setRotate(degree, x, y);        
        invalidate();     
        
        return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
		Log.i("onDraw", "onDraw()");
		
		if(bmp != null)
			canvas.drawBitmap(bmp, matrix, null);
	}
	
	
	/**
	 * 룰렛분할갯수지정(0보다 큰값), default value = 8;
	 * @param num
	 */
	public void setMaxRoulette(int num){
		if(num < 1) num = 8;
		maxRoulette = num;
		rouletteDegree = new float[maxRoulette];
		
		for(int i=0; i<num; i++){
			rouletteDegree[i] = (DEGREE360 / num) * i;
		}
	}
	
}
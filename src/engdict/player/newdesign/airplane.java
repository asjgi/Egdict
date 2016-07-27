package engdict.player.newdesign;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
  
 public class airplane extends View
 {
     Paint  paint;
     Bitmap bitmap;
     int    X;
     int    canvasSize; 
    int    bitmapWidth; 
 
  @SuppressWarnings("deprecation")
  public airplane( Context context, AttributeSet attrs )
     {
         super( context, attrs );
  
         WindowManager wm = (WindowManager)context.getSystemService( Context.WINDOW_SERVICE );
         Display display = wm.getDefaultDisplay();
         canvasSize = display.getWidth();

         bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.myairplane);       
         bitmapWidth = bitmap.getWidth();
         X = 0;	 
     }
  
     @Override
     protected void onDraw( Canvas canvas )
     {
         super.onDraw( canvas );
         canvas.drawBitmap( bitmap, X, 0, paint );
         invalidate();
     }
  
     public void move( int total, int current )
     {
         int dx = (((canvasSize-bitmapWidth)*current)/total);		 

        if ( dx > 0 && dx < ( canvasSize - bitmapWidth ) )
         {
             X = dx;
         }
  
     }
 }

package com.jmg.lib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

public class BorderedTextView extends TextView {
    private Paint paint = new Paint();
    public static final int BORDER_TOP = 0x00000001;
    public static final int BORDER_RIGHT = 0x00000002;
    public static final int BORDER_BOTTOM = 0x00000004;
    public static final int BORDER_LEFT = 0x00000008;

    public boolean showBorders;

    public BorderedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BorderedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BorderedTextView(Context context) {
        super(context);
        init();
    }
    
    public void setShowBorders(boolean showBorders)
    {
    	this.showBorders = showBorders;
    	this.invalidate();
    }
    
    private void init(){
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(4);        
    }
    @Override
    protected void onDraw(Canvas canvas) 
    {
        super.onDraw(canvas);
        if(!showBorders) return;
        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), 6, 6, paint);
    }

}
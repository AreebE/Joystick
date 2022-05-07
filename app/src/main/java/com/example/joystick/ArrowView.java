package com.example.joystick;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class ArrowView extends View {

    private double ratio;
    private boolean isVertical;
    private int colorToUse;
    public ArrowView(Context context) {
        super(context);
        ratio = 0;
        isVertical = false;
        colorToUse = context.getResources().getColor(R.color.white);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int width = getWidth();
        int height = getHeight();
        Paint paint = new Paint();
        paint.setColor(colorToUse);
        int xCenter = width / 2;
        int yCenter = height / 2;
        int circleRadius = ((isVertical)? width: height) / 2;
        canvas.drawArc(xCenter - circleRadius,
                yCenter - circleRadius,
                xCenter + circleRadius,
                yCenter + circleRadius,
                0,
                360,
                true, paint);
        paint.setStrokeWidth((isVertical)? width / 4 : height / 4);
        canvas.drawLine(
                xCenter,
                yCenter,
                (isVertical)? xCenter                               :   (float) (xCenter + ratio * xCenter),
                (isVertical)? (float) (yCenter + ratio * yCenter)   :   yCenter,
                paint
        );
    }

    public void setRatio(double ratio)
    {
        this.ratio = ratio;
        this.invalidate();

    }

    public void setColor(int colorCode)
    {
        this.colorToUse = colorCode;
    }

    public void setVertical(boolean isVertical)
    {
        this.isVertical = isVertical;
    }
}

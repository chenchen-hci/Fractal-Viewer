package uk.ac.nottingham.chen_chen.fractalviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * The FractalView class provides essential methouds for background calculation and
 * painting fractal image
 * @Author: Chen Chen
 * First Android project created on April 2016 for Web Based Computing (H63JAV), University of Nottingham
 */

public class FractalView extends View {
    public Bitmap image;
    private Paint paint;

    private double x0 = -2.5;
    private double x1 = +2.5;
    private double y0 = -2.5;
    private double y1 = +1.5;

    private int i0, j0, i1, j1, i1_second, j1_second;
    private int i0_drag, j0_drag, i1_drag, j1_drag;

    private boolean isdrag = false;
    private int mode = Mode.NOTHING;

    public FractalView(Context context) {
        super(context);
        paint = new Paint();
    }

    public FractalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public void reset(){
        this.x0 = -2.5;
        this.x1 = +2.5;
        this.y0 = -2.5;
        this.y1 = +1.5;
        updateImage();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(image, 0, 0, paint);
        paint.setColor(Color.BLACK);
        if(isdrag == true && mode == Mode.DRAG)
            canvas.drawRect(Math.min(i0,i0_drag), Math.min(j0,j0_drag), Math.max(i0,i0_drag), Math.max(j0,j0_drag),paint);
        if(isdrag == true && mode == Mode.PINCH)
            canvas.drawRect(Math.min(i0_drag,i1_drag), Math.min(j0_drag,j1_drag), Math.max(i0_drag,i1_drag), Math.max(j0_drag,j1_drag),paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        image = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        updateImage();
    }

    public void updateImage(){
        int w = getWidth();
        int h = getHeight();
        image = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        for(int i = 0; i < w; ++i){
            for(int j = 0; j < h; ++j){
                double x = x0 + i/(double)w * (x1 - x0);
                double y = y0 + (h - j) / (double)h * (y1 - y0);
                double zx = 0;
                double zy = 0;
                int iter = 512;
                while(zx * zx + zy * zy < 4 && iter > 0){
                    double zx_new = zx * zx - zy * zy + x;
                    double zy_new = 2 * zx * zy + y;
                    zx = zx_new;
                    zy = zy_new;
                    --iter;
                }
                image.setPixel(i, j, iter | (iter << 8) | (iter << 16));
            }
        }
    }

    public void setViewPort(double _i0, double _j0, double _i1, double _j1){
        int w = getWidth();
        int h = getHeight();

        double x0_new = x0 + _i0 / (double)w * (x1 - x0);
        double y0_new = y0 + (h - _j0) / (double)h * (y1 - y0);
        double x1_new = x0 + _i1 / (double)w * (x1 - x0);
        double y1_new = y0 + (h - _j1) / (double)h * (y1 - y0);

        x0 = Math.min(x0_new, x1_new);
        x1 = Math.max(x0_new,x1_new);
        y0 = Math.min(y0_new, y1_new);
        y1 = Math.max(y0_new, y1_new);

        updateImage();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction() & MotionEvent.ACTION_MASK) {
            // first flinger down
            case MotionEvent.ACTION_DOWN:
                mode  = Mode.DRAG;
                isdrag = false;
                i0 = (int) event.getX();
                j0 = (int) event.getY();
                System.out.println("First finger down: " + i0 + ", " + j0);
                break;

            // first finger up
            case MotionEvent.ACTION_UP:
                mode = Mode.NOTHING;
                isdrag = false;
                i1 = (int) event.getX();
                j1 = (int) event.getY();
                System.out.println("First finger up: " + i1 + ", " + j1);
                setViewPort(i0, j0, i1, j1);
                break;

            // second finger down
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = Mode.PINCH;
                isdrag = false;
                break;

            // sencond finger up
            case MotionEvent.ACTION_POINTER_UP:
                mode = Mode.NOTHING;
                isdrag = false;
                i1_second = (int) event.getX();
                j1_second = (int) event.getY();
                setViewPort(i1, j1, i1_second, j1_second);
                break;

            case MotionEvent.ACTION_MOVE:
                isdrag = true;

                // get first finger
                if(mode == Mode.DRAG){
                    System.out.println("now is draging");
                    i0_drag = (int) event.getX();
                    j0_drag = (int) event.getY();
                }

                if(mode == Mode.PINCH){
                    System.out.println("now is pinching");
                    i0_drag = (int) event.getX(0);
                    j0_drag = (int) event.getY(0);

                    // get second finger
                    i1_drag = (int) event.getX(1);
                    j1_drag = (int) event.getY(1);
                }
                break;
            default: break;
        }
        return true;
    }
}

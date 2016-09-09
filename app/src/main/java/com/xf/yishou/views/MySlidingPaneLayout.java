package com.xf.yishou.views;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by xsp on 2016/9/5.
 *
 */
public class MySlidingPaneLayout extends SlidingPaneLayout{
    private float mInitialMotionX;
    private float mEdgeSlop;

    public MySlidingPaneLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        ViewConfiguration config = ViewConfiguration.get(context);
        mEdgeSlop = config.getScaledEdgeSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

         switch(MotionEventCompat.getActionMasked(ev)){
             case MotionEvent.ACTION_DOWN:
                 mInitialMotionX = ev.getX();
                 break;

             case MotionEvent.ACTION_MOVE:
                 final float x = ev.getX();
                 final float y = ev.getY();

                 if(mInitialMotionX > mEdgeSlop && !isOpen() && canScroll(this,false,
                         Math.round(x - mInitialMotionX),Math.round(x),Math.round(y))){
                    return false;
                 }
         }
        //return true;
        return super.onInterceptTouchEvent(ev);
    }
}

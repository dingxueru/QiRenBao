package com.xiumi.qirenbao.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 作者 ：Created by DXR on 2017/2/18.
 * 自定义上下不滚动的Grid View
 */
public class CustomerGridView extends GridView {
        public CustomerGridView(Context context) {
            super(context);
        }

        public CustomerGridView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public CustomerGridView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
        /**
         * 设置上下不滚动
         */
        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if(ev.getAction() == MotionEvent.ACTION_MOVE) {
                return true;//true:禁止滚动  
            }
            return super.dispatchTouchEvent(ev);
        }

        /**
         * 重写该方法，达到使ListView适应ScrollView的效果
         */
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        }
}

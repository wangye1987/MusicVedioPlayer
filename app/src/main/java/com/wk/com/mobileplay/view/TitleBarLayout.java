package com.wk.com.mobileplay.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wk.com.mobileplay.R;

/**
 * Created by wangkui on 2016/11/21.
 */

public class TitleBarLayout extends LinearLayout {
    private Context context;
    public TitleBarLayout(Context context) {
        this(context,null);
    }

    public TitleBarLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        this.context = context;
    }

    public TitleBarLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view_search = this.getChildAt(1);
        View view_game = this.getChildAt(2);
        View view_history = this.getChildAt(3);
        view_search.setOnClickListener(new MyClickLister());
        view_game.setOnClickListener(new MyClickLister());
        view_history.setOnClickListener(new MyClickLister());
    }


    public class MyClickLister implements  OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.lv_seach:
                    Toast.makeText(context,"搜索",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.iv_msg:
                    Toast.makeText(context,"消息",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.iv_histroy:
                    Toast.makeText(context,"历史记录",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}

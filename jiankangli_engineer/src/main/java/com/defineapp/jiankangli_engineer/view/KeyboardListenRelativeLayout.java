package com.defineapp.jiankangli_engineer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class KeyboardListenRelativeLayout extends LinearLayout {

    private static final String TAG = KeyboardListenRelativeLayout.class.getSimpleName();

    public static final byte KEYBOARD_STATE_SHOW = -3;//键盘显示出来的时候
    public static final byte KEYBOARD_STATE_HIDE = -2;//键盘隐藏的时候
    public static final byte KEYBOARD_STATE_INIT = -1;//键盘初始化的时候

    private boolean mHasInit = false;//判断是否初始化了
    private boolean mHasKeyboard = false;//是否有键盘
    private int mHeight;

    private IOnKeyboardStateChangedListener onKeyboardStateChangedListener;

    public KeyboardListenRelativeLayout(Context context) {
        super(context);
    }

    public KeyboardListenRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardListenRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnKeyboardStateChangedListener(IOnKeyboardStateChangedListener onKeyboardStateChangedListener) {
        this.onKeyboardStateChangedListener = onKeyboardStateChangedListener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!mHasInit) {
            mHasInit = true;
            mHeight = b;
            onKeyboardStateChanged(KEYBOARD_STATE_INIT);
        } else {
            mHeight = mHeight < b ? b : mHeight;
        }

        if (mHasInit && mHeight > b) {
            mHasKeyboard = true;
            onKeyboardStateChanged(KEYBOARD_STATE_SHOW);
        }
        if (mHasInit && mHasKeyboard && mHeight == b) {
            mHasKeyboard = false;
            onKeyboardStateChanged(KEYBOARD_STATE_HIDE);
        }
    }

    private void onKeyboardStateChanged(final int keyboardStateHide) {

        if (onKeyboardStateChangedListener != null){

            post(new Runnable() {
                @Override
                public void run() {
                    onKeyboardStateChangedListener.onKeyboardStateChanged(keyboardStateHide);
                }
            });
        }
    }

    public interface IOnKeyboardStateChangedListener {
        public void onKeyboardStateChanged(int state);
    }
}  
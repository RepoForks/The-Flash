package com.fastaccess.tfl.ui.widget;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.fastaccess.tfl.helper.TypeFaceHelper;
import com.fastaccess.tfl.helper.ViewHelper;


/**
 * Created by Kosh on 8/18/2015. copyrights are reserved
 */
public class FontEditTextView extends AppCompatEditText {

    public FontEditTextView(Context context) {
        super(context);
        init();
    }

    public FontEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (isInEditMode()) return;
        TypeFaceHelper.applyTypeface(this);
    }

    public void setTextColor(@ColorRes int normalColor, @ColorRes int pressedColor) {
        int nColor = getResources().getColor(normalColor);
        int pColor = getResources().getColor(pressedColor);
        setTextColor(ViewHelper.textSelector(nColor, pColor));
    }
}

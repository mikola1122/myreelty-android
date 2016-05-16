package com.helio.myreelty.common.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.helio.myreelty.R;


/**
 * Created by admin on 10/20/15.
 */
public class FontButton  extends Button {

    private Typeface mFontTypeface;

    public FontButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initNewFontIfPresentIn(attrs);
    }

    public FontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initNewFontIfPresentIn(attrs);

    }

    public FontButton(Context context) {
        super(context);
        initNewFontIfPresentIn(null);
    }

    private void initNewFontIfPresentIn(AttributeSet attrs) {
        if (attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FontButton);
            String fontName = a.getString(R.styleable.FontButton_button_fontName);
            if (fontName!=null) {
                mFontTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/"+fontName);
            }
            a.recycle();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mFontTypeface !=null)
            setTypeface(mFontTypeface);
    }
}

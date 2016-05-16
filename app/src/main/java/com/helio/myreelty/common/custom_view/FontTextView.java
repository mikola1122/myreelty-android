package com.helio.myreelty.common.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.helio.myreelty.R;


/**
 * Created by admin on 10/16/15.
 */
public class FontTextView extends TextView {

    private Typeface mTypeface;

    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initNewFontIfPresentIn(attrs);
    }

    /**
     * http://www.techrepublic.com/article/pro-tip-extend-androids-textview-to-use-custom-fonts/
     */
    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initNewFontIfPresentIn(attrs);

    }

    /**
     * http://www.techrepublic.com/article/pro-tip-extend-androids-textview-to-use-custom-fonts/
     */
    public FontTextView(Context context) {
        super(context);
        initNewFontIfPresentIn(null);
    }

    private void initNewFontIfPresentIn(AttributeSet attrs) {
        if (attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FontTextView);
            String fontName = a.getString(R.styleable.FontTextView_fontName);
            if (fontName!=null) {
                mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/"+fontName);
            }
            a.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    public boolean onPreDraw() {
        return super.onPreDraw();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTypeface!=null)
            setTypeface(mTypeface);
    }
}

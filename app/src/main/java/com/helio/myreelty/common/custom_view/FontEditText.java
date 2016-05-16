package com.helio.myreelty.common.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

import com.helio.myreelty.R;


/**
 * Created by admin on 10/20/15.
 */
public class FontEditText extends EditText {

    private Typeface mFontTypeface;


    public FontEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initNewFontIfPresentIn(attrs);
    }

    public FontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initNewFontIfPresentIn(attrs);

    }

    public FontEditText(Context context) {
        super(context);
        initNewFontIfPresentIn(null);
    }

    private void initNewFontIfPresentIn(AttributeSet attrs) {
        if (attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FontTextView);
            String fontName = a.getString(R.styleable.FontTextView_fontName);
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


    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection conn = super.onCreateInputConnection(outAttrs);
        outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        return conn;
    }
}

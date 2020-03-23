package com.mark.sharee.widgets;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.TextViewCompat;

import com.example.sharee.R;
import com.mark.sharee.utils.FontManager;

public class ShareeTextView extends AppCompatTextView {

    public ShareeTextView(Context context) {
        this(context, null);
    }

    public ShareeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShareeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            FontManager.forAttrs(R.styleable.ShareeTextView, R.styleable.ShareeTextView_sharee_font)
                    .apply(this, attrs, defStyle);
        }
    }

    public void setAutoSize() {
        TextViewCompat.setAutoSizeTextTypeWithDefaults(this, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
    }

    public void disabeAutoSize() {
        TextViewCompat.setAutoSizeTextTypeWithDefaults(this, TextViewCompat.AUTO_SIZE_TEXT_TYPE_NONE);
    }
}

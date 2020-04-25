package com.mark.sharee.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.cardview.widget.CardView
import com.example.sharee.R
import kotlinx.android.synthetic.main.home_button.view.*

class ShareeImageButton : CardView {

    constructor(context: Context) : super(context) { initialize(context) }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context)
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(context)
        initAttrs(attrs)
    }

    private fun initialize(context: Context) {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.home_button, this, true)
    }

    private fun initAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShareeImageButton)
        imageBtn.setImageResource(typedArray.getResourceId(R.styleable.ShareeImageButton_image_src, R.drawable.ic_logo_round))
        textView.text = typedArray.getString(R.styleable.ShareeImageButton_text)

        typedArray.recycle()
    }
}
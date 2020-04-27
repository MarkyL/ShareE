package com.mark.sharee.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.sharee.R
import kotlinx.android.synthetic.main.profile_image_view_layout.view.*

class ProfileImageView : ConstraintLayout {

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
        inflater.inflate(R.layout.profile_image_view_layout, this, true)
    }

    private fun initAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProfileImageView)
        profileImage.setImageResource(typedArray.getResourceId(R.styleable.ProfileImageView_profile_image, R.drawable.ic_logo_round))
        profileName.text = typedArray.getString(R.styleable.ProfileImageView_profile_name)
        profileRole.text = typedArray.getString(R.styleable.ProfileImageView_profile_role)
        typedArray.recycle()
    }
}
package com.yusril.storyapp.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.yusril.storyapp.R

open class MyEditText : AppCompatEditText, View.OnTouchListener {
    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private lateinit var border : Drawable

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        background = border
        setPaddingRelative(48, 48, 48, 48)
        maxLines = 1
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        return false
    }

    private fun init() {
        border = ContextCompat.getDrawable(context, R.drawable.border) as Drawable
        setOnTouchListener(this)
    }
}
package com.krendel.neusfeet.screens.common.android_views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import android.widget.CompoundButton
import android.widget.RelativeLayout
import com.krendel.neusfeet.R

class CheckableRelativeLayout : RelativeLayout, Checkable {

    private var isChecked = false
    private var mOnCheckedChangeListener: CompoundButton.OnCheckedChangeListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        checkAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        checkAttributes(attrs)
    }

    private fun checkAttributes(attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CheckableRelativeLayout)
        try {
            setChecked(typedArray.getBoolean(R.styleable.CheckableRelativeLayout_checked, false))
        } finally {
            typedArray.recycle()
        }
    }

    override fun isChecked() = isChecked

    override fun toggle() {
        setChecked(!isChecked)
    }

    override fun setChecked(checked: Boolean) {
        this.isChecked = checked
        invalidate()
        refreshDrawableState()
        mOnCheckedChangeListener?.onCheckedChanged(null, isChecked)
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes.
     *
     * @param listener the callback to call on checked state change
     */
    fun setOnCheckedChangeListener(listener: CompoundButton.OnCheckedChangeListener?) {
        mOnCheckedChangeListener = listener
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked) {
            View.mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        }
        return drawableState
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        val drawable = background
        if (drawable != null) {
            val myDrawableState = drawableState
            drawable.state = myDrawableState
            invalidate()
        }
    }

    companion object {
        private val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)
    }

}
package com.sproject.winlink.presentation.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.core.view.doOnLayout
import com.google.android.material.card.MaterialCardView
import com.sproject.winlink.R
import com.sproject.winlink.databinding.SliderBinding

class ResizeWidthAnimation(private val mView: View, private val mWidth: Int) : Animation() {
    private val mStartWidth: Int = mView.width

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        mView.layoutParams.width = mStartWidth + ((mWidth - mStartWidth) * interpolatedTime).toInt()
        mView.requestLayout()
    }

    override fun willChangeBounds(): Boolean {
        return true
    }
}

typealias OnValueChangeListener = (Int) -> Unit

class Slider(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding: SliderBinding

    private var sliderWidth = 0

    private var listener: OnValueChangeListener? = null

    var value: Int = 0
        set(value) {
            field = value

            updateValue()
        }

    constructor(context: Context, attrs: AttributeSet?, defStyleRes: Int) : this(
        context,
        attrs,
        defStyleRes,
        0
    )

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.slider, this, true)

        binding = SliderBinding.bind(this)
        initializeAttributes(attrs, defStyleAttr, defStyleRes)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initializeAttributes(
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.Slider,
            defStyleAttr,
            defStyleRes
        )

        with(binding) {
            value = typedArray.getInteger(R.styleable.Slider_sliderValue, 0)

            val label = typedArray.getString(R.styleable.Slider_sliderLabel)
            val iconRes = typedArray.getResourceId(R.styleable.Slider_sliderIcon, 0)

            typedArray.recycle()

            sliderTitle.text = label

            sliderIcon.setImageResource(iconRes)

            doOnLayout {
                sliderWidth = measuredWidth

                updateValue()

                val progressWidth = (sliderWidth / 100.0 * value).toInt()

                sliderProgress.layoutParams =
                    sliderProgress.layoutParams.apply {
                        width = progressWidth
                    }
            }

            setOnTouchListener { _, event ->
                val x = event.x

                value = (x / sliderWidth * 100).toInt().coerceIn(0, 100)

                notifyListener()

                sliderProgress.visibility = if (value > 0) View.VISIBLE else View.GONE

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        animatedUpdateWidth(x.toInt())
                    }
                    MotionEvent.ACTION_MOVE -> {
                        sliderProgress.clearAnimation()

                        sliderProgress.layoutParams =
                            sliderProgress.layoutParams.apply {
                                width = x.toInt()
                            }
                    }
                }

                true
            }
        }
    }

    private fun animatedUpdateWidth(width: Int) {
        with(binding) {
            sliderProgress.layoutParams = sliderProgress.layoutParams.apply {
                val anim = ResizeWidthAnimation(
                    sliderProgress,
                    width
                ).apply {
                    duration = 250
                }

                sliderProgress.startAnimation(anim)
            }
        }
    }

    private fun notifyListener() {
        listener?.invoke(value)
    }

    private fun updateValue() {
        binding.sliderValue.text = "$value%"
    }

    fun setListener(listener: OnValueChangeListener?) {
        this.listener = listener
    }
}

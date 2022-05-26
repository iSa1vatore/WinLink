package com.sproject.winlink.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.sproject.winlink.R
import com.sproject.winlink.databinding.SquareButtonBinding

class SquareButton(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: SquareButtonBinding

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
        inflater.inflate(R.layout.square_button, this, true)

        binding = SquareButtonBinding.bind(this)
        initializeAttributes(attrs, defStyleAttr, defStyleRes)
    }

    private fun initializeAttributes(
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.SquareButton,
            defStyleAttr,
            defStyleRes
        )

        val iconRes = typedArray.getResourceId(R.styleable.SquareButton_buttonIcon, 0)

        binding.buttonIcon.setImageResource(iconRes)

        typedArray.recycle()
    }
}

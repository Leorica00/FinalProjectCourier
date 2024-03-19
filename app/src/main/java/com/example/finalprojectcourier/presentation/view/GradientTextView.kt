package com.example.finalprojectcourier.presentation.view

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.example.finalprojectcourier.R

class GradientTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var gradientStartColor = 0
    private var gradientEndColor = 0

    init {
        gradientStartColor = context.getColor(R.color.gradient_start_color)
        gradientEndColor = context.getColor(R.color.gradient_end_color)
    }

    override fun onSizeChanged(
        w: Int, h: Int, oldw: Int, oldh: Int
    ) {
        super.onSizeChanged(w, h, oldw, oldh)

        val shader = LinearGradient(
            0f, 0f, w.toFloat(), h.toFloat(),
            gradientStartColor, gradientEndColor,
            Shader.TileMode.CLAMP
        )

        paint.shader = shader
        invalidate()
    }
}
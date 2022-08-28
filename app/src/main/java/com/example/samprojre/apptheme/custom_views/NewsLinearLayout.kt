package com.example.samprojre.apptheme.custom_views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.samprojre.apptheme.Theme
import com.example.samprojre.apptheme.ThemeManager

class NewsLinearLayout @JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0

) : LinearLayout(context, attrs, defStyle), ThemeManager.ThemeChangedListener {
    override fun onFinishInflate() {
        super.onFinishInflate()
//        setBackgroundColor(
//            ContextCompat.getColor(
//                context,
//                ThemeManager.theme.viewGroupTheme.backgroundColor
//            )
//        )
        backgroundTintList =
            context.getColorStateList(ThemeManager.theme.viewGroupTheme.backgroundColor)
        ThemeManager.addListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ThemeManager.addListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        ThemeManager.removeListener(this)
    }

    override fun onThemeChanged(theme: Theme) {

    }

}
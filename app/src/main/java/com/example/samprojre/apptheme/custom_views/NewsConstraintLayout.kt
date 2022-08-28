package com.example.samprojre.apptheme.custom_views

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.samprojre.apptheme.Theme
import com.example.samprojre.apptheme.ThemeManager

class NewsConstraintLayout @JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle:Int = 0

): ConstraintLayout(context,attrs,defStyle), ThemeManager.ThemeChangedListener
{
    override fun onFinishInflate() {
        super.onFinishInflate()
        setBackgroundColor(
            ContextCompat.getColor(
                context,
                ThemeManager.theme.viewGroupTheme.backgroundColor
            ))
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
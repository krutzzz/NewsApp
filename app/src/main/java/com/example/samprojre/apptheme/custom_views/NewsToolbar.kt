package com.example.samprojre.apptheme.custom_views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.samprojre.apptheme.Theme
import com.example.samprojre.apptheme.ThemeManager
import com.example.samprojre.apptheme.ThemeManager.theme

class NewsToolbar
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle:Int = 0

    ):Toolbar(context,attrs,defStyle), ThemeManager.ThemeChangedListener
{
    override fun onFinishInflate() {
        super.onFinishInflate()
        setBackgroundColor(
            ContextCompat.getColor(
                context,
                theme.bottomNavigationViewTheme.backgroundColor
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
        setBackgroundColor(
            ContextCompat.getColor(
                context,
                theme.bottomNavigationViewTheme.backgroundColor
            ))
    }

}
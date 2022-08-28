package com.example.samprojre.apptheme.custom_views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.samprojre.apptheme.Theme
import com.example.samprojre.apptheme.ThemeManager

@SuppressLint("AppCompatCustomView")
class NewsTextView@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle:Int = 0

): TextView(context,attrs,defStyle), ThemeManager.ThemeChangedListener
{
    override fun onFinishInflate() {
        super.onFinishInflate()
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
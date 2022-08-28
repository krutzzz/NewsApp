package com.example.samprojre.apptheme.custom_views

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.example.samprojre.R
import com.example.samprojre.apptheme.Theme
import com.example.samprojre.apptheme.ThemeManager
import com.google.android.material.bottomnavigation.BottomNavigationView


class NewsBottomNavigation
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : BottomNavigationView(context, attrs, defStyle), ThemeManager.ThemeChangedListener {

    override fun onFinishInflate() {
        super.onFinishInflate()
        setBackgroundColor(
            ContextCompat.getColor(
                context,
                ThemeManager.theme.bottomNavigationViewTheme.backgroundColor
            )
        )

//        val tintList = ColorStateList(
//            arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf()), intArrayOf(
//                Color.RED,
//                Color.BLUE
//            )
//        )
//
//        itemIconTintList = tintList;

        itemActiveIndicatorColor =
            ContextCompat.getColorStateList(
                context,ThemeManager.theme.bottomNavigationViewTheme.containerColor
            )

//        ThemeManager.theme.bottomNavigationViewTheme.containerColor
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
        itemActiveIndicatorColor =
            ContextCompat.getColorStateList(context, theme.bottomNavigationViewTheme.containerColor)
        setBackgroundColor(
            ContextCompat.getColor(
                context,
                theme.bottomNavigationViewTheme.backgroundColor
            )
        )
    }
}
package com.example.samprojre.apptheme

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.example.samprojre.apptheme.custom_views.NewsBottomNavigation
import com.example.samprojre.apptheme.custom_views.NewsConstraintLayout
import com.example.samprojre.apptheme.custom_views.NewsLinearLayout
import com.example.samprojre.apptheme.custom_views.NewsToolbar
import com.example.samprojre.utils.Constants

class MyLayoutInflater(
    private val delegate: AppCompatDelegate
) : LayoutInflater.Factory2 {

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        return when (name) {
            Constants.NEWS_BOTTOMNAVIGATION_NAME -> NewsBottomNavigation(context, attrs)
            Constants.NEWS_TOOLBAR_NAME -> NewsToolbar(context, attrs)
            Constants.NEWS_CONSTRAINT -> NewsConstraintLayout(context, attrs)
            Constants.NEWS_LINEAR -> NewsLinearLayout(context, attrs)
            else -> delegate.createView(parent, name, context, attrs)
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return onCreateView(null, name, context, attrs)
    }
}
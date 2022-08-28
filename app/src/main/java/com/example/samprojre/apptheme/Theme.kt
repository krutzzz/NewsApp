package com.example.samprojre.apptheme

import com.example.samprojre.R
import com.example.samprojre.apptheme.view_themes.BottomNavigationViewTheme
import com.example.samprojre.apptheme.view_themes.TextViewTheme
import com.example.samprojre.apptheme.view_themes.ToolbarTheme
import com.example.samprojre.apptheme.view_themes.ViewGroupTheme

enum class Theme(
    val textViewTheme: TextViewTheme,
    val viewGroupTheme: ViewGroupTheme,
    val bottomNavigationViewTheme: BottomNavigationViewTheme,
    val toolbarTheme: ToolbarTheme
) {


    BLUE(
        textViewTheme = TextViewTheme(
            textColor = android.R.color.black
        ),
        viewGroupTheme = ViewGroupTheme(
            backgroundColor = R.color.colorBlueSecondaryContainer
        ),
        bottomNavigationViewTheme = BottomNavigationViewTheme(
            backgroundColor = R.color.colorBlueTheme,
            containerColor = R.color.colorBlueSecondaryContainer
        ),
        toolbarTheme = ToolbarTheme(
            backgroundColor = R.color.colorBlueTheme
        )
    ),
    PINK(
        textViewTheme = TextViewTheme(
            textColor = android.R.color.black
        ),
        viewGroupTheme = ViewGroupTheme(
            backgroundColor =  R.color.colorPinkSecondaryContainer
        ),
        bottomNavigationViewTheme = BottomNavigationViewTheme(
            backgroundColor = R.color.colorPinkTheme,
            containerColor = R.color.colorPinkSecondaryContainer
        ),
        toolbarTheme = ToolbarTheme(
            backgroundColor = R.color.colorPinkTheme
        )
    ),
    BROWN(
        textViewTheme = TextViewTheme(
            textColor = android.R.color.black
        ),
        viewGroupTheme = ViewGroupTheme(
            backgroundColor =  R.color.colorBrownSecondaryContainer
        ),
        bottomNavigationViewTheme = BottomNavigationViewTheme(
            backgroundColor = R.color.colorBrownTheme,
            containerColor = R.color.colorBrownSecondaryContainer
        ),
        toolbarTheme = ToolbarTheme(
            backgroundColor = R.color.colorBrownTheme
        )
    ),
    GREEN(
        textViewTheme = TextViewTheme(
            textColor = android.R.color.black
        ),
        viewGroupTheme = ViewGroupTheme(
            backgroundColor = R.color.colorGreenSecondaryContainer
        ),
        bottomNavigationViewTheme = BottomNavigationViewTheme(
            backgroundColor = R.color.colorGreenTheme,
            containerColor = R.color.colorGreenSecondaryContainer
        ),
        toolbarTheme = ToolbarTheme(
            backgroundColor = R.color.colorGreenTheme
        )
    )
}
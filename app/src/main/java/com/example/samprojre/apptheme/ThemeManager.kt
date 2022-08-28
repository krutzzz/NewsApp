package com.example.samprojre.apptheme

object ThemeManager {


    var theme: Theme = Theme.BLUE
        set(value) {
            field = value
            listeners.forEach { listener -> listener.onThemeChanged(value) }
        }

    private val listeners = mutableSetOf<ThemeChangedListener>()


    interface ThemeChangedListener {

        fun onThemeChanged(theme: Theme)
    }

    fun addListener(listener: ThemeChangedListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: ThemeChangedListener) {
        listeners.remove(listener)
    }
}
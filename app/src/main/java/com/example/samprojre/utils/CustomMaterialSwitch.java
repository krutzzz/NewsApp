package com.example.samprojre.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.materialswitch.MaterialSwitch;

public class CustomMaterialSwitch extends MaterialSwitch {
    public CustomMaterialSwitch(@NonNull Context context) {
        super(context);
    }

    public CustomMaterialSwitch(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomMaterialSwitch(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(ev.getActionMasked() == MotionEvent.ACTION_MOVE){
            performClick();
            return true;
        }
        return false;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

}

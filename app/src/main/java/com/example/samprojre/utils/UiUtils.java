package com.example.samprojre.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.samprojre.R;
import com.example.samprojre.apptheme.custom_views.NewsTextView;

public class UiUtils {

    public static void showToastMessage(String message, LayoutInflater layoutInflater,Context context){
        Toast toast = new Toast(context);
        View toastView = layoutInflater.inflate(R.layout.toast_message_news_details, null);
        NewsTextView newsTextView = toastView.findViewById(R.id.toastTextView);
        newsTextView.setText(message);
        toast.setView(toastView);
        toast.show();
    }

}

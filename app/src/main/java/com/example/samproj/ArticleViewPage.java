package com.example.samproj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ArticleViewPage extends AppCompatActivity {

    LinearLayout linearLayout;                      //DynamicLayout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view_page);
        linearLayout=findViewById(R.id.Page);


        for(int i=0;i<2;i++){                           //Practice with dynamic layouts
            TextView textView=new TextView(this);
            textView.setText("Text View" +String.valueOf(i));
            linearLayout.addView(textView);

        }

    }
}

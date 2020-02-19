package com.example.samproj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.CollapsingToolbarLayout;

public class ArticleViewPage extends AppCompatActivity {

    CoordinatorLayout CoordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;

                                        //DynamicLayout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view_page);
        CoordinatorLayout=findViewById(R.id.Page);
        collapsingToolbarLayout= findViewById(R.id.collapsingToolBar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //for(int i=0;i<2;i++){                           //Practice with dynamic layouts
        //    TextView textView=new TextView(this);
        //   textView.setText("Text View" +String.valueOf(i));
        //    linearLayout.addView(textView);
        // }

    }
}

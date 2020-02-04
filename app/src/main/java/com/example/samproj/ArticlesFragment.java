package com.example.samproj;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArticlesFragment extends Fragment implements ArticlesAdapter.ViewHolder.OnItemListener {

    private static final String TAG ="Tester" ;

    public ArticlesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_articles, container, false);

        List<Article> articles=new ArrayList<>();
        articles.add(new Article("FirstArticle","It is the first article(actually it is not an article)", R.drawable.ic_news));
        articles.add(new Article("FirstArticle","It is the first article(actually it is not an article)", R.drawable.ic_news));
        articles.add(new Article("FirstArticle","It is the first article(actually it is not an article)", R.drawable.ic_news));
        articles.add(new Article("FirstArticle","It is the first article(actually it is not an article)", R.drawable.ic_news));
        articles.add(new Article("FirstArticle","It is the first article(actually it is not an article)", R.drawable.ic_news));
        articles.add(new Article("FirstArticle","It is the first article(actually it is not an article)", R.drawable.ic_news));
        articles.add(new Article("FirstArticle","It is the first article(actually it is not an article)", R.drawable.ic_news));
        articles.add(new Article("FirstArticle","It is the first article(actually it is not an article)", R.drawable.ic_news));
        articles.add(new Article("FirstArticle","It is the first article(actually it is not an article)", R.drawable.ic_news));
        
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.articles_list);
        ArticlesAdapter articlesAdapter=new ArticlesAdapter(articles,this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(articlesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));




        return view;
    }

    @Override
    public void OnItemClickListener(int position) {
       // Log.d(TAG, "OnItemClickListener: clicked"+position);
        Intent intent=new Intent(getContext(),Tester.class);
        startActivity(intent);
    }

    @Override
    public void OnIconClickListener(int position) {
        Toast toast = Toast.makeText(getContext(),
                "Downloading", Toast.LENGTH_SHORT);
        toast.show();
    }
}

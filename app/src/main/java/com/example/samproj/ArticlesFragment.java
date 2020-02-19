package com.example.samproj;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import com.example.samproj.mservice.NewsClient;
import com.example.samproj.mservice.ServiceGenerator;
import com.example.samproj.model.News;
import com.example.samproj.model.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArticlesFragment extends Fragment /*implements ArticlesAdapter.ViewHolder.OnItemListener*/ {

    public static final String API_KEY = "2421bc6e-ed0b-4ee1-9566-ce17c4240633";
    public static final String API_REQUEST_TYPE = "article";
    public static final String API_BLOCK_TYPE = "body";
    public static final int BODY_SUMMARY_TEXT_LENGTH_SHORT = 150;
    public static final int BODY_SUMMARY_TEXT_LENGTH_LONG = 400;
    Button refreshButton;
    ImageView refresh_animation;
    SwipeRefreshLayout swipeRefreshLayout;


    public ArticlesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_articles, container, false);

        // Find reference to the bottom app bar and set it up as a regular app bar
        //BottomAppBar bar = findViewById(R.id.bar);
        //setSupportActionBar(bar);

        // Create and setup the navigation drawer
        // onCreateNavigationDrawer();

        // Get the current network connectivity info and call make the base call accordingly
        makeInitialCallWithConnectivityCheck(view);
        swipeRefreshLayout=view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        makeInitialCallWithConnectivityCheck(view);
                    }
                }, 4000);
            }
        });

        //List<Article> articles=new ArrayList<>();
        //articles.add(new Article("FirstArticle","It is the first article(actually it is not an article)", R.drawable.ic_news));
        //articles.add(new Article("FirstArticle","It is the first article(actually it is not an article)", R.drawable.ic_news));
        //articles.add(new Article("FirstArticle","It is the first article(actually it is not an article)", R.drawable.ic_news));
        //articles.add(new Article("FirstArticle","It is the first article(actually it is not an article)", R.drawable.ic_news));
        //articles.add(new Article("FirstArticle","It is the first article(actually it is not an article)", R.drawable.ic_news));
        //articles.add(new Article("FirstArticle","It is the first article(actually it is not an article)", R.drawable.ic_news));
        //articles.add(new Article("FirstArticle","It is the first article(actually it is not an article)", R.drawable.ic_news));
        //articles.add(new Article("FirstArticle","It is the first article(actually it is not an article)", R.drawable.ic_news));
        //articles.add(new Article("FirstArticle","It is the first article(actually it is not an article)", R.drawable.ic_news));
        
      //  RecyclerView recyclerView=view.findViewById(R.id.articles_list);
      //  ArticlesAdapter articlesAdapter=new ArticlesAdapter(articles,this);
      //  RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
      //  itemAnimator.setAddDuration(1000);
      //  itemAnimator.setRemoveDuration(1000);
      //  recyclerView.setItemAnimator(itemAnimator);
      //  recyclerView.setAdapter(articlesAdapter);
      //  recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));




        return view;
    }

    /*@Override
    public void OnItemClickListener(int position) {
        Intent intent=new Intent(getContext(),ArticleViewPage.class);
        startActivity(intent);
    }

    @Override
    public void OnIconClickListener(int position) {

    }*/

    /*@Override
    public void OnIconClickListener(int position) {
        Toast toast = Toast.makeText(getContext(),
                "Downloading", Toast.LENGTH_SHORT);
        toast.show();
    }*/

    private void makeInitialCallWithConnectivityCheck(final View view){
        // Create a connectivity manager and get the active network information

        ConnectivityManager connManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            makeBaseNetworkCall(view);
        }
        else {
            final TextView noConnectionTextView = view.findViewById(R.id.catalog_hint_no_available_network_connection);
            noConnectionTextView.setVisibility(View.VISIBLE);
            refreshButton= view.findViewById(R.id.refresh_button);
            refreshButton.setVisibility(View.VISIBLE);
            hideSpinnerFromCatalog(view);
            refreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noConnectionTextView.setVisibility(View.INVISIBLE);
                    refreshButton.setVisibility(View.INVISIBLE);
                    makeBaseNetworkCall(view);




                }
            });

        }
    }

    private void makeBaseNetworkCall(final View view){
        final ListView listView = view.findViewById(R.id.articles_list);
        //final Handler handler = new Handler();

        // Create a Retrofit client with the ServiceGenerator class
        NewsClient client = ServiceGenerator.createService(NewsClient.class);

        Call<News> call = client.getBaseJson(API_KEY, API_REQUEST_TYPE, API_BLOCK_TYPE);

        // Call the endpoint and respond to failure and success events
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                // Parse the response to match the List of JSONArray objects
                List<Result> results = response.body().getResponse().getResults();

                hideSpinnerFromCatalog(view);


                // Set the adapter and remove the divider between the list items
                NewsAdapter adapter = new NewsAdapter(getContext(), results);
                listView.setDividerHeight(0);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {


                hideSpinnerFromCatalog(view);
                Toast.makeText(getContext(), "There was a problem with the network call", Toast.LENGTH_SHORT).show();
                Log.e("The throwable is", t.toString());
            }
        });
    }

    /**
     * Simple helper method to hide the loading spinner from the catalog activity
     */
    private void hideSpinnerFromCatalog(View view){
        ProgressBar spinner = view.findViewById(R.id.loading_spinner);
        spinner.setVisibility(View.GONE);
    }

}

package com.example.samprojre.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.samprojre.R;
import com.example.samprojre.apptheme.Theme;
import com.example.samprojre.base_adapter.BaseAdapter;
import com.example.samprojre.base_adapter.ListAdapterItem;
import com.example.samprojre.screens.settings_screens.settings_screen.AdapterSourcesSettings;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewBindingAdapters {

    @BindingAdapter("setAutoCompleteVerificationState")
    public static void setAutoCompleteVerificationState(TextInputLayout autoCompleteTextContainer, boolean state) {
        if (!state) {
            //Show error style
            Log.d("setAutoComplete", "setAutoCompleteVerificationState: error");
            autoCompleteTextContainer.setErrorEnabled(true);
            autoCompleteTextContainer.setError("Not found");
        } else {
            Log.d("setAutoComplete", "setAutoCompleteVerificationState: no error");
            //Show default style
            autoCompleteTextContainer.setErrorEnabled(false);
            autoCompleteTextContainer.setError("");
        }
    }

    @BindingAdapter("setTopHeadlines")
    public static void setTopHeadlines(TextView textView, int position) {
        if (position == 0) {
            textView.setVisibility(View.VISIBLE);
        }
    }

    @BindingAdapter("setImageState")
    public static void setImageState(ImageView imageView, boolean state) {
        Context context = imageView.getContext();
        if (state) {
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            imageView.setColorFilter(ContextCompat.getColor(context, R.color.text_preferences), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    @BindingAdapter("setViewVisibility")
    public static void setViewVisibility(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @BindingAdapter("textColorSetter")
    public static void colorSetter(TextView textView, boolean isChecked) {
        Resources resources = textView.getResources();
        if (isChecked) {
            textView.setTextColor(resources.getColor(R.color.black));
        } else {
            textView.setTextColor(resources.getColor(R.color.text_preferences));
        }
    }

    @BindingAdapter("setAdapter")
    public static void setAdapter(RecyclerView recyclerView,
                                  @Nullable BaseAdapter<ViewDataBinding, ListAdapterItem> adapter) {
        if (adapter != null) {
            recyclerView.setAdapter(adapter);
        }
    }

    @SuppressWarnings("UNCHECKED_CAST")
    @BindingAdapter("submitList")
    public static void submitList(RecyclerView recyclerView, @Nullable List<? extends ListAdapterItem> list) {
        BaseAdapter<ViewDataBinding, ListAdapterItem> adapter =
                (BaseAdapter<ViewDataBinding, ListAdapterItem>) recyclerView.getAdapter();

        if (adapter != null) {

            adapter.updateData(list);
        }
    }

    @BindingAdapter("disableRecyclerView")
    public static void disableRecyclerView(RecyclerView recyclerView, boolean state){
        AdapterSourcesSettings adapter = (AdapterSourcesSettings) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setEnabled(state);
            adapter.notifyDataSetChanged();
        }
    }

    @BindingAdapter(value = {"setImage", "progressView"}, requireAll = false)
    public static void setImage(ImageView imageView, String imageUrl, ProgressBar progressBar) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .placeholder(Utils.getRandomDrawableColor())
                .error(Utils.getRandomDrawableColor())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@androidx.annotation.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    @BindingAdapter(value = {"setDownloadsImage", "progressDownloadsView"}, requireAll = false)
    public static void setDownloadsImage(ImageView imageView, String img, ProgressBar progressBar) {
        Glide.with(imageView.getContext())
                .load(new File(img))
                .placeholder(Utils.getRandomDrawableColor())
                .error(Utils.getRandomDrawableColor())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@androidx.annotation.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    @BindingAdapter("setTime")
    public static void setTime(TextView textView, String publishedAt) {
        textView.setText(String.format(" • %s", Utils.DateToTimeFormat(publishedAt)));
    }

    @BindingAdapter("setDate")
    public static void setDate(TextView textView, String date) {
        textView.setText(Utils.DateFormat(date));
    }

    @BindingAdapter("setImageDetails")
    public static void setImageDetails(ImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .error(Utils.getRandomDrawableColor())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    @BindingAdapter("setImageDownloadedDetails")
    public static void setImageDownloadedDetails(ImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext())
                .load(new File(imageUrl))
                .error(Utils.getRandomDrawableColor())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    @BindingAdapter(value = {"setWebPage", "nestedScrollView", "progressBar"}, requireAll = false)
    public static void setWebPage(WebView webView, String url, NestedScrollView nestedScroll,
                                  ProgressBar progressBar) {

        WebViewClient client = new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                nestedScroll.setNestedScrollingEnabled(true);
                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
        };

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(client);
        webView.loadUrl(url);
    }

    @BindingAdapter(value = {"source", "formattedAuthor", "date"})
    public static void setText(TextView textView, String source, String formattedAuthor, String date) {
        String sourceAndTime = String.format(textView.getContext().getString(R.string.source_and_time_ago),
                source, formattedAuthor, Utils.DateToTimeFormat(date));
        textView.setText(sourceAndTime);

    }

    @BindingAdapter("itemDecoration")
    public static void setItemDecoration(RecyclerView view, RecyclerView.ItemDecoration old,
                                         RecyclerView.ItemDecoration newVal) {
        if (old != null) {
            view.removeItemDecoration(old);
        }
        if (newVal != null) {
            view.addItemDecoration(newVal);
        }
    }
}

package com.example.samprojre.screens.newsfeed_screen;


import android.animation.Animator;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samprojre.apptheme.Theme;
import com.example.samprojre.apptheme.ThemeManager;
import com.example.samprojre.databinding.FragmentNewsFeedBinding;
import com.example.samprojre.screens.about_screen.AboutActivity;
import com.example.samprojre.screens.settings_screens.SettingsActivity;
import com.example.samprojre.utils.FragmentHelper;
import com.example.samprojre.R;
import com.example.samprojre.data.model.FavoritesModel;
import com.example.samprojre.data.model.newsapi.Article;
import com.example.samprojre.screens.newsdetails_screen.NewsDetailActivity;
import com.example.samprojre.screens.newsfeed_screen.newsadapter.NewsAdapter;
import com.example.samprojre.base_adapter.ItemDecorator;
import com.example.samprojre.utils.UiUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import de.hdodenhof.circleimageview.CircleImageView;


@AndroidEntryPoint
public class NewsFeedFragment extends Fragment {

    private BottomSheetDialog optionsBottomSheetDialog;
    private View bottomSheetOptionsView;
    private FragmentHelper fragmentHelper;
    private FavoritesModel favoriteArticle;
    private NewsViewModel viewModel;
    private Article clickedArticle;
    private boolean newsLoadingState;
    private int isProgressIndicatorVisible;
    private int pageSize = 100;
    private String searchText;

    private SearchView searchView;

    public NewsFeedFragment() {
        // Required empty public constructor
    }


    private FragmentNewsFeedBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_feed, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setIsDatabaseLoading(View.GONE);
        binding.setPageSize(pageSize);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbarNewsFeed);
        Objects.requireNonNull(((AppCompatActivity) requireActivity())
                        .getSupportActionBar())
                .setDisplayShowTitleEnabled(false);
        binding.toolbarNewsFeed.setContentInsetStartWithNavigation(0);

        addMenuHost();
        initLoadingStates();
        initErrorLayout();

        viewModel.newsFeedList.observe(getViewLifecycleOwner(), news -> {
            if (news != null) {
                if (news.getArticle().isEmpty() && binding.errorLayoutNewsFeed.errorContent.getVisibility() != View.VISIBLE) {
                    binding.nothingWasFoundNewsFeed.setVisibility(View.VISIBLE);
                } else {
                    binding.nothingWasFoundNewsFeed.setVisibility(View.GONE);
                }
                if (binding.getAdapter() == null)
                    binding.setAdapter(new NewsAdapter(news.getArticle(), viewModel));

            }

        });

        viewModel.optionsClicked.observe(getViewLifecycleOwner(), isOpened -> {
            if (isOpened) {

                fragmentHelper = new FragmentHelper();
                bottomSheetOptionsView = fragmentHelper.openOptionsBottomSheetDialog(requireActivity(), favoriteArticle);
                optionsBottomSheetDialog = fragmentHelper.getOptionsBottomSheetDialog();
                //Share the article
                initListenersForBottomSheetView();

            }
        });

        viewModel.clickedArticle.observe(getViewLifecycleOwner(), clickedArticle -> {
            this.clickedArticle = clickedArticle;
        });

        viewModel.optionsFavoriteState.observe(getViewLifecycleOwner(), state -> {
            favoriteArticle = state;
        });

        viewModel.itemClick.observe(getViewLifecycleOwner(), this::goToDetailsActivity);

        binding.setItemDecorator(new ItemDecorator(48, 0, 24));


    }


    private void goToDetailsActivity(int position) {
        RecyclerView.ViewHolder viewHolder = binding.recyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder != null) {
            ImageView imageView = viewHolder.itemView.findViewById(R.id.articleImg);
            Intent intent = new Intent(requireContext(), NewsDetailActivity.class);
            intent.putExtra("url", clickedArticle.getUrl());
            intent.putExtra("title", clickedArticle.getTitle());
            intent.putExtra("img", clickedArticle.getUrlToImage());
            intent.putExtra("date", clickedArticle.getPublishedAt());
            intent.putExtra("source", clickedArticle.getSourceName().getName());
            intent.putExtra("author", clickedArticle.getAuthor());
            intent.putExtra("urlToImg", clickedArticle.getUrlToImage());
            intent.putExtra("desc", clickedArticle.getDescription());
            Pair<View, String> pair = Pair.create(imageView, "hi");
            ActivityOptions optionsCompat
                    = ActivityOptions.makeSceneTransitionAnimation(requireActivity(), pair);
            startActivity(intent, optionsCompat.toBundle());
        }
    }

    private void initErrorLayout() {
        binding.errorLayoutNewsFeed.setViewModel(viewModel);
        binding.errorLayoutNewsFeed.setPageSize(pageSize);
        binding.errorLayoutNewsFeed.setSearchQuery(null);


    }

    private void initLoadingStates() {
        viewModel.isLoading.observe(getViewLifecycleOwner(), state -> {
            switch (state) {
                case LOADING:
                    newsLoadingState = true;
                    binding.errorLayoutNewsFeed.errorContent.setVisibility(View.GONE);
                    break;
                case ERROR:
                    newsLoadingState = false;
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.errorLayoutNewsFeed.errorContent.setVisibility(View.VISIBLE);
                    binding.errorLayoutNewsFeed.errorTitleTextView.setText("Something went wrong");
                    binding.errorLayoutNewsFeed.errorMessageTextView.setText(viewModel.getErrorMessage());
                    break;
                case FINISHED:
                    newsLoadingState = false;
                    binding.errorLayoutNewsFeed.errorContent.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    break;
            }
            binding.setNewsLoadingState(newsLoadingState);
        });

        viewModel.isDatabaseLoading.observe(getViewLifecycleOwner(), state -> {
            switch (state) {
                case LOADING:
                    isProgressIndicatorVisible = View.VISIBLE;
                    break;
                case ERROR:
                    isProgressIndicatorVisible = View.GONE;
                    optionsBottomSheetDialog.dismiss();
                    UiUtils.showToastMessage("Something went wrong",getLayoutInflater(),requireContext());
                    break;
                case FINISHED:
                    isProgressIndicatorVisible = View.GONE;
                    UiUtils.showToastMessage("Updated successfully",getLayoutInflater(),requireContext());
                    optionsBottomSheetDialog.dismiss();
                    break;
                default:
                    isProgressIndicatorVisible = View.GONE;
            }
            binding.setIsDatabaseLoading(isProgressIndicatorVisible);
        });
    }

    private void initListenersForBottomSheetView() {
        optionsBottomSheetDialog.findViewById(R.id.shareLinearLayout).setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plan");
                intent.putExtra(Intent.EXTRA_SUBJECT, clickedArticle.getSourceName().getName());
                String body = clickedArticle.getTitle() + "\n" + clickedArticle.getUrl() + "\n" + getString(R.string.shareMessage) + "\n";
                intent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(intent, getString(R.string.shareChooser)));
            } catch (Exception e) {
                UiUtils.showToastMessage(getString(R.string.toastMessageCannotBeShared), getLayoutInflater(), requireContext());
            }
        });

        //Add to favourites
        bottomSheetOptionsView.findViewById(R.id.favoritesLinearLayout).setOnClickListener(v -> {
            if (favoriteArticle == null) {
                viewModel.insertFavorite();
            } else {
                viewModel.deleteFavorite();
            }

        });

        optionsBottomSheetDialog.setOnCancelListener(dialogInterface -> {
            viewModel._optionsClicked.setValue(false);
        });
        optionsBottomSheetDialog.setOnDismissListener(dialogInterface -> {
            viewModel._optionsClicked.setValue(false);
        });
    }


    private void addMenuHost() {
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                SearchManager searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);
                menuInflater.inflate(R.menu.menu_articles, menu);
                searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
                MenuItem searchMenuItem = menu.findItem(R.id.action_search);
                EditText txtSearch = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
                txtSearch.setHintTextColor(Color.WHITE);
                txtSearch.setTextColor(Color.WHITE);

                searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));
                searchView.setQueryHint(getString(R.string.search_query_hint));
                searchView.setOnCloseListener(() -> {
                    searchText = null;
                    binding.setSearchQuery(null);
                    return false;
                });
                if (searchText != null) {
                    if (!searchText.equals("")) {
                        searchView.setQuery(searchText, false);
                        searchView.clearFocus();
                    } else
                        searchText = null;
                }
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if (query.length() > 2) {
                            searchText = query.trim();
                            viewModel.getNews(searchText, pageSize);
                            binding.setSearchQuery(searchText);
                            return true;
                        } else {
                            UiUtils.showToastMessage("Type more than two letters!", getLayoutInflater(), requireContext());
                        }
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        return false;
                    }
                });
                searchMenuItem.getIcon().setVisible(false, false);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.action_settings) {
                    final View settingsIcon = requireActivity().findViewById(R.id.action_settings);
                    RotateAnimation rotate
                            = new RotateAnimation(0, 120,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(400);
                    rotate.setInterpolator(new LinearInterpolator());
                    rotate.setFillAfter(true);
                    rotate.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            settingsIcon.setClickable(false);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (fragmentHelper == null) {
                                fragmentHelper = new FragmentHelper();
                            }
                            View bottomSheetViewSettings = fragmentHelper.opensBottomSheetSettingsDialog(requireActivity());

                            //Preferences
                            bottomSheetViewSettings.findViewById(R.id.preferencesLinearLayout).setOnClickListener(v -> {
                                Intent intent = new Intent(requireContext(), SettingsActivity.class);
                                fragmentHelper.getSettingsBottomSheetDialog().dismiss();
                                startActivity(intent);
                            });
                            //Themes
                            bottomSheetViewSettings.findViewById(R.id.themesLinearLayout).setOnClickListener(v -> {
                                themesDialog();
                            });
                            //About
                            bottomSheetViewSettings.findViewById(R.id.aboutLinearLayout).setOnClickListener(v -> {
                                Intent intent = new Intent(requireContext(), AboutActivity.class);
                                startActivity(intent);
                            });

                            settingsIcon.setClickable(true);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    settingsIcon.startAnimation(rotate);
                    return true;
                }
                if (id == R.id.action_search) {
                    if (searchText != null) {
                        if (!searchText.equals(""))
                            searchView.setQuery(searchText, false);
                        else
                            searchText = null;
                    }
                }

                return false;
            }
        });
    }


    private void themesDialog() {
        fragmentHelper.getSettingsBottomSheetDialog().dismiss();
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(requireContext());
        View mView = getLayoutInflater().inflate(R.layout.fragment_themes_pop_up, null);
        ImageView close = mView.findViewById(R.id.closePopUp);

        CircleImageView blueThemeCircle = mView.findViewById(R.id.ThemeBlue);
        CircleImageView greenThemeCircle = mView.findViewById(R.id.ThemeGreen);
        CircleImageView brownThemeCircle = mView.findViewById(R.id.ThemeBrown);
        CircleImageView pinkThemeCircle = mView.findViewById(R.id.ThemePink);

        ImageView checkedBlueTheme = mView.findViewById(R.id.checkedBlueThemeImageView);
        ImageView checkedBrownTheme = mView.findViewById(R.id.checkedBrownThemeImageView);
        ImageView checkedGreenTheme = mView.findViewById(R.id.checkedGreenThemeImageView);
        ImageView checkedPinkTheme = mView.findViewById(R.id.checkedPinkThemeImageView);

        final CircleImageView[] currentPicked = new CircleImageView[1];
        final ImageView[] currentChecked = new ImageView[1];

        int borderColorGone = getResources().getColor(android.R.color.transparent);
        int pickedColor = getResources().getColor(R.color.pickedTheme);

        switch (ThemeManager.INSTANCE.getTheme()) {
            case BLUE:
                currentPicked[0] = blueThemeCircle;
                currentChecked[0] = checkedBlueTheme;
                break;
            case PINK:
                currentPicked[0] = pinkThemeCircle;
                currentChecked[0] = checkedPinkTheme;
                break;
            case BROWN:
                currentPicked[0] = brownThemeCircle;
                currentChecked[0] = checkedBrownTheme;
                break;
            case GREEN:
                currentPicked[0] = greenThemeCircle;
                currentChecked[0] = checkedGreenTheme;
                break;
        }
        currentPicked[0].setBorderColor(pickedColor);
        currentChecked[0].setVisibility(View.VISIBLE);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        close.setOnClickListener(v -> dialog.dismiss());

        blueThemeCircle.setOnClickListener(v -> {
            if (currentPicked[0] == blueThemeCircle)
                return;

            currentChecked[0].setVisibility(View.GONE);
            currentPicked[0].setBorderColor(borderColorGone);
            setTheme(Theme.BLUE);


            checkedBlueTheme.setVisibility(View.VISIBLE);
            blueThemeCircle.setBorderColor(pickedColor);
            currentPicked[0] = blueThemeCircle;
            currentChecked[0] = checkedBlueTheme;
        });
        brownThemeCircle.setOnClickListener(v -> {
            if (currentPicked[0] == brownThemeCircle)
                return;

            currentChecked[0].setVisibility(View.GONE);
            currentPicked[0].setBorderColor(borderColorGone);
            setTheme(Theme.BROWN);


            checkedBrownTheme.setVisibility(View.VISIBLE);
            brownThemeCircle.setBorderColor(pickedColor);
            currentPicked[0] = brownThemeCircle;
            currentChecked[0] = checkedBrownTheme;
        });
        greenThemeCircle.setOnClickListener(v -> {
            if (currentPicked[0] == greenThemeCircle)
                return;

            currentChecked[0].setVisibility(View.GONE);
            currentPicked[0].setBorderColor(borderColorGone);
            setTheme(Theme.GREEN);

            checkedGreenTheme.setVisibility(View.VISIBLE);
            greenThemeCircle.setBorderColor(pickedColor);
            currentPicked[0] = greenThemeCircle;
            currentChecked[0] = checkedGreenTheme;
        });
        pinkThemeCircle.setOnClickListener(v -> {
            if (currentPicked[0] == pinkThemeCircle)
                return;

            currentChecked[0].setVisibility(View.GONE);
            currentPicked[0].setBorderColor(borderColorGone);
            setTheme(Theme.PINK);

            checkedPinkTheme.setVisibility(View.VISIBLE);
            pinkThemeCircle.setBorderColor(pickedColor);
            currentPicked[0] = pinkThemeCircle;
            currentChecked[0] = checkedPinkTheme;
        });
        Rect displayRectangle = new Rect();
        Window window = requireActivity().getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        int width = (int) (displayRectangle.width() * 0.9f);
        int height = (int) (displayRectangle.height() * 0.5f);

        dialog.show();
        dialog.getWindow().setLayout(width, height);
    }

    private void setTheme(Theme theme) {

        ImageView themeImageView = requireActivity().findViewById(R.id.imageViewChangeTheme);
        ConstraintLayout mainActivityContainer
                = requireActivity().findViewById(R.id.mainActivityContainer);

        if (themeImageView.getVisibility() == View.VISIBLE) {
            return;
        }

        int width = mainActivityContainer.getWidth();
        int height = mainActivityContainer.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        mainActivityContainer.draw(canvas);


        themeImageView.setImageBitmap(bitmap);
        themeImageView.setVisibility(View.VISIBLE);

        float finalRadius = (float) Math.hypot(width, height);

        ThemeManager.INSTANCE.setTheme(theme);

        Animator animation = ViewAnimationUtils.createCircularReveal(
                mainActivityContainer,
                width,
                0,
                0f,
                finalRadius);
        animation.setDuration(400);
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {
                Window window = requireActivity().getWindow();
                switch (theme) {
                    case BLUE:
                        viewModel.setTheme(R.style.AppThemeBlue);
                        window.setStatusBarColor(getResources().getColor(R.color.colorBlueThemeDark));
                        break;
                    case PINK:
                        viewModel.setTheme(R.style.AppThemePink);
                        window.setStatusBarColor(getResources().getColor(R.color.colorPinkThemeDark));
                        break;
                    case BROWN:
                        viewModel.setTheme(R.style.AppThemeBrown);
                        window.setStatusBarColor(getResources().getColor(R.color.colorBrownThemeDark));
                        break;
                    case GREEN:
                        viewModel.setTheme(R.style.AppThemeGreen);
                        window.setStatusBarColor(getResources().getColor(R.color.colorGreenThemeDark));
                        break;
                }
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                ThemeManager.INSTANCE.setTheme(theme);

                themeImageView.setImageDrawable(null);
                themeImageView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {

            }
        });
        animation.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.getSettings();
        }
    }


}

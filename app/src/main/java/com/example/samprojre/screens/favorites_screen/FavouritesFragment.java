package com.example.samprojre.screens.favorites_screen;


import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samprojre.R;
import com.example.samprojre.data.model.FavoritesModel;
import com.example.samprojre.databinding.FragmentFavouritesBinding;
import com.example.samprojre.screens.favorites_screen.favoritesadapter.FavoritesAdapter;
import com.example.samprojre.screens.newsdetails_screen.NewsDetailActivity;
import com.example.samprojre.utils.FragmentHelper;
import com.example.samprojre.base_adapter.ItemDecorator;
import com.example.samprojre.utils.SwipeToDeleteCallback;
import com.example.samprojre.utils.UiUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class FavouritesFragment extends Fragment {

    private BottomSheetDialog bottomSheetDialog;
    private View bottomSheetView;
    private FragmentHelper fragmentHelper;
    private FavoritesViewModel viewModel;
    private FragmentFavouritesBinding binding;
    private int isProgressIndicatorVisible;
    private FavoritesModel clickedArticle;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourites, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getFavorites();
        viewModel.favoritesList.observe(getViewLifecycleOwner(), favorites -> {
            if (favorites.isEmpty()) {
                //Is empty
                binding.emptyContainerFavorites.setVisibility(View.VISIBLE);
            } else {
                binding.emptyContainerFavorites.setVisibility(View.GONE);
                if (binding.getAdapter() == null)
                    binding.setAdapter(new FavoritesAdapter(favorites, viewModel));
            }
        });

        viewModel.isDatabaseLoading.observe(getViewLifecycleOwner(), state -> {
            switch (state) {
                case LOADING:
                    isProgressIndicatorVisible = View.VISIBLE;
                    break;
                case ERROR:
                    isProgressIndicatorVisible = View.GONE;
                    UiUtils.showToastMessage("Oops, something went wrong",getLayoutInflater(),requireContext());
                    break;
                default:
                    isProgressIndicatorVisible = View.GONE;
                    if (bottomSheetDialog != null) {
                        bottomSheetDialog.dismiss();
                    }

            }
            binding.setIsDatabaseLoading(isProgressIndicatorVisible);
        });

        viewModel.clickedArticle.observe(getViewLifecycleOwner(), clickedArticle -> {
            this.clickedArticle = clickedArticle;
        });

        viewModel.itemClick.observe(getViewLifecycleOwner(), this::goToDetailsActivity);

        viewModel.optionsClicked.observe(getViewLifecycleOwner(), isOpened -> {
            if (isOpened) {
                fragmentHelper = new FragmentHelper();
                bottomSheetView = fragmentHelper.openOptionsBottomSheetDialog(requireActivity(), clickedArticle);
                bottomSheetDialog = fragmentHelper.getOptionsBottomSheetDialog();
                //Share the article
                initListenersForBottomSheetView();
            }
        });
        initSwipeToDelete();

        ItemDecorator itemDecorator = new ItemDecorator(48, 18, 24);
        binding.setItemDecoration(itemDecorator);
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
            intent.putExtra("source", clickedArticle.getSourceName());
            intent.putExtra("author", clickedArticle.getAuthor());
            intent.putExtra("urlToImg", clickedArticle.getUrlToImage());
            intent.putExtra("desc", clickedArticle.getDescription());
            Pair<View, String> pair = Pair.create(imageView, "hi");
            ActivityOptions optionsCompat
                    = ActivityOptions.makeSceneTransitionAnimation(requireActivity(), pair);
            startActivity(intent, optionsCompat.toBundle());
        }
    }


    private void initSwipeToDelete() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(requireContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                final int snackBarDuration = 4000;
                final int position = viewHolder.getAbsoluteAdapterPosition();
                viewModel.setToDeleteItem(position);

                Snackbar snackbar
                        = Snackbar.make(binding.getRoot(), R.string.item_was_removed, snackBarDuration);

                snackbar.setAction("UNDO", view -> {
                    //Restore the item
                    viewModel.cancelDeleting();
                    binding.getAdapter().notifyItemInserted(position);

                });

                snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if (event != DISMISS_EVENT_ACTION) {
                            viewModel.procedeDeletingItem();
                            Log.d("FavoritesFragment", "onDismissed: ");
                        }
                    }

                    @Override
                    public void onShown(Snackbar sb) {
                        super.onShown(sb);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
                binding.getAdapter().notifyItemRemoved(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);
    }

    private void initListenersForBottomSheetView() {
        bottomSheetDialog.findViewById(R.id.shareLinearLayout).setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plan");
                intent.putExtra(Intent.EXTRA_SUBJECT, clickedArticle.getSourceName());
                String body = clickedArticle.getTitle() + "\n" + clickedArticle.getUrl() + "\n" + getString(R.string.shareMessage) + "\n";
                intent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(intent, getString(R.string.shareChooser)));
            } catch (Exception e) {
                UiUtils.showToastMessage(getString(R.string.toastMessageCannotBeShared),getLayoutInflater(),requireContext());
            }
        });
        //Add to favourites
        bottomSheetView.findViewById(R.id.favoritesLinearLayout).setOnClickListener(v -> {
            viewModel.deleteFavorite();

        });
        bottomSheetDialog.setOnCancelListener(dialogInterface ->
                viewModel._optionsClicked.setValue(false));

        bottomSheetDialog.setOnDismissListener(dialogInterface ->
                viewModel._optionsClicked.setValue(false));
    }

}

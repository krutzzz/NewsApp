package com.example.samprojre.screens.downloaded_screen;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.samprojre.data.model.DownloadsModel;
import com.example.samprojre.R;
import com.example.samprojre.databinding.FragmentDownloadsBinding;
import com.example.samprojre.screens.newsdetails_downloaded_screen.NewsDetailDownloaded;
import com.example.samprojre.base_adapter.ItemDecorator;
import com.example.samprojre.utils.SwipeToDeleteCallback;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class DownloadedFragment extends Fragment {

    private FragmentDownloadsBinding binding;
    private DownloadsViewModel viewModel;
    private HashMap<Integer, DownloadsModel> clickedItem;

    public DownloadedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(this).get(DownloadsViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_downloads, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.downloadsList.observe(getViewLifecycleOwner(), downloadsList -> {
            if (downloadsList.isEmpty()) {
                //Downloads list is empty
                binding.emptyContainerDownloads.setVisibility(View.VISIBLE);
            }
            else{
                binding.emptyContainerDownloads.setVisibility(View.GONE);
                if (binding.getAdapter() == null) {
                    binding.setAdapter(new DownloadsAdapter(downloadsList, viewModel));
                }
            }

        });

        viewModel.itemClicked.observe(getViewLifecycleOwner(), itemClicked -> {
            clickedItem = itemClicked;
            goToDetailsActivity();
        });

        binding.setItemDecorator(new ItemDecorator(48, 16, 24));

        initlistener();
        initSwipeToDelete();
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
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewDownloads);
    }


    private void goToDetailsActivity() {
        Map.Entry<Integer, DownloadsModel> value = clickedItem.entrySet().iterator().next();
        int position = value.getKey();
        DownloadsModel item = value.getValue();
        RecyclerView.ViewHolder viewHolder = binding.recyclerViewDownloads.findViewHolderForAdapterPosition(position);
        if (viewHolder != null) {

            ImageView imageView = viewHolder.itemView.findViewById(R.id.articleImg);
            Intent intent = new Intent(requireContext(), NewsDetailDownloaded.class);
            intent.putExtra("path", item.getPath());
            intent.putExtra("title", item.getTitle());
            intent.putExtra("date", item.getPublishedAt());
            intent.putExtra("source", item.getSource());
            intent.putExtra("author", item.getAuthor());
            intent.putExtra("imgFilePath", item.getImgFilePath());
            intent.putExtra("desc", item.getDescription());
            Pair<View, String> pair = Pair.create(imageView, "hiDownloads");
            ActivityOptions optionsCompat
                    = ActivityOptions.makeSceneTransitionAnimation(requireActivity(), pair);
            startActivity(intent, optionsCompat.toBundle());
//            startActivity(intent);
        }
    }



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


//    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//
//        @Override
//        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//            return false;
//        }
//
//        @Override
//        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//            final int position = viewHolder.getAdapterPosition();
//
//            Realm realm = Realm.getDefaultInstance();
//            DownloadsModel Model = results.get(position);
//            File file = new File(Model.getPath());
//            boolean deleted = file.delete();
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    results.deleteFromRealm(position);
//                }
//            });
//
////            recyclerViewRealmDownloads.notifyItemRemoved(position);
//            if (results.size() == 0)
//                empty_layout.setVisibility(View.VISIBLE);
//
//
//        }
//    };


    private void initlistener() {
//        recyclerViewRealmDownloads.setOnItemClickListener(new RecyclerViewRealmDownloads.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent intent=new Intent(getContext(), NewsDetailDownloaded.class);
//                DownloadsModel Model = results.get(position);
//                intent.putExtra("path",Model.getPath());
//                startActivity(intent);
//
//            }
//        });

    }


}

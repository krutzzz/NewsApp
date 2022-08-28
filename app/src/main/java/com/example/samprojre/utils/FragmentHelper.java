package com.example.samprojre.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.samprojre.R;
import com.example.samprojre.data.model.FavoritesModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class FragmentHelper {

    private BottomSheetDialog optionsBottomSheetDialog;
    private BottomSheetDialog settingsBottomSheetDialog;

    public BottomSheetDialog getSettingsBottomSheetDialog() {
        return settingsBottomSheetDialog;
    }

    public BottomSheetDialog getOptionsBottomSheetDialog() {
        return optionsBottomSheetDialog;
    }

    public View openOptionsBottomSheetDialog(Activity activity, FavoritesModel favoriteArticle) {
        optionsBottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(activity)
                .inflate(
                        R.layout.bottom_sheet,
                        activity.findViewById(R.id.bottom_sheet_container)
                );
        ImageView favoritesButton = bottomSheetView.findViewById(R.id.favoritesbtn);
        TextView favoritesText = bottomSheetView.findViewById(R.id.favorites);

        //Checking if such object exists in Realm database
        // thus checking if the article already exists in the Favourites list
        if (favoriteArticle == null) {
            favoritesButton.setImageResource(R.drawable.ic_star_border_black);
        } else {
            favoritesButton.setImageResource(R.drawable.ic_star_yellow);
            favoritesText.setText(R.string.Favorites_added);
        }

        optionsBottomSheetDialog.setDismissWithAnimation(true);
        optionsBottomSheetDialog.setContentView(bottomSheetView);
        optionsBottomSheetDialog.show();

        return bottomSheetView;
    }

    public View opensBottomSheetSettingsDialog(Activity activity) {

        settingsBottomSheetDialog = new BottomSheetDialog(
                activity, R.style.BottomSheetDialogTheme
        );

        final View bottomSheetView = LayoutInflater.from(activity)
                .inflate(
                        R.layout.bottomsheet_settings,
                        activity.findViewById(R.id.bottomsheet_settings)
                );


        settingsBottomSheetDialog.setContentView(bottomSheetView);
        settingsBottomSheetDialog.show();

        return bottomSheetView;
    }

}

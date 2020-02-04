package com.example.samproj;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    //Recycler View Adapter

    List<Article> article;
    private ViewHolder.OnItemListener onItemListener;


    public ArticlesAdapter(List<Article> article, ViewHolder.OnItemListener onItemListener)
    {
        this.article = article;
        this.onItemListener=onItemListener;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        TextView caption;
        TextView description;
        ImageView img;
        OnItemListener onItemListener;
        ImageView mDownloadImg;

        public ViewHolder(@NonNull View itemView, final OnItemListener onItemListener) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            caption = itemView.findViewById(R.id.caption);
            description = itemView.findViewById(R.id.description);
            img=itemView.findViewById(R.id.image_in_card_view);
            mDownloadImg=itemView.findViewById(R.id.image_view);
            this.onItemListener=onItemListener;

            itemView.setOnClickListener(this);

            mDownloadImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemListener!=null){
                        if(getAdapterPosition()!=RecyclerView.NO_POSITION){
                            onItemListener.OnIconClickListener(getAdapterPosition());
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            onItemListener.OnItemClickListener(getAdapterPosition());

        }

        public interface OnItemListener{                //Interface for clicking on a card view
            void OnItemClickListener(int position);
            void OnIconClickListener(int position);
        }



    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.articles_cardview, parent, false);

        ViewHolder viewHolder = new ViewHolder(v,onItemListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlesAdapter.ViewHolder holder, int position) {
        holder.caption.setText(article.get(position).getCaption());
        holder.description.setText(article.get(position).getDescription());
        holder.img.setImageResource(article.get(position).getImage_of_an_article());
    }


    @Override
    public int getItemCount() {
        return article.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Article data) {
        article.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Article data) {
        int position = article.indexOf(data);
        article.remove(position);
        notifyItemRemoved(position);
    }
}

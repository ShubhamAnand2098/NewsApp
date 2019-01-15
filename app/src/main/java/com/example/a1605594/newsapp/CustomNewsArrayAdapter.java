package com.example.a1605594.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import pl.droidsonroids.gif.GifImageView;

public class CustomNewsArrayAdapter<E> extends ArrayAdapter<E> {


    CustomNewsArrayAdapter(@NonNull Context context, ArrayList<E> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item_view, parent, false);
        }

        NewsFeed currentNewsFeed = (NewsFeed) getItem(position);

        GifImageView imageView = listItemView.findViewById(R.id.news_feed_image_view);
        TextView textView = listItemView.findViewById(R.id.news_feed_text_view);

        imageView.setBackgroundResource(R.drawable.loading_image);

        assert currentNewsFeed != null;
        Glide.with(imageView.getContext())
                    .load(currentNewsFeed.getmPhotoUrl()).asBitmap()
                    .centerCrop()
                    .error(R.drawable.image_not_found)
                    .into(imageView);


        textView.setText(currentNewsFeed.getmArticleTitle());


        return listItemView;

    }


}

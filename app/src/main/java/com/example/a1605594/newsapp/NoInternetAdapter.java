package com.example.a1605594.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class NoInternetAdapter<E> extends ArrayAdapter<E> {

    public NoInternetAdapter(@NonNull Context context, @NonNull ArrayList<E> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if(view == null)
        {
            view = LayoutInflater.from(getContext()).inflate(R.layout.no_internet_layout, parent, false);
        }

        GifImageView imageView = view.findViewById(R.id.no_image);
        imageView.setImageResource(R.drawable.no_internet_image);
        return view;
    }
}

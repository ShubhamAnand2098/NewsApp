package com.example.a1605594.newsapp;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.victor.loading.rotate.RotateLoading;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;


public class HeadLinesFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<NewsFeed>> {

    private static final String STRING_URL = "https://newsapi.org/v2/top-headlines?country=in";
    private static final String API_KEY = "1702634400f943e4b78009f42802df30";
    private static final int LOADER_ID = 1;
    private boolean hasNetwork = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView headLinesListView;
    private RotateLoading loadingBar;
    private CustomNewsArrayAdapter<NewsFeed> customNewsArrayAdapter, emptyListViewAdapter;
    private LoaderManager mLoaderManager;
    private ConnectivityManager mConnectivityManager;
    private ListView emptyListView;

    public HeadLinesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_head_lines, container, false);

        swipeRefreshLayout = view.findViewById(R.id.fragment_swipe_refresh_layout);
        headLinesListView = view.findViewById(R.id.head_lines_list_item_view);
        loadingBar = view.findViewById(R.id.fragment_rotate_loading_bar);
        emptyListView = view.findViewById(R.id.empty_list_view);

        mLoaderManager = getLoaderManager();
        mConnectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        setupEmptyListView();

        setupMainListView();

        loadingBar.start();

        mLoaderManager.initLoader(LOADER_ID, null, HeadLinesFragment.this);

        //TODO: If user clicks before loading of pic so something
        headLinesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View selectedView, int i, long l) {
                Intent intent = new Intent(getActivity(), NewsDetailDisplayActivity.class);
                GifImageView imageView = selectedView.findViewById(R.id.news_feed_image_view);
                NewsFeed clickedNewsFeed = (NewsFeed) adapterView.getItemAtPosition(i);
                imageView.buildDrawingCache();
                Bitmap bmp = imageView.getDrawingCache();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

                SharedPreferences.Editor editor = getActivity().getSharedPreferences("My Shared", Context.MODE_PRIVATE).edit();
                editor.putInt("ListViewPosition", i);
                editor.putString("bmp", encoded);
                editor.apply();

                /*
                One of the correct ways
              ====================================================================
                try {
                    //Write file
                    String filename = "bitmap.png";
                    FileOutputStream stream = getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    //Cleanup
                    stream.close();
                    bmp.recycle();

                    //Pop intent
                    intent.putExtra("bmp", filename);
                } catch (Exception e) {
                    e.printStackTrace();
                }
              =====================================================================
                 */


                intent.putExtra("Image", clickedNewsFeed.getmPhotoUrl());
                intent.putExtra("Title", clickedNewsFeed.getmArticleTitle());
                intent.putExtra("Description", clickedNewsFeed.getmDescription());
                intent.putExtra("Read More", clickedNewsFeed.getmReadMoreUrl());
                intent.putExtra("Source", clickedNewsFeed.getmSource());
                intent.putExtra("Author", clickedNewsFeed.getmAuthor());

                ActivityOptions options = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(
                            getActivity(), imageView, getString(R.string.image_view_transition_name));
                }
                startActivity(intent, options.toBundle());
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingBar.start();
                emptyListView.setVisibility(View.INVISIBLE);
                mLoaderManager.restartLoader(LOADER_ID, null, HeadLinesFragment.this);
            }
        });

        headLinesListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0)
                    swipeRefreshLayout.setEnabled(true);
                else
                    swipeRefreshLayout.setEnabled(false);
            }
        });

        return view;
    }

    private void setupEmptyListView() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Default");
       NoInternetAdapter<String> internetAdapter = new NoInternetAdapter<>(getActivity(), arrayList);
        emptyListView.setAdapter(internetAdapter);
        emptyListView.setVisibility(View.INVISIBLE);
    }

    private void setupMainListView()
    {
        customNewsArrayAdapter = new CustomNewsArrayAdapter<>(
                getActivity(), new ArrayList<NewsFeed>());
        headLinesListView.setAdapter(customNewsArrayAdapter);
    }


    @NonNull
    @Override
    public Loader<List<NewsFeed>> onCreateLoader(int id, @Nullable Bundle args) {
        Uri baseUrl = Uri.parse(STRING_URL);
        Uri.Builder builder = baseUrl.buildUpon();
        builder.appendQueryParameter("apiKey", API_KEY);

        return new NewsFeedLoader(getContext(), builder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsFeed>> loader, List<NewsFeed> data) {

        loadingBar.stop();
        swipeRefreshLayout.setRefreshing(false);

        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        hasNetwork = networkInfo != null && networkInfo.isConnected();

        if (!hasNetwork) {
            emptyListView.setVisibility(View.VISIBLE);
            headLinesListView.setVisibility(View.INVISIBLE);
            return;
        }

        if (!data.isEmpty()) {
            headLinesListView.setVisibility(View.VISIBLE);
            emptyListView.setVisibility(View.INVISIBLE);
            customNewsArrayAdapter.clear();
            customNewsArrayAdapter.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsFeed>> loader) {
        customNewsArrayAdapter.clear();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}

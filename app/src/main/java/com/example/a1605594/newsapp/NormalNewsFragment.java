package com.example.a1605594.newsapp;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.victor.loading.rotate.RotateLoading;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class NormalNewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<NewsFeed>>,
        NewsFeedActivity.QueryTransfer {

    private static final String STRING_URL = "https://newsapi.org/v2/everything?";
    private static final String API_KEY = "1702634400f943e4b78009f42802df30";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static final int LOADER_ID = 1;
    private boolean hasNetwork = false;
    private ListView normalNewsListView;
    private RotateLoading loadingBar;
    private CustomNewsArrayAdapter<NewsFeed> customNewsArrayAdapter;
    private LoaderManager mLoaderManager;
    private ConnectivityManager mConnectivityManager;
    private ListView emptyListView;
    private ListView noDataFoundListView;
    private NewsFeedActivity newsFeedActivity;
    private String transferdQuery;

    public NormalNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newsFeedActivity = (NewsFeedActivity)getActivity();
        if (newsFeedActivity != null) {
            newsFeedActivity.setQueryTransferListener(this);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_normal_news, container, false);

        mSwipeRefreshLayout = view.findViewById(R.id.fragment_normal_news_swipe_refresh_layout);
        normalNewsListView = view.findViewById(R.id.normal_news_list_item_view);
        loadingBar = view.findViewById(R.id.fragment_normal_news_rotate_loading_bar);
        emptyListView = view.findViewById(R.id.empty_list_view);
        noDataFoundListView = view.findViewById(R.id.no_data_list_view);

        mLoaderManager = getLoaderManager();
        mConnectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        setupEmptyListView();
        setupNoDataFoundListView();
        setupMainListView();

        loadingBar.start();
        transferdQuery = "USA";
        mLoaderManager.initLoader(LOADER_ID, null, NormalNewsFragment.this);

        //TODO: If user clicks before loading of pic so something
        normalNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                emptyListView.setVisibility(View.INVISIBLE);
                noDataFoundListView.setVisibility(View.INVISIBLE);
                loadingBar.start();
                mLoaderManager.restartLoader(LOADER_ID, null, NormalNewsFragment.this);
            }
        });

        normalNewsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0)
                    mSwipeRefreshLayout.setEnabled(true);
                else
                    mSwipeRefreshLayout.setEnabled(false);
            }
        });


        return view;
    }

    private void setupNoDataFoundListView() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Default");

       NoDataAdapter<String> noDataAdapter = new NoDataAdapter<>(getActivity(), arrayList);
        noDataFoundListView.setAdapter(noDataAdapter);
        noDataFoundListView.setVisibility(View.INVISIBLE);

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
        normalNewsListView.setAdapter(customNewsArrayAdapter);
    }


    @NonNull
    @Override
    public Loader<List<NewsFeed>> onCreateLoader(int id, @Nullable Bundle args) {

        return new NewsFeedLoader(getContext(), buildUrl(transferdQuery));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsFeed>> loader, List<NewsFeed> data) {

        loadingBar.stop();
        mSwipeRefreshLayout.setRefreshing(false);

        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        hasNetwork = networkInfo != null && networkInfo.isConnected();

        if (!hasNetwork) {
            emptyListView.setVisibility(View.VISIBLE);
            normalNewsListView.setVisibility(View.INVISIBLE);
            noDataFoundListView.setVisibility(View.INVISIBLE);
            return;
        }

        if (!data.isEmpty()) {
            normalNewsListView.setVisibility(View.VISIBLE);
            emptyListView.setVisibility(View.INVISIBLE);
            noDataFoundListView.setVisibility(View.INVISIBLE);
            customNewsArrayAdapter.clear();
            customNewsArrayAdapter.addAll(data);
        }
        else
        {
            customNewsArrayAdapter.clear();
            noDataFoundListView.setVisibility(View.VISIBLE);
            emptyListView.setVisibility(View.INVISIBLE);
            normalNewsListView.setVisibility(View.INVISIBLE);
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


    @Override
    public void transferData(String text) {
        transferdQuery = text;
        loadingBar.start();
        mLoaderManager.restartLoader(LOADER_ID, null, NormalNewsFragment.this);
    }

    private String buildUrl(String query)
    {
        Uri baseUrl = Uri.parse(STRING_URL);
        Uri.Builder builder = baseUrl.buildUpon();
        builder.appendQueryParameter("q", query);
        builder.appendQueryParameter("apiKey", API_KEY);

        return builder.toString();
    }


}


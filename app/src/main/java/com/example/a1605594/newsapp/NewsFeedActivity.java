package com.example.a1605594.newsapp;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.transition.TransitionManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NewsFeedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ViewPager newsViewPager;
    private Toolbar toolbar;
    private SearchView searchView;
    private QueryTransfer mQueryTransfer;
    private NavigationView mNavigationView;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);


        SharedPreferences sharedPreferences = getSharedPreferences("USER INFO", MODE_PRIVATE);

        mDrawerLayout = findViewById(R.id.drawer);
        newsViewPager = findViewById(R.id.news_view_pager);
        mNavigationView = findViewById(R.id.navigation_view);
        //Set user name
        TextView f = mNavigationView.getHeaderView(0).findViewById(R.id.username);
        f.setText(sharedPreferences.getString("name", "GUEST"));

        toolbar = findViewById(R.id.toolbar);
        toolbar.setCollapsible(true);
        toolbar.setTitle(R.string.app_name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setBackground(getDrawable(R.drawable.splash_screen_background));
        }
        setSupportActionBar(toolbar);

        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                0, 0);

        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NewsCategoryAdapter newsCategoryAdapter = new NewsCategoryAdapter(getSupportFragmentManager());
        newsViewPager.setAdapter(newsCategoryAdapter);

        TabLayout tabLayout = findViewById(R.id.news_tab_layout);
        tabLayout.setupWithViewPager(newsViewPager);

        TabLayout.Tab tab1 = tabLayout.getTabAt(0);
        tab1.setIcon(R.drawable.headlines_icon);

        TabLayout.Tab tab2 = tabLayout.getTabAt(1);
        tab2.setIcon(R.drawable.news_tab);

        mNavigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem menuItem  = menu.findItem(R.id.action_search);
        searchView = (SearchView)menuItem.getActionView();


        searchView.setQueryHint(getResources().getString(R.string.search_text));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                menuItem.collapseActionView();
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                newsViewPager.setCurrentItem(1, true);
                mQueryTransfer.transferData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int selectedItemId = item.getItemId();
        switch (selectedItemId)
        {
            case R.id.home:
                newsViewPager.setCurrentItem(0,true);
                break;

            case R.id.sports:
                newsViewPager.setCurrentItem(1, true);
                mQueryTransfer.transferData("sports");
                break;

            case R.id.politics:
                newsViewPager.setCurrentItem(1, true);
                mQueryTransfer.transferData("politics");
                break;

            case R.id.business:
                newsViewPager.setCurrentItem(1, true);
                mQueryTransfer.transferData("stock market");
                break;

            case R.id.technology:
                newsViewPager.setCurrentItem(1, true);
                mQueryTransfer.transferData("latest technology");
                break;

            case R.id.fashion:
                newsViewPager.setCurrentItem(1, true);
                mQueryTransfer.transferData("fashion");
                break;

            case R.id.about_us:
                Intent intent = new Intent(NewsFeedActivity.this, AboutUsActivity.class);
                ActivityOptions activityOptions = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    activityOptions = ActivityOptions.makeSceneTransitionAnimation(this);
                }
                startActivity(intent, activityOptions.toBundle());
                break;

            case R.id.logout:
                Intent logOutIntent = new Intent(NewsFeedActivity.this, MainActivity.class);
                SharedPreferences.Editor editor = getSharedPreferences("USER INFO", Context.MODE_PRIVATE).edit();
                editor.putBoolean("LoggedIn", false);
                editor.apply();
                startActivity(logOutIntent);
                finish();
                break;

            default:
                break;
        }

        mDrawerLayout.closeDrawer(Gravity.START);

        return true;
    }

    public interface QueryTransfer{
        void transferData(String text);
    }

    public void setQueryTransferListener(QueryTransfer queryTransfer)
    {
        this.mQueryTransfer = queryTransfer;
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewsFeedActivity.this,
                AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setTitle("EXIT");
        builder.setMessage("Are you sure?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}

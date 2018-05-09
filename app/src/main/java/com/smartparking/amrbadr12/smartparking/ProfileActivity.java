package com.smartparking.amrbadr12.smartparking;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class ProfileActivity extends AppCompatActivity {
    int amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        android.support.v7.widget.Toolbar primaryToolbar = findViewById(R.id.primary_toolbar);
        setSupportActionBar(primaryToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle("");
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow);
        }
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        TabLayout.Tab tabWallet = tabLayout.newTab();
        tabWallet.setText(getResources().getString(R.string.wallet_label));
        tabLayout.addTab(tabWallet);
        TabLayout.Tab tabStats = tabLayout.newTab();
        tabStats.setText(getResources().getString(R.string.stats_label));
        tabLayout.addTab(tabStats);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = findViewById(R.id.view_pager);
        com.smartparking.amrbadr12.smartparking.PagerAdapter pagerAdapter = new com.smartparking.amrbadr12.smartparking.PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}

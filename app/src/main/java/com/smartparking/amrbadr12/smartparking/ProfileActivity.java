package com.smartparking.amrbadr12.smartparking;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class ProfileActivity extends AppCompatActivity implements StatsFragment.stats, WalletFragment.walletListener {
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
        tabWallet.setText("Wallet");
        tabLayout.addTab(tabWallet);
        TabLayout.Tab tabStats = tabLayout.newTab();
        tabStats.setText("Stats");
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

    //TODO:implement the stats methods.
    @Override
    public int getParkCount() {
        return 0;
    }

    @Override
    public String getLastVisitedDate() {
        return null;
    }

    @Override
    public int getPointsCount() {
        return 0;
    }

    @Override
    public int chargeWallet() {
        Log.e("Profile Activity", "charge wallet button is called");
        final View dialogLayout = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.charge_wallet_dialog_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        AlertDialog alertDialog = builder.setTitle("Choose the amount").setView(dialogLayout).setPositiveButton("Charge", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText amountText = dialogLayout.findViewById(R.id.amount_edit_text);
                amount = Integer.parseInt(amountText.getText().toString());
                //TODO:Add it to firebase and display it in the fragment.
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).create();
        alertDialog.show();
        return amount;
    }

    @Override
    public int redeemPoints() {
        //TODO:add some logic here to redeem the points
        return 0;
    }
}

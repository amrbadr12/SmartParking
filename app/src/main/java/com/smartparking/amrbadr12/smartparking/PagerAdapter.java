package com.smartparking.amrbadr12.smartparking;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int tabsNum;

    public PagerAdapter(FragmentManager fm, int numberofTabs) {
        super(fm);
        tabsNum = numberofTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                WalletFragment walletFragment = new WalletFragment();
                return walletFragment;
            case 1:
                StatsFragment statsFragment = new StatsFragment();
                return statsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabsNum;
    }
}

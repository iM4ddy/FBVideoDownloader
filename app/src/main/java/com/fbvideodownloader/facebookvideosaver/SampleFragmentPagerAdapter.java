package com.fbvideodownloader.facebookvideosaver;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> mTabHeader;
    public SampleFragmentPagerAdapter(FragmentManager fm, ArrayList<String> tabheader) {
        super(fm);
        this.mTabHeader = tabheader;
    }

    @Override
    public int getCount() {
        return mTabHeader.size();
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub

        switch (position) {
            case 0:
                // tabs home
                //return new facebook();
                return new BrowseBtnFragment();
            case 1:
                // how to use
                //return new howToUse();
                return new home();
            case 2:
                // tabs video managers
                //return new videoManagers();
                return new recyclerview();
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return mTabHeader.get(position);
    }
}

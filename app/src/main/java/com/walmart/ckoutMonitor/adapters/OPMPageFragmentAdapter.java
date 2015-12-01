package com.walmart.ckoutMonitor.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.walmart.ckoutMonitor.fragments.ItemQtyFragment;
import com.walmart.ckoutMonitor.fragments.ODBySlotFragment;
import com.walmart.ckoutMonitor.fragments.OrderDistributionFragment;

/**
 * Created by sgovind on 11/21/15.
 */
public class OPMPageFragmentAdapter extends FragmentPagerAdapter {

    public String[] tabTitles = {"Top Items", "Del Loc", "Del Time"};

    public OPMPageFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
           return ItemQtyFragment.newInstance();
        }
        if (position == 1) {
            return OrderDistributionFragment.newInstance();

        }

        if (position == 2) {
            return ODBySlotFragment.newInstance();

        }

        return null;
    }

    @Override
    public int getCount() {
        return tabTitles.length;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.v("Titles", tabTitles[position]);
        return tabTitles[position];
    }
}

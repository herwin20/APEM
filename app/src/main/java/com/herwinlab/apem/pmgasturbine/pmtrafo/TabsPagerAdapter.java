package com.herwinlab.apem.pmgasturbine.pmtrafo;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.herwinlab.apem.R;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private final int number_tabs;

    public TabsPagerAdapter(FragmentManager fm, int number_tabs) {
        super(fm);
        this.number_tabs = number_tabs;
    }

    //Mengembalikan Fragment yang terkait dengan posisi tertentu
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MainTrafoFragment();
            case 1:
                return new UATFragment();
            case 2:
                return new SSTFragment();
            default:
                return null;
        }
    }

    //Mengembalikan jumlah tampilan yang tersedia.
    @Override
    public int getCount() {
        return number_tabs;
    }

}

package com.jf.djplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by JF on 2016/1/20.
 * 我自己的Viewpager专用适配器
 */
public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter{

    private List<Fragment> fragmentList;
    public FragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(List<Fragment> fragmentList){
        this.fragmentList = fragmentList;
    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
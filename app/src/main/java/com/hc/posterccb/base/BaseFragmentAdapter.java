package com.hc.posterccb.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 2017/7/10.
 */

public class BaseFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList=new ArrayList<>();
    private List<String> mTitles;

    public BaseFragmentAdapter(FragmentManager fm,List<Fragment> mFragmentList) {
        super(fm);
        this.mFragmentList=mFragmentList;
    }

    public BaseFragmentAdapter(FragmentManager fm,List<Fragment> mFragmentList,List<String> mTitles){
        super(fm);
        this.mFragmentList=mFragmentList;
        this.mTitles=mTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}

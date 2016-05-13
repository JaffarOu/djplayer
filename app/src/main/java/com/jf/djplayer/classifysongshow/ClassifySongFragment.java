package com.jf.djplayer.classifysongshow;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.jf.djplayer.adapter.FragmentViewPagerAdapter;
import com.jf.djplayer.base.basefragment.BaseGroupFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JF on 2016/5/1.
 * 用于分类显示歌曲
 * 在“歌手”、“文件夹”、“专辑”页面里面点击栏目，将会跳到这个页面
 */
public class ClassifySongFragment extends BaseGroupFragment{

    @Override
    protected void initBeforeReturnView() {
        //搜索按钮菜单按钮设置可见
        mFragmentTitleLayout.setSearchIvVisibility(View.VISIBLE);
        mFragmentTitleLayout.setMoreIvVisivility(View.VISIBLE);
    }

    @Override
    protected String[] getTextViewTabsText() {
        return null;
    }

    @Override
    protected FragmentStatePagerAdapter getViewPagerAdapter() {
        List<Fragment> fragmentList = new ArrayList<>(1);
        fragmentList.add(new ClassifySongListFragment());
        mFragmentStatePagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(), fragmentList);
        return mFragmentStatePagerAdapter;
    }
}
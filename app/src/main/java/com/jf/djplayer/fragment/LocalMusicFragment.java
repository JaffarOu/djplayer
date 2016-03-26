package com.jf.djplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;

import com.jf.djplayer.R;
import com.jf.djplayer.customview.FragmentTitleLinearLayout;
import com.jf.djplayer.customview.ListViewPopupWindows;
import com.jf.djplayer.customview.TextViewLinearLayout;

import com.jf.djplayer.adapter.FragmentViewPagerAdapter;
import com.jf.djplayer.interfaces.ChangeFragment;
import com.jf.djplayer.tool.database.SongInfoOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JF on 2016/1/19.
 */
public class LocalMusicFragment extends Fragment implements
        FragmentTitleLinearLayout.FragmentTitleListener,AdapterView.OnItemClickListener,ViewPager.OnPageChangeListener{

    private View layoutView;
    private FragmentTitleLinearLayout fragmentTitleLinearLayout;
    private ViewPager viewPager;
    private FragmentViewPagerAdapter fragmentViewPagerAdapter;
    private int windowWidths;
    private int windowHeight;
    private PopupWindow itemPopupWindows;
    private List<Fragment> fragmentList;

    private TextViewLinearLayout textViewLinearLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_local_music,container,false);
        windowWidths = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        windowHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        viewInit();
        return layoutView;
    }


    //    view做初始化
    private void viewInit() {
//        fragmentTitleLinearLayout
        fragmentTitleLinearLayout = (FragmentTitleLinearLayout) layoutView.findViewById(R.id.fragmentTitleLinearLayout_fragment_local_music);
        fragmentTitleLinearLayout.setItemClickListener(this);
//        viewPager初始化
        viewPager = (ViewPager) layoutView.findViewById(R.id.vp_fragment_local_music);
        viewPagerInit();
//        textViewLinearLayout设置
        textViewLinearLayout = (TextViewLinearLayout) layoutView.findViewById(R.id.textViewLinearLayout_fragment_local_music);
        textViewLinearLayout.setTextViewText(new String[]{"歌曲", "歌手", "专辑", "文件夹"});
        textViewLinearLayout.setOnItemClickListener(this);
    }

    //    viewPager初始化
    private void viewPagerInit() {
//        初始化数据库适配器和数据集合
        SongInfoOpenHelper songInfoOpenHelper = new SongInfoOpenHelper(getActivity(), 1);
        fragmentViewPagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager());
        fragmentList = new ArrayList<>(4);
//   如果数据库里面有歌曲
            fragmentList.add(new SongFragment());
            fragmentList.add(new SingerFragment());
            fragmentList.add(new AlbumFragment());
            fragmentList.add(new FolderFragment());
//        if (songInfoOpenHelper.getLocalMusicNumber() != 0) {
//            fragmentList.add(new SongFragment());
//            fragmentList.add(new SingerFragment());
//            fragmentList.add(new AlbumFragment());
//            fragmentList.add(new FolderFragment());
//        } else {
////            所有页卡都装着同一个Fragment
//            for (int i = 0; i < 4; i++) fragmentList.add(new NoSongFragment());
//        }
        fragmentViewPagerAdapter.setFragments(fragmentList);
        viewPager.setAdapter(fragmentViewPagerAdapter);
        viewPager.setOnPageChangeListener(this);
    }

    //    FragmentTitleLinearLayout
//    回调

    @Override
    public void onTitleClick() {

//        Toast.makeText(getActivity(),"点击标题",Toast.LENGTH_SHORT).show();
        ((ChangeFragment)getActivity()).finishFragment();
    }

    //    标题栏的搜索按钮被按下了
    @Override
    public void onSearchIvOnclick() {

    }

    //    标题栏的"更多按钮"被按下了
    @Override
    public void onMoreIvOnclick() {
        int currentItems = viewPager.getCurrentItem();
        ListViewPopupWindows listViewPopupWindows;
        if(currentItems==0) {
            listViewPopupWindows = ((ExpandableFragment)fragmentList.get(0)).getListViewPopupWindow();
            listViewPopupWindows.showAsDropDown(fragmentTitleLinearLayout,windowWidths-listViewPopupWindows.getWidth(),0);
        }
        else{
            listViewPopupWindows = ((ListViewFragment)fragmentList.get(currentItems)).getListViewPopupWindow();
            listViewPopupWindows.showAsDropDown(fragmentTitleLinearLayout, windowWidths - listViewPopupWindows.getWidth(), 0);
        }
    }



//    监听TextViewLinearLayout用的回调方法
//    回调方法

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        viewPager.setCurrentItem(position);//viewPager跟着栏目的变化而变
    }


    //    OnPagerChangeListener
//    覆盖
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position){
        textViewLinearLayout.setCurrentItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

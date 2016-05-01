package com.jf.djplayer.basefragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.jf.djplayer.R;
import com.jf.djplayer.customview.FragmentTitleLayout;
import com.jf.djplayer.customview.TextViewTabs;

/**
 * Created by JF on 2016/4/25.
 * 项目里面大量使用到了"Fragment"嵌套"Fragment"，
 * 这是作为容器（外层"Fragment"）的"Fragment"的基类，基类实现：
 * >带有一个自定义的"FragmentTitleLayout"
 * >带有一个自定义的"TextViewTabs"
 * >定义好了"ViewPager"，子类将适配器做好就可以了
 */
abstract public class BaseGroupFragment extends Fragment
        implements FragmentTitleLayout.FragmentTitleListener, ViewPager.OnPageChangeListener,
        AdapterView.OnItemClickListener{

    protected FragmentTitleLayout mFragmentTitleLayout;//这是"Fragment"统一标题
    private TextViewTabs mTextViewTabs;//自定义栏目指示器
    private ViewPager mViewPager;//用来装填多个的"Fragment"
    protected FragmentStatePagerAdapter mFragmentStatePagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.fragment_base_group,container,false);

        //findViewById()
        mFragmentTitleLayout = (FragmentTitleLayout)layoutView.findViewById(R.id.fragmentTitleLayout_fragment_base_group);
        mViewPager = (ViewPager)layoutView.findViewById(R.id.vp_fragment_base_group);
        mTextViewTabs = (TextViewTabs)layoutView.findViewById(R.id.textViewLinearLayout_fragment_base_group);

        viewInit();
        initBeforeReturnView();
        return layoutView;
    }

    private void viewInit(){
        //"FragmentTitleLayout"初始化
        mFragmentTitleLayout.setTitleClickListener(this);

        //"TextViewLinearLayout"初始化
        mTextViewTabs.setItemText(getTextViewTabsText());
        mTextViewTabs.setOnItemClickListener(this);

        //"ViewPager"初始化
        mViewPager.setOnPageChangeListener(this);
        mFragmentStatePagerAdapter = getViewPagerAdapter();
        mViewPager.setAdapter(mFragmentStatePagerAdapter);
    }

    /**
     * 在"onCreateView()"方法返回前回调的方法
     * 子类可在此做自己的初始化
     */
    abstract protected void initBeforeReturnView();

    /**
     * 子类在该方法里面返回"TextViewTabs"所显示的文字内容
     * @return
     */
    abstract protected String[] getTextViewTabsText();

    /**
     * 子类在该方法里面返回"ViewPager"的适配器
     * @return "FragmentStatePagerAdapter"或其子类
     */
    abstract protected FragmentStatePagerAdapter getViewPagerAdapter();

    /**
     * 设置标题图片资源
     * @param resourceId 标题的图片资源:R.drawable.xxxx
     */
    protected final void setTitleImageResourceId(int resourceId){
        mFragmentTitleLayout.setTitleImageResource(resourceId);
    }

    /**
     * 设置标题的文字
     * @param titleText 标题文字
     */
    protected final void setTitleText(String titleText){
        mFragmentTitleLayout.setTitleText(titleText);
    }

    /**
     * 设置搜索按钮是否可见
     * @param visibility 他只能是如下几个值的一个：View.VISIBLE、View.INVISIBLE、View.GONE
     */
    protected final void setTitleSearchVisibility(int visibility){
        mFragmentTitleLayout.setSearchIvVisibility(visibility);
    }

    /**
     * 设置更多按钮是否可见
     * @param visibility 他只能是如下几个值的一个：View.VISIBLE、View.INVISIBLE、View.GONE
     */
    protected final void setTitleMoreVisibility(int visibility){
        mFragmentTitleLayout.setMoreIvVisivility(visibility);
    }

    /**
     * 获取"ViewPager"当前处于第几个选项卡
     * @return 当前选项卡的位置
     */
    protected final int getCurrentItem(){
        return mViewPager.getCurrentItem();
    }

      /*"FragmentTitleListener"方法实现--start，子类根据需要进行重写*/
    @Override
    public void onTitleClick() {
    }

    @Override
    public void onSearchIvOnclick() {
    }

    @Override
    public void onMoreIvOnclick() {
    }
        /*"FragmentTitleListener"方法实现--end*/

        /*"OnPageChangedListener"方法实现--start，子类根据需要进行重写*/
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //当"ViewPager"被滑动了，"TextViewTabs"变为相对应的栏目
        if(mTextViewTabs.getItemText()!=null){
            mTextViewTabs.setCurrentItem(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
        /*"OnPageChangedListener"方法实现--end*/

    /*"TextViewTabs"点击监听方法实现*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //当"TextViewTabs"发生改变，"ViewPager"变为相对应的页卡
        mViewPager.setCurrentItem(position);
    }
}

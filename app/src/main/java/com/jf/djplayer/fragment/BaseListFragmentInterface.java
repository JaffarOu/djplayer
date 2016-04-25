package com.jf.djplayer.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.jf.djplayer.customview.ListViewPopupWindows;

import java.util.List;

/**
 * Created by JF on 2016/4/23.
 * 该类作为"BaseListFragment"类和其子类间的一个类，
 * 使得其子类不需要被迫实现用不上的方法
 */
public class BaseListFragmentInterface extends BaseListFragment{


    @Override
    protected void initBeforeReturnView() {

    }

    @Override
    protected View getLoadingHintView() {
        return null;
    }

    @Override
    protected View getNoDataHintView() {
        return null;
    }

    @Override
    protected View getListViewHeaderView() {
        return null;
    }

    @Override
    protected View getListViewFooterView() {
        return null;
    }

    @Override
    protected List getData() {
        return null;
    }

    @Override
    protected BaseAdapter getListViewAdapter(List dataList) {
        return null;
    }

    @Override
    public ListViewPopupWindows getListViewPopupWindow() {
        return null;
    }

    @Override
    protected void readDataFinish(List dataList) {

    }

    @Override
    protected void doListViewOnItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void doListViewOnItemLongClick(AdapterView<?> parent, View view, int position, long id) {

    }
}

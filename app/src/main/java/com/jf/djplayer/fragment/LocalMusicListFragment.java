package com.jf.djplayer.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.adapter.ListViewFragmentAdapter;
import com.jf.djplayer.customview.ListViewPopupWindows;
import com.jf.djplayer.fragment.BaseFragment;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by JF on 2016/1/23.
 * 本地音乐里面三个"ListViewFragment"共有该类作为基类
 * “歌手Fragment”“专辑Fragment”“文件夹的Fragment”共用此类
 */
public abstract class LocalMusicListFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    protected ListView listView;//显示栏目用的控件
    protected ProgressBar loadingProgressBar;//信息读取完成之前的进度条
    protected TextView loadingTv;//信新都区提示文字
    protected String title = "title";
    protected String content = "content";
    protected ListViewFragmentAdapter listViewFragmentAdapter;
    protected PopupWindow popupWindows;
    protected List<Map<String,String>> dataList;//装填所获取的数据用的集合
    private ReadDataAsyncTask readDataAsyncTask;
    protected View layoutView;
    protected View footerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_list_view,container,false);
        loadingProgressBar = (ProgressBar)layoutView.findViewById(R.id.pb_fragment_list_view);
        listView = (ListView)layoutView.findViewById(R.id.lv_fragment_list_view);
        loadingTv = (TextView)layoutView.findViewById(R.id.tv_fragment_list_view);
//        开始执行读数据的异步任务
        readDataAsyncTask = new ReadDataAsyncTask();
        readDataAsyncTask.execute();
        return layoutView;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        如果用户在异步任务完成前离开界面
//        则要取消异步任务
        readDataAsyncTask.cancel(true);
    }

    protected void sortAccordingTitle(){
        final Collator collator = Collator.getInstance(Locale.CHINA);
        Collections.sort(dataList, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> lhs, Map<String, String> rhs) {
                CollationKey key1 = collator
                        .getCollationKey(lhs.get(title));
                CollationKey key2 = collator
                        .getCollationKey(rhs.get(title));
                return key1.compareTo(key2);
            }
        });
    }

    protected void sortAccordingContent(){
        Collections.sort(dataList, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> lhs, Map<String, String> rhs) {
                return lhs.get(content).compareTo(rhs.get(content));
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        doListViewOnItemClick();
    }

    /**
     * 异步任务调用这个方法获得数据
     * @return 这是读取到的数据集合
     */
    abstract protected List<Map<String,String>> getData();

    abstract public ListViewPopupWindows getListViewPopupWindow();

    /**
     * 当异步任务完成时将会回调这个方法
     */
    abstract protected void readDataFinish();

    /**
     * 当"ListView"的Item被点击，将会回调这个方法
     */
    abstract protected void doListViewOnItemClick();


   //    异步读取数据的内部类
    private class  ReadDataAsyncTask extends AsyncTask<Void,Void,List<Map<String,String>>>{

        @Override
        protected List<Map<String, String>> doInBackground(Void... params) {
            try {
                Thread.sleep(400L);
            }catch (Exception e){}
            return getData();
        }

        protected void onPostExecute(List<Map<String, String>> mapList) {
            super.onPostExecute(mapList);
            if(mapList==null){
//                给"ListView"设置一个空的布局
            }else{
                dataList = mapList;
                readDataFinish();//任务完成之后回调方法
//                将数据设置进适配器里
                listViewFragmentAdapter = new ListViewFragmentAdapter(getActivity(), dataList);
                listView.setAdapter(listViewFragmentAdapter);
            }
            loadingProgressBar.setVisibility(View.GONE);
            loadingTv.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
//            listViewFragmentAdapter = new ListViewFragmentAdapter(getActivity(), dataList);
//            listView.setAdapter(listViewFragmentAdapter);
        }
    }

}
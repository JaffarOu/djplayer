package com.jf.djplayer.controller.localmusic;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.base.fragment.BaseFragment;
import com.jf.djplayer.bean.Singer;
import com.jf.djplayer.controller.classifysong.ClassifySongActivity;
import com.jf.djplayer.datamanager.SingerLoader;
import com.jf.djplayer.controller.scansong.ScanSongEntranceActivity;
import com.jf.djplayer.util.LogUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17.
 * 本地音乐-歌手列表
 */
public class SingerListFragment extends BaseFragment implements
        SingerLoader.LoadListener, AdapterView.OnItemClickListener{

    private ListView listView;               // 歌手列表
    private SingerListAdapter singerListAdapter; // 歌手列表适配器
    private View loadingHintView;            // ListView加载提示
    private View emptyView;                  // ListView没数据时的提示
    private View footerView;                 // ListView的footView
    private boolean isDestroyView = false;   // 标识Fragment是否已执行onDestroyView()
    private List<Singer> singerList;         // 歌手列表数据

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View layoutView = inflater.inflate(R.layout.layout_local_music_list, container, false);
        isDestroyView = false;
        initView(layoutView);
        return layoutView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyView = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == ScanSongEntranceActivity.REQUEST_CODE_SCAN_MUSIC){
                //如果是扫描音乐的返回，调用异步任务刷新数据
                loadSinger();
            }
        }
    }

    private void initView(View layoutView){
        // find view
        listView = (ListView)layoutView.findViewById(R.id.lv_layout_local_music_list);
        loadingHintView = layoutView.findViewById(R.id.ll_layout_local_music_list_loading_view);
        emptyView = layoutView.findViewById(R.id.ll_layout_local_music_list_empty_view);
        // 扫描音乐执行按钮
        View scanMusicBtn = emptyView.findViewById(R.id.btn_layout_local_music_list_scan_music);
        scanMusicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.i("扫描音乐");
                startActivityForResult(new Intent(getActivity(), ScanSongEntranceActivity.class), ScanSongEntranceActivity.REQUEST_CODE_SCAN_MUSIC);
            }
        });
        loadSinger();
    }

    // 读取本地音乐
    private void loadSinger(){
        // 隐藏除了进度条以外的其它View
        listView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
        // 加载音乐
        SingerLoader singerLoader = new SingerLoader();
        singerLoader.setLoadListener(this);
        singerLoader.load();
    }

    /****************歌手加载器回调接口****************/
    @Override
    public void onSuccess(List<Singer> singerList) {
        this.singerList = singerList;
        if(isDestroyView){
            // no thing to do
        }else if( singerList == null || singerList.size() == 0 ){
            // 没有歌手
            emptyView.setVisibility(View.VISIBLE);
            loadingHintView.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }else{
            emptyView.setVisibility(View.INVISIBLE);
            loadingHintView.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            // 初始化ListView
            footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view, null);
            ((TextView) footerView.findViewById(R.id.tv_list_footer_view)).setText(singerList.size() + "歌手");
            listView.addFooterView(footerView);
            singerListAdapter = new SingerListAdapter(getActivity(), singerList);
            listView.setAdapter(singerListAdapter);
            listView.setOnItemClickListener(this);
        }
    }

    @Override
    public void onFailed(Exception exception) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        // 传递歌手名字到“分类歌曲显示界面”
        String singer = singerList.get(position).getName();
        Intent intent = new Intent(getActivity(), ClassifySongActivity.class);
        intent.putExtra(ClassifySongActivity.TYPE_NAME, ClassifySongActivity.TYPE_NAME_SINGER);
        intent.putExtra(ClassifySongActivity.TYPE_VALUE, singer);
        startActivity(intent);
    }
    /****************歌手加载器回调接口****************/
}

package com.jf.djplayer.localmusic;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.jf.djplayer.R;
import com.jf.djplayer.base.baseadapter.BaseExpandFragmentAdapter;
import com.jf.djplayer.module.SongInfo;

import java.util.List;

/**
 * Created by jf on 2016/5/20.
 * 本地音乐-"SongFragment"列表适配器
 */
public class SongFragmentAdapter extends BaseExpandFragmentAdapter {

    public SongFragmentAdapter(Fragment fragment, List<SongInfo> dataList) {
        super(fragment, dataList);
    }

    @Override
    protected void initBeforeChildViewReturn(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //如果当前歌曲被用户所收藏，需要修改显示图标
        if(dataList.get(groupPosition).getCollection() == 1){
            childItemImageId[0] = R.drawable.fragment_song_collection;
        }
    }


}

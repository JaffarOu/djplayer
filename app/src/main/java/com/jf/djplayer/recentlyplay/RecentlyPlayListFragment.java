package com.jf.djplayer.recentlyplay;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.jf.djplayer.R;
import com.jf.djplayer.interfaces.PlayControls;
import com.jf.djplayer.other.SongInfo;
import com.jf.djplayer.base.baseadapter.BaseExpandFragmentAdapter;
import com.jf.djplayer.base.basefragment.BaseExpandFragment;
import com.jf.djplayer.database.SongInfoOpenHelper;

import java.util.List;

/**
 * Created by JF on 2016/4/27.
 * 最近播放数据列表，显示最近所播放的歌曲
 */
public class RecentlyPlayListFragment extends BaseExpandFragment {

    private List<SongInfo> recentlyPlaySongInfo;//数据
    private PlayControls playControls;//歌曲播放控制接口对象
    private View footerView;//"ListView"的"footerView"

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        playControls = (PlayControls)getActivity();
    }

    @Override
    protected void initBeforeReturnView() {
        if(footerView == null){
            return;
        }
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(recentlyPlaySongInfo.size()+"首歌");
    }

    @Override
    protected View getLoadingView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.loading_layout,null);
    }

    @Override
    protected View getExpandListEmptyView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_no_recently_play,null);
    }

    @Override
    protected List getData() {
        recentlyPlaySongInfo = new SongInfoOpenHelper(getActivity()).getRecentlyPlaySong(8);
        //只有数据库里没有歌曲那是，才会为空
        if(recentlyPlaySongInfo == null){
            return null;
        }
        //遍历集合过滤所有从未播放过的歌曲（即"lastPlayTime==0"的歌）
        for(int i = recentlyPlaySongInfo.size()-1; i>=0; i--){
            if(recentlyPlaySongInfo.get(i).getLastPlayTime()==0){
                recentlyPlaySongInfo.remove(i);
            }
        }
        return recentlyPlaySongInfo;
    }

    @Override
    protected void asyncReadDataFinished(List dataList) {

    }

    @Override
    protected BaseExpandableListAdapter getExpandableAdapter() {
        return new BaseExpandFragmentAdapter(getActivity(),recentlyPlaySongInfo);
    }

    @Override
    protected boolean doOnGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        playControls.play(recentlyPlaySongInfo, groupPosition);
        return true;
    }

    @Override
    protected boolean doExpandableItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    protected View getExpandableHeaderView() {
        return null;
    }

    @Override
    protected View getExpandableFooterView() {
        if(recentlyPlaySongInfo==null || recentlyPlaySongInfo.size()==0){
            return null;
        }
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_footer_view,null);
        ((TextView)footerView.findViewById(R.id.tv_list_footer_view)).setText(recentlyPlaySongInfo.size()+"首歌曲");
        return footerView;
    }
}

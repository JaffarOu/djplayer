package com.jf.djplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jf.djplayer.SongInfo;
import com.jf.djplayer.dialogfragment.SetToBellDialog;
import com.jf.djplayer.tool.SongOperationUtils;
import com.jf.djplayer.R;
import com.jf.djplayer.broadcastreceiver.UpdateUiSongInfoReceiver;
import com.jf.djplayer.dialogfragment.DeleteSongDialogFragment;
import com.jf.djplayer.dialogfragment.SongInfoDialog;
import com.jf.djplayer.tool.database.SongInfoOpenHelper;

import java.util.List;

/**
 *
 * Created by Administrator on 2015/9/14.
 * 所有继承ExpandableFragment
 * 的类所用ExpandableListView
 * 适配
 */
public class ExpandableFragmentAdapter extends BaseExpandableListAdapter {

    private List<SongInfo> songInfoList;
    private Context context;
    private ExpandableListView expandableListView;

    public ExpandableFragmentAdapter(Context context, ExpandableListView expandableListView, List<SongInfo> songInfoList){
        this.songInfoList = songInfoList;
        this.context = context;
        this.expandableListView = expandableListView;
    }
    @Override
    public int getGroupCount() {
        return songInfoList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return songInfoList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //用于优化ListView性能的内部类
    private class MyGroupViewHolder {
        RelativeLayout rl = null;
        TextView position = null;
        TextView songName = null;
        TextView artistName = null;
        ImageView arrow = null;//箭头图标的ImageView
    }

    private class MyChildViewHolder {
        GridView gridView = null;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        MyGroupViewHolder myGroupViewHolder = null;
        if (convertView==null) {
            myGroupViewHolder = new MyGroupViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_expandable_fragment_group, null);
            myGroupViewHolder.position = (TextView) convertView.findViewById(R.id.tv_item_expandable_fragment_group_position);
            myGroupViewHolder.songName = (TextView)convertView.findViewById(R.id.tv_item_expandable_fragment_group_songName);
            myGroupViewHolder.artistName = (TextView)convertView.findViewById(R.id.tv_item_expandable_fragment_group_artist);
            myGroupViewHolder.arrow = (ImageView)convertView.findViewById(R.id.iv_item_expandable_fragment_group_arrow);
            convertView.setTag(myGroupViewHolder);
        }else{
            myGroupViewHolder = (MyGroupViewHolder)convertView.getTag();
        }
//        设置要显示的歌曲信息
        myGroupViewHolder.position.setText(groupPosition+1+"");
        myGroupViewHolder.songName.setText(songInfoList.get(groupPosition).getSongName());
        myGroupViewHolder.artistName.setText(songInfoList.get(groupPosition).getSongSinger());
        //如果栏目原本打开那就绘制向上那个箭头
        if (isExpanded) myGroupViewHolder.arrow.setImageResource(R.drawable.icon_drop);
//        否则会指向下那个
        else myGroupViewHolder.arrow.setImageResource(R.drawable.icon_down);
//        给箭头按钮设置监听器
        myGroupViewHolder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableListView.isGroupExpanded(groupPosition)) expandableListView.collapseGroup(groupPosition);
                else expandableListView.expandGroup(groupPosition);
            }
        });
        return convertView;
    }//getGroupView()

    /*
    expandableListView的子列表，
    使用自定义的那个GridView填充
     */
    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        MyChildViewHolder myChildViewHolder = null;
        if (convertView == null) {
            myChildViewHolder = new MyChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_expandable_fragment_chil, null);
            myChildViewHolder.gridView = (GridView) convertView.findViewById(R.id.gv_item_expandable_fragment_child);
            convertView.setTag(myChildViewHolder);
        } else {
            myChildViewHolder = (MyChildViewHolder) convertView.getTag();
        }
//        准备GridView要显示的文字以及图片
        String[] text = new String[]{"收藏", "删除", "添加", "设为铃声", "分享", "发送", "歌曲信息"};
        int[] icon = new int[]{R.drawable.fragment_song_no_collection, R.drawable.icon_del, R.drawable.icon_plus, R.drawable.icon_bell,
                R.drawable.expandable_fragment_adapter_childview_share, R.drawable.icon_send, R.drawable.icon_info};

//        如果歌曲已被收藏需要更改一下图片
        if (songInfoList.get(groupPosition).getCollection() == 1) icon[0] = R.drawable.fragment_song_collection;

        ExpandableChildItemAdapter childItemAdapter = new ExpandableChildItemAdapter(context, text, icon);
//        设置GridView每一项的点击事件
        myChildViewHolder.gridView.setOnItemClickListener(new ExpandableChildItemClick(songInfoList.get(groupPosition),groupPosition));
        myChildViewHolder.gridView.setAdapter(childItemAdapter);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /*
    该类定义"ExpandableListView"里的子列表里面的"GridView"里的item点击事件
     */
    private class ExpandableChildItemClick implements AdapterView.OnItemClickListener{
        private SongOperationUtils songOperationTools;
        private SongInfo songInfo;
        private int groupPosition;

        ExpandableChildItemClick(SongInfo songInfo,int groupPosition){
            this.songOperationTools = new SongOperationUtils(songInfo);
            this.songInfo = songInfo;
            this.groupPosition = groupPosition;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            如果用户收藏或者取消收藏某一首歌
            if(position==0){
                SongInfoOpenHelper collectionOpenHelper = new SongInfoOpenHelper(context,1);
                Intent updateCollection;
                if (songInfo.getCollection()==0) {
                //如果歌曲原先没有被收藏的
                    collectionOpenHelper.updateCollection(songInfo, 1);//1表示歌曲需收藏
                    songInfo.setCollection(1);
                    updateCollection = new Intent(UpdateUiSongInfoReceiver.COLLECTION_SONG);
                }else{
                    collectionOpenHelper.updateCollection(songInfo, 0);
                    songInfo.setCollection(0);
                    updateCollection = new Intent(UpdateUiSongInfoReceiver.CANCEL_COLLECTION_SONG);
                }
//                发送广播通知界面更新UI
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
                updateCollection.putExtra(UpdateUiSongInfoReceiver.position,groupPosition);//传递所操作的序号
                localBroadcastManager.sendBroadcast(updateCollection);
//                Toast.makeText(context,toastContent,Toast.LENGTH_SHORT).show();
            }else if(position==1){
//                如果点击删除歌曲
//                打开删除的提示框
                DeleteSongDialogFragment deleteSongDialogFragment = new DeleteSongDialogFragment((Activity)context,songInfo,groupPosition);
                deleteSongDialogFragment.show( ((Activity)context).getFragmentManager(),"DeleteSongDialogFragment");
            }else if(position==3){
                SetToBellDialog setToBellDialog = new SetToBellDialog(context,this.songInfo);
                setToBellDialog.show(((Activity)context).getFragmentManager(),"setToBellDialog");
            }else if(position==6){
                SongInfoDialog songInfoDialog = new SongInfoDialog(songInfo,groupPosition);
                songInfoDialog.show(((Activity)context).getFragmentManager(), "songOperationDialog");
            }//判断position
        }
    }//ExpandableChildItemClick

}

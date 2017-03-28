package com.anlaiye.swt.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 介绍：这里写介绍
 * 作者：sweet
 * 邮箱：sunwentao@imcoming.cn
 * 时间: 2017/3/28
 */
public abstract class ViewModle<T> implements IGetLastData<T>{
    private int maxSpanSize = 1;
    private List<T> dataList = new ArrayList<>();

    private List<T> emptyList = new ArrayList<>();

    public  T getItem(int dataPosition){
        if(dataPosition>=0&&dataPosition<dataList.size()){
            return dataList.get(dataPosition);
        }
        return null;
    }

    public T getItemByLayoutPosition(int layuotPosition){
        int dataPosition = layuotPosition-startPosition;
        return getItem(dataPosition);
    }


    public List<T> getDataList(){
        return new ArrayList<>(dataList);//重新创建list，避免同一个list被set进来，避免list在viewmodel之外被改变，没有调用notifyDataSetChanged
    }



    public int getSize(){
        return dataList.size();
    }


    public void clear(){
        dataList.clear();
        notifyDataSetChanged();
    }

    public void add(T t){
        dataList.add(t);
        notifyDataSetChanged();
    }

    public void add(int location,T t){
        if(location<0){
            return;
        }
        if(location>dataList.size()){
            dataList.add(dataList.size(), t);
        }else{
            dataList.add(location, t);
        }
        notifyDataSetChanged();
    }

    public void addAll(List<T> ts){
        dataList.addAll(ts);
        notifyDataSetChanged();
    }

    public void addAll(int location,List<T> ts){
        dataList.addAll(location, ts);
        notifyDataSetChanged();
    }

    public void remove(int location){
        dataList.remove(location);
        notifyDataSetChanged();
    }

    public void remove(T t){
        dataList.remove(t);
        notifyDataSetChanged();
    }

    public void set(int location,T t) {
        if(location<0||location>=dataList.size()){
            return;
        }
        dataList.set(location, t);
        notifyDataSetChanged();

    }

    public void setDataList(List<T> dataList) {

        this.dataList.clear();
        if(null!= dataList){
            this.dataList.addAll(dataList);
        }
       notifyDataSetChanged();
//        if(dataList!=null){
//            notifyDataSetChanged(startPosition,dataList.size());
//        }else{
//            notifyDataSetChanged();
//        }

    }

    public void setData(T data){
        this.dataList.clear();
        this.dataList.add(data);
//        if(data!=null){
//            notifyDataSetChanged(startPosition,1);
//        }else{
//            notifyDataSetChanged();
//        }

        notifyDataSetChanged();

    }


    public abstract BaseViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType);
    public int getLayoutId(int viewType){
        return 0;
    }

    public int getItemViewType(int dataPosition){
        return 0;
    }


    final BaseViewHolder<T> createViewHolder(ViewGroup parent, int viewType){
        if(context==null){
            context = parent.getContext();
            mInflater = LayoutInflater.from(context);
        }
        BaseViewHolder<T> viewHolder = onCreateViewHolder(parent,viewType);
        viewHolder.setOnModleItemClickLisenter(onModleItemClickLisenter);
        return viewHolder;
    }

    private OnModleItemClickLisenter<T> onModleItemClickLisenter;

    public void setOnModleItemClickLisenter(OnModleItemClickLisenter<T> onModleItemClickLisenter) {
        this.onModleItemClickLisenter = onModleItemClickLisenter;
    }


    public void notifyDataSetChanged(){
        if(getSpanCount()>1&&getSize()>0){
            dataList.removeAll(emptyList);
            emptyList.clear();
            if(getSize()>0){
                int emptySize = getSize()%getSpanCount();
                if(emptySize>0){
                    emptySize = getSpanCount()-emptySize;
                }
                for(int i = 0;i<emptySize;i++){
                    emptyList.add(null);
                }
                dataList.addAll(emptyList);
            }
        }

        if(dataChangeLisentner!=null){
            dataChangeLisentner.onDataSizeChange(this);
        }
    }

    private void notifyDataSetChanged(int startPosition,int itemCount){
        if(getSpanCount()>1&&getSize()>0){
            dataList.removeAll(emptyList);
            emptyList.clear();
            if(getSize()>0){
                int emptySize = getSize()%getSpanCount();
                if(emptySize>0){
                    emptySize = getSpanCount()-emptySize;
                }
                for(int i = 0;i<emptySize;i++){
                    emptyList.add(null);
                }
                dataList.addAll(emptyList);
            }
        }

        if(dataChangeLisentner!=null){
            dataChangeLisentner.onDataSizeChange(this,startPosition,itemCount);
        }
    }


    public void notifyItemChanged(int dataPosition){
        if(dataChangeLisentner!=null){
            dataChangeLisentner.onDataChange(this,dataPosition,dataPosition+startPosition);
        }
    }

    public void notifyItemChanged(int dataPosition,Object payload){
        if(dataChangeLisentner!=null){
            dataChangeLisentner.onDataChange(this,dataPosition,dataPosition+startPosition,payload);
        }
    }


    protected LayoutInflater mInflater;
    protected Context context;
    //修改getLayoutId()方法不必须重写
    @Deprecated
    protected View getItemView(ViewGroup parent, int viewType) {
        if (mInflater == null) {
            context = parent.getContext();
            mInflater = LayoutInflater.from(parent.getContext());
        }
        if(getLayoutId(viewType)<=0){
            return null;
        }
        View itemView = mInflater.inflate(getLayoutId(viewType), parent, false);
        //itemView.setBackgroundResource(R.drawable.app_listview_item_bg);
        return itemView;
    }
    /*****************为adapter helper 服务  不用管***********************/
    private int viewTypeFlag;
    private int startPosition;
    private DataChangeLisentner dataChangeLisentner;

    void setDataChangeLisentner(DataChangeLisentner dataChangeLisentner) {
        this.dataChangeLisentner = dataChangeLisentner;
    }

    protected int getStartPosition() {
        return startPosition;
    }

    void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }


    void setViewTypeFlag(int viewTypeFlag) {
        this.viewTypeFlag = viewTypeFlag;
    }

    final int getAdapterItemViewType(int dataPosition){
        return viewTypeFlag+getItemViewType(dataPosition);
    }


    public int getSpanCount(){
        return 1;
    }


    public int convertSpanCount(int position){
        int dataPosition = position-startPosition;
        int tempSpanCount = getSize()%getSpanCount();
        int tempPosition = getSize()-tempSpanCount;
        if(dataPosition>=tempPosition){
            return maxSpanSize/tempSpanCount;
        }
        return maxSpanSize/getSpanCount();
    }
    public final int getSpanSize(int position){
        if(maxSpanSize<=1){
            return 1;
        }
        if((maxSpanSize%getSpanCount())!=0){
            throw new RuntimeException("最大列数必须是所取列数的整数倍");
        }
        return convertSpanCount(position);
    }

    public void setMaxSpanSize(int maxSpanSize) {
        this.maxSpanSize = maxSpanSize;
    }


    public void refresh(){

    }


    @Override
    public LastData<T> getLastData(int dataPosition) {
        if(dataPosition==0){
            return null;
        }
        for(int index = dataPosition-1;index>=0;index--){
            if(isLastPosition(index)){
                return new LastData<>(index,getItem(index),getLayoutId(getItemViewType(index)));
            }
        }
        return null;
    }

    protected boolean isLastPosition(int dataPosition){
        return true;
    }
}

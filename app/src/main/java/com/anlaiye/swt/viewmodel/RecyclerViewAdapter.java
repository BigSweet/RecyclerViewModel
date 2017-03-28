package com.anlaiye.swt.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


/**
 * 介绍：这里写介绍
 * 作者：sweet
 * 邮箱：sunwentao@imcoming.cn
 * 时间: 2017/3/28
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder>{
    private IAdapterHelper adapterHelper;
    public RecyclerViewAdapter(ViewModle ... viewModles){
        adapterHelper = new AdapterHelper(this,viewModles);


    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // FIXME: 2016/8/9 处理空数据
        if(viewType==-1){
            return new SpaceViewHolder(parent);
        }
        ViewModle viewModle = adapterHelper.getViewModleByViewType(viewType);
        int modleViewType = adapterHelper.getModleViewType(viewType);
        BaseViewHolder viewHolder = viewModle.createViewHolder(parent, modleViewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        ViewModle viewModle = adapterHelper.getViewModleByPosition(position);
        int dataPosition = adapterHelper.getDataPosition(position);
        holder.bindData(viewModle.getItem(dataPosition),dataPosition,position);
    }



    @Override
    public int getItemCount() {
        return adapterHelper.getSize();
    }

    @Override
    public int getItemViewType(int position) {
        ViewModle viewModle = adapterHelper.getViewModleByPosition(position);
        // FIXME: 2016/8/9 处理空数据
        if(viewModle.getItemByLayoutPosition(position)==null){
            return -1;
        }
        int dataPosition = adapterHelper.getDataPosition(position);
        return viewModle.getAdapterItemViewType(dataPosition);
    }

    @Override
    public void onViewRecycled(BaseViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(BaseViewHolder holder) {
        //LogUtils.d("RecyclerViewAdapter", "onFailedToRecycleView:"+holder.getClass().getSimpleName() + holder.getAdapterPosition());
        return super.onFailedToRecycleView(holder);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        adapterHelper.setLayoutManager(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewDetachedFromWindow(BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.onDetachedFromWindow();
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.onAttachedToWindow();
    }
}

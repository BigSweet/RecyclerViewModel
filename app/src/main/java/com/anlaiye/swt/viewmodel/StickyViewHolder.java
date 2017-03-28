package com.anlaiye.swt.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 介绍：这里写介绍
 * 作者：sweet
 * 邮箱：sunwentao@imcoming.cn
 * 时间: 2017/3/28
 */
public abstract class StickyViewHolder<T> extends BaseViewHolder<T> implements IGetLastData<T> {
    private IGetLastData<T> getLastData;
    private LayoutInflater inflater;

    private T stickyData;
    private int stickyDataPosition;
    private int stickyLayoutPosition;

    private int stickyLayoutId;

    protected StickyViewHolder(View itemView) {
        super(itemView);
    }

    public StickyViewHolder(LayoutInflater inflater, ViewGroup parent, int layoutId, IGetLastData<T> lastData) {
        this(inflater.inflate(layoutId, parent, false));
        this.getLastData = lastData;
        this.inflater = inflater;
        this.stickyLayoutId = layoutId;
        itemView.setTag("stickyView");
    }

    @Override
    public void bindData(T t, int dataPosition, int layoutPosition) {
        super.bindData(t, dataPosition, layoutPosition);
        this.stickyData = t;
        this.stickyLayoutPosition = layoutPosition;
        this.stickyDataPosition = dataPosition;
    }


    @Override
    public LastData<T> getLastData(int dataPosition) {
        if (getLastData != null) {
            return getLastData.getLastData(dataPosition);
        }
        return null;
    }

    protected abstract StickyViewHolder<T> getStickyViewHolder(View itemView);


    void copyDataToStickyViewHolder(StickyViewHolder<T> stickyViewHolder) {
        stickyViewHolder.onBindData(stickyData, stickyDataPosition, stickyLayoutPosition);
    }



    View getStickyView(ViewGroup parent) {
        View stickyView = inflater.inflate(stickyLayoutId, parent, false);
        stickyView.setTag(stickyLayoutId);
        return stickyView;
    }

    int getStickyLayoutId() {
        return stickyLayoutId;
    }

    int getStickyDataPosition() {
        return stickyDataPosition;
    }
}

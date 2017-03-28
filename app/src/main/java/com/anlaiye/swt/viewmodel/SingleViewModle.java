package com.anlaiye.swt.viewmodel;

import android.view.ViewGroup;

/**
 * 介绍：这里写介绍
 * 作者：sweet
 * 邮箱：sunwentao@imcoming.cn
 * 时间: 2017/3/28
 */
public abstract class SingleViewModle<T> extends ViewModle<T>{
    @Override
    public BaseViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder<T>(getItemView(parent,viewType)) {

            @Override
            protected void onBindData(T t, int dataPosition, int layoutPosition) {
                SingleViewModle.this.bindData(this,t,dataPosition,layoutPosition);
            }
        };
    }

    @Override
    public int getItemViewType(int dataPosition) {
        return 0;
    }

    public abstract void bindData(BaseViewHolder<T> holder, T t, int dataPosition, int layoutPosition);
}

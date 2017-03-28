package com.anlaiye.swt.viewmodel;

import android.support.v7.widget.RecyclerView;


/**
 * 介绍：这里写介绍
 * 作者：sweet
 * 邮箱：sunwentao@imcoming.cn
 * 时间: 2017/3/28
 */
public interface IAdapterHelper {

    ViewModle getViewModleByPosition(int position);

    int getDataPosition(int position);

    int getSize();

    ViewModle getViewModleByViewType(int viewType);

    int getModleViewType(int viewType);

    void setLayoutManager(RecyclerView recyclerView);

}

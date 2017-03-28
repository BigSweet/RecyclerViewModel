package com.anlaiye.swt.viewmodel;


/**
 * 介绍：这里写介绍
 * 作者：sweet
 * 邮箱：sunwentao@imcoming.cn
 * 时间: 2017/3/28
 */
public interface DataChangeLisentner {
    void onDataChange(ViewModle viewModle, int dataPosition, int layoutPosition);

    void onDataSizeChange(ViewModle viewModle);

    void onDataSizeChange(ViewModle viewModle, int startPosition, int itemCount);

    void onDataChange(ViewModle viewModle, int dataPosition, int layoutPosition, Object payload);
}

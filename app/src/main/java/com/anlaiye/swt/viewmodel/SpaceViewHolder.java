package com.anlaiye.swt.viewmodel;

import android.view.LayoutInflater;
import android.view.ViewGroup;
/**
 * 介绍：这里写介绍
 * 作者：sweet
 * 邮箱：sunwentao@imcoming.cn
 * 时间: 2017/3/28
 */
public class SpaceViewHolder extends BaseViewHolder{
    public SpaceViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.a_test_space,parent,false));
    }

    @Override
    protected void onBindData(Object o, int dataPosition, int layoutPosition) {

    }
}

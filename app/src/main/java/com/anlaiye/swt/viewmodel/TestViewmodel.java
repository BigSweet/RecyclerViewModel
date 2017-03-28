package com.anlaiye.swt.viewmodel;

/**
 * 介绍：这里写介绍
 * 作者：sweet
 * 邮箱：sunwentao@imcoming.cn
 * 时间: 2017/3/28
 */

public class TestViewmodel extends SingleViewModle {


    @Override
    public void bindData(BaseViewHolder holder, Object o, int dataPosition, int layoutPosition) {
                holder.setText(R.id.tv,"呵呵呵呵呵呵呵呵呵呵");
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.test;
    }
}

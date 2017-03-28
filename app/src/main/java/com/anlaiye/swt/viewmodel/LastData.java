package com.anlaiye.swt.viewmodel;


/**
 * 介绍：这里写介绍
 * 作者：sweet
 * 邮箱：sunwentao@imcoming.cn
 * 时间: 2017/3/28
 */
public class LastData<T> {
    private int dataPosition;
    private T data;
    private int layoutId;

    public LastData(int dataPosition, T data, int layoutId) {
        this.dataPosition = dataPosition;
        this.data = data;
        this.layoutId = layoutId;
    }

    public int getDataPosition() {
        return dataPosition;
    }

    public T getData() {
        return data;
    }

    public int getLayoutId() {
        return layoutId;
    }

    @Override
    public String toString() {
        return "LastData{" +
                "dataPosition=" + dataPosition +
                ", data=" + data +
                ", layoutId=" + layoutId +
                '}';
    }
}

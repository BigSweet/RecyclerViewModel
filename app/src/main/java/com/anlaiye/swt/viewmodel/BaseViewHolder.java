package com.anlaiye.swt.viewmodel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



/**
 * 介绍：这里写介绍
 * 作者：sweet
 * 邮箱：sunwentao@imcoming.cn
 * 时间: 2017/3/28
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder{
    private SparseArray<View> mViews;
    protected View mConvertView;
    protected Context context;
    public BaseViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }


    public void bindData(final T t, final int dataPosition, final int layoutPosition){
        if(onModleItemClickLisenter!=null){
            mConvertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onModleItemClickLisenter.onModleItemClick(t, dataPosition, layoutPosition);
                }
            });
        }

        onBindData(t,dataPosition,layoutPosition);

    }

    protected abstract void onBindData(T t, int dataPosition, int layoutPosition);

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public BaseViewHolder<T> setText(int viewId, String text) {
        TextView tv = getView(viewId);
        if(tv!=null){
            tv.setText(text);
        }
        return this;
    }

    public BaseViewHolder<T> setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        if(tv!=null){
            tv.setText(text);
        }
        return this;
    }

    public BaseViewHolder<T> setText(TextView tv, String text) {
        if(tv!=null){
            tv.setText(text);
        }
        return this;
    }

    public BaseViewHolder<T> setTextColor(int viewId, int color) {
        TextView tv = getView(viewId);
        if(tv!=null){
            tv.setTextColor(color);
        }
        return this;
    }


    public BaseViewHolder<T> setVisable(int viewId,boolean visable){
        View view = getView(viewId);
        return setVisable(view,visable);
    }

    public BaseViewHolder<T> setVisable(View view,boolean visable){
        if(view==null){
            return this;
        }
        if(visable){
            if(view.getVisibility()!=View.VISIBLE){
                view.setVisibility(View.VISIBLE);
            }
        }else{
            if(view.getVisibility()!=View.GONE){
                view.setVisibility(View.GONE);
            }
        }
        return this;
    }


    public BaseViewHolder<T> setImageUrl(int viewId,String url){
//        ImageView imageView = getView(viewId);
//        if(imageView==null){
//            return this;
//        }
//        LoadImgUtils.loadImage(imageView, url);
//        return this;

        return setImageUrlBig(viewId,url);
    }


    public BaseViewHolder<T> setImageUrl(int viewId,String url,String uid){
        ImageView imageView = getView(viewId);
        LoadImgUtils.loadAvatar(imageView,url,uid);
        return this;
    }


    public BaseViewHolder<T> setImageUrlBig(int viewId,String url){
        ImageView imageView = getView(viewId);
        if(imageView==null){
            return this;
        }
        LoadImgUtils.loadImgBig(imageView, url);
        //Glide.with(imageView.getContext()).load(url).centerCrop().placeholder(R.drawable.app_default).into(imageView);
        return this;
    }


    public BaseViewHolder<T> setImageUrlFix(int viewId,String url){
//        ImageView imageView = getView(viewId);
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        if(imageView==null){
//            return this;
//        }
//        LoadImgUtils.loadImageDeepZoom(imageView, url);
//        return this;
        return setImageUrlBig(viewId,url);
    }

    public BaseViewHolder<T> setImageIcon(int viewId,int res){
        ImageView imageView = getView(viewId);
        if(imageView==null){
            return this;
        }
        imageView.setImageResource(res);
        return this;
    }

    public BaseViewHolder<T> setOnClickListner(int viewId,View.OnClickListener onClickListener){
        View view = getView(viewId);
        if(view==null){
            return this;
        }
        view.setOnClickListener(onClickListener);
        return this;
    }
    public BaseViewHolder<T> setSelected(int viewId,boolean selected){
        View view = getView(viewId);
        if(view==null){
            return this;
        }
        view.setSelected(selected);
        return this;
    }

    public BaseViewHolder<T> setPading(int viewId,int l,int r,int t,int b){
        View view = getView(viewId);
        if(view==null){
            return this;
        }
        view.setPadding(l,t,r,b);
        return this;
    }

    private OnModleItemClickLisenter<T> onModleItemClickLisenter;

    void setOnModleItemClickLisenter(OnModleItemClickLisenter<T> onModleItemClickLisenter) {
        this.onModleItemClickLisenter = onModleItemClickLisenter;
    }


    public void onAttachedToWindow(){

    }


    public void onDetachedFromWindow(){

    }
}

package com.anlaiye.swt.viewmodel;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;



/**
 * 介绍：这里写介绍
 * 作者：sweet
 * 邮箱：sunwentao@imcoming.cn
 * 时间: 2017/3/28
 */
class AdapterHelper implements IAdapterHelper, DataChangeLisentner {
    private Map<Integer, ViewModle> indexModleMap = new TreeMap<>();
    private static final int INDEX_START = 0;
    private static final int FLAG_VIEW_TYPE = 10000;
    private int lastIndex = INDEX_START;



    private Map<Integer, ViewModle> positionViewModels = new HashMap<>();
    private int size;

    private final RecyclerViewAdapter adapter;


    public AdapterHelper(final RecyclerViewAdapter adapter, ViewModle... vms) {
        this.adapter = adapter;
        List<ViewModle> viewModles = new ArrayList<>(Arrays.asList(vms));
        for (ViewModle viewModle : viewModles) {
            addViewModel(viewModle);
        }
        update();
    }


    public void addViewModel(ViewModle viewModle) {
        viewModle.setDataChangeLisentner(this);
        viewModle.setViewTypeFlag(lastIndex * FLAG_VIEW_TYPE);
        indexModleMap.put(lastIndex, viewModle);
        lastIndex++;
    }


    private void update(ViewModle viewModle) {
        viewModle.setStartPosition(size);
        size += viewModle.getSize();
    }


    private void update() {
        positionViewModels.clear();
        size = 0;
        Collection<ViewModle> viewModles = indexModleMap.values();
        for (ViewModle viewModle : viewModles) {
            update(viewModle);
        }
        adapter.notifyDataSetChanged();
    }


    private void update(ViewModle updateViewModle, int startPositon, int itemCount) {
        if (startPositon == 0 && itemCount == 0) {
            update();
            return;
        }
        positionViewModels.clear();
        size = 0;
        Collection<ViewModle> viewModles = indexModleMap.values();
        for (ViewModle viewModle : viewModles) {
            update(viewModle);
        }
        //adapter.notifyItemRangeInserted(startPositon,itemCount);
        //adapter.notifyItemRangeChanged(startPositon,size-startPositon);
        adapter.notifyDataSetChanged();
    }


    @Override
    public ViewModle getViewModleByPosition(int position) {
        if (positionViewModels.containsKey(position)) {
            return positionViewModels.get(position);
        }
        Collection<ViewModle> viewModles = indexModleMap.values();
        ViewModle result = null;
        int tempStartPosition = 0;
        for (ViewModle viewModle : viewModles) {
            tempStartPosition = viewModle.getStartPosition();
            if (position >= tempStartPosition && position < tempStartPosition + viewModle.getSize()) {
                result = viewModle;
                positionViewModels.put(position, result);
            }
        }
        if (result != null) {
            return result;
        }
        throw new RuntimeException("After the data changes, must be called notif");
    }

    @Override
    public int getDataPosition(int position) {
        ViewModle viewModle = getViewModleByPosition(position);
        int dataPosition = position - viewModle.getStartPosition();
        if (dataPosition >= 0 && dataPosition < viewModle.getSize()) {
            return dataPosition;
        }
        throw new RuntimeException("After the data changes, must be called notif");
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public ViewModle getViewModleByViewType(int viewType) {
        int index = viewType / FLAG_VIEW_TYPE;
        if (indexModleMap.containsKey(index)) {
            return indexModleMap.get(index);
        }
        throw new RuntimeException("After the data changes, must be called notif");
    }

    @Override
    public int getModleViewType(int viewType) {
        return viewType % FLAG_VIEW_TYPE;
    }

    @Override
    public void setLayoutManager(final RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null && layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager glm = ((GridLayoutManager) layoutManager);
            glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    ViewModle viewModle = getViewModleByPosition(position);
                    viewModle.setMaxSpanSize(glm.getSpanCount());
                    return viewModle.getSpanSize(position);
                }
            });
        }
        handlerStickyView(recyclerView);
    }


    @Override
    public void onDataChange(ViewModle viewModle, int dataPosition, int layoutPosition) {
        adapter.notifyItemChanged(layoutPosition);

        if(layoutViewHolder==null){//以下是为了解决sticky item数据变化
            return;
        }
        int layoutId = viewModle.getLayoutId(viewModle.getItemViewType(dataPosition));
        StickyViewHolder viewHolder = layoutViewHolder.get(layoutId);
        if(viewHolder!=null){
            viewHolder.bindData(viewModle.getItem(dataPosition),dataPosition,layoutPosition);
        }

    }
    @Override
    public void onDataChange(ViewModle viewModle, int dataPosition, int layoutPosition, Object payload) {
        adapter.notifyItemChanged(layoutPosition,payload);
        if(layoutViewHolder==null){//以下是为了解决sticky item数据变化
            return;
        }
        int layoutId = viewModle.getLayoutId(viewModle.getItemViewType(dataPosition));
        StickyViewHolder viewHolder = layoutViewHolder.get(layoutId);
        if(viewHolder!=null){
            viewHolder.bindData(viewModle.getItem(dataPosition),dataPosition,layoutPosition);
        }
    }



    @Deprecated
    @Override
    public void onDataSizeChange(ViewModle viewModle) {
        update(viewModle, 0, 0);
    }

    @Override
    public void onDataSizeChange(ViewModle viewModle, int startPosition, int itemCount) {
        update(viewModle, startPosition, itemCount);
    }




    private LinearLayout container;
    private StickyViewHolder currentStickyViewHolder = null;
    private int currentStickyLayoutId;
    private Map<Integer, StickyViewHolder> layoutViewHolder;

    private void handlerStickyView(final RecyclerView recyclerView) {
        if (!(recyclerView.getParent() instanceof FrameLayout)) {
            return;
        }
        layoutViewHolder = new HashMap<>();
        FrameLayout frameLayout = (FrameLayout) recyclerView.getParent();
        container = new LinearLayout(recyclerView.getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        frameLayout.addView(container, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            View stickyView;
            StickyViewHolder stickyViewHolder;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                stickyView = recyclerView.findViewWithTag("stickyView");
                if (stickyView == null) {
                    return;
                }
                int top = stickyView.getTop();

                stickyViewHolder = (StickyViewHolder) recyclerView.findContainingViewHolder(stickyView);
                if (top <= 0) {
                    container.setTranslationY(0);
                    showStityView(stickyViewHolder);
                } else if (top > 0 && top <= container.getMeasuredHeight()) {
                    container.setTranslationY(top - container.getMeasuredHeight());
                    rebindData(stickyViewHolder);
                } else {
                    container.setTranslationY(0);
                    rebindData(stickyViewHolder);
                }
            }
        });


        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                if(view==null){
                    return;
                }
                if(TextUtils.equals("stickyView",String.valueOf(view.getTag()))){//解决快速滑动跳过了stickyView的bug 此方案暂时解决
                    container.setTranslationY(0);
                    StickyViewHolder stickyViewHolder = (StickyViewHolder) recyclerView.findContainingViewHolder(view);
                    showStityView(stickyViewHolder);
                }

            }
        });
    }

    private void rebindData(StickyViewHolder holder) {
        if (holder == null) {
            return;
        }
        LastData lastData = holder.getLastData(holder.getStickyDataPosition());

        if (lastData == null) {
            showStityView(null);
            return;
        }


        int layoutId = lastData.getLayoutId();
        StickyViewHolder viewholder = layoutViewHolder.get(layoutId);
        if (viewholder == null) {
            showStityView(null);
            return;
        }
        if(changeSticyLayoutId(layoutId)){
            viewholder.bindData(lastData.getData(), lastData.getDataPosition(), 0);
        }
    }

    private void showStityView(StickyViewHolder holder) {
        if (handlerEmptyHolder(holder)) {
            return;
        }
        if (holder == currentStickyViewHolder) {
            return;
        }
        currentStickyViewHolder = holder;
        currentStickyLayoutId = holder.getStickyLayoutId();

        View child;
        View sticvyView = null;

        for (int i = 0; i < container.getChildCount(); i++) {
            child = container.getChildAt(i);
            if (TextUtils.equals(String.valueOf(child.getTag()), String.valueOf(currentStickyLayoutId))) {
                child.setVisibility(View.VISIBLE);
                sticvyView = child;
            } else {
                child.setVisibility(View.GONE);
            }
        }
        if (sticvyView == null) {
            sticvyView = holder.getStickyView(container);
            container.addView(sticvyView);
        }

        StickyViewHolder viewHolder = null;
        if (layoutViewHolder.containsKey(currentStickyLayoutId)) {
            viewHolder = layoutViewHolder.get(currentStickyLayoutId);
        } else {
            viewHolder = holder.getStickyViewHolder(sticvyView);
            layoutViewHolder.put(currentStickyLayoutId, viewHolder);
        }
        if (viewHolder != null) {
            holder.copyDataToStickyViewHolder(viewHolder);
        } else {
            showStityView(null);
        }

    }

    private boolean changeSticyLayoutId(int layoutId) {
        if (currentStickyLayoutId == layoutId) {
            return false;
        }
        View child;
        for (int i = 0; i < container.getChildCount(); i++) {
            child = container.getChildAt(i);
            if (TextUtils.equals(String.valueOf(child.getTag()), String.valueOf(layoutId))) {
                child.setVisibility(View.VISIBLE);
            } else {
                child.setVisibility(View.GONE);
            }
        }

        currentStickyLayoutId = layoutId;
        currentStickyViewHolder = layoutViewHolder.get(currentStickyLayoutId);
        return true;
    }


    private boolean handlerEmptyHolder(StickyViewHolder holder) {
        if (holder == null) {
            currentStickyViewHolder = null;
            for (int i = 0; i < container.getChildCount(); i++) {
                container.getChildAt(i).setVisibility(View.GONE);
            }
            return true;
        }
        return false;
    }

}

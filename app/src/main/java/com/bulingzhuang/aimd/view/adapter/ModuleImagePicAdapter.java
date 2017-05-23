package com.bulingzhuang.aimd.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bulingzhuang.aimd.R;
import com.bumptech.glide.Glide;
import com.yalantis.ucrop.entity.LocalMedia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by bulingzhuang
 * on 2017/5/10
 * E-mail:bulingzhuang@foxmail.com
 */

public class ModuleImagePicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<LocalMedia> mDataList;
    private Callback mCallback;

    public interface Callback {
        void notifyChange(int itemCount);
    }

    public ModuleImagePicAdapter(Context context, Callback callback) {
        mContext = context;
        mDataList = new ArrayList<>();
        mCallback = callback;
    }

    public void addAll(Collection<? extends LocalMedia> collection) {
        int itemCount = getItemCount() + collection.size();
        mCallback.notifyChange(itemCount);
        mDataList.addAll(collection);
        notifyItemRangeInserted(getItemCount(), collection.size());
    }

    public void moveItem(int movePosition, int targetPosition) {
        if (movePosition < targetPosition) {
            for (int i = movePosition; i < targetPosition; i++) {
                Collections.swap(mDataList, i, i + 1);
            }
        } else {
            for (int i = movePosition; i > targetPosition; i--) {
                Collections.swap(mDataList, i, i - 1);
            }
        }
        notifyItemMoved(movePosition, targetPosition);
    }

    public void removeItem(int position) {
        mCallback.notifyChange(getItemCount() - 1);
        mDataList.remove(position);
        notifyItemRemoved(position);
    }

    public List<LocalMedia> getDataList() {
        return mDataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_module_image, parent, false);
        return new ModuleImageAdapterViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ModuleImageAdapterViewHolder viewHolder = (ModuleImageAdapterViewHolder) holder;
        viewHolder.mIv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Glide.with(mContext).load(mDataList.get(position).getPath()).crossFade().into(viewHolder.mIv);
        viewHolder.mV.setAlpha(0);
        viewHolder.mIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    private class ModuleImageAdapterViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mIv;
        private final View mV;


        public ModuleImageAdapterViewHolder(View itemView) {
            super(itemView);
            mIv = (ImageView) itemView.findViewById(R.id.iv);
            mV = itemView.findViewById(R.id.v);
        }
    }
}

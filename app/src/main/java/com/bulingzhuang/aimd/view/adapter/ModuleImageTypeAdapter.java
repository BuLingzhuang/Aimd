package com.bulingzhuang.aimd.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bulingzhuang.aimd.R;
import com.bulingzhuang.aimd.entity.ModuleImageEntity;
import com.bulingzhuang.aimd.utils.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by bulingzhuang
 * on 2017/5/12
 * E-mail:bulingzhuang@foxmail.com
 */

public class ModuleImageTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private SparseArray<Integer[]> mLibArray;//图片组合样式库

    private Context mContext;
    private List<Integer> mDataList;
    private Callback mCallback;
    private int checkPosition = -1;

    public interface Callback {
        void checkedType(int type);
    }


    public ModuleImageTypeAdapter(Context context, Callback callback) {
        mContext = context;
        mDataList = new ArrayList<>();
        mCallback = callback;
        prepareLibArray();
    }

    private void prepareLibArray() {
        if (mLibArray == null) {
            mLibArray = new SparseArray<>();
            mLibArray.append(1, new Integer[]{R.drawable.ic_image_1_0});
            mLibArray.append(2, new Integer[]{R.drawable.ic_image_2_0, R.drawable.ic_image_2_1});
            mLibArray.append(3, new Integer[]{R.drawable.ic_image_3_0, R.drawable.ic_image_3_1, R.drawable.ic_image_3_2, R.drawable.ic_image_3_3});
            mLibArray.append(4, new Integer[]{R.drawable.ic_image_4_0, R.drawable.ic_image_4_1, R.drawable.ic_image_4_2, R.drawable.ic_image_4_3, R.drawable.ic_image_4_4});
            mLibArray.append(5, new Integer[]{R.drawable.ic_image_5_0, R.drawable.ic_image_5_1, R.drawable.ic_image_5_3, R.drawable.ic_image_5_4, R.drawable.ic_image_5_5, R.drawable.ic_image_5_6});
            mLibArray.append(6, new Integer[]{R.drawable.ic_image_6_0});
            mLibArray.append(7, new Integer[]{R.drawable.ic_image_7_0});
            mLibArray.append(8, new Integer[]{R.drawable.ic_image_8_0});
            mLibArray.append(9, new Integer[]{R.drawable.ic_image_9_0});
        }
    }

    public void changeList(int picNum) {
        prepareLibArray();
        mDataList.clear();
        if (picNum > 0 && picNum <= 9) {
            Integer[] integers = mLibArray.get(picNum);
            mDataList.addAll(Arrays.asList(integers));
        }
        checkPosition = -1;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_module_image_type, parent, false);
        return new ModuleImageTypeAdapterViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ModuleImageTypeAdapterViewHolder viewHolder = (ModuleImageTypeAdapterViewHolder) holder;
        Integer drawableId = mDataList.get(position);
        viewHolder.ivCenter.setImageDrawable(ContextCompat.getDrawable(mContext, drawableId));
        viewHolder.itemView.setOnClickListener(v -> {
            mCallback.checkedType(position);
            checkPosition = position;
            notifyDataSetChanged();
        });
        if (position == checkPosition) {
            viewHolder.rlCenter.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_module_i_white_c));
            viewHolder.ivCenterCheck.setVisibility(View.VISIBLE);
        } else {
            viewHolder.rlCenter.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_module_i_white));
            viewHolder.ivCenterCheck.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    private class ModuleImageTypeAdapterViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivCenter;
        private final ImageView ivCenterCheck;
        private final RelativeLayout rlCenter;

        public ModuleImageTypeAdapterViewHolder(View itemView) {
            super(itemView);
            ivCenter = (ImageView) itemView.findViewById(R.id.iv_center);
            ivCenterCheck = (ImageView) itemView.findViewById(R.id.iv_center_check);
            rlCenter = (RelativeLayout) itemView.findViewById(R.id.rl_center);
        }
    }
}

package com.bulingzhuang.aimd.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.bulingzhuang.aimd.R
import com.bumptech.glide.Glide
import com.yalantis.ucrop.entity.LocalMedia

import java.util.ArrayList
import java.util.Collections

/**
 * Created by bulingzhuang
 * on 2017/5/10
 * E-mail:bulingzhuang@foxmail.com
 */

class ModuleImagePicAdapter(private val mContext: Context, private val mCallback: ModuleImagePicAdapter.Callback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mDataList: MutableList<LocalMedia>

    interface Callback {
        fun notifyChange(itemCount: Int)
    }

    init {
        mDataList = ArrayList<LocalMedia>()
    }

    fun addAll(collection: Collection<LocalMedia>) {
        val itemCount = itemCount + collection.size
        mCallback.notifyChange(itemCount)
        mDataList.addAll(collection)
        notifyItemRangeInserted(getItemCount(), collection.size)
    }

    fun moveItem(movePosition: Int, targetPosition: Int) {
        if (movePosition < targetPosition) {
            for (i in movePosition..targetPosition - 1) {
                Collections.swap(mDataList, i, i + 1)
            }
        } else {
            for (i in movePosition downTo targetPosition + 1) {
                Collections.swap(mDataList, i, i - 1)
            }
        }
        notifyItemMoved(movePosition, targetPosition)
    }

    fun removeItem(position: Int) {
        mCallback.notifyChange(itemCount - 1)
        mDataList.removeAt(position)
        notifyItemRemoved(position)
    }

    val dataList: List<LocalMedia>
        get() = mDataList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(mContext).inflate(R.layout.item_module_image, parent, false)
        return ModuleImageAdapterViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ModuleImageAdapterViewHolder
        viewHolder.mIv.scaleType = ImageView.ScaleType.CENTER_INSIDE
        Glide.with(mContext).load(mDataList[position].path).crossFade().into(viewHolder.mIv)
        viewHolder.mV.alpha = 0f
        viewHolder.mIv.scaleType = ImageView.ScaleType.CENTER_CROP
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    private inner class ModuleImageAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val mIv: ImageView = itemView.findViewById(R.id.iv) as ImageView
        val mV: View = itemView.findViewById(R.id.v)
    }
}

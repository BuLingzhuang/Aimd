package com.bulingzhuang.aimd.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.transition.TransitionManager
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout

import com.bulingzhuang.aimd.R
import com.bulingzhuang.aimd.entity.ModuleImageEntity
import com.bulingzhuang.aimd.utils.Tools

import java.util.ArrayList
import java.util.Arrays

/**
 * Created by bulingzhuang
 * on 2017/5/12
 * E-mail:bulingzhuang@foxmail.com
 */

class ModuleImageTypeAdapter(private val mContext: Context, private val mCallback: ModuleImageTypeAdapter.Callback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mLibArray: SparseArray<Array<Int>>? = null//图片组合样式库
    private val mDataList: MutableList<Int>
    private var checkPosition = -1

    interface Callback {
        fun checkedType(type: Int)
    }


    init {
        mDataList = ArrayList<Int>()
        prepareLibArray()
    }

    private fun prepareLibArray() {
        if (mLibArray == null) {
            mLibArray = SparseArray<Array<Int>>()
            mLibArray!!.append(1, arrayOf(R.drawable.ic_image_1_0))
            mLibArray!!.append(2, arrayOf(R.drawable.ic_image_2_0, R.drawable.ic_image_2_1))
            mLibArray!!.append(3, arrayOf(R.drawable.ic_image_3_0, R.drawable.ic_image_3_1, R.drawable.ic_image_3_2, R.drawable.ic_image_3_3))
            mLibArray!!.append(4, arrayOf(R.drawable.ic_image_4_0, R.drawable.ic_image_4_1, R.drawable.ic_image_4_2, R.drawable.ic_image_4_3, R.drawable.ic_image_4_4))
            mLibArray!!.append(5, arrayOf(R.drawable.ic_image_5_0, R.drawable.ic_image_5_1, R.drawable.ic_image_5_3, R.drawable.ic_image_5_4, R.drawable.ic_image_5_5, R.drawable.ic_image_5_6))
            mLibArray!!.append(6, arrayOf(R.drawable.ic_image_6_0))
            mLibArray!!.append(7, arrayOf(R.drawable.ic_image_7_0))
            mLibArray!!.append(8, arrayOf(R.drawable.ic_image_8_0))
            mLibArray!!.append(9, arrayOf(R.drawable.ic_image_9_0))
        }
    }

    fun changeList(picNum: Int) {
        prepareLibArray()
        mDataList.clear()
        if (picNum in 1..9) {
            val integers = mLibArray!!.get(picNum)
            mDataList.addAll(Arrays.asList(*integers))
        }
        checkPosition = -1
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(mContext).inflate(R.layout.item_module_image_type, parent, false)
        return ModuleImageTypeAdapterViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val viewHolder = holder as ModuleImageTypeAdapterViewHolder
        val drawableId = mDataList[position]
        viewHolder.ivCenter.setImageDrawable(ContextCompat.getDrawable(mContext, drawableId))
        viewHolder.itemView.setOnClickListener {
            mCallback.checkedType(position)
            checkPosition = position
            notifyDataSetChanged()
        }
        if (position == checkPosition) {
            viewHolder.rlCenter.background = ContextCompat.getDrawable(mContext, R.drawable.bg_module_i_white_c)
            viewHolder.ivCenterCheck.visibility = View.VISIBLE
        } else {
            viewHolder.rlCenter.background = ContextCompat.getDrawable(mContext, R.drawable.bg_module_i_white)
            viewHolder.ivCenterCheck.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    private inner class ModuleImageTypeAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivCenter: ImageView = itemView.findViewById(R.id.iv_center) as ImageView
        val ivCenterCheck: ImageView = itemView.findViewById(R.id.iv_center_check) as ImageView
        val rlCenter: RelativeLayout = itemView.findViewById(R.id.rl_center) as RelativeLayout
    }
}

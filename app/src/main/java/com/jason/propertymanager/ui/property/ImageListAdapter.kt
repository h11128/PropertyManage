package com.jason.propertymanager.ui.property

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jason.propertymanager.R
import com.jason.propertymanager.databinding.AdapterImageListBinding
import com.squareup.picasso.Picasso

class ImageListAdapter() : RecyclerView.Adapter<ImageListAdapter.MyViewHolder>() {
    var mList = ArrayList<String>()
    private lateinit var mContext: Context
    lateinit var binding: AdapterImageListBinding

    class MyViewHolder(itemView: View, val binding: AdapterImageListBinding) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(item: String) {
            Picasso.get().load(item).resize(400, 400).centerCrop()
                .into(binding.imageAdapterImageList)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.adapter_image_list, parent, false)
        binding = AdapterImageListBinding.bind(view)
        return MyViewHolder(view, binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    fun updateItem(position: Int, data: String) {
        if (position + 1 > mList.size) {
            mList.add(data)
        } else {
            mList[position] = data
        }
        notifyDataSetChanged()
    }

    fun removeItem(data: String) {
        mList.remove(data)
        notifyDataSetChanged()
    }

    fun refreshDataList(newList: ArrayList<String>) {
        mList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }


}
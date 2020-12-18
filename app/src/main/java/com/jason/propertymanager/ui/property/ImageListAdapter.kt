package com.jason.propertymanager.ui.property

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.jason.propertymanager.R
import com.jason.propertymanager.databinding.AdapterImageBinding
import com.jason.propertymanager.other.*
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.coroutines.*
import java.lang.Exception

class ImageListAdapter() : RecyclerView.Adapter<ImageListAdapter.MyViewHolder>() {
    var mList = ArrayList<String>()
    private lateinit var mContext: Context
    lateinit var binding: AdapterImageBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView
    private lateinit var imageView: ImageView
    private lateinit var buttonClose: ImageButton


    class MyViewHolder(itemView: View, binding: AdapterImageBinding) :
        RecyclerView.ViewHolder(itemView) {
        val imageView = binding.image
        val progressBar = binding.progress
        val textView = binding.text
        val button = binding.buttonClose
        val target: Target = object: Target{
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                textView.text = "$load_status_4, loaded from $from"
                CoroutineScope(Dispatchers.Main).launch{
                    delay(1400L)
                }
                progressBar.visibility = View.GONE
                textView.visibility = View.GONE
                imageView.setImageBitmap(bitmap)
                imageView.visibility = View.VISIBLE
                button.bringToFront()


            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                textView.text = load_status_5
                CoroutineScope(Dispatchers.Main).launch{
                    withContext(Dispatchers.IO){
                        delay(1000L)
                    }
                    progressBar.visibility = View.GONE
                    textView.visibility = View.VISIBLE
                    imageView.setImageDrawable(errorDrawable)
                    imageView.visibility = View.VISIBLE
                    button.bringToFront()


                }
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                textView.text = load_status_2
                progressBar.visibility = View.VISIBLE
                textView.visibility = View.VISIBLE
                imageView.visibility = View.GONE
                button.bringToFront()


            }
        }
        fun bind(item: String) {
            if (item == load_status_1){
                textView.text = load_status_1
                progressBar.visibility = View.VISIBLE
                textView.visibility = View.VISIBLE
                imageView.visibility = View.GONE
                button.bringToFront()
                Log.d(tag_d, "bind $load_status_1")
            }
            else{
                Picasso.get().load(item).resize(400, 400).centerCrop().error(R.drawable.ic_baseline_error_24)
                    .into(target)
            }
            imageView.tag = target
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.adapter_image, parent, false)
        binding = AdapterImageBinding.bind(view)
        imageView = binding.image
        progressBar = binding.progress
        textView = binding.text
        buttonClose = binding.buttonClose

        return MyViewHolder(view, binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        buttonClose.setOnClickListener {
            removeItem(position)
        }
        holder.bind(mList[position])
    }

    fun updateItem(position: Int, data: String) {
        if (position == mList.size) {
            mList.add(data)
        } else {
            mList[position] = data
        }
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        mList.removeAt(position)
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
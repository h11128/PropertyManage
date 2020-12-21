package com.jason.propertymanager.ui.helper

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jason.propertymanager.R
import com.jason.propertymanager.data.model.Property
import com.jason.propertymanager.databinding.AdapterPropertyBinding
import com.jason.propertymanager.other.*
import com.jason.propertymanager.ui.property.PropertyAddFragment
import com.jason.propertymanager.ui.property.PropertyFragment
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.coroutines.*

class AdapterProperty : RecyclerView.Adapter<AdapterProperty.MyViewHolder>() {
    lateinit var binding: AdapterPropertyBinding
    private var mList: ArrayList<Property> = arrayListOf()
    var mParentFragment: PropertyFragment? = null

    class MyViewHolder(var itemView: View, binding: AdapterPropertyBinding) :
        RecyclerView.ViewHolder(itemView) {
        val textView = binding.textProgress
        val button = binding.buttonClose
        val imageView = binding.imageProperty
        val section1 = binding.linearLoadingSection
        val section2 = binding.linearPropertySection
        val textLocation = binding.textLocation
        val textPrice = binding.textPriceTitle
        val target: Target = object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                textView.text = "$load_status_4_success, loaded from $from"
                CoroutineScope(Dispatchers.Main).launch {
                    delay(1400L)
                }
                changeUI(section1, section2, load_status_4_success)
                imageView.setImageBitmap(bitmap)
                button.bringToFront()
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                textView.text = load_status_5_fail
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        delay(1000L)
                    }
                    changeUI(section1, section2, load_status_5_fail)
                    imageView.setImageDrawable(errorDrawable)
                    textLocation.text = "$load_status_5_fail, $e"
                    textPrice.visibility = View.VISIBLE
                    button.bringToFront()
                }
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                textView.text = load_status_2_onLoading
                changeUI(section1, section2, load_status_2_onLoading)
                button.bringToFront()
            }
        }

        fun bind(item: Property) {
            if (item.image == load_status_1_begin) {
                textView.text = load_status_1_begin
                changeUI(section1, section2, load_status_1_begin)
                button.bringToFront()
                Log.d(tag_d, "bind $load_status_1_begin")
            } else {
                val imageString = Property.imageStringToImageList(item.image)[0]
                textLocation.text = item.getAddressString()
                textPrice.text = item.getDetailString()
                if (Constant.isValidUrl(imageString)) {
                    Picasso.get()
                        .load(imageString)
                        .resize(400, 400)
                        .centerCrop()
                        .error(R.drawable.ic_baseline_error_24)
                        .into(target)
                    imageView.tag = target
                } else {
                    changeUI(section1, section2, load_status_5_fail)
                    imageView.setImageResource(R.drawable.ic_baseline_error_24)
                    textLocation.text = "Not image \n${textLocation.text}"
                    button.bringToFront()
                }

            }
        }
    }

    companion object {
        fun changeUI(section1: View, section2: View, status: String) {
            when (status) {
                load_status_1_begin, load_status_2_onLoading -> {
                    section1.visibility = View.VISIBLE
                    section2.visibility = View.GONE
                }
                load_status_4_success, load_status_5_fail -> {
                    section1.visibility = View.GONE
                    section2.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_property, parent, false)
        binding = AdapterPropertyBinding.bind(view)

        return MyViewHolder(view, binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        binding.buttonClose.setOnClickListener {
            notifyDataSetChanged()
            if (position < mList.size) {
                mParentFragment?.deleteProperty(mList[position])
            } else {
                mParentFragment?.deleteProperty(mList[itemCount-1])
                Log.d(
                    tag_d,
                    "error! try to remove position $position with adapter size ${mList.size}"
                )
            }
        }
        binding.root.setOnClickListener {
            mParentFragment?.activity?.supportFragmentManager?.beginTransaction()
                ?.addToBackStack("propertyFragment")
                ?.replace(R.id.nav_host_fragment, PropertyAddFragment.newInstance(mList[position]))
                ?.commit()
        }
        holder.bind(mList[position])

    }


    fun updateItem(position: Int, data: Property) {
        if (position == mList.size) {
            mList.add(data)
        } else {
            mList[position] = data
        }
        notifyDataSetChanged()
    }

    private fun removeItem(position: Int) {

        mList.removeAt(position)

        notifyDataSetChanged()
    }

    fun refreshDataList(newList: ArrayList<Property>) {
        mList = newList
        Log.d(tag_d, "refresh adapter size ${mList.size}")
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}
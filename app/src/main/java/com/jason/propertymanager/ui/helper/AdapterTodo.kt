package com.jason.propertymanager.ui.helper

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.jason.propertymanager.R
import com.jason.propertymanager.data.model.ToDoItem
import com.jason.propertymanager.databinding.AdapterTodoBinding
import com.jason.propertymanager.other.tag_d
import com.jason.propertymanager.ui.todo.ToDoFragment
import com.jason.propertymanager.ui.todo.TodoViewModel

class AdapterTodo : RecyclerView.Adapter<AdapterTodo.MyViewHolder>() {
    private lateinit var mContext: Context
    private lateinit var binding: AdapterTodoBinding
    var mList = ArrayList<ToDoItem>()
    var todoViewModel: TodoViewModel? = null

    class MyViewHolder(var todoViewModel: TodoViewModel?, var binding: AdapterTodoBinding, itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(item: ToDoItem) {
            binding.textTodoTitle.text = item.title
            if (item.detail.isNullOrEmpty()) {
                binding.buttonDetail.visibility = View.GONE
            } else {
                binding.buttonDetail.visibility = View.VISIBLE
                binding.textTodoDetail.text = item.detail
                binding.buttonDetail.setOnClickListener {
                    if (binding.textTodoDetail.visibility == View.VISIBLE){
                        binding.textTodoDetail.visibility = View.GONE
                        binding.buttonDetail.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                    }
                    else if (binding.textTodoDetail.visibility == View.GONE){
                        binding.textTodoDetail.visibility = View.VISIBLE
                        binding.buttonDetail.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                    }
                }
            }
            binding.checkboxTodo.setOnClickListener {
                Log.d(tag_d, "on click")
                if (item.isFinished){
                    val string = SpannableString(binding.textTodoDetail.text.toString())
                    string.setSpan(
                        StrikethroughSpan(),
                        0,
                        binding.textTodoDetail.text.toString().length - 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    Log.d(tag_d, "$string")
                    binding.textTodoDetail.text = string
                }
                item.isFinished = binding.checkboxTodo.isChecked
                todoViewModel?.updateTodo(item)
            }
            Log.d(tag_d, "current Item ${item}")

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.adapter_todo, parent, false)
        binding = AdapterTodoBinding.bind(view)
        return MyViewHolder(todoViewModel, binding, view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])
    }


    fun updateItem(position: Int, data: ToDoItem) {
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

    fun refreshDataList(newList: ArrayList<ToDoItem>) {
        mList = newList
        Log.d(tag_d, "refresh newList ${newList.size}")
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}
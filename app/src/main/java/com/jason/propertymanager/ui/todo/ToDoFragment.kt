package com.jason.propertymanager.ui.todo

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jason.propertymanager.R
import com.jason.propertymanager.data.model.ToDoItem
import com.jason.propertymanager.databinding.FragmentTodoBinding
import com.jason.propertymanager.other.tag_d
import com.jason.propertymanager.ui.helper.AdapterTodo


class ToDoFragment : Fragment() {

    private lateinit var todoViewModel: TodoViewModel
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!
    private var viewAdapter: AdapterTodo = AdapterTodo()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        todoViewModel =
            ViewModelProvider(this).get(TodoViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_todo, container, false)
        _binding = FragmentTodoBinding.bind(root)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding.recyclerTodo.apply {
            adapter = viewAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.fab.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("add todo item")
            val todoTitle = EditText(requireContext())
            val todoDetail = EditText(requireContext())
            val linearLayout = LinearLayout(requireContext())
            linearLayout.addView(todoTitle)
            linearLayout.addView(todoDetail)
            linearLayout.orientation = LinearLayout.VERTICAL
            linearLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )
            todoTitle.hint = "enter todo title"
            todoDetail.hint = "enter detail or leave blank"
            builder.setView(linearLayout)
            builder.setPositiveButton("OK") { dialogue, p1 ->
                dialogue?.dismiss()
                todoViewModel.addTodo(
                    ToDoItem(
                        todoTitle.text.toString(),
                        todoDetail.text.toString()
                    )
                )

            }
            builder.setNegativeButton("Cancel") { dialogue, p1 -> dialogue?.dismiss() }
            builder.show()
        }

        todoViewModel.todoList.observe(viewLifecycleOwner, {
            Log.d(tag_d, "observe $it")
            if (it != null) {
                val result = filterFinishedItem(it)
                when (result.size) {
                    0 -> binding.textFinishTodo.visibility = View.VISIBLE
                    else -> {
                        binding.textFinishTodo.visibility = View.GONE
                        viewAdapter.refreshDataList(result)
                    }
                }
            }
        }

        )
    }
    companion object{
        fun filterFinishedItem(it: ArrayList<ToDoItem>): ArrayList<ToDoItem>{
            val result = arrayListOf<ToDoItem>()
            for (item in it){
                if (!item.isFinished){
                    result.add(item)
                }
            }
            return result
        }

    }
}
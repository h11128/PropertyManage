package com.jason.propertymanager.ui.todo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
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
        viewAdapter.todoViewModel = todoViewModel
        binding.recyclerTodo.apply {
            adapter = viewAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.fab.setOnClickListener {
            val dialogFragment = TodoDialogFragment()
            dialogFragment.showNow(requireActivity().supportFragmentManager, "dialog fragment")
        }

        todoViewModel.todoList.observe(viewLifecycleOwner, {
            Log.d(tag_d, "observe $it")

            when (it.size) {
                0 -> binding.textFinishTodo.visibility = View.VISIBLE
                else -> {
                    binding.textFinishTodo.visibility = View.GONE
                    viewAdapter.refreshDataList(it)
                }
            }

        }

        )
    }

    override fun onStop() {
        super.onStop()
        Log.d(tag_d, "OnStop")
        todoViewModel.onCleared()
    }

    companion object {
        fun filterFinishedItem(it: ArrayList<ToDoItem>): ArrayList<ToDoItem> {
            val result = arrayListOf<ToDoItem>()
            for (item in it) {
                if (!item.isFinished) {
                    result.add(item)
                }
            }
            return result
        }

    }
}
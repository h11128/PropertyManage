package com.jason.propertymanager.ui.todo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.jason.propertymanager.R
import com.jason.propertymanager.data.model.ToDoItem
import com.jason.propertymanager.databinding.FragmentDialogTodoBinding

class TodoDialogFragment: DialogFragment() {
    private lateinit var binding: FragmentDialogTodoBinding
    val todoViewModel: TodoViewModel by activityViewModels()
    private lateinit var mDialog: Dialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view =  inflater.inflate(R.layout.fragment_dialog_todo, container)
        binding = FragmentDialogTodoBinding.bind(view)
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mDialog =  super.onCreateDialog(savedInstanceState)
        return mDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding.buttonDialogOK.setOnClickListener {
            todoViewModel.updateTodo(
                ToDoItem(
                    binding.editTodoTitle.text.toString(),
                    binding.editTodoDetail.text.toString()
                )
            )
            onDismiss(mDialog)
        }

        binding.buttonDialogCancel.setOnClickListener {
            onCancel(mDialog)
            onDismiss(mDialog)
        }
    }



}
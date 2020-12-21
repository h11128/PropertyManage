package com.jason.propertymanager.ui.todo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jason.propertymanager.data.model.ToDoItem
import com.jason.propertymanager.other.tag_d

class TodoViewModel : ViewModel() {




    val todoList =  MutableLiveData<ArrayList<ToDoItem>>()

    init {
        todoList.value = arrayListOf()
    }

    fun addTodo(item: ToDoItem) {
        Log.d(tag_d, "add todo ${item}")
        val temp = todoList.value!!
        temp.add(item)
        todoList.value = temp

    }

}
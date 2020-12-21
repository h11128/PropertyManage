package com.jason.propertymanager.ui.todo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jason.propertymanager.data.model.ToDoItem
import com.jason.propertymanager.other.tag_d

class TodoViewModel : ViewModel(), TodoRepository.RepoCallBack {

    val todoList =  MutableLiveData<ArrayList<ToDoItem>>()
    val todoRepository = TodoRepository().apply {
        repocallback = this@TodoViewModel
    }
    init {
        todoList.value = arrayListOf()
    }

    fun updateTodo(item: ToDoItem) {
        todoRepository.writeData(item)
    }

    fun removeTodo(item: ToDoItem){
        todoRepository.removeData(item)
    }

    override fun onChangeTodoInDataBase(todoLista: ArrayList<ToDoItem>) {
        todoList.value = todoLista
    }

    public override fun onCleared() {
        super.onCleared()
        Log.d(tag_d, "todo view model onClear")
        for (item in todoList.value!!){
            if (item.isFinished){
                removeTodo(item)
            }
        }
    }

}
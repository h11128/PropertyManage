package com.jason.propertymanager.ui.todo

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.jason.propertymanager.data.model.ToDoItem

class TodoRepository {
    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var databaseReference: DatabaseReference = Firebase.database.getReference("todo")
    var repocallback: RepoCallBack? = null

    interface RepoCallBack {
        fun onChangeTodoInDataBase(todoList: ArrayList<ToDoItem>)
    }

    init {
        databaseConnectionTesting()
        monitorDatabase()
    }

    private fun handleTask(task: Task<Void>, text: String) {
        if (task.isSuccessful) {
            Log.d("abc", "$text success.")
        } else {
            Log.d("abc", "$text fail ${task.exception?.message}.")
        }
    }


    private fun databaseConnectionTesting() {
        val connectedRef = firebaseDatabase.getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) {
                    Log.d("abc", "fireBase connected")
                } else {
                    Log.d("abc", "fireBase not connected")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("abc", "Listener was cancelled")
            }
        })
    }

    private fun monitorDatabase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList: ArrayList<ToDoItem> = arrayListOf()
                for (data in snapshot.children) {
                    val todo = data.getValue<ToDoItem>()
                    if (todo != null) {
                        tempList.add(todo)
                        repocallback?.onChangeTodoInDataBase(tempList)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("abc", "onCancel")
            }
        })
    }

    fun writeData(item: ToDoItem) {
        val key: String = if (item.key == "abc") {
            databaseReference.push().key!!
        } else {
            item.key
        }
        item.key = key
        databaseReference.child(key).setValue(item).addOnCompleteListener { task ->
            handleTask(task, "insert value <${item}> to firebase")
        }


    }

    fun removeData(item: ToDoItem) {
        val key = item.key
        databaseReference.child(key).removeValue().addOnCompleteListener { task ->
            handleTask(task, "delete value of id $key to firebase")
        }.addOnFailureListener { Log.d("abc", "meet exception ${it.message}") }
    }


}
package com.jason.propertymanager.data.model

class ToDoItem(
    var title: String? = null,
    var detail: String? = null,
    var isFinished: Boolean = false,
    var key: String = "abc"
) {
    override fun toString(): String{
        return "title $title detail $detail key $key"
    }
}
package com.example.countersapp.util

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.countersapp.ui.models.Counter


fun ViewBinding.visible(visible: Boolean = true) {
    root.visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.visible(visible: Boolean = true) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.invisible(invisible: Boolean = true) {
    visibility = if (invisible) View.INVISIBLE else View.VISIBLE
}

fun ViewGroup.inflate(layout: Int) = LayoutInflater.from(context).inflate(layout, this, false)

fun SharedPreferences.save(vararg data: Pair<String, Any>) {
    val editor = edit()
    data.forEach { (key, value) ->
        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Long -> editor.putLong(key, value)
        }

    }
    editor.apply()
}

fun <T> NavController.navigateBackWithResult(key: String, value: T) {
    previousBackStackEntry?.savedStateHandle?.set(key, value)
    popBackStack()
}

fun <T> Fragment.observe(liveData: LiveData<T>?, observer: Observer<in T>) {
    liveData?.observe(viewLifecycleOwner, observer)
}

fun <T> Fragment.getSavedStateLiveData(key: String) =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.observeSavedStateLiveData(key: String, observer: Observer<in T>) {
    observe(getSavedStateLiveData<T>(key), observer)
}
package com.example.countersapp.data.preferences

import android.content.SharedPreferences
import com.example.countersapp.util.save
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(private val prefs: SharedPreferences) {

    var firstTime: Boolean
        get() = prefs.getBoolean(KEY_FIRST_TIME, true)
        set(value) = prefs.save(KEY_FIRST_TIME to value)

    companion object{
        private const val KEY_FIRST_TIME = "first_time"
    }
}
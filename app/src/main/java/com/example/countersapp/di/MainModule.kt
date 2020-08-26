package com.example.countersapp.di

import android.app.Activity
import com.example.countersapp.ui.navigation.NavActivity
import com.example.countersapp.ui.navigation.Navigator
import com.example.countersapp.ui.navigation.NavigatorImp
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class MainModule {

    @Binds
    abstract fun binNavigator(navigatorImp: NavigatorImp): Navigator

    companion object {

        @Provides
        fun provideNavActivity(activity: Activity): NavActivity? {
            return activity as? NavActivity
        }
    }
}
package com.example.countersapp.di

import android.app.Activity
import com.example.countersapp.ui.NavActivity
import com.example.countersapp.ui.Navigator
import com.example.countersapp.ui.NavigatorImp
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class AppModule {

    @Binds
    abstract fun binNavigator(navigatorImp: NavigatorImp): Navigator
    companion object {

        @Provides
        fun provideNavActivity(activity: Activity): NavActivity? {
            return activity as? NavActivity
        }
    }
}
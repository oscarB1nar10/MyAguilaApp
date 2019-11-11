package com.example.myaguilaapp.di

import com.example.myaguilaapp.ui.MainActivity
import com.example.myaguilaapp.di.main.MainFragmentBuildersModule
import com.example.myaguilaapp.di.main.MainModule
import com.example.myaguilaapp.di.main.MapFragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule{

    @MapFragmentScope
    @ContributesAndroidInjector(
        modules = [MainFragmentBuildersModule::class, MainModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity

}
package com.example.myaguilaapp.di.main

import com.example.myaguilaapp.ui.initMapFragment.InitMapFragment
import com.example.myaguilaapp.ui.mapFragment.MapFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeMapFragment(): MapFragment

    @ContributesAndroidInjector
    abstract fun contributeInitMapFragment(): InitMapFragment

}
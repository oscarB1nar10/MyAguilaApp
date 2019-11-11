package com.example.myaguilaapp.di.main

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.example.myaguilaapp.R
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.*
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Named

@Module
abstract class MainModule{

    @Module
    companion object {

        @JvmStatic
        @MapFragmentScope
        @Provides
        @Named("bitMapDescriptorInitLocationIcon")
        fun bitMapDescriptorInitLocationIcon(application: Application): BitmapDescriptor {
            val vectorDrawable = ContextCompat.getDrawable(application, R.drawable.ic_my_location)
            MapsInitializer.initialize(application)
            vectorDrawable!!.setBounds(
                0,
                0,
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight
            )
            val bitmap = Bitmap.createBitmap(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            vectorDrawable.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }

        @JvmStatic
        @MapFragmentScope
        @Provides
        @Named("bitMapDescriptorLastLocationIcon")
        fun bitMapDescriptorLastLocationIcon(application: Application): BitmapDescriptor {
            val vectorDrawable = ContextCompat.getDrawable(application, R.drawable.ic_location)
            MapsInitializer.initialize(application)
            vectorDrawable!!.setBounds(
                0,
                0,
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight
            )
            val bitmap = Bitmap.createBitmap(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            vectorDrawable.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }

        @JvmStatic
        @MapFragmentScope
        @Provides
        @Named("bitMapDescriptorCurrentLocationIcon")
        fun provideBitMapDescriptorCurrentLocationIcon(application: Application): BitmapDescriptor {
            val vectorDrawable = ContextCompat.getDrawable(application, R.drawable.ic_directions_run_)
            MapsInitializer.initialize(application)
            vectorDrawable!!.setBounds(
                0,
                0,
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight
            )
            val bitmap = Bitmap.createBitmap(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            vectorDrawable.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }

        @JvmStatic
        @MapFragmentScope
        @Provides
        fun provideLocationRequest(): LocationRequest{
           return LocationRequest.create().apply {
                interval = 5000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
        }

        @JvmStatic
        @MapFragmentScope
        @Provides
        fun provideDot(): Dot{
            return Dot()
        }

        @JvmStatic
        @MapFragmentScope
        @Provides
        fun provideGap(): Gap{
            val PATTERN_GAP_LENGTH_PX = 20
            return  Gap(PATTERN_GAP_LENGTH_PX.toFloat())
        }

        @JvmStatic
        @MapFragmentScope
        @Provides
        fun providePolyLinePattern(dot: Dot, gap: Gap): MutableList<PatternItem>{
            return  Arrays.asList(dot, gap)
        }




    }


}
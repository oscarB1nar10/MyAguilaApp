package com.example.myaguilaapp.util

import com.google.android.gms.maps.model.LatLng

class Constants {

    companion object{
        const val DEFAULT_ZOOM: Float = 15f
        const val POLYLINE_STROKE_WIDTH_PX = 16
        const val COLOR_BLUE_ARGB = -0x657db
        const val DRAWABLE_RESOLUTION = 10f
        const val ERROR_DIALOG_REQUEST = 9001
        val DEFAULT_LOCATION: LatLng = LatLng(4.667426, -74.056624)
    }
}
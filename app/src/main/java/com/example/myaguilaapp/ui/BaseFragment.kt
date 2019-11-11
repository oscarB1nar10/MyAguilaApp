package com.example.myaguilaapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myaguilaapp.R
import dagger.android.support.DaggerFragment

abstract class BaseFragment : DaggerFragment(){
    //const
    private val TAG = "BaseFragment"
    private val REQUESTPERMISSION = 123
    private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION

    //vars
    protected var mLocationPermissionGranted = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val coordinatorLayout: CoordinatorLayout = layoutInflater.inflate(R.layout.fragment_base, null) as CoordinatorLayout
        val activityContainer: FrameLayout = coordinatorLayout.findViewById(R.id.fl_general_container)
        layoutInflater.inflate(initialLayout(), activityContainer, true)
        return  coordinatorLayout
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initComponents(view)
        backActivityOrFragment(view)
    }


    abstract fun initialLayout(): Int
    abstract fun initComponents(view: View)


    protected fun checkPermissions(){
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_NETWORK_STATE)

        if((ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_NETWORK_STATE )
            != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(activity!!.applicationContext,FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) ||
            (ContextCompat.checkSelfPermission(activity!!.applicationContext,COURSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)){

            activity?.let {
                ActivityCompat.requestPermissions(it, permissions, REQUESTPERMISSION)
            }
        }else{
            mLocationPermissionGranted = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when(requestCode){
            REQUESTPERMISSION -> {
                if (grantResults.isNotEmpty()) {
                    for (i in grantResults.indices) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false
                            Log.d(TAG, "onRequestPermissionsResult : permission failed")
                            return
                        }

                    }
                    Log.d(TAG, "onRequestPermissionsResult : permission granted")
                    mLocationPermissionGranted = true
                }}
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun backActivityOrFragment(view: View){
        val arrowBack = view.findViewById<ImageView>(R.id.imv_back_arrow_main)
        arrowBack.setOnClickListener {
            activity?.let {
                activity!!.onBackPressed()
            }
        }
    }
}
package com.example.myaguilaapp.ui.initMapFragment


import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.myaguilaapp.R
import com.example.myaguilaapp.ui.BaseFragment
import com.example.myaguilaapp.util.Constants.Companion.ERROR_DIALOG_REQUEST
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.android.synthetic.main.fragment_init_map.*

/**
 * A simple [Fragment] subclass.
 */
class InitMapFragment : BaseFragment() {

    //const
    private val TAG = "MainActivity"
    //vars
    private lateinit var navController: NavController


    override fun initialLayout(): Int {
        return R.layout.fragment_init_map
    }

    override fun initComponents(view: View) {
        navController = Navigation.findNavController(view)
        checkPermissions()
        if(isMapServiceOk()){
            initMap()
        }
    }

    private fun isMapServiceOk() : Boolean{
        Log.d(TAG, "inServicesOk:  checking google services version")
        when(val available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity)){
            ConnectionResult.SUCCESS -> {
                Log.i(TAG,"Google play services is working")
                return true
            }
            else -> {
                if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
                    Log.d(TAG, "an error occurred but we can fix it")
                    val dialog = GoogleApiAvailability.getInstance()
                        .getErrorDialog(activity, available, ERROR_DIALOG_REQUEST)
                    dialog.show()
                }
            }
        }
        return false
    }

    private fun initMap(){
        btn_map.setOnClickListener {
            if(mLocationPermissionGranted){
                navController.navigate(R.id.mapFragment)
            }else{
                checkPermissions()
            }
        }
    }


}

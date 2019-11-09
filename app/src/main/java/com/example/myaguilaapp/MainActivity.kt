package com.example.myaguilaapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //const
    private val TAG = "MainActivity"
    private val ERROR_DIALOG_REQUEST = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(isMapServiceOk()){
            initMap()
        }
    }
    private fun isMapServiceOk() : Boolean{
        Log.d(TAG, "inServicesOk:  checking google services version")
        val available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this@MainActivity)
        when(available){
            ConnectionResult.SUCCESS -> {
                Log.i(TAG,"Google play services is working")
                return true
            }
            else -> {
                if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
                    Log.d(TAG, "an error occurred but we can fix it")
                    val dialog = GoogleApiAvailability.getInstance()
                        .getErrorDialog(this@MainActivity, available, ERROR_DIALOG_REQUEST)
                    dialog.show()
                }
            }
        }
        return false
    }

    private fun initMap(){
        btn_map.setOnClickListener {
            val intent = Intent(this@MainActivity, MapActivity::class.java)
            startActivity(intent)
        }
    }
}

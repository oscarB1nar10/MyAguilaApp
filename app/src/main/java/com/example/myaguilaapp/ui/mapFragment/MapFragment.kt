package com.example.myaguilaapp.ui.mapFragment


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.myaguilaapp.R
import com.example.myaguilaapp.ui.BaseFragment
import com.example.myaguilaapp.util.Constants.Companion.COLOR_BLUE_ARGB
import com.example.myaguilaapp.util.Constants.Companion.DEFAULT_LOCATION
import com.example.myaguilaapp.util.Constants.Companion.DEFAULT_ZOOM
import com.example.myaguilaapp.util.Constants.Companion.DRAWABLE_RESOLUTION
import com.example.myaguilaapp.util.Constants.Companion.POLYLINE_STROKE_WIDTH_PX
import com.example.myaguilaapp.util.showMessage
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import javax.inject.Inject
import javax.inject.Named

/**
 * A simple [Fragment] subclass.
 */
class MapFragment : BaseFragment(), OnMapReadyCallback, SensorEventListener {

    //const
    private val TAG = "MapActivity"

    //vars
    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient : FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var linearAcceleration : Sensor
    private lateinit var sensorManager: SensorManager
    private  var markersInMap: ArrayList<Marker> = ArrayList()
    private  var currentVelocity: Int = 0

    @field:[Inject Named("bitMapDescriptorInitLocationIcon")]
    lateinit var bitMapDescriptorInitLocationIcon : BitmapDescriptor
    @field:[Inject Named("bitMapDescriptorLastLocationIcon")]
    lateinit var bitMapDescriptorLastLocationIcon : BitmapDescriptor
    @field:[Inject Named("bitMapDescriptorCurrentLocationIcon")]
    lateinit var provideBitMapDescriptorCurrentLocationIcon: BitmapDescriptor
    @Inject
    lateinit var provideLocationRequest: LocationRequest
    @Inject
    lateinit var providePolyLinePattern: MutableList<PatternItem>


    override fun initialLayout(): Int {
       return R.layout.fragment_map
    }

    override fun initComponents(view: View) {
        createLocationRequest()
    }

    private fun createLocationRequest() {
        initMap()
        startLocationUpdates()
        setUpLinearAccelerationSensor()
    }

    private fun startLocationUpdates() {
        activity?.let {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(it)
            updateCurrentLocation()
            mFusedLocationProviderClient.requestLocationUpdates(
                provideLocationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private fun updateCurrentLocation(){
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                activity?.let {
                    addMarkerToMapAndList(locationResult)
                }
            }
        }
    }

    private fun addMarkerToMapAndList(locationResult: LocationResult){
        if(::mMap.isInitialized){
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude))
                    .icon(provideBitMapDescriptorCurrentLocationIcon))
            if(markersInMap.size < 2){
                markersInMap.add(marker)
            }else{
                markersInMap.add(marker)
                markersInMap[0].remove()
                markersInMap.removeAt(0)
                showDistanceBetweenCurrentLocationAndLastLocation(locationResult)
            }
        }
    }

    private fun showDistanceBetweenCurrentLocationAndLastLocation(locationResult: LocationResult){
        markersInMap[0].title = "vel: $currentVelocity m/s"
        markersInMap[0].snippet = "distance: ${distanceBetweenPoints(locationResult)} m"
        markersInMap[0].showInfoWindow()
    }

    private fun distanceBetweenPoints(locationResult: LocationResult): Float{
        val finalLocation = Location("lastLocation")
        finalLocation.latitude = 4.672655
        finalLocation.latitude = -74.054071
        return locationResult.lastLocation.distanceTo(finalLocation)
    }

    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map1) as? SupportMapFragment
        mapFragment!!.getMapAsync(this)
    }

    private fun setUpLinearAccelerationSensor() {
        this.sensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        linearAcceleration = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        linearAcceleration.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getDeviceLocation()
        mMap.isMyLocationEnabled = true
        addRoutePolyLine()
    }

    private fun getDeviceLocation(){
        try {
            getLastLocationIfExist()
        }catch (e: SecurityException){
            Log.e(TAG, "getDeviceLocation: SecurityException ${e.message}")
        }
    }

    @Throws(SecurityException::class)
    private fun getLastLocationIfExist() {
        val location: com.google.android.gms.tasks.Task<Location> = mFusedLocationProviderClient.lastLocation
        location.addOnCompleteListener {
            it.let {
                if (it.isSuccessful) {
                    val currentLocation: Location? = it.result
                    if (currentLocation == null) {
                        moveCamera(DEFAULT_LOCATION)
                    } else {
                        moveCamera(LatLng(currentLocation.altitude, currentLocation.longitude))
                    }

                } else {
                    activity?.showMessage("Unable to get current location")
                }
            }
        }

    }

    private fun moveCamera(latLng: LatLng){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM))
    }

    private fun addRoutePolyLine(){
        val polyline1 = mMap.addPolyline(
            PolylineOptions()
                .clickable(true)
                .pattern(providePolyLinePattern)
                .add(
                    LatLng(4.667426, -74.056624),
                    LatLng(4.672655, -74.054071)
                )
        )
        polyline1.tag = "firstPolyLine"
        setStyleForPolyLines(polyline1)
    }

    private fun setStyleForPolyLines(polyline: Polyline) {
        when (polyline.tag.toString()) {
            "firstPolyLine" -> {
                polyline.startCap = CustomCap(
                    bitMapDescriptorInitLocationIcon, DRAWABLE_RESOLUTION
                )
                polyline.endCap = CustomCap(
                    bitMapDescriptorLastLocationIcon, DRAWABLE_RESOLUTION
                )
            }
        }
        polyline.width = POLYLINE_STROKE_WIDTH_PX.toFloat()
        polyline.color = COLOR_BLUE_ARGB
        polyline.jointType = JointType.ROUND
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.i(TAG,"No action to do")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when(event?.sensor?.type){
            Sensor.TYPE_LINEAR_ACCELERATION -> {
                activity?.let {
                    if(event.values[0] > 2){
                        currentVelocity = event.values[0].toInt()
                    }
                }
            }
        }
    }
}

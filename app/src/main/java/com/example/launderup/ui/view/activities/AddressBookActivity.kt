package com.example.launderup.ui.view.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.launderup.R
import com.example.launderup.ui.adapter.AddressAdapter
import com.google.android.gms.location.*


class AddressBookActivity : AppCompatActivity() {
    private lateinit var newLocation:FusedLocationProviderClient
    private val permissionID:Int=42
    private var latitude:Double=0.0
    private var longitude:Double=0.0
    private lateinit var addressRecyclerView: RecyclerView
    private lateinit var addNewAddressButton: Button
    private val sharedPrefFile = "LaunderUp"
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_book)
        sharedPreferences=this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        addNewAddressButton=findViewById(R.id.address_book_button)
        addressRecyclerView=findViewById(R.id.address_book_recycler_view)

        addressRecyclerView.layoutManager= LinearLayoutManager(this)

        val addressAdapter = AddressAdapter(this@AddressBookActivity)
        addressRecyclerView.adapter=addressAdapter

        addNewAddressButton.setOnClickListener {
            newLocation=LocationServices.getFusedLocationProviderClient(this)
            getLocation()
//            val intent=Intent(this, EditAddressActivity::class.java)
//            startActivity(intent)
        }
    }

    private fun getLocation(){
        if(checkPermissions()){
            if(isLocationEnabled()){
                newLocation.lastLocation.addOnCompleteListener(this) {task->
                        val location:Location = task.result
                        if (location == null) {
                            requestNewLocationData()
                        } else {
                            latitude=location.latitude
                            longitude=location.longitude
                            Log.i("Latitude",latitude.toString())
                            Log.i("Longitude",longitude.toString())
                        }
                }
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                val intent= Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent);
            }
        } else requestPermissions()
    }

    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        newLocation= LocationServices.getFusedLocationProviderClient(this)
        newLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
            latitude=mLastLocation.latitude
            longitude=mLastLocation.longitude
        }
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)==PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager:LocationManager= getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermissions() {
        val permission:Array<String> = arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION)
        ActivityCompat.requestPermissions(this,permission, permissionID)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == permissionID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            }
        }
    }

//    override fun onResume() {
//        super.onResume()
//        if(checkPermissions())
//            getLocation()
//    }

}
package com.fitscorp.apps.indika.schoolbus.firebase

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

import android.os.Bundle
import android.os.Looper

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.fitscorp.apps.indika.schoolbus.R
import com.fitscorp.apps.indika.schoolbus.collection.MarkerCollection
import com.fitscorp.apps.indika.schoolbus.firebase.model.Bus
import com.fitscorp.apps.indika.schoolbus.helpers.FirebaseEventListenerHelper
import com.fitscorp.apps.indika.schoolbus.helpers.GoogleMapHelper
import com.fitscorp.apps.indika.schoolbus.helpers.MarkerAnimationHelper
import com.fitscorp.apps.indika.schoolbus.helpers.UiHelper
import com.fitscorp.apps.indika.schoolbus.interfaces.FirebaseDriverListener
import com.fitscorp.apps.indika.schoolbus.interfaces.LatLngInterpolator
import com.fitscorp.apps.indika.schoolbus.login.ContactActivity
import com.fitscorp.apps.indika.schoolbus.login.MsgToDriver_Activity
import com.fitscorp.apps.indika.schoolbus.login.UserRegister
import com.fitscorp.apps.indika.schoolbus.model.BusListMainResponse
import com.fitscorp.apps.indika.schoolbus.model.BusMainListData
import com.fitscorp.apps.indika.schoolbus.model.BusStatusObj
import com.fitscorp.apps.indika.schoolbus.model.Driver
import com.fitscorp.apps.indika.schoolbus.rest.ApiInterface
import com.fitscorp.apps.indika.schoolbus.rest.PrefManager
import com.fitscorp.apps.indika.schoolbus.rest.RetrofitApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_new_map_kotlin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NewMapKotlinActivity : AppCompatActivity(), FirebaseDriverListener {

    companion object {
        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6161
        private const val ONLINE_DRIVERS = "online_drivers"
        private const val ZOOM_LEVEL = 15
        private const val TILT_LEVEL = 15

    }
    var busNumber:String=""
    var driverName:String=""

    private lateinit var googleMap: GoogleMap
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private var locationRequest: LocationRequest? = null
    private lateinit var locationCallback: LocationCallback
    private var locationFlag = true
    private lateinit var valueEventListener: FirebaseEventListenerHelper
    private val uiHelper = UiHelper()
    private val googleMapHelper = GoogleMapHelper()
    private val databaseReference = FirebaseDatabase.getInstance().reference.child(ONLINE_DRIVERS)
    internal var lastLocationData: LatLng? = null
    private lateinit var mMap: GoogleMap
    private var SELCTED_DRIVER = "0"
    internal var pref: PrefManager?=null

    private var mFirebaseDatabase: DatabaseReference? = null
    private var mFirebaseInstance: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_map_kotlin)
        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.supportMap) as SupportMapFragment
        mapFragment.getMapAsync { googleMap = it }

        pref = PrefManager(this)
        SELCTED_DRIVER=  pref!!.getBusRegistered()


        // SAVE BUS STATUS IN FIREBASE
        mFirebaseInstance = FirebaseDatabase.getInstance()
        mFirebaseDatabase = mFirebaseInstance!!.getReference("buses")
        mFirebaseInstance!!.getReference("app_title").setValue("Schoooly Driver Database")



        createLocationCallback()

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = uiHelper.getLocationRequest()

        if (!uiHelper.isPlayServicesAvailable(this)) {
            Toast.makeText(this, "Play Services did not installed!", Toast.LENGTH_SHORT).show()
            finish()
        } else requestLocationUpdate()

        valueEventListener = FirebaseEventListenerHelper(this)

        databaseReference.addChildEventListener(valueEventListener)


      if(pref!!.getBusOwner().get("str_driver_name")!!.isNotEmpty()){

          var busNumber:String="Bus Number  :  "+ pref!!.getBusOwner().get("str_driver_name")
          var driverName:String="Driver Name  :  "+pref!!.getBusOwner().get("str_bus_number")

          txt_bus_number.text=busNumber
          txt_driver_name.text=driverName
      }else{
          getMyBus(pref!!.getBusRegistered())
      }
        mFirebaseDatabase!!.child("37").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
            //    Log.e(TAG, "App title updated")

              //  val appTitle = dataSnapshot.getValue(String::class.java)
                var bus = dataSnapshot.getValue(Bus::class.java)
                if(bus!!.status.equals("10")){
                    txt_driver_status.text="Bus Status : Not Ready"
                }
                if(bus!!.status.equals("0")){
                    txt_driver_status.text="Bus Status :  Travelling Started"
                }
                if(bus!!.status.equals("1")){
                    txt_driver_status.text="Bus Status : Picked"
                }
                if(bus!!.status.equals("3")){
                    txt_driver_status.text="Bus Status : Travelling"
                }
                if(bus!!.status.equals("4")){
                    txt_driver_status.text="Bus Status : Dropped"
                }
                // update toolbar title
              //  supportActionBar.setTitle(appTitle)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e("onCancelled", "Failed to read app title value.", error.toException())
            }
        })
        mFirebaseDatabase!!.child("37").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val bus = dataSnapshot.getValue(Bus::class.java)

                // Check for null
                if (bus == null) {
                 //   Log.e(TAG, "Bus data is null!")
                    return
                }

              //  Log.e(TAG, "User data is changed!" + bus!!.id + ", " + bus.status)

                // Display newly updated name and email
                //    txtDetails.setText(bus.name + ", " + user.email)

                // clear edit text
                //    inputEmail.setText("")
                //    inputName.setText("")

                //    toggleButton()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
             //   Log.e(TAG, "Failed to read user", error.toException())
            }
        })
    }

    override fun onResume() {
        super.onResume()

      //  getMyBusStatus(pref!!.getBusRegistered())
    }


    @SuppressLint("MissingPermission")
    private fun requestLocationUpdate() {
        if (!uiHelper.isHaveLocationPermission(this)) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
            return
        }
        if (uiHelper.isLocationProviderEnabled(this))

//            uiHelper.showPositiveDialogWithListener(this, resources.getString(R.string.need_location), resources.getString(R.string.location_content), object : IPositiveNegativeListener {
//                override fun onPositive() {
//                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
//                }
//            }, "Turn On", false)

            animateCamera(this!!.lastLocationData!!)
        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

    }

    public fun sendMessage(v: View) {
        startActivity(Intent(this, MsgToDriver_Activity::class.java))
    }
    public fun callToDriver(v: View) {
        val intent = Intent(Intent.ACTION_DIAL)
        // intent.setData(Uri.parse("tel:0773771925"));
        val phone:String="0773771925"
        intent.data = Uri.parse("tel:$phone")
        startActivity(intent)
    }
    public fun editProfile(v: View) {
        startActivity(Intent(this, UserRegister::class.java))
    }
    public fun contactUs(v: View) {
        startActivity(Intent(this, ContactActivity::class.java))
    }
    public fun RefreshGPS(v: View) {
        val cameraUpdate = googleMapHelper.buildCameraUpdate(this!!.lastLocationData!!)
        googleMap.moveCamera(cameraUpdate)
        googleMap.animateCamera(cameraUpdate, 17, null)
    }


    private fun createLocationCallback() {
        Log.d("PPPPPP", "----------createLocationCallback----------------------------")
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                if (locationResult!!.lastLocation == null) return
                val latLng = LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
                lastLocationData = latLng

                Log.e("Location", latLng.latitude.toString() + " , " + latLng.longitude)
                if (locationFlag) {
                    locationFlag = false
                    animateCamera(latLng)
                }
            }
        }
    }


    private fun animateCamera(latLng: LatLng) {
        if (googleMap != null) {
            val cameraUpdate = googleMapHelper.buildCameraUpdate(latLng)
            googleMap.moveCamera(cameraUpdate)
            googleMap.animateCamera(cameraUpdate, 15, null)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            val value = grantResults[0]
            if (value == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Location Permission denied", Toast.LENGTH_SHORT).show()
                finish()
            } else if (value == PackageManager.PERMISSION_GRANTED) requestLocationUpdate()
        }
    }


    override fun onDriverAdded(driver: Driver) {
        if (driver.driverId == SELCTED_DRIVER) {
            var marker = MarkerCollection.getMarker(driver.driverId)
            val busLoc = LatLng(driver.lat, driver.lng)

            if (marker == null) {
                val markerOptions = googleMapHelper.getDriverMarkerOptions(busLoc)
                marker = googleMap.addMarker(markerOptions)
                marker!!.tag = driver.driverId
                MarkerCollection.insertMarker(driver.driverId, marker)
            }
            marker.position = busLoc
            animateCamera(busLoc)
            totalOnlineDrivers.text =
                resources.getString(R.string.total_online_drivers).plus(" ").plus(MarkerCollection.allMarkers().size)
        }
    }

    override fun onDriverRemoved(driver: Driver) {
        MarkerCollection.removeMarker(driver.driverId)
        totalOnlineDrivers.text =
            resources.getString(R.string.total_online_drivers).plus(" ").plus(MarkerCollection.allMarkers().size)
    }

    override fun onDriverUpdated(driver: Driver) {
        val marker = MarkerCollection.getMarker(driver.driverId)
        marker?.position = LatLng(driver.lat, driver.lng)
        animateCamera(marker?.position!!)
        MarkerAnimationHelper.animateMarkerToGB(
            marker!!,
            LatLng(driver.lat, driver.lng),
            LatLngInterpolator.Spherical()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseReference.removeEventListener(valueEventListener)
        locationProviderClient.removeLocationUpdates(locationCallback)
        MarkerCollection.clearMarkers()
    }

    fun getMyBus(busid: String) {

        val apiService = RetrofitApiClient.getClient().create(ApiInterface::class.java)
        val call = apiService.getMyBus(busid)
        call.enqueue(object : Callback<BusListMainResponse> {
            override fun onResponse(call: Call<BusListMainResponse>, response: Response<BusListMainResponse>) {
                if (response.isSuccessful) {

                    val dataList = response.body().data
                    txt_bus_number.text="Bus Number "+ dataList.get(0).busNumber
                    txt_driver_name.text="Driver Name "+dataList.get(0).driverName

                    dataList.get(0).driverPhone

                }
            }

            override fun onFailure(call: Call<BusListMainResponse>, t: Throwable) {


                println("========t======$t")
            }
        })
    }

    fun getMyBusStatus(busid: String) {

        val apiService = RetrofitApiClient.getClient().create(ApiInterface::class.java)
        val call = apiService.getMyBusStatus(busid)
        call.enqueue(object : Callback<List<BusMainListData>> {
            override fun onResponse(call: Call<List<BusMainListData>>, response: Response<List<BusMainListData>>) {
                if (response.isSuccessful) {
                    val dataList = response.body()



                    println("=======Status====="+dataList.get(0).travel_status)

                }
            }

            override fun onFailure(call: Call<List<BusMainListData>>, t: Throwable) {


                println("========t======$t")
            }
        })
    }
}



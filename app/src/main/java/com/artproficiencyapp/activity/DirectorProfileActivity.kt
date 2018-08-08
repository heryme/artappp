package com.artproficiencyapp.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.artproficiencyapp.R
import com.artproficiencyapp.extension.goActivity
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.gms.location.places.*
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_director.*
import kotlinx.android.synthetic.main.parent_toolbar.*
import android.text.method.TextKeyListener.clear
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.OnMapReadyCallback
import android.graphics.PorterDuff
import android.graphics.Bitmap
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.view.LayoutInflater
import android.support.v4.content.ContextCompat
import android.widget.ImageView


import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Marker
import java.io.IOException
import java.util.*


class DirectorProfileActivity : BaseActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private val PLACE_PICKER_REQ_CODE = 2
    private val PLACE_PICKER = 1
    private var geoDataClient: GeoDataClient? = null
    //    private var placeName: TextView? = null
    private var photosDataList: MutableList<PlacePhotoMetadata>? = null
    private var currentPhotoIndex = 0
    internal lateinit var mapFragment: SupportMapFragment
    internal lateinit var map: GoogleMap
    var photoBitmap: Bitmap? = null
    internal var mGoogleApiClient: GoogleApiClient? = null
    internal lateinit var mLastLocation: Location
    internal var mCurrLocationMarker: Marker? = null
    internal lateinit var mLocationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_director)
        tv_toolbar_title.setText("Director Profile")
        img_toolbar_edit.setVisibility(View.VISIBLE)
        img_toolbar_edit.setOnClickListener {
            goActivity<EditProfileActivity>()
            finish()
        }
        img_toolbar_back.setOnClickListener {
            /*goActivity<DirectorDashboardActiv>()
            finish()*/
            val i = Intent(this@DirectorProfileActivity, DashBordActivityDIreStu::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(i)
            finish()
        }
        val imageUri = Uri.parse("https://i.imgur.com/tGbaZCY.jpg")
        val profileUri = Uri.parse(getUserModel()!!.data.profile_image)
        if (profileUri != null) {
            round_border.setImageURI(profileUri)
        } else {
            round_border.setImageURI(imageUri)
        }
        txtDirectorAddress.setOnClickListener(View.OnClickListener {
            showPlacePicker()
            map.clear()
        })

        mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            map = googleMap
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            setCurrentLocation()
            map.setOnMapClickListener {
                map.clear()
                showPlacePicker()
            }
        }
        geoDataClient = Places.getGeoDataClient(this, null)
        txtDirectorName.setText(getUserModel()!!.data.name)
        txtDirectorEmail.setText(getUserModel()!!.data.email)
        txtDirectorNumber.setText(getUserModel()!!.data.phone_number)
        txtDirectorDesignation.setText(getUserModel()!!.data.designation)
    }

    private fun showPlacePicker() {
        val builder = PlacePicker.IntentBuilder()
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQ_CODE)
        } catch (e: Exception) {
            Log.e(TAG, e.stackTrace.toString())
        }
    }


    /***
     * Set Current Location
     */
    private fun setCurrentLocation() {
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this@DirectorProfileActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient()
                map.isMyLocationEnabled = false
            }
        } else {
            buildGoogleApiClient()
            map.isMyLocationEnabled = false
        }
    }

    /**
     * Set Up Google Api Client
     */
    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        mGoogleApiClient!!.connect()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQ_CODE && resultCode == Activity.RESULT_OK) {
            val place = PlacePicker.getPlace(this, data)
            txtDirectorAddress.setText(place.address)
            val toLatLng = place.latLng
            map.getUiSettings().setAllGesturesEnabled(false)
            map.getUiSettings().setScrollGesturesEnabled(false)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(toLatLng, 15f))
            getPhotoMetadata(place.id, toLatLng, place.address.toString())
            Log.d(TAG, "selected place " + place.name)
            getPhotoMetadata(place.id, toLatLng, place.address.toString())
        } else {
            //User first time back from display of list of marker view then display current location
            setCurrentLocation()
        }

    }

    /**
     * Get Adress From Geocoder
     * @param latitude
     * @param longitude
     * @return
     */
    private fun getAddress(latitude: Double?, longitude: Double?): String? {
        var street: String? = null
        val geocoder = Geocoder(this@DirectorProfileActivity, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude!!, longitude!!, 1)
            if (addresses != null) {
                val returnedAddress = addresses[0]
                val strReturnedAddress = StringBuilder()
                for (j in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(j)).append("")
                }
                street = strReturnedAddress.toString()
            }
        } catch (e: IOException) {
        }

        return street
    }

    override fun onConnected(bundle: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
        }
    }

    override fun onConnectionSuspended(i: Int) {

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }

        //Place current location marker
        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        txtDirectorAddress!!.text = getAddress(location.latitude, location.longitude)

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        mCurrLocationMarker = map.addMarker(markerOptions)

        //move map camera
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        map.animateCamera(CameraUpdateFactory.zoomTo(11f))


        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {

    }

    private fun getPhotoMetadata(placeId: String, toLatLng: LatLng, address: String) {

        val photoResponse = geoDataClient!!.getPlacePhotos(placeId)

        photoResponse.addOnCompleteListener { task ->
            currentPhotoIndex = 0
            photosDataList = ArrayList()
            val photos = task.result
            val photoMetadataBuffer = photos.photoMetadata

            Log.d(TAG, "number of photos " + photoMetadataBuffer.count)

            for (photoMetadata in photoMetadataBuffer) {
                photosDataList!!.add(photoMetadataBuffer.get(0).freeze())
            }
            photoMetadataBuffer.release()
            displayPhoto(toLatLng, address)
        }
    }


    private fun getPhoto(photoMetadata: PlacePhotoMetadata, toLatLng: LatLng, address: String) {
        val photoResponse = geoDataClient!!.getPhoto(photoMetadata)
        photoResponse.addOnCompleteListener { task ->
            val photo = task.result
            setGoogleMap(toLatLng, address, photo.bitmap)
            Log.d(TAG, "photo--->Bitmap " + photo.toString())
        }
    }


    private fun nextPhoto() {
        currentPhotoIndex++
//        displayPhoto()
    }

    private fun prevPhoto() {
        currentPhotoIndex--
        //  displayPhoto()
    }

    private fun displayPhoto(toLatLng: LatLng, address: String) {
        Log.d(TAG, "index " + currentPhotoIndex)
        Log.d(TAG, "size " + photosDataList!!.size)
        if (photosDataList!!.isEmpty() || currentPhotoIndex > photosDataList!!.size - 1) {
            //   imageView.setImageResource(R.drawable.black_marker);
            setGoogleMap(toLatLng, address, null);

            return
        }
        getPhoto(photosDataList!![currentPhotoIndex], toLatLng, address)
        //   setButtonVisibility();
    }


    private fun getMarkerBitmapFromView(bitmap: Bitmap?): Bitmap {
        val customMarkerView = (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.view_custom_marker, null)
        val markerImageView = customMarkerView.findViewById<View>(R.id.profile_image) as ImageView

        if (bitmap != null) {
            markerImageView.setImageBitmap(bitmap);
        } else {
            markerImageView.setImageResource(R.drawable.ic_app_icon);
        }
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.layout(0, 0, customMarkerView.measuredWidth, customMarkerView.measuredHeight)
        customMarkerView.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(customMarkerView.measuredWidth, customMarkerView.measuredHeight,
                Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable = customMarkerView.background
        drawable?.draw(canvas)
        customMarkerView.draw(canvas)
        return returnedBitmap
    }

    private fun setGoogleMap(toLatLng: LatLng, address: String, bitmap: Bitmap?) {
        map.addMarker(MarkerOptions()
                .position(toLatLng)
                .title(address)
                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(bitmap))))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        goActivity<DashBordActivityDIreStu>()
        finish()
    }
}

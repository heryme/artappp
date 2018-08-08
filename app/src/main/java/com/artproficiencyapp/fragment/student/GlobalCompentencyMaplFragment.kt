package com.artproficiencyapp.fragment.student


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.artproficiencyapp.R
import com.artproficiencyapp.fragment.commanfragment.BaseFragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_director.*
import android.support.v4.content.ContextCompat
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.widget.TextView
import com.artproficiencyapp.common.RegularBoldTextview
import com.artproficiencyapp.common.RegularTextview
import com.google.android.gms.maps.model.BitmapDescriptor
import java.security.AccessController


class GlobalCompentencyMaplFragment : BaseFragment(), OnMapReadyCallback {
    private var googleMap: GoogleMap? = null
    var Latitude = 0.00
    var Longitude = 0.00
    internal lateinit var mapFragment: SupportMapFragment
    val location = ArrayList<HashMap<String, String>>()
    var map: HashMap<String, String>? = null
    private var myMarker: Marker? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //setTitle("g")
        return inflater.inflate(R.layout.fragment_global_compentency_map, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpMap()
        map = HashMap()
        map!!["LocationID"] = "1"
        map!!["Latitude"] = "23.020763"
        map!!["Longitude"] = "72.570011"
        map!!["LocationName"] = "40%"
        location.add(map!!)

        // Location 2
        map = HashMap()
        map!!["LocationID"] = "2"
        map!!["Latitude"] = "23.215635"
        map!!["Longitude"] = "72.636941"
        map!!["LocationName"] = "50%"
        location.add(map!!)

        // Location 3
        map = HashMap()
        map!!["LocationID"] = "3"
        map!!["Latitude"] = " 21.170240"
        map!!["Longitude"] = "72.831062"
        map!!["LocationName"] = "100%"
        location.add(map!!)


        // Location 3
        map = HashMap()
        map!!["LocationID"] = "3"
        map!!["Latitude"] = " 23.5879607"
        map!!["Longitude"] = "72.3693252"
        map!!["LocationName"] = "100%"
        location.add(map!!)

        // *** Display Google Map




//        mapFragment.getMapAsync { Map ->
//            googleMap = Map
//            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
//            googleMap.setMapType(com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID)
//            Latitude = java.lang.Double.parseDouble(location[0]["Latitude"].toString())
//            Longitude = java.lang.Double.parseDouble(location[0]["Longitude"].toString())
//            val coordinate = LatLng(Latitude, Longitude)
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 17f))
//        }


        // *** Focus & Zoom


        // *** Marker (Loop)


    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0!!
        initGoogleMap()

    }

    private fun initGoogleMap() {
        if (ActivityCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        googleMap?.isMyLocationEnabled = true
        googleMap?.uiSettings?.isZoomControlsEnabled = false
        googleMap?.uiSettings?.isMyLocationButtonEnabled = true
        googleMap?.uiSettings?.isCompassEnabled = true
        googleMap?.uiSettings?.isRotateGesturesEnabled = true
        googleMap?.uiSettings?.isZoomGesturesEnabled = false
        googleMap?.uiSettings?.setAllGesturesEnabled(true)

        for (i in 0 until location.size) {
            Latitude = java.lang.Double.parseDouble(location[i]["Latitude"].toString())
            Longitude = java.lang.Double.parseDouble(location[i]["Longitude"].toString())
            val name = location[i]["LocationName"].toString()

            val marker = MarkerOptions().position(LatLng(Latitude, Longitude))
            marker.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(name)))
            googleMap!!.addMarker(marker)

        }

       // googleMap?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(Latitude, Longitude)))
        val coordinate = LatLng(Latitude, Longitude)
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 6f))
    }

    private fun setUpMap() {
        if (googleMap == null) {
            try {
                var mMapFragment = MapFragment.newInstance() as MapFragment
                fragmentManager.beginTransaction().replace(R.id.globalMap, mMapFragment).commit()
                mMapFragment.getMapAsync(this)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun getMarkerBitmapFromView(name: String): Bitmap {

        //HERE YOU CAN ADD YOUR CUSTOM VIEW

        val customMarkerView = (activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.global_map_icon, null)
        //IN THIS EXAMPLE WE ARE TAKING TEXTVIEW BUT YOU CAN ALSO TAKE ANY KIND OF VIEW LIKE IMAGEVIEW, BUTTON ETC.
        val textView = customMarkerView.findViewById(R.id.globalmapid) as RegularBoldTextview
        textView.text = name
        if (name=="100%")
        {
            textView.setTextSize(11F);
        }
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight())
        customMarkerView.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable = customMarkerView.getBackground()
        if (drawable != null)
            drawable.draw(canvas)
        customMarkerView.draw(canvas)
        return returnedBitmap
    }

}

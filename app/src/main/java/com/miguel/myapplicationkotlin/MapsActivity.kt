package com.miguel.myapplicationkotlin

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }


    private val bottomSheetView by lazy { findViewById<ConstraintLayout>(R.id.bottomSheet) }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    //########################################################################################

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView)
        setBottomSheetVisibility(false)
    }

    private fun createMarker() {

        val b = LatLng(-36.6368442, -71.9976243)
        map.addMarker(MarkerOptions().position(b).title("AULAS B").snippet("B1\nB2\nB3\nB4\nB5\nB6\nB7\nB8"))
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(b, 18f),
            4000,
            null
        )
        map.setOnMarkerClickListener { marker ->
            onMarkerClicked(marker)
            false
        }
        map.setOnMapClickListener { setBottomSheetVisibility(false) }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(b, 15F))


        //#############################################################################

        val d = LatLng(-36.6363575, -71.9968689)
        map.addMarker(MarkerOptions().position(d).title("AULAS D"))
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(d, 18f),
            4000,
            null
        )
        map.setOnMarkerClickListener { marker ->
            onMarkerClicked(marker)
            false
        }
        map.setOnMapClickListener { setBottomSheetVisibility(false) }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(d, 15F))

        //############################################################################
        val a = LatLng(-36.6368711, -71.9964213)
        map.addMarker(MarkerOptions().position(a).title("AULAS A"))
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(a, 18f),
            4000,
            null
        )
        map.setOnMarkerClickListener { marker ->
            onMarkerClicked(marker)
            false
        }
        map.setOnMapClickListener { setBottomSheetVisibility(false) }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(a, 15F))
        map.uiSettings.isZoomControlsEnabled = true

    }

    //########################################################################################

    override fun onMapReady(googleMap: GoogleMap) {

        map = googleMap
        createMarker()
        enableMyLocation()


    }

    //#############################################################################################
    private fun onMarkerClicked(marker: Marker) {

        bottomSheetView.findViewById<TextView>(R.id.city_name).text = marker.title
        bottomSheetView.findViewById<TextView>(R.id.longitude).text = marker.snippet

            marker.position.latitude.toString()

        setBottomSheetVisibility(true)
    }

    private fun setBottomSheetVisibility(isVisible: Boolean) {
        val updatedState =
            if (isVisible) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.state = updatedState
    }


    //######################################################

    private fun isPermissionsGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED


    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        if (isPermissionsGranted()) {
            map.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }
    }

    //#############################################3

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            Toast.makeText(this, "Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
    }

    //######################################################3333

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                map.isMyLocationEnabled = true
            } else {
                Toast.makeText(
                    this,
                    "Para activar la localización ve a ajustes y acepta los permisos",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {}
        }
    }

    //################################################
    override fun onResumeFragments() {
        super.onResumeFragments()
        if (!::map.isInitialized) return
        if (!isPermissionsGranted()) {
            map.isMyLocationEnabled = false
            Toast.makeText(
                this,
                "Para activar la localización ve a ajustes y acepta los permisos",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

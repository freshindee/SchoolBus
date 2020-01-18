package com.fitscorp.apps.indika.schoolbus.collection

import com.google.android.gms.maps.model.Marker
import java.util.*
import kotlin.collections.HashMap

object MarkerCollection {

    private val markers: HashMap<String, Marker> = HashMap()

    fun insertMarker(driverId: String, marker: Marker) {
        markers[driverId] = marker
    }

    fun getMarker(driverId: String): Marker? {
        return markers[driverId]
    }

    fun clearMarkers() {
        markers.clear()
    }

    fun removeMarker(driverId: String) {
         markers.remove(driverId)?.remove()
    }

    fun allMarkers() = markers
}

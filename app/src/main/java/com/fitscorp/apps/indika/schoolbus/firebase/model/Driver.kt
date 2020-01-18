package com.fitscorp.apps.indika.schoolbus.model

class Driver constructor() {

    var lat: Double = 0.0
    var lng: Double = 0.0
    var driverId: String = ""

    fun setLat(lat: Double) = apply { this.lat = lat }

    fun setLng(lng: Double) = apply { this.lng = lng }

    fun setDriverId(driverId: String) = apply { this.driverId = driverId }

    fun updateDriver(driverID:String, lat: Double, lng: Double) {
        this.driverId = driverID
        this.lat = lat
        this.lat = lng
    }
}
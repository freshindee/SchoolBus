package com.fitscorp.apps.indika.schoolbus.interfaces

import com.fitscorp.apps.indika.schoolbus.model.Driver


interface FirebaseDriverListener {

    fun onDriverAdded(driver: Driver)

    fun onDriverRemoved(driver: Driver)

    fun onDriverUpdated(driver: Driver)
}
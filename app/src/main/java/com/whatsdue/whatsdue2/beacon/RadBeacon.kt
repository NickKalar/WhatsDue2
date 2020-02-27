package com.whatsdue.whatsdue2.beacon

import android.bluetooth.le.ScanRecord
import android.os.ParcelUuid

data class RadBeacon(val record: ScanRecord, val address: String, val rssi: Int) {

        companion object {
            //        val BEACON_UUID = ParcelUuid.fromString("41a49f9ee59a49c4842bff100029c0be")
            val BEACON_UUID = ParcelUuid.fromString("A5CA95E6-0B7C-4FD5-88A7-8151BCF28271")
        }

        var data: ByteArray = record.bytes
        var name = record.deviceName

        override fun toString(): String {
            return address
        }

    }
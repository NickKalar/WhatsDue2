package com.whatsdue.whatsdue2.beacon

import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.Distance
import com.google.android.gms.nearby.messages.Message
import com.google.android.gms.nearby.messages.MessageListener
import com.whatsdue.whatsdue2.MainActivity

private const val TAG = "BLESCAN"

object BeaconScan {

    private lateinit var act: AppCompatActivity;
    private var found = false;

    fun start(activity: AppCompatActivity): Unit {
        act = activity;
        Nearby.getMessagesClient(act).subscribe(MessageListener)
//        stop()
//        (Handler()).postDelayed(Runnable{
//            stop();
//
//        }, 200)
//        return found;
    }
    private fun stop(): Boolean {
        Nearby.getMessagesClient(act).unsubscribe(MessageListener)
        return true
    }


    /*
    Message Listener uses P/S Model to subscribe to beacons repeatedly
     */
    private val MessageListener = object : MessageListener() {
        override fun onFound(m: Message?) {
            Toast.makeText(act, "Beacon(s) found. Classes Loaded", Toast.LENGTH_LONG).show()
            m?.let {
                Log.i(TAG, "Found BLE: ${m.namespace}")
                found = true
                // Activate Parser

            }

        }

        override fun onLost(p0: Message?) {
            Log.e(TAG, "Cannot find BLE / BLE Messages")
            Toast.makeText(act, "No Beacons Nearby, You Have No Courses As a Result", Toast.LENGTH_LONG).show()
        }

        override fun onDistanceChanged(m: Message?, p1: Distance?) {
            Toast.makeText(act, "Beacon Found, distance: ${p1!!.meters}", Toast.LENGTH_SHORT).show();
//            m?.let {
//
//            }
        }
    }
}
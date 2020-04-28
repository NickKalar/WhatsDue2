package com.whatsdue.whatsdue2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.Distance
import com.google.android.gms.nearby.messages.Message
import com.google.android.gms.nearby.messages.MessageListener

const val TAG = "BeaconTestActivity"

class BeaconTestActivity : AppCompatActivity() {

    private var beaconNameText = "Test Name"
    private var beaconDistanceText = 2.7
    private var beaconAccuracyText = 83.2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.beacon_info)

        val beaconName: TextView = findViewById(R.id.beaconName)
        val beaconDistance: TextView = findViewById(R.id.beaconDistance)
        val beaconAccuracy: TextView = findViewById(R.id.beaconAccuracy)

        beaconName.text = beaconNameText
        beaconDistance.text = beaconDistanceText.toString()
        beaconAccuracy.text = beaconAccuracyText.toString()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainmenu, menu)
        return true
    }

    // function to handle activity switching within the menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var intent = Intent(this, this::class.java)

        when (item.itemId) {
            R.id.classList ->
                intent = Intent(this, MainActivity::class.java)
            R.id.beaconTest -> {
                intent = Intent(this, BeaconTestActivity::class.java)
//                Nearby.getMessagesClient(this).unsubscribe(Listener)
            }
            R.id.courseView ->
                intent = Intent(this, CourseActivity::class.java)
        }

        intent.putExtra("login", "success")
        startActivity(intent)
        finish()
        return true
    }

    override fun onStart() {
        super.onStart()
        Nearby.getMessagesClient(this).subscribe(MessageListener)
        println("subscribed")



    }

    override fun onDestroy() {
        super.onDestroy()
        Nearby.getMessagesClient(this).unsubscribe(MessageListener)
    }

    private val MessageListener = object : MessageListener() {

        override fun onLost(p0: Message?) {
            super.onLost(p0)
            Log.e(TAG, "Cannot find BLE / BLE Messages")
            Toast.makeText(
                this@BeaconTestActivity.applicationContext,
                "No Beacons Nearby, You Have No Courses As a Result",
                Toast.LENGTH_LONG
            ).show()
        }

        override fun onDistanceChanged(m: Message?, p1: Distance?) {
            Toast.makeText(this@BeaconTestActivity.applicationContext, "Distance Changed", Toast.LENGTH_SHORT).show()
            super.onDistanceChanged(m, p1)

            val beaconName: TextView = findViewById(R.id.beaconName)
            val beaconDistance: TextView = findViewById(R.id.beaconDistance)
            val beaconAccuracy: TextView = findViewById(R.id.beaconAccuracy)

            beaconName.text = String(m!!.content)
            beaconDistance.text = (p1!!.meters * 52).toString()
            beaconAccuracy.text = (p1.accuracy).toString()
            this@BeaconTestActivity.onStart()


        }


    }
}
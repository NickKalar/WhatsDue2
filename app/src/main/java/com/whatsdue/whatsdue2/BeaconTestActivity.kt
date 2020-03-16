package com.whatsdue.whatsdue2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BeaconTestActivity : AppCompatActivity() {
    var beaconNameText = "Test Name"
    var beaconDistanceText = 2.7
    var beaconAccuracyText = 83.2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.beacon_info)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainmenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var intent = Intent(this, this::class.java)

        when (item.itemId) {
            R.id.classList ->
                intent = Intent(this, MainActivity::class.java)
            R.id.beaconTest ->
                intent = Intent(this, BeaconTestActivity::class.java)
        }

        startActivity(intent)
        finish()
        return true
    }

    override fun onStart() {
        super.onStart()
        val beaconName: TextView = findViewById(R.id.beaconName)
        val beaconDistance: TextView = findViewById(R.id.beaconDistance)
        val beaconAccuracy: TextView = findViewById(R.id.beaconAccuracy)

        beaconName.text = beaconNameText
        beaconDistance.text = beaconDistanceText.toString()
        beaconAccuracy.text = beaconAccuracyText.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
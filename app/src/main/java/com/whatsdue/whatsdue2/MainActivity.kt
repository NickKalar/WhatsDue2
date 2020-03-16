package com.whatsdue.whatsdue2

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.Distance
import com.google.android.gms.nearby.messages.Message
import com.google.android.gms.nearby.messages.MessageListener
import com.whatsdue.whatsdue2.beacon.BeaconScan
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import kotlin.properties.Delegates

class FeedEntry {
    var title: String = ""
    var courseNumber: String = ""
    var professor: String = ""
    var time = arrayListOf<String>()
    var officeHours = arrayListOf<String>()
    var assignments = arrayListOf<String>()
    var test = arrayListOf<String>()
    var misc = arrayListOf<String>()

    override fun toString(): String {
        return """
            name = $title
            professor = $professor
            time = $time
            office hours = $officeHours
        """.trimIndent()
    }
}

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val downloadData by lazy {DownloadData(this, courseListView)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate Called")

        val bundle = intent.getStringExtra("login")
        Log.d(TAG, "bundle" + bundle)

        if (savedInstanceState == null && bundle != "success"){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Nearby.getMessagesClient(this).subscribe(Listener)
            downloadData.execute("http://www.whatsdue.net/tes.xml")
//            Toast.makeText(this.applicationContext, "Beacon(s) being searched for", Toast.LENGTH_SHORT).show()
//            if (BeaconScan.start(this)) {
//                Log.d(TAG, "BeaconScan Success")
//                val t = Toast.makeText(this.applicationContext, "Beacon(s) found. Classes Loaded", Toast.LENGTH_LONG)
//                t.show()
//                downloadData.execute("http://www.whatsdue.net/tes.xml")
//            } else {
//                Log.d(TAG, "BeaconScan failed")
//                val t = Toast.makeText(this.applicationContext, "Beacon(s) NOT found. Classes NOT Loaded", Toast.LENGTH_LONG)
//                t.show()
//            }
        }
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
        Log.d(TAG, "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop called")

    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
        downloadData.cancel(true)
    }

    companion object {
        private class DownloadData(context: Context, listView: ListView): AsyncTask<String, Void, String>() {
            private val TAG = "DownloadData"

            var propContext : Context by Delegates.notNull()
            var propListView : ListView by Delegates.notNull()

            init {
                propContext = context
                propListView = listView
            }

            override fun onPostExecute(result: String) {

                super.onPostExecute(result)
//                Log.d(TAG, "onPostExecute: parameter is $result")
                val parseApplications = ParseApplications()
                parseApplications.parse(result)

//                val arrayAdaptor = ArrayAdapter<FeedEntry>(propContext, R.layout.list_item, parseApplications.applications)
//                propListView.adapter = arrayAdaptor

                val feedAdaptor = FeedAdaptor(propContext, R.layout.course_record, parseApplications.applications)
                propListView.adapter = feedAdaptor
            }

            override fun doInBackground(vararg url: String?): String {
                Log.d(TAG, "doInBackground: starts with ${url[0]}")
                val rssFeed = downloadXML(url[0])
                if (rssFeed.isEmpty()) {
                    Log.e(TAG, "doInBackground: Error downloading")
                }
                return rssFeed
            }

            private fun downloadXML(urlPath: String?): String {
                return URL(urlPath).readText()
            }
        }
    }

    private val Listener = object : MessageListener() {
        override fun onFound(m: Message?) {
            super.onFound(m)
            m?.let {
                Log.i(TAG, "Found BLE: ${m.namespace}")
                Toast.makeText(this@MainActivity.applicationContext, "Beacon(s) found. Classes Loaded", Toast.LENGTH_LONG).show()
                println("Message found")
                // Activate Parser

            }

        }

        override fun onLost(p0: Message?) {
            super.onLost(p0)
            Log.e(TAG, "Cannot find BLE / BLE Messages")
            Toast.makeText(this@MainActivity.applicationContext, "No Beacons Nearby, You Have No Courses As a Result", Toast.LENGTH_LONG).show()
        }

        override fun onDistanceChanged(m: Message?, p1: Distance?) {
            super.onDistanceChanged(m, p1)
            Toast.makeText(this@MainActivity.applicationContext, "Beacon Found, distance: ${p1!!.meters}", Toast.LENGTH_SHORT).show();
//            m?.let {
//
//            }
        }
    }


}

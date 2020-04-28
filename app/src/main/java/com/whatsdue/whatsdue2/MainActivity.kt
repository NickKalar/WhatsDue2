package com.whatsdue.whatsdue2

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.Distance
import com.google.android.gms.nearby.messages.Message
import com.google.android.gms.nearby.messages.MessageListener
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import kotlin.properties.Delegates

//class to handle the individual courses
class FeedEntry() : Parcelable {
    var title: String = ""
    var courseNumber: String = ""
    var professor: String = ""
    var time: String = ""
    var officeHours: String = ""
    var assignments: String = ""
    var test: String = ""
    var misc: String = ""

    constructor(parcel: Parcel) : this() {
        title = parcel.readString().toString()
        courseNumber = parcel.readString().toString()
        professor = parcel.readString().toString()
    }

    // toString function to display the information in this class
    override fun toString(): String {
        return """
            name = $title
            courseNumber = $courseNumber
            professor = $professor
            time = $time
            office hours = $officeHours
            assignments = $assignments
            tests = $test
            misc = $misc
        """.trimIndent()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(courseNumber)
        parcel.writeString(professor)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FeedEntry> {
        override fun createFromParcel(parcel: Parcel): FeedEntry {
            return FeedEntry(parcel)
        }

        override fun newArray(size: Int): Array<FeedEntry?> {
            return arrayOfNulls(size)
        }
    }
}

object Values {
    var beaconNameText = "Test Name"
    var beaconDistanceText = 2.7
    var beaconAccuracyText = 83.2
}


class MainActivity : AppCompatActivity(), FeedAdaptor.OnCourseListener{

    private val TAG = "MainActivity"
    private val downloadData by lazy { DownloadData(this, courseRecycView) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate Called")
        val bundle = intent.getStringExtra("login")
        Log.d(TAG, "bundle" + bundle)

        // checks to see if the bundle has a success from login. if not, it switches to the login activity
        if (savedInstanceState == null && bundle != "success") {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Nearby.getMessagesClient(this).subscribe(listener)
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

    // function to create the menu at the top of the application
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

    // companion object to
    companion object {
        private class DownloadData(context: Context, recyclerView: RecyclerView) :
            AsyncTask<String, Void, String>()  {
            private val TAG = "DownloadData"

            var propContext: Context by Delegates.notNull()
            var propListView: RecyclerView by Delegates.notNull()

            init {
                propContext = context
                propListView = recyclerView
                propListView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL ,false)
            }

            override fun onPostExecute(result: String) {

                super.onPostExecute(result)
//                Log.d(TAG, "onPostExecute: parameter is $result")
                val parseApplications = ParseApplications()
                parseApplications.parse(result)

//                val arrayAdaptor = ArrayAdapter<FeedEntry>(propContext, R.layout.list_item, parseApplications.applications)
//                propListView.adapter = arrayAdaptor

                val feedAdaptor =
                    FeedAdaptor(propContext, R.layout.course_record, parseApplications.applications)
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

    // This listener works in the background to search for messages from BLE beacons
    private val listener = object : MessageListener() {
        override fun onFound(m: Message?) {
            super.onFound(m)
            m?.let {
                Log.i(TAG, "Found BLE: ${String(m.content)}")
                Toast.makeText(
                    this@MainActivity.applicationContext,
                    "Beacon(s) found. Classes Loaded",
                    Toast.LENGTH_LONG
                ).show()
                println("Message found")
                // Activate Parser

            }

        }

        // handles losing a signal from a BLE beacon
        override fun onLost(p0: Message?) {
            super.onLost(p0)
            Log.e(TAG, "Cannot find BLE / BLE Messages")
            Toast.makeText(
                this@MainActivity.applicationContext,
                "No Beacons Nearby, You Have No Courses As a Result",
                Toast.LENGTH_LONG
            ).show()
        }

        // This function executes code when the distance from a BLE beacon is detected.
        override fun onDistanceChanged(m: Message?, p1: Distance?) {
            super.onDistanceChanged(m, p1)
//            Toast.makeText(this@MainActivity.applicationContext, "Beacon Found, distance: ${p1!!.meters}", Toast.LENGTH_SHORT).show();
//            println("Beacon Found, distance: ${p1!!.meters * 52.63} meters with an accuracy of ${p1.accuracy}")
//            Values.beaconNameText = m!!.namespace.toString()
//            Values.beaconDistanceText = p1!!.meters * 52.63
//            Values.beaconAccuracyText = p1.accuracy.toDouble()

//            m?.let {
//
//            }
        }
    }

    override fun onCourseClicked(position: Int) {
    }


}

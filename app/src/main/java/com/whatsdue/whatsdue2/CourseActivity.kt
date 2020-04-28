package com.whatsdue.whatsdue2

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CourseActivity: AppCompatActivity(){

    private val TAG = "CourseActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate Called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)

        val courseName: TextView = findViewById(R.id.courseName)
        val courseProf: TextView = findViewById(R.id.courseProfessor)
        val courseAssign: TextView = findViewById(R.id.courseAssign)
        val courseTest: TextView = findViewById(R.id.courseTest)

        val bundle = intent.getStringExtra("courseTitle")

        if(bundle != null){
            courseName.text = bundle
            courseProf.text = intent.getStringExtra("courseProfessor")
            courseAssign.text = intent.getStringExtra("courseAssignments")
            courseTest.text = intent.getStringExtra("courseTest")
        } else{
            courseName.text = getString(R.string.errorString)
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

        intent.putExtra("login", "success")
        startActivity(intent)
        finish()
        return true
    }
}
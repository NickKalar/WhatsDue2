package com.whatsdue.whatsdue2

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class FeedAdaptor(context: Context, private val resource: Int, private val applications: List<FeedEntry>)
    : ArrayAdapter<FeedEntry>(context, resource) {

    private val TAG = "FeedAdapter"
    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        Log.d(TAG, "getCount called")
        return applications.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        Log.d(TAG, "getView called")
        val view: View
        if (convertView == null){
            Log.d(TAG, "getView called with null convertView")
            view = inflater.inflate(resource, parent, false)
        } else {
            Log.d(TAG, "getView provided a convertView")
            view = convertView
        }

        val courseTitle: TextView = view.findViewById(R.id.courseTitle)
        val courseProfessor: TextView = view.findViewById(R.id.courseProfessor)
        val courseTime: TextView = view.findViewById(R.id.courseTime)

        val currentApp = applications[position]

        courseTitle.text = currentApp.title
        courseProfessor.text = currentApp.professor
        courseTime.text = currentApp.time.toString()

        return view
    }
}
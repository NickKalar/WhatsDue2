package com.whatsdue.whatsdue2

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class FeedAdaptor(context: Context, private val resource: Int, private val courses: List<FeedEntry>) :
    RecyclerView.Adapter<FeedAdaptor.FeedEntryHolder>() {
    class FeedEntryHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener, OnCourseListener {
        private var view: View = v;
        private var feed: FeedEntry? = null;
        private val TAG = "RecyclerView"
        private val faContext = null

        val courseTitle: TextView?
        val courseProfessor: TextView?
        val courseTime: TextView?
        var courseOfficeHours = ""
        var courseAssignments = ""
        var courseTest = ""
        var courseMisc = ""

        init {
            view.setOnClickListener(this)
            courseTitle = view.findViewById(R.id.courseTitle) as? TextView
            courseProfessor = view.findViewById(R.id.courseProfessor) as? TextView
            courseTime = view.findViewById(R.id.courseTime) as? TextView
        }

        override fun onClick(v: View?) {
            Log.d(TAG, "Click Motherfuckers ")
            val intent = Intent(view.context, CourseActivity::class.java)
            onCourseClicked(adapterPosition)
            intent.putExtra("courseTitle", courseTitle?.text.toString())
            intent.putExtra("courseProfessor", courseProfessor?.text.toString())
            intent.putExtra("courseTime", courseTime?.text.toString())
            intent.putExtra("courseOfficeHours", courseOfficeHours)
            intent.putExtra("courseAssignments", courseAssignments)
            intent.putExtra("courseTest", courseTest)
            intent.putExtra("courseMisc", courseMisc)

            view.context.startActivity(intent)
        }

        override fun onCourseClicked(position: Int) {
            Log.d(TAG, "onCourseClicked")
            val context = view.context
            val intent = Intent(context, CourseActivity::class.java)
            intent.putExtra("course", "Here is a course name")
            context.startActivity(intent)
        }

        override fun toString(): String {
            return """
            name = $feed.title
            professor = $feed.professor
            time = $.feedtime
            office hours = $feed.officeHours
        """.trimIndent()
        }

        companion object {
            private val COURSE_KEY = "COURSE"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedEntryHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.course_record, parent, false)
        val feedEntryHolder: FeedEntryHolder = FeedEntryHolder(view);
        return feedEntryHolder;
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    override fun onBindViewHolder(holder: FeedEntryHolder, position: Int) {
        val item: FeedEntry = courses[position]
        println(item)
        holder.courseTitle?.text = item.title
        holder.courseProfessor?.text = item.professor
        holder.courseTime?.text = item.time
        holder.courseOfficeHours = item.officeHours
        holder.courseAssignments = item.assignments
        holder.courseTest = item.test
        holder.courseMisc = item.misc
    }

    interface OnCourseListener{
        fun onCourseClicked(position: Int){
            Log.d(TAG, "original onCourseClicked")
        }
    }
}
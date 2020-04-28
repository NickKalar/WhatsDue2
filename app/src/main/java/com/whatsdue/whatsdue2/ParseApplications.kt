package com.whatsdue.whatsdue2

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ParseApplications {

    private val TAG = "ParseApplications"
    val applications = ArrayList<FeedEntry>()

    fun parse(xmlData: String): Boolean {
        Log.d(TAG, "parse called with $xmlData")
        var status = true
        var inEntry = false
        var textValue = ""

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType = xpp.eventType
            var currentRecord = FeedEntry()
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = xpp.name?.toLowerCase(Locale.ROOT)
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        Log.d(TAG, "parse: Starting tag for $tagName")
                        if (tagName == "course") {
                            inEntry = true
                        }
                    }
                    XmlPullParser.TEXT -> textValue = xpp.text

                    XmlPullParser.END_TAG -> {
                        Log.d(TAG, "parse: Ending tag for $tagName")
                        if (inEntry) {
                            when (tagName) {
                                "course" -> {
                                    applications.add(currentRecord)
                                    inEntry = false
                                    currentRecord = FeedEntry()
                                }
                                "title" -> currentRecord.title = textValue
                                "professor" -> currentRecord.professor = textValue
                                "time" -> currentRecord.time += textValue + " "
                                "officeHours" -> currentRecord.officeHours += textValue + "\n"
                                "assignments" -> currentRecord.assignments += textValue + "\n"
                                "test" -> currentRecord.test += textValue + "\n"
                                "misc" -> currentRecord.misc += textValue + "\n"

                            }
                        }
                    }
                }

                eventType = xpp.next()
            }

            for (app in applications) {
                Log.d(TAG, "********************")
                Log.d(TAG, app.toString())
            }

        } catch (e: Exception){
            e.printStackTrace()
            status = false
        }

        return status
    }
}
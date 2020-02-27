package com.whatsdue.whatsdue2

class CourseInfo (val courseID: String, val title: String)

class SyllabusInfo (val course: CourseInfo, var textbook: String, var officeHoursArray: String, var assignmentsArray: String) {
    fun getCourseID() : String {
        return course.courseID
    }
}
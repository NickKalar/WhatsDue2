package com.whatsdue.whatsdue2.restmodels

import java.io.Serializable

data class Course( val id: Long = 0, var name: String = "",
                   var updated: Boolean = false, var section: String = "", var syllabus: Syllabus?) :
    Serializable {
        //var instructor = Admin(firstName = "", lastName = "", username = "", schoolID = 0)
        // var roster: MutableSet<Student> = mutableSetOf()
}
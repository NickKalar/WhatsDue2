package com.whatsdue.whatsdue2.restmodels

import java.time.LocalDate

data class Syllabus constructor(var current: LocalDate? = LocalDate.now(),
                                var last: LocalDate? = LocalDate.now(),
                                var instructorInfo: String = "",
                                var meetings: String = "",
                                var meetDates: String = "") {
}
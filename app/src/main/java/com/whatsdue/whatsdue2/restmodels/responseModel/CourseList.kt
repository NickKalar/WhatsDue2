package com.whatsdue.whatsdue2.restmodels.responseModel

import com.google.gson.annotations.SerializedName
import com.whatsdue.whatsdue2.restmodels.Course

data class CourseList (@SerializedName("courses") var courses: List<Course>)
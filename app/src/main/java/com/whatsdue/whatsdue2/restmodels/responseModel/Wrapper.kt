package com.whatsdue.whatsdue2.restmodels.responseModel

import com.google.gson.annotations.SerializedName

data class Wrapper(@SerializedName("_embedded") var embed: CourseList)
package com.whatsdue.whatsdue2.restmodels.responseModel

import com.google.gson.annotations.SerializedName

data class GenericWrapper<T>(@SerializedName("_embedded")var obj: T)
package com.future.json.filter.model

import com.google.gson.annotations.SerializedName

data class Sheet1(
    @SerializedName("Sheet1")
    var testList: ArrayList<TestModel> = ArrayList()
)
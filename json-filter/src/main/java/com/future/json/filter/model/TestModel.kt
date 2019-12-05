package com.future.json.filter.model

import com.google.gson.annotations.SerializedName

data class TestModel(
    @SerializedName("一级分类")
    var oneTab: String = "",
    @SerializedName("二级分类")
    var twoTab: String = "",
    @SerializedName("标准问")
    var questionContent: String = ""
)
package com.android.secondhand

import java.io.Serializable

data class Item (
    val name: String,
    val price: Int,
    val category: String,
    val image_paths: List<String>
): Serializable
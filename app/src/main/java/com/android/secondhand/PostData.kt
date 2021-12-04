package com.android.secondhand

import java.io.Serializable

data class PostData (
    val name: String,
    val price: Int,
    val category: String,
    val image_paths: List<String>
): Serializable
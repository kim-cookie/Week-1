package com.example.myapplication.util

import android.content.Context

fun convertResIdToUri(context: Context, resId: Int): String {
    return "android.resource://${context.packageName}/drawable/" +
            context.resources.getResourceEntryName(resId)
}

package com.source.kotfirebase.abs.analytics

import android.content.Context
import android.os.Bundle

interface AnalyticsServices {
    fun logBundledEvent(context: Context, string: String, bundle: Bundle)
    fun logEvent(context: Context, string: String)
}
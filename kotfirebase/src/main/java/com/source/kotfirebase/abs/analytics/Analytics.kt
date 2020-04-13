package com.source.kotfirebase.abs.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

internal object Analytics: AnalyticsServices {

    override fun logBundledEvent(context: Context, string: String, bundle: Bundle) {
        FirebaseAnalytics.getInstance(context).logEvent(string, bundle)
    }

    override fun logEvent(context: Context, string: String) {
        FirebaseAnalytics.getInstance(context).logEvent(string, null)
    }
}
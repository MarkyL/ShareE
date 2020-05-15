package com.mark.sharee.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import timber.log.Timber

class NetworkUtils {

    companion object {
        fun openUrl(context: Context, url: String) {
            var linkToOpen = url
            Timber.i("openUrl - $url")
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                linkToOpen = "http://$url"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkToOpen))
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }

    }
}
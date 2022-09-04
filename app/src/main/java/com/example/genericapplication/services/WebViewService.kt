package com.example.genericapplication.services

import com.example.genericapplication.factories.DotenvFactory

class WebViewService {
    companion object {
        fun read(key: String?): String {
            return UrlReader.perform(DotenvFactory.getInstance()["WEBVIEW_HOST"] + "/" + key)
        }

        fun isAppUrl(url: String): Boolean {
            return url.startsWith("app:")
        }

        fun catchWebViewIdentifier(url: String): String? {
            val values = sanitizeUrl(url).split("?")
            if(!values.isEmpty())
                return values[0]
            return null
        }

        fun catchData(url: String): HashMap<String, String> {
            val map = hashMapOf<String, String>()
            val values = sanitizeUrl(url).split("?")
            if(values.size <= 1)
                return map
            val data = values[1].split("&")
            var keyValue: List<String>? = null
            data.forEach {
                try {
                    keyValue = it.split("=")
                    map.put(keyValue!![0], keyValue!![1])
                } catch(e: Exception) {}
            }
            return map
        }

        private fun sanitizeUrl(url: String): String {
            return url.replace("app:", "")
        }
    }
}
package com.example.genericapplication.services

import java.net.HttpURLConnection
import java.net.URL

class UrlReader {
    companion object {
        fun perform(url: String?): String {
            var text: String
            val urlConnection = URL(url).openConnection() as HttpURLConnection
            try {
                text = urlConnection.inputStream.bufferedReader().readText()
            } finally {
                urlConnection.disconnect()
            }
            return text
        }
    }
}
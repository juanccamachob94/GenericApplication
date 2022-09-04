package com.example.genericapplication.services

import com.example.genericapplication.factories.DotenvFactory

class VideoProviderService {
    companion object {
        fun buildUrl(identifier: String?): String {
            return DotenvFactory.getInstance()["VIDEO_PROVIDER_HOST"] + "/" + identifier + ".m3u8"
        }
    }
}
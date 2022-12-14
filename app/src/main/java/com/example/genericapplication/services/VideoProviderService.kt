package com.example.genericapplication.services

import com.example.genericapplication.factories.DotenvFactory

class VideoProviderService {
    companion object {
        fun buildUrl(identifier: String?): String? {
            if(identifier == null)
                return null
            return DotenvFactory.getInstance()["VIDEO_PROVIDER_HOST"] + "/" + identifier + ".m3u8"
        }
    }
}
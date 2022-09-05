package com.example.genericapplication.services

import com.example.genericapplication.factories.DotenvFactory

class AudioProviderService {
    companion object {
        fun buildUrl(identifier: String?): String? {
            if(identifier == null)
                return null
            return DotenvFactory.getInstance()["AUDIO_PROVIDER_HOST"] + "/DASH" + identifier
        }
    }
}
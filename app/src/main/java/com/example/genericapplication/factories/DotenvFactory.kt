package com.example.genericapplication.factories

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv

class DotenvFactory {
    companion object {
        var dotenvInstance: Dotenv? = null

        fun getInstance(): Dotenv {
            if(dotenvInstance == null)
                dotenvInstance = dotenv {
                    directory = "/assets"
                    filename = "env"
                }
            return dotenvInstance!!
        }
    }
}
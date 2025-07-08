package com.example.wordle

import android.content.Context

object ContextProvider {
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun getContext(): Context {
        return appContext ?: throw IllegalStateException("ContextProvider not initialized")
    }
}
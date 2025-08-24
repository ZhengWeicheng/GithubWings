package com.wings.githubwings.framework.properties


object SPManager {
    lateinit var spImpl: ISPPropertyInterface
    fun init(spImpl: ISPPropertyInterface) {
        SPManager.spImpl = spImpl
    }

    fun getSPImpl(): ISPPropertyInterface {
        return spImpl
    }

}
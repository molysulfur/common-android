package com.awonar.android.shared.utils

import android.content.Context
import com.orhanobut.hawk.DefaultHawkFacade
import com.orhanobut.hawk.HawkBuilder
import com.orhanobut.hawk.HawkFacade
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HawkUtil @Inject constructor(private val context: Context) {

    var hawkFacade: HawkFacade = DefaultHawkFacade(HawkBuilder(context))

    fun <T> put(key: String, value: T): Boolean = hawkFacade.put(key, value)

    fun <T> get(key: String): T? = hawkFacade.get(key)

    fun <T> get(key: String, defaultValue: T): T =
        hawkFacade.get(key, defaultValue) ?: defaultValue

    fun count(): Long = hawkFacade.count()

    fun deleteAll(): Boolean = hawkFacade.deleteAll()

    fun delete(key: String): Boolean = hawkFacade.delete(key)

    fun contains(key: String): Boolean = hawkFacade.contains(key)

    fun destroy() {
        hawkFacade.destroy()
    }

}
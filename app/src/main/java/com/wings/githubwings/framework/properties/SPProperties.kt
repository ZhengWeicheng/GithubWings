package com.wings.githubwings.framework.properties

class SPProperty<T>(
    key: String,
    default: T,
    update: Boolean = false,
    preFixUserId: Boolean = true
) {
    var mValue: T = default
    lateinit var mSpImpl: ISPPropertyInterface
    val mKey: String = key
    val mDefault: T = default
    var mUpdate: Boolean = update
    var mPreFixUserId: Boolean = preFixUserId

    init {
        mSpImpl = SPManager.getSPImpl()
    }

    fun setValue(value: T) {
        if (value == null) {
            return
        }
        if (mUpdate && mValue?.equals(value) == true) {
            return
        }
        mValue = value
        mUpdate = true
        when (value) {
            is String -> mSpImpl.putString(mKey, value)
            is Int -> mSpImpl.putInt(mKey, value)
            is Long -> mSpImpl.putLong(mKey, value)
            is Float -> mSpImpl.putFloat(mKey, value)
            is Boolean -> mSpImpl.putBoolean(mKey, value)
        }
    }

    fun getValue(): T {
        if (mUpdate) {
            return mValue
        }
        when (mValue) {
            is String -> mValue = mSpImpl.getString(mKey, mDefault as String) as T
            is Int -> mValue = mSpImpl.getInt(mKey, mDefault as Int) as T
            is Long -> mValue = mSpImpl.getLong(mKey, mDefault as Long) as T
            is Float -> mValue = mSpImpl.getFloat(mKey, mDefault as Float) as T
        }
        return mValue
    }
}
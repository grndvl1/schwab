package com.matthewscorp.schwab.interview.data

import java.lang.Exception

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

class Resource<T> private constructor(val status: Status, val data: T?, val throwable: Throwable?) {

    val message = throwable?.message

    companion object {

        @JvmStatic
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        @JvmStatic
        @JvmOverloads
        fun <T> error(msg: String, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, Exception(msg))
        }

        @JvmStatic
        @JvmOverloads
        fun <T> error(t: Throwable, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, t)
        }

        @JvmStatic
        @JvmOverloads
        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }

    override fun toString(): String {
        return "Resource(status=$status, data=$data, message=$message)"
    }

}
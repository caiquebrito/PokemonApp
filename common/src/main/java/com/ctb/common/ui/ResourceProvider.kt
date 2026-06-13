package com.ctb.common.ui

import androidx.annotation.StringRes

interface ResourceProvider {
    fun getString(
        @StringRes resId: Int,
        vararg params: String = emptyArray(),
    ): String

    fun getColor(colorId: Int): Int
}

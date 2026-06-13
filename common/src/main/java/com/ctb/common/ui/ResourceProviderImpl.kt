package com.ctb.common.ui

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

class ResourceProviderImpl(
    private val context: Context,
) : ResourceProvider {
    override fun getString(
        @StringRes resId: Int,
        vararg params: String,
    ) = context.resources.getString(resId, *params)

    override fun getColor(
        @ColorRes colorId: Int,
    ) = ContextCompat.getColor(context, colorId)
}

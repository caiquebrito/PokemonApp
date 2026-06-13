package com.ctb.common

import com.ctb.common.ui.ResourceProvider
import com.ctb.common.ui.ResourceProviderImpl
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object CommonModule {
    fun injectCommon() {
        loadKoinModules(loadCommon)
    }

    internal val loadCommon by lazy {
        listOf(commonKoinModule)
    }

    private val commonKoinModule =
        module {
            factory<ResourceProvider> {
                ResourceProviderImpl(context = get())
            }
        }
}

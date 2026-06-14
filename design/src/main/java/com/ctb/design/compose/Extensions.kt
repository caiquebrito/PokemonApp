package com.ctb.design.compose

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import com.ctb.design.compose.theme.PokemonTheme

fun ComposeView.setComposableContent(content: @Composable () -> Unit) {
    this.apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            PokemonTheme {
                content()
            }
        }
    }
}

val Int.toDp get() = (this / Resources.getSystem().displayMetrics.density).toInt().dp

package com.ctb.design.compose.component

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

private const val ANIMATION_TWEEN_DURATION: Int = 300

@Composable
fun QuickStartNavHost(
    navController: NavHostController,
    startDestination: Any,
    builder: NavGraphBuilder.() -> Unit,
) {
    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideInHorizontally(
                animationSpec = tween(ANIMATION_TWEEN_DURATION),
                initialOffsetX = { fullWidth -> fullWidth },
            )
        },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
        popEnterTransition = {
            slideInHorizontally(
                animationSpec = tween(ANIMATION_TWEEN_DURATION),
                initialOffsetX = { fullWidth -> -fullWidth },
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                animationSpec = tween(ANIMATION_TWEEN_DURATION),
                targetOffsetX = { fullWidth -> fullWidth },
            )
        },
        builder = builder,
    )
}

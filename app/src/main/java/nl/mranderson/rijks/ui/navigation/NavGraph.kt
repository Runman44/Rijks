package nl.mranderson.rijks.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import nl.mranderson.rijks.ui.detail.DetailRoute
import nl.mranderson.rijks.ui.image.ImageRoute
import nl.mranderson.rijks.ui.list.ListRoute

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.List.route
    ) {
        composable(route = Screens.List.route) {
            ListRoute(
                onArtClicked = { id ->
                    navController.navigate(Screens.Detail.route.plus("/$id"))
                }
            )
        }

        composable(route = "${Screens.Detail.route}/{${Screens.Detail.argArtId}}") { backStackEntry ->
            backStackEntry.arguments?.getString(Screens.Detail.argArtId)?.let {
                DetailRoute(
                    onBackClicked = {
                        navController.popBackStack()
                    },
                    onImageClicked = { url ->
                        navController.navigate(Screens.Image.route.plus("/$url"))
                    }
                )
            }
        }

        composable(route = "${Screens.Image.route}/{${Screens.Image.argArtImageUrl}}") { backStackEntry ->
            backStackEntry.arguments?.getString(Screens.Image.argArtImageUrl)?.let {
                ImageRoute(
                    imageUrl = it,
                    onBackClicked = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
package com.example.galleryapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.galleryapp.ml.ImageClassifier
import com.example.galleryapp.presentation.ui.FolderPhotosScreen
import com.example.galleryapp.presentation.ui.FoldersScreen
import com.example.galleryapp.presentation.ui.MLFolderPhotosScreen
import com.example.galleryapp.presentation.ui.MLFoldersScreen
import com.example.galleryapp.presentation.ui.PhotosScreenWrapper
import com.example.galleryapp.presentation.viewmodel.GalleryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val viewModel: GalleryViewModel = hiltViewModel()
    val photos by viewModel.photos.collectAsState(initial = emptyList())

    val classifier = remember { ImageClassifier(context) }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Photos.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Photos.route) {
                PhotosScreenWrapper()
            }
            composable(Screen.Folders.route) {
                FoldersScreen(onFolderClick = { folder ->
                    navController.navigate(
                        Screen.FolderPhotos.createRoute(folder.id, folder.name)
                    )
                })
            }
            composable(
                route = Screen.FolderPhotos.routePattern,
                arguments = listOf(
                    navArgument("bucketId") { type = NavType.StringType },
                    navArgument("folderName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val bucketId = backStackEntry.arguments?.getString("bucketId") ?: ""
                val folderName = backStackEntry.arguments?.getString("folderName") ?: "Folder"
                FolderPhotosScreen(
                    bucketId = bucketId,
                    folderName = folderName,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.ML.route) {
                MLFoldersScreen(
                    onFolderClick = { category ->
                        navController.navigate(Screen.MLFolderPhotos.createRoute(category))
                    }
                )
            }
            composable(
                route = Screen.MLFolderPhotos.routePattern,
                arguments = listOf(
                    navArgument("folderName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val folderName = backStackEntry.arguments?.getString("folderName") ?: ""
                MLFolderPhotosScreen(
                    onPhotoClick = { uri ->
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val screens = listOf(Screen.Photos, Screen.Folders, Screen.ML)
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(tonalElevation = 8.dp) {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = screen.label) },
                label = { androidx.compose.material3.Text(screen.label) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

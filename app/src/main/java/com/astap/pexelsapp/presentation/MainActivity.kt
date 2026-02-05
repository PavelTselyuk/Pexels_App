package com.astap.pexelsapp.presentation

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.astap.pexelsapp.domain.PhotosRepository
import com.astap.pexelsapp.presentation.screens.detail.DetailsScreen
import com.astap.pexelsapp.presentation.screens.detail.DetailsViewModel
import com.astap.pexelsapp.presentation.ui.theme.PexelsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repository: PhotosRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {

                false
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    1.0f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    1.0f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd { screen.remove() }

                zoomY.start()
                zoomX.start()

            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {


            PexelsAppTheme {
//                FavouritesScreen(
//                    onHomePageClick = {  },
//                    onPhotoClick = {  },
//                    onExploreClick = {  }
//                )
                
//                HomeScreen(
//                    onFavouritesClick = { },
//                    onPhotoClick = { },
//                    onExploreClick = {}
//                )
                
                DetailsScreen(
                    photoId = 2880507,
                    source = DetailsViewModel.FROM_OTHERS,
                    onBackClick = {  },
                    onExploreClick = {  }
                ) 
            }
        }
    }
}

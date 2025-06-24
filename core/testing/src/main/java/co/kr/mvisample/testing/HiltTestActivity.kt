package co.kr.mvisample.testing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import co.kr.mvisample.feature.R
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.test.FakeImageLoaderEngine
import coil3.test.default
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HiltTestActivity : ComponentActivity() {

    @OptIn(DelicateCoilApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val engine = FakeImageLoaderEngine.Builder()
            .default(ContextCompat.getDrawable(applicationContext, R.drawable.img_charizard_icon) ?: error("drawable is null"))
            .build()

        val imageLoader = ImageLoader.Builder(applicationContext)
            .components { add(engine) }
            .build()

        SingletonImageLoader.setUnsafe(imageLoader)
    }
}
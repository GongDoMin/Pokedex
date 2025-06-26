package co.kr.mvisample.testing

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import co.kr.mvisample.feature.R
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.asImage
import coil3.test.FakeImageLoaderEngine
import coil3.test.default
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HiltTestActivity : ComponentActivity() {

    @OptIn(DelicateCoilApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val iconDrawable = ContextCompat.getDrawable(this, R.drawable.img_charizard_icon)
            ?: error("icon Drawable not found")
        val drawable = ContextCompat.getDrawable(this, R.drawable.img_charizard)
            ?: error("Drawable not found")

        val engine = FakeImageLoaderEngine.Builder()
            .intercept({ it is String && it.startsWith("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vii/icons/") }, iconDrawable.toBitmap(config = Bitmap.Config.ARGB_8888).asImage())
            .intercept({ it is String && it.startsWith("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/transparent/") }, drawable.toBitmap(config = Bitmap.Config.ARGB_8888).asImage())
            .default(drawable)
            .build()

        val imageLoader = ImageLoader.Builder(applicationContext)
            .components { add(engine) }
            .build()

        SingletonImageLoader.setUnsafe(imageLoader)
    }
}
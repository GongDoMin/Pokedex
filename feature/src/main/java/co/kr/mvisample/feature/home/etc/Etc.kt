package co.kr.mvisample.feature.home.etc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import co.kr.mvisample.core.theme.PokemonTheme

@Composable
fun EtcScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PokemonTheme.colors.backgroundRed),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "설정"
        )
    }
}
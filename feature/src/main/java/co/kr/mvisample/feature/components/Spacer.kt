package co.kr.mvisample.feature.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun HeightSpacer(
    height: Dp
) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun WidthSpacer(
    width: Dp
) {
    Spacer(modifier = Modifier.width(width))
}

@Composable
fun ColumnScope.WeightSpacer(
    weight: Float = 1f
) {
    Spacer(modifier = Modifier.weight(weight))
}

@Composable
fun RowScope.WeightSpacer(
    weight: Float = 1f
) {
    Spacer(modifier = Modifier.weight(weight))
}
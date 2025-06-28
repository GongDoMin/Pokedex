package co.kr.mvisample.common.utils

import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver

val OffsetY = SemanticsPropertyKey<Int>("offsetY")
var SemanticsPropertyReceiver.offsetY by OffsetY
package co.kr.mvisample.feature.utils.sementics

import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver

val CustomOffsetYKey = SemanticsPropertyKey<Int>("customOffsetY")
var SemanticsPropertyReceiver.customOffsetY by CustomOffsetYKey
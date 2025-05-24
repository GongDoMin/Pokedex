package co.kr.mvisample.design

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp

val BasicLineHeightStyle = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.None
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 36.sp,          // 도감 제목 등 가장 큰 타이틀에 사용
        lineHeight = 44.sp,
        lineHeightStyle = BasicLineHeightStyle
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,          // 포켓몬 이름, 분류 타이틀
        lineHeight = 32.sp,
        lineHeightStyle = BasicLineHeightStyle
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,          // 하위 타이틀, 정보 섹션 제목
        lineHeight = 28.sp,
        lineHeightStyle = BasicLineHeightStyle
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,          // 설명 텍스트나 스탯 정보
        lineHeight = 24.sp,
        letterSpacing = 0.2.sp,
        lineHeightStyle = BasicLineHeightStyle
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,          // 작은 설명, UI 안의 일반 텍스트
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        lineHeightStyle = BasicLineHeightStyle
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,          // 버튼 텍스트, 태그, 작은 강조 텍스트
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        lineHeightStyle = BasicLineHeightStyle
    )
)
package co.kr.mvisample.feature.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastMap
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import co.kr.mvisample.core.theme.PokemonTheme
import co.kr.mvisample.feature.components.pokemonCard
import co.kr.mvisample.feature.home.HomeSections.Companion.toSection
import co.kr.mvisample.feature.home.computer.ComputerScreen
import co.kr.mvisample.feature.home.pokedex.PokedexScreen
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@Composable
fun HomeContainer(
    onNavigateToPokemonDetail: (id: Int, name: String, isDiscovered: Boolean) -> Unit
) {
    val nestedNavController = rememberNavController()
    val navBackStackEntry by nestedNavController.currentBackStackEntryAsState()
    val currentSection = navBackStackEntry?.destination?.route.toSection()
    Scaffold(
        bottomBar = {
            PokemonBottomBar(
                tabs = HomeSections.entries.toList(),
                currentSection = currentSection ?: HomeSections.POKEDEX,
                onClickSection = {
                    if (it != (currentSection?.route ?: HomeSections.POKEDEX)) {
                        nestedNavController.navigate(it) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(nestedNavController.graph.id) {
                                saveState = true
                            }
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            navController = nestedNavController,
            startDestination = HomeRoutes.Pokedex
        ) {
            composable<HomeRoutes.Pokedex> {
                PokedexScreen(
                    onNavigateToPokemonDetail = onNavigateToPokemonDetail
                )
            }
            composable<HomeRoutes.Computer> {
                ComputerScreen()
            }
        }
    }
}

@Composable
fun PokemonBottomBar(
    tabs: List<HomeSections>,
    currentSection: HomeSections,
    onClickSection: (HomeRoutes) -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = PokemonTheme.colors.backgroundRed
) {
    PokemonBottomNavLayout(
        modifier = modifier,
        itemCount = tabs.size,
        contentColor = contentColor
    ) {
        tabs.forEach {
            PokemonBottomNavItemLayout(
                showIcon = it == currentSection,
                icon = {
                    Image(
                        modifier = Modifier,
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null
                    )
                },
                text = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .pokemonCard(
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(vertical = 4.dp)
                            .clickable {
                                onClickSection(it.route)
                            },
                        text = it.label,
                        style = PokemonTheme.typography.titleMedium,
                        color = PokemonTheme.colors.basicText,
                        textAlign = TextAlign.Center
                    )
                }
            )
        }
    }
}

@Composable
fun PokemonBottomNavLayout(
    itemCount: Int,
    modifier: Modifier = Modifier,
    contentColor: Color = PokemonTheme.colors.backgroundRed,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier
            .background(contentColor), // 배경 먼저 설정
        content = content
    ) { measurables, constraints ->

        val spacing = BottomBarPadding.roundToPx()

        val totalSpacing = spacing * (itemCount + 1)
        val availableWidth = constraints.maxWidth - totalSpacing
        val itemWidth = availableWidth / itemCount

        val itemConstraints = constraints.copy(
            minWidth = itemWidth,
            maxWidth = itemWidth,
            minHeight = 0,
            maxHeight = BottomNavHeight.roundToPx() - (spacing * 2)
        )

        val itemPlaceables = measurables.fastMap {
            it.measure(itemConstraints)
        }

        val layoutHeight = itemPlaceables.maxOfOrNull { it.height }?.plus(spacing * 2) ?: 0

        layout(
            width = constraints.maxWidth,
            height = layoutHeight
        ) {
            var x = spacing
            val y = spacing
            itemPlaceables.forEach {
                it.placeRelative(x = x, y = y)
                x += it.width + spacing
            }
        }
    }
}

@Composable
fun PokemonBottomNavItemLayout(
    showIcon: Boolean,
    modifier: Modifier = Modifier,
    icon: @Composable BoxScope.() -> Unit,
    text: @Composable BoxScope.() -> Unit
) {
    val iconAlpha by animateFloatAsState(
        targetValue = if (showIcon) 1f else 0f
    )

    Layout(
        modifier = modifier,
        content = {
            if (iconAlpha != 0f) {
                Box(
                    modifier = Modifier
                        .layoutId("icon"),
                    contentAlignment = Alignment.Center,
                    content = icon
                )
            }
            Box(
                modifier = Modifier
                    .layoutId("text"),
                contentAlignment = Alignment.Center,
                content = text
            )
        }
    ) { measureables, constraints ->
        val totalWidth = constraints.maxWidth
        val totalHeight = constraints.maxHeight

        val iconMaxSize = BottomIconSize.roundToPx()
        val iconPlaceable = measureables.firstOrNull { it.layoutId == "icon" }?.measure(constraints.copy(minWidth = 0, maxWidth = iconMaxSize, minHeight = 0, maxHeight = iconMaxSize))
        val iconWidth = ((iconPlaceable?.width ?: 0) * iconAlpha).roundToInt()

        val textMaxWidth = (totalWidth - iconWidth).coerceAtLeast(0)
        val textPlaceable = measureables.first { it.layoutId == "text" }.measure(constraints.copy(minWidth = 0, maxWidth = textMaxWidth))

        layout(totalWidth, totalHeight) {
            var x = (totalWidth - (iconWidth + textPlaceable.width)) / 2
            iconPlaceable?.placeRelative(x, (totalHeight - (iconPlaceable.height)) / 2)
            x += ((iconPlaceable?.width ?: 0) * iconAlpha).roundToInt()
            textPlaceable.placeRelative(x, (totalHeight - textPlaceable.height) / 2)
        }
    }
}

enum class HomeSections(
    val label: String,
    val route: HomeRoutes
) {
    POKEDEX("도감", HomeRoutes.Pokedex),
    COMPUTER("컴퓨터", HomeRoutes.Computer);

    companion object {
        fun String?.toSection(): HomeSections? {
            val key = this?.substringAfterLast(".") ?: return null
            return HomeSections.entries.find {
                it.route.toString() == key
            }
        }
    }
}

sealed interface HomeRoutes {
    @Serializable data object Pokedex: HomeRoutes
    @Serializable data object Computer: HomeRoutes
}

private val BottomNavHeight = 56.dp
private val BottomBarPadding = 8.dp
private val BottomIconSize = 24.dp

@Preview
@Composable
fun PokemonBottomBarPreview() {
    var currentSection by remember { mutableStateOf(HomeSections.POKEDEX) }

    PokemonTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PokemonTheme.colors.backgroundRed),
            contentAlignment = Alignment.BottomCenter
        ) {
            PokemonBottomBar(
                tabs = HomeSections.entries.toList(),
                currentSection = currentSection,
                onClickSection = {
                    currentSection = when (it) {
                        HomeRoutes.Pokedex -> HomeSections.POKEDEX
                        HomeRoutes.Computer -> HomeSections.COMPUTER
                    }
                }
            )
        }
    }
}
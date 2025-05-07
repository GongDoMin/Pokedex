package co.kr.mvisample.feature.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastMap
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import co.kr.mvisample.feature.components.pokemonCard
import co.kr.mvisample.feature.home.HomeSections.Companion.toSection
import co.kr.mvisample.feature.home.etc.EtcScreen
import co.kr.mvisample.feature.home.items.ItemsScreen
import co.kr.mvisample.feature.home.pokedex.PokedexScreen
import co.kr.mvisample.theme.PokemonTheme
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@Composable
fun HomeContainer(
    onNavigateToPokemonDetail: (pokemonName: String, isDiscovered: Boolean) -> Unit
) {
    val nestedNavController = rememberNavController()
    val navBackStackEntry by nestedNavController.currentBackStackEntryAsState()
    val currentSection = navBackStackEntry?.destination?.route.toSection()
    Scaffold(
        containerColor = PokemonTheme.colors.backgroundRed,
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
            composable<HomeRoutes.Items> {
                ItemsScreen()
            }
            composable<HomeRoutes.Etc> {
                EtcScreen()
            }
        }
    }
}

@Composable
fun PokemonBottomBar(
    tabs: List<HomeSections>,
    currentSection: HomeSections,
    modifier: Modifier = Modifier,
    onClickSection: (HomeRoutes) -> Unit,
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
                section = it,
                onClickSection = {
                    onClickSection(it.route)
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
            .height(BottomNavHeight)
            .background(contentColor),
        content = content
    ) { measureables, constraints ->
        val spacing = BottomBarPadding.roundToPx()
        val totalSpacing = spacing * (itemCount + 1)
        val availableWidth = constraints.maxWidth - totalSpacing
        val itemWidth = availableWidth / itemCount
        val itemHeight = constraints.maxHeight - spacing
        val itemPlaceables = measureables.fastMap {
            it.measure(
                constraints.copy(
                    minWidth = itemWidth,
                    maxWidth = itemWidth,
                    minHeight = itemHeight,
                    maxHeight = itemHeight
                )
            )
        }
        layout(
            width = constraints.maxWidth,
            height = itemPlaceables.maxByOrNull { it.height }?.height ?: 0
        ) {
            var x = spacing
            itemPlaceables.forEach {
                it.placeRelative(x, 0)
                x += it.width
                x += spacing
            }
        }
    }
}

@Composable
fun PokemonBottomNavItemLayout(
    showIcon: Boolean,
    section: HomeSections,
    onClickSection: (HomeSections) -> Unit,
    modifier: Modifier = Modifier,
) {
    val iconAlpha by animateFloatAsState(
        targetValue = if (showIcon) 1f else 0f
    )

    Layout(
        modifier = modifier,
        content = {
            if (iconAlpha != 0f) {
                Image(
                    modifier = Modifier
                        .layoutId("icon"),
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null
                )
            }
            Box(
                modifier = Modifier
                    .layoutId("text")
                    .fillMaxWidth()
                    .pokemonCard(
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable {
                        onClickSection(section)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = section.label,
                    style = PokemonTheme.typography.titleMedium,
                    color = PokemonTheme.colors.basicText
                )
            }
        }
    ) { measureables, constraints ->
        val totalWidth = constraints.maxWidth
        val totalHeight = constraints.maxHeight

        val iconMaxWith = BottomIconSize.roundToPx()
        val iconPlaceable = measureables.firstOrNull { it.layoutId == "icon" }?.measure(constraints.copy(minWidth = 0, maxWidth = iconMaxWith))
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
    ITEMS("가방", HomeRoutes.Items),
    ETC("설정", HomeRoutes.Etc);

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
    @Serializable data object Items: HomeRoutes
    @Serializable data object Etc: HomeRoutes
}

private val BottomNavHeight = 40.dp
private val BottomBarPadding = 8.dp
private val BottomIconSize = 24.dp
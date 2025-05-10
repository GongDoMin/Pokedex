package co.kr.mvisample.feature.home.pokedex.model

sealed interface PokedexEvent {
    data class OnNavigateToDetail(
        val id: Int,
        val name: String,
        val isDiscovered: Boolean
    ) : PokedexEvent
}
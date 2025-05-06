package co.kr.mvisample.feature.home.pokedex.model

sealed interface PokedexEvent {
    data class OnNavigateToDetail(val pokemonName: String, val isDiscovered: Boolean) : PokedexEvent
}
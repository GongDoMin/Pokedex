package co.kr.mvisample.feature.home.pokedex.model

sealed interface PokedexAction {
    data class OnClickPokemon(val pokemon: PokemonModel) : PokedexAction
    data object OnClickOptionButton : PokedexAction
}
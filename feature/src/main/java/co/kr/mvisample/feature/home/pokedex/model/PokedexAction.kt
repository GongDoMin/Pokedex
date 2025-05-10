package co.kr.mvisample.feature.home.pokedex.model

sealed interface PokedexAction {
    data class OnPokemonClick(val pokemon: PokemonModel) : PokedexAction
    data object ShowPokemonDetail : PokedexAction
    data object AttemptCatchPokemon : PokedexAction
    data object MarkPokemonAsDiscovered : PokedexAction
}
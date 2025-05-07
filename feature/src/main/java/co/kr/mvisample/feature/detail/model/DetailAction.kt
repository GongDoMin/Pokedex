package co.kr.mvisample.feature.detail.model

sealed interface DetailAction {
    sealed interface SystemAction : DetailAction {
        data class FetchPokemonDetail(val pokemonName: String) : SystemAction
    }
}
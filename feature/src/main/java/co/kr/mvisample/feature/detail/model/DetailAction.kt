package co.kr.mvisample.feature.detail.model

sealed interface DetailAction {
    data class FetchPokemonDetail(val id: Int, val name: String) : DetailAction
    data object OnBackClick : DetailAction
}
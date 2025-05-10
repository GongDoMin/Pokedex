package co.kr.mvisample.feature.detail.model

sealed interface DetailAction {
    sealed interface SystemAction : DetailAction {
        data class FetchPokemonDetail(val id: Int, val name: String) : SystemAction
    }
}
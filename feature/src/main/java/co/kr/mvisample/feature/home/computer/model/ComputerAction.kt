package co.kr.mvisample.feature.home.computer.model

sealed interface ComputerAction {
    data object FetchPokemonIcons : ComputerAction
}
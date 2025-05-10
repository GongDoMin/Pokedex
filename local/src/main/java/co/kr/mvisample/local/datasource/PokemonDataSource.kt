package co.kr.mvisample.local.datasource

import androidx.paging.PagingSource
import co.kr.mvisample.local.model.PokemonEntity

interface PokemonDataSource {
    fun getRepositories(): PagingSource<Int, PokemonEntity>
}
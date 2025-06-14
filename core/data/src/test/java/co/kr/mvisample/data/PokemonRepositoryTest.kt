package co.kr.mvisample.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import co.kr.mvisample.data.impl.PokemonRepositoryImpl
import co.kr.mvisample.data.model.PokemonDetail
import co.kr.mvisample.data.model.Type
import co.kr.mvisample.data.paging.PokemonRemoteMediator
import co.kr.mvisample.data.result.Result
import co.kr.mvisample.local.model.PokemonEntity
import co.kr.mvisample.local.model.PokemonLocalEntity
import co.kr.mvisample.testing.local.FakePokemonDao
import co.kr.mvisample.testing.local.FakePokemonLocalDao
import co.kr.mvisample.testing.remote.FakePokemonDataSource
import co.kr.turbino.testTurbino
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalPagingApi::class)
class PokemonRepositoryTest : StringSpec() {

    private val fakePokemonDataSource = FakePokemonDataSource()
    private val fakePokemonDao = FakePokemonDao()
    private val fakePokemonLocalDao = FakePokemonLocalDao()

    private val remoteMediator = PokemonRemoteMediator(
        pokemonDataSource = fakePokemonDataSource,
        pokemonDao = fakePokemonDao
    )
    private val pokemonRepository = PokemonRepositoryImpl(
        pokemonDataSource = fakePokemonDataSource,
        pokemonDao = fakePokemonDao,
        pokemonLocalDao = fakePokemonLocalDao
    )

    init {
        "페이지 로드한다." {
            val pagingState = PagingState<Int, PokemonEntity>(
                pages = listOf(),
                anchorPosition = null,
                config = PagingConfig(
                    pageSize = 20
                ),
                leadingPlaceholderCount = 20
            )
            val result = remoteMediator.load(LoadType.REFRESH, pagingState)
            (result is RemoteMediator.MediatorResult.Success) shouldBe true
            (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached shouldBe true
        }
        "내부 디비에 저장되었는지 확인한다." {
            val params = PagingSource.LoadParams.Refresh<Int>(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
            val result = fakePokemonDao.getPokemons().load(params)
            (result as PagingSource.LoadResult.Page).data.size shouldBe 20
            result.prevKey shouldBe null
            result.nextKey shouldBe 1
        }
        "포켓몬을 발견한다." {
            pokemonRepository.markAsDiscovered(6).testTurbino {
                awaitItem()
                awaitItem()

                val pokemon = fakePokemonLocalDao.getPokemonLocal(6)

                pokemon shouldBe PokemonLocalEntity(
                    id = 6,
                    iconUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vii/icons/6.png",
                    isCaught = false,
                    order = null
                )
            }
        }
        "포켓몬을 포획한다." {
            pokemonRepository.markAsCaught(6).testTurbino {
                awaitItem()
                awaitItem()

                val pokemon = fakePokemonLocalDao.getPokemonLocal(6)

                pokemon shouldBe PokemonLocalEntity(
                    id = 6,
                    iconUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vii/icons/6.png",
                    isCaught = true,
                    order = 1
                )
            }
        }
        "포켓몬을 놓아준다." {
            pokemonRepository.markAsRelease(6).testTurbino {
                awaitItem()
                awaitItem()

                val pokemon = fakePokemonLocalDao.getPokemonLocal(6)

                pokemon shouldBe PokemonLocalEntity(
                    id = 6,
                    iconUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vii/icons/6.png",
                    isCaught = false,
                    order = null
                )
            }
        }
        "포켓몬 상세 정보를 불러온다." {
            pokemonRepository.fetchPokemonDetail(id = 6, name = "charizard").testTurbino {
                val loading = awaitItem()
                (loading as Result.Loading).data shouldBe PokemonDetail(
                    id = 6,
                    name = "charizard",
                    imgUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-ii/gold/transparent/6.png"
                )

                val success = awaitItem()
                (success as Result.Success).data shouldBe PokemonDetail(
                    weight = 90.5f,
                    height = 1.7f,
                    types = listOf(
                        Type(name = "fire"),
                        Type(name = "flying")
                    )
                )
            }
        }
        "포켓몬 순서를 변경한다." {
            pokemonRepository.markAsDiscovered(6).collect()
            pokemonRepository.markAsCaught(6).collect()
            pokemonRepository.markAsDiscovered(9).collect()
            pokemonRepository.markAsCaught(9).collect()

            pokemonRepository.swapPokemonOrder(6, 9).testTurbino {
                awaitItem()
                awaitItem()

                val charizard = fakePokemonLocalDao.getPokemonLocal(6)
                val blastoise = fakePokemonLocalDao.getPokemonLocal(9)

                charizard shouldBe PokemonLocalEntity(
                    id = 6,
                    iconUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vii/icons/6.png",
                    isCaught = true,
                    order = 2
                )
                blastoise shouldBe PokemonLocalEntity(
                    id = 9,
                    iconUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vii/icons/9.png",
                    isCaught = true,
                    order = 1
                )
            }
        }
    }
}
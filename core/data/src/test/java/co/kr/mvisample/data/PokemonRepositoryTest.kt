package co.kr.mvisample.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import app.cash.turbine.test
import co.kr.mvisample.data.impl.PokemonRepositoryImpl
import co.kr.mvisample.data.paging.PokemonRemoteMediator
import co.kr.mvisample.data.result.Result
import co.kr.mvisample.local.model.PokemonEntity
import co.kr.mvisample.testing.local.FakePokemonDao
import co.kr.mvisample.testing.local.FakePokemonDataSource
import co.kr.mvisample.testing.local.FakePokemonLocalDao
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow

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
            assertSuccessAndComplete(
                flow = pokemonRepository.markAsDiscovered(6),
                assertBlock = {
                    val pokemon = fakePokemonLocalDao.getPokemonLocal(6)
                    pokemon?.id shouldBe 6
                    pokemon?.isCaught shouldBe false
                }
            )
        }
        "포켓몬을 포획한다." {
            assertSuccessAndComplete(
                flow = pokemonRepository.markAsCaught(6),
                assertBlock = {
                    val pokemon = fakePokemonLocalDao.getPokemonLocal(6)
                    pokemon?.id shouldBe 6
                    pokemon?.isCaught shouldBe true
                    pokemon?.order shouldBe 1
                }
            )
        }
        "포켓몬을 놓아준다." {
            assertSuccessAndComplete(
                flow = pokemonRepository.markAsRelease(6),
                assertBlock = {
                    val pokemon = fakePokemonLocalDao.getPokemonLocal(6)
                    pokemon?.id shouldBe 6
                    pokemon?.isCaught shouldBe false
                    pokemon?.order shouldBe null
                }
            )
        }
        "포켓몬 상세 정보를 불러온다." {
            pokemonRepository.fetchPokemonDetail(id = 6, "charizard").test {
                val loading = awaitItem()
                (loading as Result.Loading).data?.id shouldBe 6
                loading.data?.name shouldBe "charizard"
                loading.data?.id shouldBe 6

                val success = awaitItem()
                (success as Result.Success).data.weight shouldBe  90.5f
                success.data.height shouldBe 1.7f

                awaitComplete()
            }
        }
        "포켓몬 순서를 변경한다." {
            assertSuccessAndComplete(
                flow = pokemonRepository.markAsCaught(6)
            )
            assertSuccessAndComplete(
                flow = pokemonRepository.markAsDiscovered(9)
            )
            assertSuccessAndComplete(
                flow = pokemonRepository.markAsCaught(9),
            )
            assertSuccessAndComplete(
                flow = pokemonRepository.swapPokemonOrder(6, 9),
                assertBlock = {
                    val charizard = fakePokemonLocalDao.getPokemonLocal(6)
                    val blastoise = fakePokemonLocalDao.getPokemonLocal(9)

                    charizard?.order shouldBe 2
                    blastoise?.order shouldBe 1
                }
            )
        }
    }

    private suspend fun assertSuccessAndComplete(
        flow: Flow<Result<Unit>>,
        assertBlock: suspend () -> Unit ={}
    ) {
        flow.test {
            awaitItem() // loading
            awaitItem() // success
            assertBlock()
            awaitComplete()
        }
    }
}
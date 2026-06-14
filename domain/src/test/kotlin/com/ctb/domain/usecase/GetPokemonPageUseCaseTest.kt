package com.ctb.domain.usecase

import com.ctb.commonkotlin.test.assertEmitsFailure
import com.ctb.commonkotlin.test.assertEmitsSuccess
import com.ctb.domain.models.Pokemon
import com.ctb.domain.models.PokemonPage
import com.ctb.domain.models.PokemonType
import com.ctb.domain.repositories.PokemonRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetPokemonPageUseCaseTest {
    private val repository: PokemonRepository = mockk()
    private val useCase = GetPokemonPageUseCase(repository, UnconfinedTestDispatcher())

    @Test
    fun `emits success when repository returns a page`() =
        runTest {
            val page =
                PokemonPage(
                    items = listOf(Pokemon(1, "Bulbasaur", "", PokemonType.GRASS)),
                    nextOffset = 20,
                    hasMore = true,
                )
            every { repository.getPokemonPage(limit = 20, offset = 0) } returns flowOf(page)

            useCase(GetPokemonPageUseCase.Params(limit = 20, offset = 0)).assertEmitsSuccess()
        }

    @Test
    fun `emits failure when repository throws`() =
        runTest {
            every { repository.getPokemonPage(any(), any()) } returns
                flow { throw IllegalStateException("boom") }

            useCase(GetPokemonPageUseCase.Params(limit = 20, offset = 0)).assertEmitsFailure()
        }
}

import com.ctb.commonkotlin.usecases.CoroutinesTestRule
import com.ctb.commonkotlin.usecases.Result
import com.ctb.domain.models.ShortenUrlAlias
import com.ctb.domain.models.ShortenUrlLink
import com.ctb.domain.repositories.ShortenUrlRepository
import com.ctb.domain.usecase.ShortenUrlUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class ShortenUrlUseCaseTest {
    @get:Rule
    val mainDispatcherRule = CoroutinesTestRule()

    private val repository = mockk<ShortenUrlRepository>()

    private val useCase = ShortenUrlUseCase(repository, mainDispatcherRule.dispatcher)

    @Test
    fun `WHEN useCase is called THEN returns flow to be mapped`() =
        runTest {
            coEvery {
                repository.getShortenUrl(any())
            } returns flowOf(getMockShortenUrl())
            useCase(ShortenUrlUseCase.ParamShortenerUrl(url = "https://sample")).collect {
                assert(it is Result.Success)
            }
        }

    @Test(expected = Exception::class)
    fun `WHEN useCase is called THEN throw exception`() =
        runTest {
            coEvery {
                repository.getShortenUrl(any())
            } throws Exception()
            useCase(ShortenUrlUseCase.ParamShortenerUrl(url = "https://sample")).collect {
                assert(it is Result.Failure)
            }
        }

    @Test
    fun `WHEN useCase is called THEN returns exception failure to be mapped`() =
        runTest {
            coEvery {
                repository.getShortenUrl(any())
            } returns
                flow {
                    throw IllegalStateException("simulated network failure")
                }

            // use the helper to assert the flow emits a failure result
            useCase(ShortenUrlUseCase.ParamShortenerUrl(url = "https://sample")).collect {
                assert(it is Result.Failure)
            }
        }

    fun getMockShortenUrl() =
        ShortenUrlAlias(
            alias = "abcd",
            links =
            ShortenUrlLink(
                self = "https://self.com/abcd",
                short = "https://short.com/abcd",
            ),
        )
}

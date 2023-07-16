import kotlinx.coroutines.runBlocking
import net.sevendays.android.code_check.R
import net.sevendays.android.code_check.OneViewModel
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4

// Ideally, should use a Mock Server
@RunWith(AndroidJUnit4::class)
class OneViewModelTest {

    private lateinit var viewModel: OneViewModel

    @Before
    fun setup() {
        // Assuming the context can be mocked
        val context = mock(Context::class.java)
        `when`(context.getString(R.string.written_language, anyString())).thenAnswer { i ->
            "Language: ${i.arguments[1]}"
        }

        viewModel = OneViewModel(context)
    }

    @Test
    fun searchResults_returnExpectedItems() = runBlocking {
        // This should be replaced with a query that you know will return a stable result
        val query = "stable_query"
        val result = viewModel.searchResults(query)

        // Because we can't guarantee what the actual server will return, this will
        // just assert that the request didn't fail and some results were returned
        assertTrue(result.isNotEmpty())
    }
}

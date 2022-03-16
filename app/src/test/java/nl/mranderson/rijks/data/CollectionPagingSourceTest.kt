package nl.mranderson.rijks.data

import androidx.paging.PagingSource
import kotlinx.coroutines.test.runTest
import nl.mranderson.rijks.data.api.CollectionApiService
import nl.mranderson.rijks.data.mapper.CollectionMapper
import nl.mranderson.rijks.data.model.CollectionResponse
import nl.mranderson.rijks.domain.model.Art
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class CollectionPagingSourceTest {

    private lateinit var collectionPagingSource: CollectionPagingSource

    private var collectionApiService: CollectionApiService = mock()
    private var collectionMapper: CollectionMapper = mock()

    @BeforeEach
    fun setUp() {
        collectionPagingSource = CollectionPagingSource(
            collectionApiService = collectionApiService,
            collectionMapper = collectionMapper
        )
    }

    @AfterEach
    fun tearDown() {
        Mockito.reset(
            collectionApiService,
            collectionMapper
        )
    }

    @Test
    fun `given first load, when paging source loading, then load successful and set next keys`() =
        runTest {
            // Given
            val collectionResponse: CollectionResponse = mock()
            val art: Art = mock()
            whenever(
                collectionApiService.getCollection(
                    any(),
                    any()
                )
            ).thenReturn(collectionResponse)
            whenever(collectionMapper.map(any())).thenReturn(listOf(art))

            val expectedResult = PagingSource.LoadResult.Page(
                data = listOf(art),
                prevKey = null,
                nextKey = 1
            )

            // When
            val result = collectionPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 1,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )

            // Then
            assertEquals(expectedResult, result)
        }

    @Test
    fun `given first load but no response, when paging source loading, then load failed and set next keys`() =
        runTest {
            // Given
            val collectionResponse: CollectionResponse = mock()
            whenever(
                collectionApiService.getCollection(
                    any(),
                    any()
                )
            ).thenReturn(collectionResponse)
            whenever(collectionMapper.map(any())).thenReturn(emptyList())

            val expectedResult = PagingSource.LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )

            // When
            val result = collectionPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 1,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )

            // Then
            assertEquals(expectedResult, result)
        }

    @Test
    fun `given another load, when paging source loading, then load successful and set next keys`() =
        runTest {
            // Given
            val collectionResponse: CollectionResponse = mock()
            val art: Art = mock()
            whenever(
                collectionApiService.getCollection(
                    any(),
                    any()
                )
            ).thenReturn(collectionResponse)
            whenever(collectionMapper.map(any())).thenReturn(listOf(art))

            val expectedResult = PagingSource.LoadResult.Page(
                data = listOf(art),
                prevKey = 1,
                nextKey = 2
            )

            // When
            val result = collectionPagingSource.load(
                PagingSource.LoadParams.Append(
                    key = 2,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )

            // Then
            assertEquals(expectedResult, result)
        }

    @Test
    fun `given an exception, when paging source loading, then return error`() = runTest {
        // Given
        val error = RuntimeException("404", Throwable())
        whenever(collectionApiService.getCollection(any(), any())).thenThrow(error)

        val expectedResult = PagingSource.LoadResult.Error<Int, Art>(error)

        // When
        val result = collectionPagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        // Then
        assertEquals(expectedResult, result)
    }
}
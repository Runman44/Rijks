package nl.mranderson.rijks.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import nl.mranderson.rijks.domain.CollectionRepository
import nl.mranderson.rijks.domain.model.Art
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetCollectionTest {

    private lateinit var getCollection: GetCollection

    private var collectionRepository: CollectionRepository = mock()

    @BeforeEach
    fun setUp() {
        getCollection = GetCollection(
            collectionRepository = collectionRepository
        )
    }

    @AfterEach
    fun tearDown() {
        Mockito.reset(
            collectionRepository
        )
    }

    @Test
    fun `When retrieving the collection, Then return paging data`() =
        runTest {
            // Given
            val pagingData: Flow<PagingData<Art>> = mock()
            whenever(collectionRepository.getCollection()).thenReturn(pagingData)

            // When
            val result = getCollection()

            // Then
            assertEquals(pagingData, result)
        }
}
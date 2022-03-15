package nl.mranderson.rijks.data

import nl.mranderson.rijks.data.api.CollectionApiService
import nl.mranderson.rijks.data.mapper.ArtDetailsMapper
import nl.mranderson.rijks.domain.model.ArtDetails

class CollectionRemoteDataSource(
    private val collectionApiService: CollectionApiService,
    private val artDetailsMapper: ArtDetailsMapper
) : CollectionDataSource {

    override suspend fun getArtDetails(artId: String): ArtDetails {
        val response = collectionApiService.getArtDetails(artId)
        return artDetailsMapper.map(response)
    }

}
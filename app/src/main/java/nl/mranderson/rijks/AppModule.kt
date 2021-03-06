package nl.mranderson.rijks

import nl.mranderson.rijks.data.CollectionPagingSource
import nl.mranderson.rijks.data.mapper.CollectionMapper
import nl.mranderson.rijks.data.CollectionRemoteDataSource
import nl.mranderson.rijks.data.CollectionRepositoryImpl
import nl.mranderson.rijks.data.mapper.ArtDetailsMapper
import nl.mranderson.rijks.domain.CollectionRepository
import nl.mranderson.rijks.domain.usecase.GetArtDetails
import nl.mranderson.rijks.domain.usecase.GetCollection
import nl.mranderson.rijks.ui.detail.DetailViewModel
import nl.mranderson.rijks.ui.list.ListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { ListViewModel(getCollection = get()) }

    viewModel { (artId: String) -> DetailViewModel(artId = artId, getArtDetails = get()) }

    factory { GetCollection(collectionRepository = get()) }

    factory { GetArtDetails(collectionRepository = get()) }

    factory {
        CollectionRemoteDataSource(
            collectionApiService = get(),
            artDetailsMapper = ArtDetailsMapper
        )
    }

    factory {
        CollectionPagingSource(
            collectionApiService = get(),
            collectionMapper = CollectionMapper
        )
    }

    factory<CollectionRepository> {
        CollectionRepositoryImpl(
            collectionPagingSource = get(),
            collectionRemoteDataSource = get()
        )
    }
}
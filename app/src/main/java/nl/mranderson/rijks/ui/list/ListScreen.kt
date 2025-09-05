package nl.mranderson.rijks.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import nl.mranderson.rijks.R
import nl.mranderson.rijks.ui.components.ArtImage
import nl.mranderson.rijks.ui.components.ErrorButton
import nl.mranderson.rijks.ui.components.LoadingSpinner
import nl.mranderson.rijks.ui.list.ListViewModel.ArtUIModel
import nl.mranderson.rijks.ui.list.ListViewModel.ArtUIModel.ArtData
import nl.mranderson.rijks.ui.list.ListViewModel.ArtUIModel.AuthorSeparator

@Composable
internal fun ListRoute(
    onArtClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListViewModel = hiltViewModel(),
) {
    val artCollection = viewModel.artCollectionFlow.collectAsLazyPagingItems()

    ListScreen(
        modifier = modifier,
        artCollection = artCollection,
        onArtClicked = onArtClicked
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ListScreen(
    artCollection: LazyPagingItems<ArtUIModel>,
    onArtClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold { innerPadding ->
        LazyColumn(
            modifier = modifier
                .consumeWindowInsets(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = innerPadding
        ) {
            items(artCollection.itemCount) { index ->
                artCollection[index]?.let { art ->
                    when (art) {
                        is AuthorSeparator -> Separator(
                            author = art.author
                        )

                        is ArtData -> ArtListItem(
                            art = art,
                            onArtClicked = { id ->
                                onArtClicked(id)
                            })
                    }
                }
            }
            renderLoading(artCollection)
            renderError(artCollection)
        }
    }
}

private fun LazyListScope.renderLoading(lazyArtCollection: LazyPagingItems<ArtUIModel>) {
    lazyArtCollection.apply {
        when {
            loadState.refresh is LoadState.Loading -> {
                item {
                    LoadingSpinner(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .wrapContentSize(Alignment.Center)
                    )
                }
            }

            loadState.append is LoadState.Loading -> {
                item {
                    LoadingSpinner(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .wrapContentSize(Alignment.Center)
                    )
                }
            }

            else -> return
        }
    }
}

private fun LazyListScope.renderError(lazyArtCollection: LazyPagingItems<ArtUIModel>) {
    lazyArtCollection.apply {
        when {
            loadState.refresh is LoadState.Error -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(all = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.global_error_message),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ErrorButton(onClickRetry = { retry() })
                    }
                }
            }

            loadState.append is LoadState.Error -> {
                item {
                    ErrorButton(
                        modifier = Modifier
                            .padding(all = 8.dp),
                        onClickRetry = { retry() })
                }
            }

            else -> return
        }
    }
}

@Composable
private fun ArtListItem(
    modifier: Modifier = Modifier,
    art: ArtData,
    onArtClicked: (String) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onArtClicked(art.id) }
            .then(modifier),
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
    ) {
        Row {
            ArtImage(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
                    .size(84.dp)
                    .clip(RoundedCornerShape(corner = CornerSize(16.dp))),
                imageUrl = art.imageUrl
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = art.title, style = typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = R.string.view_details),
                    style = typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun Separator(modifier: Modifier = Modifier, author: String) {
    Text(
        text = author,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
    )
}
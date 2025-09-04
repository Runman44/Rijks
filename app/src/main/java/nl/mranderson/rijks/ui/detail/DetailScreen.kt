package nl.mranderson.rijks.ui.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import nl.mranderson.rijks.R
import nl.mranderson.rijks.domain.model.ArtDetails
import nl.mranderson.rijks.ui.components.ArtImage
import nl.mranderson.rijks.ui.components.Chips
import nl.mranderson.rijks.ui.components.ErrorView
import nl.mranderson.rijks.ui.components.LoadingView
import nl.mranderson.rijks.ui.detail.DetailViewModel.ScreenState.Data
import nl.mranderson.rijks.ui.detail.DetailViewModel.ScreenState.Error
import nl.mranderson.rijks.ui.detail.DetailViewModel.ScreenState.Loading

@Composable
internal fun DetailRoute(
    onBackClicked: () -> Unit,
    onImageClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val detailState by viewModel.state.collectAsStateWithLifecycle()

    DetailScreen(
        viewData = detailState,
        onRetryClicked = viewModel::onRetryClicked,
        onBackClicked = onBackClicked,
        onImageClicked = onImageClicked,
        modifier = modifier
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailScreen(
    viewData: DetailViewModel.ScreenState,
    onRetryClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onImageClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier) { _ ->
        when (viewData) {
            is Data -> {
                ArtDetail(viewData.artDetail, onBackClicked, onImageClicked)
            }

            is Error -> {
                ErrorView(
                    message = stringResource(id = R.string.global_error_message),
                    onClickRetry = { onRetryClicked() }
                )
            }

            is Loading -> {
                LoadingView()
            }
        }
    }
}


@Composable
private fun ArtDetail(
    artDetail: ArtDetails,
    onBackClicked: () -> Unit,
    onImageClicked: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    BoxWithConstraints {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            Box {
                ArtImage(
                    modifier = Modifier
                        .heightIn(max = this@BoxWithConstraints.maxHeight / 3),
                    imageUrl = artDetail.imageUrl
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onBackClicked,
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.statusBars)
                            .padding(horizontal = 16.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null,
                        )
                    }
                    Button(
                        onClick = {
                            onImageClicked(
                                URLEncoder.encode(
                                    artDetail.imageUrl,
                                    StandardCharsets.UTF_8.toString()
                                )
                            )
                        },
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.statusBars)
                            .padding(horizontal = 16.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = null,
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ArtTitle(text = artDetail.title)
                Chips(titles = artDetail.types)
                ArtProperty(
                    label = stringResource(id = R.string.description),
                    value = artDetail.description ?: stringResource(id = R.string.dash)
                )
                ArtProperty(
                    label = stringResource(id = R.string.author),
                    value = artDetail.author
                )
                ArtProperty(
                    label = stringResource(id = R.string.object_name),
                    value = artDetail.objectNumber
                )
            }
        }
    }
}

@Composable
private fun ArtTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
private fun ArtProperty(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Divider(modifier = Modifier.padding(bottom = 4.dp))
        Text(
            text = label,
            modifier = Modifier.height(24.dp),
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
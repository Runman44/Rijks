package nl.mranderson.rijks.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
fun DetailScreen(
    viewData: DetailViewModel.ScreenState,
    onRetryClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    when (viewData) {
        is Data -> {
            ArtDetail(viewData.artDetail) {
                onBackClicked()
            }
        }

        is Error -> {
            ErrorView(message = stringResource(id = R.string.global_error_message)) {
                onRetryClicked()
            }
        }

        is Loading -> {
            LoadingView()
        }
    }
}

@Composable
fun ArtDetail(artDetail: ArtDetails, onBackClicked: () -> Unit) {
    val scrollState = rememberScrollState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            BoxWithConstraints {
                Surface {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState),
                    ) {
                        Box {
                            ArtImage(
                                modifier = Modifier
                                    .heightIn(max = this@BoxWithConstraints.maxHeight / 2)
                                    .fillMaxWidth(),
                                imageUrl = artDetail.imageUrl
                            )
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
                        }
                        Column(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)) {
                            ArtTitle(text = artDetail.title)
                            Chips(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                titles = artDetail.types
                            )
                            ArtProperty(
                                stringResource(
                                    id = R.string.description
                                ), artDetail.description ?: stringResource(id = R.string.dash)
                            )
                            ArtProperty(
                                stringResource(
                                    id = R.string.author
                                ), artDetail.author
                            )
                            ArtProperty(
                                stringResource(
                                    id = R.string.object_name
                                ), artDetail.objectNumber
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ArtTitle(
    text: String
) {
    Column(modifier = Modifier.padding(all = 16.dp)) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ArtProperty(label: String, value: String) {
    Column(modifier = Modifier.padding(all = 16.dp)) {
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
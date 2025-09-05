package nl.mranderson.rijks.ui.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import nl.mranderson.rijks.ui.theme.RijksTheme

const val LOADING_INDICATOR_TEST_TAG = "loadingTag"

@Composable
fun LoadingSpinner(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier = modifier.testTag(LOADING_INDICATOR_TEST_TAG))
}

@Preview
@Composable
private fun Preview() {
    RijksTheme {
        LoadingSpinner()
    }
}
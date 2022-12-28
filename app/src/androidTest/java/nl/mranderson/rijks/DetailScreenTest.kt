package nl.mranderson.rijks

import androidx.compose.material.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import nl.mranderson.rijks.domain.model.ArtDetails
import nl.mranderson.rijks.ui.MainActivity
import nl.mranderson.rijks.ui.components.LOADING_INDICATOR_TEST_TAG
import nl.mranderson.rijks.ui.detail.DetailScreen
import nl.mranderson.rijks.ui.detail.DetailViewModel
import nl.mranderson.rijks.ui.detail.DetailViewModel.ScreenState.Error
import nl.mranderson.rijks.ui.detail.DetailViewModel.ScreenState.Loading
import nl.mranderson.rijks.ui.theme.RijksTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailScreenTest {

    @Rule
    @JvmField
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val artDetails = ArtDetails(
        objectNumber = "ID-002",
        title = "Landschap",
        author = "Vermeer",
        imageUrl = "www.mranderson.nl",
        types = emptyList(),
        description = "nice painting"
    )

    @Test
    fun checkIfLoadingStateIsDisplayed() {
        composeTestRule.setContent {
            RijksTheme {
                Surface {
                    DetailScreen(
                        state = Loading,
                        interaction = object : DetailInteraction {
                            override fun onBackClicked() {
                                //NO-OP
                            }

                            override fun onRetryClicked() {
                                //NO-OP
                            }
                        })
                }
            }
        }

        composeTestRule.onNodeWithTag(LOADING_INDICATOR_TEST_TAG).assertIsDisplayed()
    }

    @Test
    fun checkIfErrorStateIsDisplayed() {
        composeTestRule.setContent {
            RijksTheme {
                Surface {
                    DetailScreen(
                        state = Error,
                        interaction = object : DetailInteraction {
                            override fun onBackClicked() {
                                //NO-OP
                            }

                            override fun onRetryClicked() {
                                //NO-OP
                            }
                        })
                }
            }
        }

        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.global_error_message))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.global_retry_message))
            .performClick()
    }

    @Test
    fun checkIfDataStateIsDisplayed() {
        composeTestRule.setContent {
            RijksTheme {
                Surface {
                    DetailScreen(
                        state = DetailViewModel.ScreenState.Data(artDetails),
                        interaction = object : DetailInteraction {
                            override fun onBackClicked() {
                                //NO-OP
                            }

                            override fun onRetryClicked() {
                                //NO-OP
                            }
                        })
                }
            }
        }

        composeTestRule.onNodeWithText("Vermeer").assertIsDisplayed()
        composeTestRule.onNodeWithText("Landschap").assertIsDisplayed()
        composeTestRule.onNodeWithText("ID-002").assertIsDisplayed()
        composeTestRule.onNodeWithText("nice painting").assertIsDisplayed()
    }
}
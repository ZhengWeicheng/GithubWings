package com.wings.githubwings.ui

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wings.githubwings.biz.home.HomeScreen
import com.wings.githubwings.framework.navgation.MainDestinations
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testNavigateToSearchScreen() {
        var gotoSearch = false
        composeTestRule.setContent {
            HomeScreen(
                goToRepositoryDetails = { owner, repo ->
                },
                gotoSearch = {
                    gotoSearch = true
                },
                goToProfile = {
                }
            )
        }

        composeTestRule.onNodeWithContentDescription("Search").assertHasClickAction()

        composeTestRule.onNodeWithContentDescription("Search").performClick()

        assert(gotoSearch)
    }

    @Test
    fun testNavigateToLoginScreen() {
        var goToProfile = false
        composeTestRule.setContent {
            HomeScreen(
                goToRepositoryDetails = { owner, repo ->
                },
                gotoSearch = {
                },
                goToProfile = {
                    goToProfile=true
                }
            )
        }

        composeTestRule.onNodeWithContentDescription("Login").performClick()

        assert(goToProfile)
    }
}
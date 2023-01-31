package com.payrem


import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.payrem.ui.theme.PayRemTheme
import org.junit.Rule
import org.junit.Test

class Tests {
    @get:Rule
    val composeTestRule = createComposeRule()
    var id = 4

    @Test
    fun signInNavigation() {
        // Start the app
        composeTestRule.setContent {
            PayRemTheme {
                MainSignInScreen()
            }
        }

        composeTestRule.onNodeWithText("SIGN UP").performClick()

        composeTestRule.onNodeWithText("SIGN IN").assertIsDisplayed()
    }

    @Test
    fun signInSuccessful() {
        // Start the app
        composeTestRule.setContent {
            PayRemTheme {
                MainSignInScreen()
            }
        }

        composeTestRule.onNodeWithText("Login").performTextInput("test@test.com")
        composeTestRule.onNodeWithText("Password").performTextInput("pass")

        composeTestRule.onNodeWithText("SIGN IN").performClick()

        composeTestRule.onNodeWithText("Personal").assertIsDisplayed()
    }

    @Test
    fun signInFailed() {
        // Start the app
        composeTestRule.setContent {
            PayRemTheme {
                MainSignInScreen()
            }
        }

        composeTestRule.onNodeWithText("Login").performTextInput("no_such_user")
        composeTestRule.onNodeWithText("Password").performTextInput("no_such_password")

        composeTestRule.onNodeWithText("SIGN IN").performClick()

        composeTestRule.onNodeWithText("SIGN IN").assertIsDisplayed()
//        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")
    }

    // change email and username before run
    @Test
    fun signUpSuccessful() {
        // Start the app
        composeTestRule.setContent {
            PayRemTheme {
                MainSignInScreen()
            }
        }

        composeTestRule.onNodeWithText("SIGN UP").performClick()

        composeTestRule.onNodeWithText("E-mail").performTextInput("test_user_assertion${id}@gmail.com")
        composeTestRule.onNodeWithText("Username").performTextInput("test_user_assertion${id}")
        composeTestRule.onNodeWithText("Password").performTextInput("pass")
        composeTestRule.onNodeWithText("Repeat password").performTextInput("pass")


        composeTestRule.onNodeWithText("SIGN UP").performClick()

        composeTestRule.onNodeWithText("Personal").assertIsDisplayed()
    }

    @Test
    fun signUpFailed() {
        // Start the app
        composeTestRule.setContent {
            PayRemTheme {
                MainSignInScreen()
            }
        }

        composeTestRule.onNodeWithText("SIGN UP").performClick()

        composeTestRule.onNodeWithText("E-mail").performTextInput("")
        composeTestRule.onNodeWithText("Username").performTextInput("test_user_assertion")
        composeTestRule.onNodeWithText("Password").performTextInput("pass")
        composeTestRule.onNodeWithText("Repeat password").performTextInput("pass")


        composeTestRule.onNodeWithText("SIGN UP").performClick()

        composeTestRule.onNodeWithText("SIGN UP").assertIsDisplayed()
    }

    @Test
    fun mainNavigation() {
        // Start the app
        composeTestRule.setContent {
            PayRemTheme {
                MainScreen()
            }
        }


        composeTestRule.onNode(hasTestTag("personalList"), useUnmergedTree = true).assertExists()

        composeTestRule.onNodeWithText("Group").performClick()

        composeTestRule.onNodeWithText("Create").assertIsDisplayed()

        composeTestRule.onNodeWithText("Members").performClick()

        composeTestRule.onNodeWithText("Invite").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Add new reminder").performClick()

        composeTestRule.onNodeWithText("Title").assertIsDisplayed()

        composeTestRule.onNode(hasTestTag("logout"), useUnmergedTree = true).performClick()

        composeTestRule.onNodeWithText("SIGN IN").assertIsDisplayed()
    }

    // change title before run TODO
//    @Test
//    fun addPersonalReminder() {
//        // Start the app
//        composeTestRule.setContent {
//            PayRemTheme {
//                MainSignInScreen()
//            }
//        }
//
//        composeTestRule.onNodeWithText("Login").performTextInput("test@test.com")
//        composeTestRule.onNodeWithText("Password").performTextInput("pass")
//
//        composeTestRule.onNodeWithText("SIGN IN").performClick()
//
//        composeTestRule.onNodeWithContentDescription("Add spending").performClick()
//
//        composeTestRule.onNodeWithText("Title").performTextInput("Assertion_tests${id}")
//        composeTestRule.onNodeWithText("Category").performTextInput("Assertion_tests${id}")
//        composeTestRule.onNodeWithText("Save").performClick()
//        composeTestRule.onNodeWithText("Group").performClick()
//        composeTestRule.onNodeWithText("Personal").performClick()
//
//        composeTestRule.onNodeWithText("Assertion_tests").assertIsDisplayed()
//    }

    // change title before run TODO
//    @Test
//    fun addGroupReminder() {
//        // Start the app
//        composeTestRule.setContent {
//            PayRemTheme {
//                MainSignInScreen()
//            }
//        }
//
//        composeTestRule.onNodeWithText("Login").performTextInput("user.2@gmail.com")
//        composeTestRule.onNodeWithText("Password").performTextInput("pass")
//        composeTestRule.onNodeWithText("SIGN IN").performClick()
//
//
//        composeTestRule.onNodeWithContentDescription("Add spending").performClick()
//
//        composeTestRule.onNodeWithText("Title").performTextInput("Assertion_group_tests")
//        composeTestRule.onNodeWithText("Category").performTextInput("Assertion_group_tests")
//        composeTestRule.onNode(hasTestTag("addCost"), useUnmergedTree = true).performTextInput("2.2")
//        composeTestRule.onNode(hasTestTag("groupExpenseCheckBox"), useUnmergedTree = true).performClick()
//        composeTestRule.onNode(hasTestTag("groupSelectExpenseDropdown"), useUnmergedTree = true).performClick()
//        composeTestRule.onNodeWithText("test16").performClick()
//        composeTestRule.onNodeWithText("Artemii").performClick()
//
//        composeTestRule.onNodeWithText("Save").performClick()
//        composeTestRule.onNodeWithText("Personal").performClick()
//        composeTestRule.onNodeWithText("Group").performClick()
//        composeTestRule.onNodeWithText("List").performClick()
//        composeTestRule.onNode(hasTestTag("groupSelectionGroupScreen"), useUnmergedTree = true).performClick()
//        composeTestRule.onNodeWithText("test16").performClick()
//
//        composeTestRule.onNodeWithText("Assertion_group_tests").assertIsDisplayed()
//    }

    // change group name before run
    @Test
    fun createAndInviteToGroup() {
        // Start the app
        composeTestRule.setContent {
            PayRemTheme {
                MainSignInScreen()
            }
        }

        composeTestRule.onNodeWithText("Login").performTextInput("test@test.com")
        composeTestRule.onNodeWithText("Password").performTextInput("pass")
        composeTestRule.onNodeWithText("SIGN IN").performClick()

        composeTestRule.onNodeWithText("Group").performClick()
        composeTestRule.onNodeWithText("Create").performClick()
        composeTestRule.onNodeWithText("Name").performTextInput("assertion_test_group${id}")
        composeTestRule.onNodeWithText("Description").performTextInput("assertion_test_group${id}")
        composeTestRule.onNodeWithText("Save").performClick()

        composeTestRule.onNodeWithText("Members").performClick()

        composeTestRule.onNodeWithText("Email").performTextInput("user.3@gmail.com")
        composeTestRule.onNodeWithText("Invite").performClick()
    }
}
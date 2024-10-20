package com.android.voyageur.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.android.voyageur.model.user.User
import com.android.voyageur.model.user.UserRepository
import com.android.voyageur.model.user.UserViewModel
import com.android.voyageur.ui.navigation.NavigationActions
import com.android.voyageur.ui.navigation.Route
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class ProfileScreenTest {

  private lateinit var navigationActions: NavigationActions
  private lateinit var userRepository: UserRepository
  private lateinit var userViewModel: UserViewModel
  private lateinit var firebaseAuth: FirebaseAuth
  private lateinit var firebaseUser: FirebaseUser

  @get:Rule val composeTestRule = createComposeRule()

  @Before
  fun setUp() {
    // Mock dependencies
    navigationActions = mock(NavigationActions::class.java)
    userRepository = mock(UserRepository::class.java)
    firebaseAuth = mock(FirebaseAuth::class.java)
    firebaseUser = mock(FirebaseUser::class.java)

    // Mock FirebaseAuth to return our mocked firebaseUser
    `when`(firebaseAuth.currentUser).thenReturn(firebaseUser)

    // Create the UserViewModel with the mocked userRepository and firebaseAuth
    userViewModel = UserViewModel(userRepository)

    // Mocking initial navigation state
    `when`(navigationActions.currentRoute()).thenReturn(Route.PROFILE)

    // Set the content for Compose rule
    composeTestRule.setContent {
      ProfileScreen(userViewModel = userViewModel, navigationActions = navigationActions)
    }
  }

  @Test
  fun displayTextWhenUserIsNull() {
    // Arrange: Mock empty user state and not loading
    `when`(firebaseAuth.currentUser).thenReturn(null)

    // Update the ViewModel state directly (without calling setContent again)
    userViewModel._user.value = null
    userViewModel._isLoading.value = false

    // Assert: Check that the "noUserData" prompt is displayed
    composeTestRule.onNodeWithTag("noUserData").assertIsDisplayed()
  }

  @Test
  fun displayUserProfileWhenUserIsLoggedIn() {
    // Arrange: Mock a logged-in user
    val user = User("123", "Jane Doe", "jane@example.com")
    userViewModel._user.value = user
    userViewModel._isLoading.value = false

    // Assert: Check that the user profile information is displayed
    composeTestRule.onNodeWithTag("userName").assertIsDisplayed()
    composeTestRule.onNodeWithTag("userEmail").assertIsDisplayed()
  }

  @Test
  fun logoutButtonIsDisplayedAndCallsLogoutAction() {
    // Arrange: Mock a logged-in user
    val user = User("123", "Jane Doe", "jane@example.com")
    userViewModel._user.value = user
    userViewModel._isLoading.value = false

    // Assert: Check that the logout button is displayed
    composeTestRule.onNodeWithTag("signOutButton").assertIsDisplayed()

    // Act: Perform click on logout button
    composeTestRule.onNodeWithTag("signOutButton").performClick()
  }

  @Test
  fun displayLoadingIndicatorWhenIsLoading() {
    // Arrange: Set isLoading to true
    userViewModel._isLoading.value = true
    userViewModel._user.value = null

    // Assert: Check that the loading indicator is displayed
    composeTestRule.onNodeWithTag("loadingIndicator").assertIsDisplayed()
  }

  @Test
  fun displayDefaultProfilePictureWhenNoProfilePicture() {
    // Arrange: Mock a user without a profile picture
    val user = User("123", "Jane Doe", "jane@example.com", "")
    userViewModel._user.value = user
    userViewModel._isLoading.value = false

    // Assert: Check that the default profile picture is displayed
    composeTestRule.onNodeWithTag("defaultProfilePicture").assertIsDisplayed()
  }

  @Test
  fun displayProfilePictureWhenUserHasProfilePicture() {
    // Arrange: Mock a user with a profile picture
    val user = User("123", "Jane Doe", "jane@example.com", "http://example.com/profile.jpg")
    userViewModel._user.value = user
    userViewModel._isLoading.value = false

    // Assert: Check that the profile picture is displayed
    composeTestRule.onNodeWithTag("profilePicture").assertIsDisplayed()
  }

  @Test
  fun handleEmptyNameAndEmailGracefully() {
    // Arrange: Mock a user with empty name and email
    val user = User("123", "", "")
    userViewModel._user.value = user
    userViewModel._isLoading.value = false

    // Assert: Check that the placeholders for name and email are displayed
    composeTestRule.onNodeWithTag("userName").assertIsDisplayed()
    composeTestRule.onNodeWithTag("userEmail").assertIsDisplayed()
  }

  @Test
  fun signOutTriggersSignOutActionAndNavigatesToAuth() {
    // Arrange: Mock the user to simulate a logged-in state
    val user = User("123", "Jane Doe", "jane@example.com")
    userViewModel._user.value = user
    userViewModel._isLoading.value = false
    // Perform the sign-out action
    composeTestRule.onNodeWithTag("signOutButton").performClick()

    // Simulate the sign-out and check if the navigation happens
    composeTestRule.runOnUiThread {
      // Mock the effect of signing out, including navigating to the Auth screen
      userViewModel._user.value = null
      userViewModel._isLoading.value = false
      verify(navigationActions).navigateTo(Route.AUTH)
    }

    // Assert: Verify that the navigation was triggered
    verify(navigationActions).navigateTo(Route.AUTH)
  }
}

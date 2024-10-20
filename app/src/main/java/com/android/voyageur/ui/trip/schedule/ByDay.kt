package com.android.voyageur.ui.trip.schedule

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.android.voyageur.model.trip.TripsViewModel
import com.android.voyageur.ui.navigation.BottomNavigationMenu
import com.android.voyageur.ui.navigation.LIST_TRIP_LEVEL_DESTINATION
import com.android.voyageur.ui.navigation.NavigationActions
import com.android.voyageur.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ByDayScreen(
    tripsViewModel: TripsViewModel,
    navigationActions: NavigationActions,
) {
  val trip =
      tripsViewModel.selectedTrip.collectAsState().value
          ?: return Text(text = "No ToDo selected. Should not happen", color = Color.Red)
  Scaffold(
      // TODO: Final implementation of ByDayScreen
      modifier = Modifier.testTag("byDayScreen"),
      topBar = {
        TopAppBar(
            modifier = Modifier.testTag("topBar"),
            title = { Text("Schedule ByDay:") },
            navigationIcon = {
              IconButton(
                  modifier = Modifier.testTag("backToOverviewButton"),
                  onClick = { navigationActions.navigateTo(Screen.OVERVIEW) }) {
                    Icon(imageVector = Icons.Outlined.Home, contentDescription = "Home")
                  }
            })
      },
      bottomBar = {
        BottomNavigationMenu(
            onTabSelect = { route -> navigationActions.navigateTo(route) },
            tabList = LIST_TRIP_LEVEL_DESTINATION,
            selectedItem = navigationActions.currentRoute())
      },
      content = { pd ->
        Text(
            modifier = Modifier.padding(pd).testTag("emptyByDayPrompt"),
            text =
                "You're viewing the ByDay screen for ${trip.name}, but it's not implemented yet.")
      })
}

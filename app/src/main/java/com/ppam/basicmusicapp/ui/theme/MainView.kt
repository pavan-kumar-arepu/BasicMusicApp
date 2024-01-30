package com.ppam.basicmusicapp.ui.theme

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ppam.basicmusicapp.DrawerItem
import com.ppam.basicmusicapp.screenInDrawer
import kotlinx.coroutines.CoroutineScope
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.primarySurface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ppam.basicmusicapp.MainViewModel
import com.ppam.basicmusicapp.R
import com.ppam.basicmusicapp.Screen
import com.ppam.basicmusicapp.ScreenInBottom
import com.ppam.basicmusicapp.ui.theme.Navigation
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainView() {

    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope: CoroutineScope = rememberCoroutineScope()
    val viewModel: MainViewModel = viewModel()

    // Allow us to find out which "view",we current are..
    val controller: NavController = rememberNavController()
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val dialogOpen = remember {
        mutableStateOf(false)
    }
    val currentScreen = remember {
        viewModel.currentScreen.value
    }

    val title = remember {
        // Helps to change the currentScreen.title
        mutableStateOf(currentScreen.title)
    }

    val isSheetFullScreen by remember {
        mutableStateOf(false)
    }

    val modalSheetState = androidx.compose.material.rememberModalBottomSheetState(
        initialValue =  ModalBottomSheetValue.Hidden,
        confirmValueChange = {
                             it != ModalBottomSheetValue.HalfExpanded })

    val roundedCornerRadius = if(isSheetFullScreen) 0.dp else 12.dp

    val modifier = if(isSheetFullScreen) Modifier.fillMaxSize() else Modifier.fillMaxWidth()

    val bottomBar: @Composable () -> Unit = {
        if(currentScreen is Screen.DrawerScreen || currentScreen == Screen.BottomScreen.Home) {
            BottomNavigation(Modifier.wrapContentSize()) {
                ScreenInBottom.forEach{
                    item ->
                    val isSelected = currentRoute == item.bRoute
                    Log.d("Navigation", "Item: ${item.bTitle}, Current Route: $currentRoute, Is Selected: $isSelected")
                    val tint = if(isSelected) Color.White else Color.Black

                    BottomNavigationItem(selected = currentRoute == item.bRoute,
                        onClick = { controller.navigate(item.bRoute) }, icon = {
                        Icon(
                            painterResource(id = item.icon), contentDescription = item.bTitle, tint = tint)
                    },
                        label = { Text(text = item.bTitle, color = tint)},
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.Black
                    )
                }
            }
        }
    }


    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = roundedCornerRadius, topEnd = roundedCornerRadius),
        sheetContent = {
        MoreBottonSheet(modifier = modifier)
    }) {
        Scaffold (
            bottomBar = bottomBar,
            topBar = {
                TopAppBar(title = { Text(text = title.value) },
                    actions = {
                              IconButton(
                                  onClick = {
                                      scope.launch {
                                          if(modalSheetState.isVisible)
                                              modalSheetState.hide()
                                          else
                                              modalSheetState.show()
                                      }
                                  }
                              ){
                                  Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                              }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            /*TODO Open the drawer*/
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }) {
                            Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Menu")
                        }
                    }
                )
            }, scaffoldState = scaffoldState,
            drawerContent = {
                LazyColumn(Modifier.padding(16.dp)) {
                    items(screenInDrawer) {
                            item ->
                        DrawerItem(selected = currentRoute == item.dRoute, item = item) {
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                            if(item.dRoute == "add_account") {
                                // Open Dialog
                                dialogOpen.value = true
                            } else {
                                controller.navigate(item.dRoute)
                                title.value = item.dTitle
                            }
                        }
                    }
                }
            }
        ) {
            Navigation(navController = controller, viewModel = viewModel, pd = it)
            AccountDialog(dialogOpen = dialogOpen)
        }
    }
}

@Composable
fun MoreBottonSheet(modifier: Modifier) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(
                MaterialTheme.colors.primarySurface
            )
    ) {
       Column(modifier = Modifier.padding(16.dp),
           verticalArrangement = Arrangement.SpaceBetween) {
           Row(modifier = Modifier.padding(end = 16.dp)) {
               Icon(modifier = Modifier.padding(end = 8.dp),
                   painter = painterResource(id = R.drawable.baseline_settings_24),
                   contentDescription = "Settings")
               Text("Settings", fontSize = 20.sp, color = Color.White)
           }
           Row(modifier = Modifier.padding(end = 16.dp)) {
               Icon(modifier = Modifier.padding(end = 8.dp),
                   painter = painterResource(id = R.drawable.baseline_ios_share_24),
                   contentDescription = "Share")
               Text("Share", fontSize = 20.sp, color = Color.White)
           }
           Row(modifier = Modifier.padding(end = 16.dp)) {
               Icon(modifier = Modifier.padding(end = 8.dp),
                   painter = painterResource(id = R.drawable.baseline_help_24),
                   contentDescription = "Help")
               Text("Help", fontSize = 20.sp, color = Color.White)
           }
       }
    }
}
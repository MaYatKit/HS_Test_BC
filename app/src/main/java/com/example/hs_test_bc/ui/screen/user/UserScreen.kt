package com.example.hs_test_bc.ui.screen.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.hs_test_bc.domain.model.User
import com.example.hs_test_bc.ui.AuthViewModel
import com.example.hs_test_bc.ui.view.ErrorView
import com.example.hs_test_bc.ui.view.LoadingView
import com.example.hs_test_bc.ui.view.RepositoryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    goToRepository: (owner: String, repo: String) -> Unit,
    goToLogin: () -> Unit,
    goBack: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel(),
    viewModel: UserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadUserProfile()
    }

    LaunchedEffect(key1 = uiState) {
        if (uiState is ProfileUiState.LoggedOut) {
            goToLogin()
        }
    }

    LaunchedEffect(key1 = isLoggedIn) {
        if (!isLoggedIn) {
            goToLogin()
        }
    }

    val showLogoutDialog = remember { mutableStateOf(false) }

    if (showLogoutDialog.value) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog.value = false },
            title = { Text("Confirm Logout") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog.value = false
                        authViewModel.logout()
                    }
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog.value = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User") },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is ProfileUiState.Loading -> {
                    LoadingView()
                }

                is ProfileUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            UserHeader(
                                user = state.user,
                                logOut = {
                                    showLogoutDialog.value = true
                                }
                            )

                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),)

                            Text(
                                text = "Repositories",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }

                        items(state.repositories) { repo ->
                            RepositoryItem(
                                repository = repo,
                                onClick = {
                                    goToRepository(repo.owner.login, repo.name)
                                }
                            )
                        }

                        if (state.hasMoreRepos) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                    LaunchedEffect(Unit) {
                                        viewModel.loadMoreRepositories()
                                    }
                                }
                            }
                        }
                    }
                }

                is ProfileUiState.Error -> {
                    ErrorView(
                        message = state.message,
                        onRetry = { viewModel.loadUserProfile() }
                    )
                }

                is ProfileUiState.LoggedOut -> {}
            }
        }
    }
}

@Composable
fun UserHeader(
    user: User,
    logOut: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = user.avatarUrl,
            contentDescription = "User avatar",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = user.login,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(onClick = logOut) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "Log Out"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Log out")
        }
    }
}
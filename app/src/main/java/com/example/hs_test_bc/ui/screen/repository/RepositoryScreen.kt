package com.example.hs_test_bc.ui.screen.repository

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.hs_test_bc.domain.model.Repository
import com.example.hs_test_bc.ui.view.ErrorView
import com.example.hs_test_bc.ui.view.LanguageBadge
import com.example.hs_test_bc.ui.view.LoadingView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryScreen(
    owner: String,
    repo: String,
    goToCreateIssue: (owner: String, repo: String) -> Unit = { _, _ -> },
    goBack: () -> Unit,
    viewModel: RepositoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = owner, key2 = repo) {
        viewModel.loadRepository(owner, repo)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$owner/$repo") },
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
                is RepositoryUiState.Loading -> {
                    LoadingView()
                }

                is RepositoryUiState.Success -> {
                    RepositoryContent(
                        repository = state.repository,
                        goToCreateIssue = { goToCreateIssue(owner, repo) }
                    )
                }

                is RepositoryUiState.Error -> {
                    ErrorView(
                        message = state.message,
                        onRetry = { viewModel.loadRepository(owner, repo) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RepositoryContent(
    repository: Repository,
    goToCreateIssue:() -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = repository.owner.avatarUrl,
                contentDescription = "Owner avatar",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = repository.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "by ${repository.owner.login}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        repository.description?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    disabledContainerColor = Color.White,
                )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Repository Stats",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(
                        icon = Icons.Default.Star,
                        label = "Stars",
                        value = repository.starsCount.toString()
                    )

                    StatItem(
                        icon = Icons.Default.Send,
                        label = "Forks",
                        value = repository.forksCount.toString()
                    )

                    StatItem(
                        icon = Icons.Default.Edit,
                        label = "Issues",
                        value = repository.issuesCount.toString()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Language: ",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            if (repository.language != null) {
                LanguageBadge(language = repository.language)
            } else {
                Text(
                    text = "Not specified",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { goToCreateIssue() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create an Issue"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "New Issue")
        }


    }
}

@Composable
fun StatItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
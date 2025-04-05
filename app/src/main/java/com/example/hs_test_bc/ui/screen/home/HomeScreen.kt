package com.example.hs_test_bc.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hs_test_bc.domain.model.Repository
import com.example.hs_test_bc.ui.view.ErrorView
import com.example.hs_test_bc.ui.view.LoadingView
import com.example.hs_test_bc.ui.view.RepositoryItem
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    goToSearch: () -> Unit = {},
    goToRepository: (owner: String, repo: String) -> Unit = { _,_ -> },
    goToLogin: () -> Unit = {},
    goToUser: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val uiState by viewModel.uiState.collectAsState()
    val refreshing by viewModel.refreshing.collectAsState()
    val loadingMore by viewModel.loadingMore.collectAsState()

    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow {
            if (uiState is HomeUiState.Success) {
                val layoutInfo = listState.layoutInfo
                val totalItemNumber = layoutInfo.totalItemsCount
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

                lastVisibleItemIndex >= totalItemNumber - 5
            } else false
        }.collectLatest { shouldLoadMore ->
            if (shouldLoadMore && uiState is HomeUiState.Success &&
                (uiState as HomeUiState.Success).hasMoreData && !loadingMore
            ) {
                viewModel.loadNextPage()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "HS_Test_BC") },
                actions = {
                    IconButton(onClick = goToSearch) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )
        }, snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            isRefreshing = refreshing,
            onRefresh = { viewModel.refresh() }
        ) {
            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    LoadingView()
                }

                is HomeUiState.Success -> {
                    RepositoriesList(
                        repositories = state.repositories,
                        listState = listState,
                        isLoadingMore = loadingMore,
                        hasMoreData = state.hasMoreData,
                        onItemClick = goToRepository
                    )
                }

                is HomeUiState.Empty -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(100.dp))
                            Text(
                                text = "No trending repositories found",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(16.dp)
                            )
                            Button(
                                onClick = { viewModel.refresh() },
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text("Refresh")
                            }
                        }
                    }
                }

                is HomeUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ErrorView(
                            message = state.message,
                            onRetry = { viewModel.loadPopularRepositories(initialLoad = true) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RepositoriesList(
    repositories: List<Repository>,
    listState: LazyListState,
    isLoadingMore: Boolean,
    hasMoreData: Boolean,
    onItemClick: (owner: String, repo: String) -> Unit = { _, _ -> }
) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = repositories,
            key = { repo -> repo.id }
        ) { repository ->
            RepositoryItem(
                repository = repository,
                onClick = {
                    onItemClick(
                        repository.owner.login,
                        repository.name
                    )
                }
            )
        }

        if (hasMoreData && isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
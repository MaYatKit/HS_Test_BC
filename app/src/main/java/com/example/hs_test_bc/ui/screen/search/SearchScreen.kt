package com.example.hs_test_bc.ui.screen.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hs_test_bc.ui.view.ErrorView
import com.example.hs_test_bc.ui.view.LoadingView
import com.example.hs_test_bc.ui.view.RepositoryItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    goToRepository: (owner: String, repo: String) -> Unit,
    goBack: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf<String?>(null) }

    val languageOptions = listOf(
        "All" to null,
        "C#" to "c#",
        "Java" to "java",
        "Python" to "python",
        "JavaScript" to "javascript",
        "Kotlin" to "kotlin"
    )

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(Unit) {
        viewModel.scrollToTopEvent.collect {
            coroutineScope.launch {
//                delay(100)
                listState.requestScrollToItem(0)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Repositories") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .focusRequester(focusRequester),
                placeholder = { Text("Search repositories...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (searchQuery.isNotEmpty()) {
                            viewModel.searchRepositories(searchQuery, selectedLanguage)
                            focusManager.clearFocus()
                        }
                    }
                )
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(languageOptions) { (name, value) ->
                    FilterChip(
                        modifier = Modifier.height(30.dp),
                        selected = selectedLanguage == value,
                        onClick = {
                            selectedLanguage = value
                            if (searchQuery.isNotEmpty()) {
                                viewModel.searchRepositories(searchQuery, value)
                            }
                        },
                        label = { Text(name) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (val state = uiState) {
                is SearchUiState.Initial -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Search for GitHub repositories",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                is SearchUiState.Loading -> {
                    LoadingView()
                }

                is SearchUiState.Empty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No repositories found",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                is SearchUiState.Success -> {
                    Column {
                        Text(
                            text = "Found ${state.totalCount} repositories",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.repositories) { repo ->
                                RepositoryItem(
                                    repository = repo,
                                    onClick = {
                                        goToRepository(repo.owner.login, repo.name)
                                    }
                                )
                            }

                            item {
                                if (state.hasMoreData) {
                                    LaunchedEffect(Unit) {
                                        viewModel.loadNextPage()
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                is SearchUiState.Error -> {
                    ErrorView(
                        message = state.message,
                        onRetry = {
                            viewModel.searchRepositories(searchQuery, selectedLanguage)
                        }
                    )
                }
            }
        }
    }
}
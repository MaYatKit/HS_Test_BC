package com.example.hs_test_bc.ui.screen.issue

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hs_test_bc.ui.view.LoadingView
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateIssueScreen(
    owner: String,
    repo: String,
    navigateBack: () -> Unit,
    viewModel: CreateIssueViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = uiState) {
        if (uiState is CreateIssueUiState.Success) {
            snackBarHostState.showSnackbar("Issue created successfully!")
            delay(1000)
            navigateBack()
        } else if (uiState is CreateIssueUiState.Error) {
            snackBarHostState.showSnackbar((uiState as CreateIssueUiState.Error).message)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Issue") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.createIssue(owner, repo, title, body)
                        },
                        enabled = title.isNotBlank() && uiState !is CreateIssueUiState.Loading
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Create Issue"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState is CreateIssueUiState.Loading) {
                LoadingView()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Repository: $owner/$repo",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        placeholder = { Text("Enter issue title") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = uiState is CreateIssueUiState.Error && title.isBlank(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true
                    )

                    if (uiState is CreateIssueUiState.Error && title.isBlank()) {
                        Text(
                            text = "Title is required",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = body,
                        onValueChange = { body = it },
                        label = { Text("Description") },
                        placeholder = { Text("Describe the issue in detail") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            viewModel.createIssue(owner, repo, title, body)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = title.isNotBlank() && uiState !is CreateIssueUiState.Loading
                    ) {
                        Text("Create Issue")
                    }
                }
            }
        }
    }
}
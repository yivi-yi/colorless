package com.colorless.app.ui.chat

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(viewModel: ChatViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val listState = rememberLazyListState()
    val active = state.active

    var windowMenu by remember { mutableStateOf(false) }
    var modelMenu by remember { mutableStateOf(false) }
    var plusMenu by remember { mutableStateOf(false) }
    var hint by remember { mutableStateOf("") }

    val messages = active?.messages.orEmpty()

    LaunchedEffect(state.activeId, messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.lastIndex)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                conversations = state.ordered,
                activeId = state.activeId,
                onSelect = {
                    viewModel.selectConversation(it)
                    scope.launch { drawerState.close() }
                },
                onNew = {
                    viewModel.newConversation()
                    scope.launch { drawerState.close() }
                },
                onOpenSettings = {
                    scope.launch { drawerState.close() }
                    hint = "设置还没做"
                },
                onEntry = { label ->
                    scope.launch { drawerState.close() }
                    hint = "$label 还没做"
                },
            )
        },
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Rounded.Menu, contentDescription = "窗口列表")
                        }
                    },
                    title = {
                        Text(
                            text = active?.title ?: "无色",
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    actions = {
                        IconButton(onClick = { viewModel.newConversation() }) {
                            Icon(Icons.Rounded.Add, contentDescription = "开新窗口")
                        }
                        Box {
                            IconButton(onClick = { windowMenu = true }) {
                                Icon(Icons.Rounded.MoreVert, contentDescription = "窗口设置")
                            }
                            DropdownMenu(
                                expanded = windowMenu,
                                onDismissRequest = { windowMenu = false },
                            ) {
                                DropdownMenuItem(
                                    text = { Text(if (active?.pinned == true) "取消置顶" else "置顶窗口") },
                                    onClick = {
                                        viewModel.togglePin()
                                        windowMenu = false
                                    },
                                )
                                DropdownMenuItem(
                                    text = { Text("窗口设置") },
                                    onClick = {
                                        windowMenu = false
                                        hint = "窗口设置还没做"
                                    },
                                )
                                DropdownMenuItem(
                                    text = { Text("删除窗口") },
                                    onClick = {
                                        viewModel.deleteActive()
                                        windowMenu = false
                                    },
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                        navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                        actionIconContentColor = MaterialTheme.colorScheme.onBackground,
                    ),
                )
            },
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                if (messages.isEmpty()) {
                    EmptyHint(Modifier.weight(1f))
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 8.dp),
                    ) {
                        items(messages, key = { it.id }) { message ->
                            MessageItem(message)
                        }
                    }
                }

                if (hint.isNotEmpty()) {
                    Text(
                        text = hint,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }

                InputBar(
                    value = state.input,
                    onValueChange = viewModel::onInputChange,
                    onSend = viewModel::send,
                    canSend = state.input.isNotBlank() && !state.sending,
                    modelName = state.model.ifEmpty { "选模型" },
                    models = state.models,
                    modelMenuOpen = modelMenu,
                    onModelMenuOpenChange = { modelMenu = it },
                    onPickModel = {
                        viewModel.pickModel(it)
                        modelMenu = false
                    },
                    webSearch = state.webSearch,
                    onToggleWebSearch = viewModel::toggleWebSearch,
                    plusMenuOpen = plusMenu,
                    onPlusMenuOpenChange = { plusMenu = it },
                    onPlusPick = {
                        plusMenu = false
                        hint = "$it 还没做"
                    },
                )
            }
        }
    }
}

@Composable
private fun EmptyHint(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(
            modifier = Modifier
                .size(28.dp)
                .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
        )
        Spacer(Modifier.height(14.dp))
        Text(
            "它还没有颜色",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "先去设置里填一个模型站点",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
        )
    }
}

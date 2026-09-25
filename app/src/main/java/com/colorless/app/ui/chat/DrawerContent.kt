package com.colorless.app.ui.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Hub
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SmartToy
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.colorless.app.model.Conversation
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 侧边栏：上小下大。
 * 上：时间日期 + 头像名字 + 四个入口（模型 / 个性化 / 记忆 / MCP）。
 * 下：窗口列表；最下一条是设置。
 *
 * 除了"选窗口"，其它全是占位，点了只会出来一句"还没做"。
 */
@Composable
fun DrawerContent(
    conversations: List<Conversation>,
    activeId: Long,
    onSelect: (Long) -> Unit,
    onNew: () -> Unit,
    onEntry: (String) -> Unit,
    onOpenSettings: () -> Unit,
) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        drawerContentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 12.dp),
        ) {
            Spacer(Modifier.height(8.dp))
            TopBlock(onEntry)
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "窗口",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onNew, modifier = Modifier.size(28.dp)) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = "新窗口",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
            Spacer(Modifier.height(4.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(conversations, key = { it.id }) { c ->
                    val here = c.id == activeId
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (here) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
                        border = if (here) BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(c.id) },
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (c.pinned) {
                                Icon(
                                    Icons.Rounded.PushPin,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                                Spacer(Modifier.width(6.dp))
                            }
                            Text(
                                c.title,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline)

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable(onClick = onOpenSettings),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Rounded.Settings,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.width(10.dp))
                    Text("设置", style = MaterialTheme.typography.bodyLarge)
                }
            }
            Spacer(Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun TopBlock(onEntry: (String) -> Unit) {
    // 小时间 + 日期，左上角
    var now by remember { mutableStateOf(LocalDateTime.now()) }
    LaunchedEffect(Unit) {
        while (true) {
            now = LocalDateTime.now()
            delay(15_000)
        }
    }
    val timeText = remember(now) { now.format(DateTimeFormatter.ofPattern("HH:mm")) }
    val dateText = remember(now) { "${now.monthValue}月${now.dayOfMonth}日 ${weekday(now)}" }

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                timeText,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                dateText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
            )

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(38.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            "我",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("我", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        "还没设置名字",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // 四个入口：两行两列
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                EntryTile("模型", Icons.Rounded.SmartToy, Modifier.weight(1f)) { onEntry("模型") }
                EntryTile("个性化", Icons.Rounded.Palette, Modifier.weight(1f)) { onEntry("个性化") }
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                EntryTile("记忆", Icons.Rounded.Bookmarks, Modifier.weight(1f)) { onEntry("记忆") }
                EntryTile("MCP", Icons.Rounded.Hub, Modifier.weight(1f)) { onEntry("MCP") }
            }
        }
    }
}

@Composable
private fun EntryTile(
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = modifier.clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.width(6.dp))
            Text(label, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
        }
    }
}

private fun weekday(now: LocalDateTime): String = when (now.dayOfWeek.value) {
    1 -> "周一"
    2 -> "周二"
    3 -> "周三"
    4 -> "周四"
    5 -> "周五"
    6 -> "周六"
    else -> "周日"
}

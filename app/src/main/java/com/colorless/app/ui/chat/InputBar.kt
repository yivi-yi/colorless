package com.colorless.app.ui.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * 底栏：一张悬浮的长方形卡片，有描边、有阴影。
 * 左：选模型 + 联网开关；中：输入框；右：加号 + 发送。
 */
@Composable
fun InputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    canSend: Boolean,
    modelName: String,
    models: List<String>,
    modelMenuOpen: Boolean,
    onModelMenuOpenChange: (Boolean) -> Unit,
    onPickModel: (String) -> Unit,
    webSearch: Boolean,
    onToggleWebSearch: () -> Unit,
    plusMenuOpen: Boolean,
    onPlusMenuOpenChange: (Boolean) -> Unit,
    onPlusPick: (String) -> Unit,
) {
    val shape = RoundedCornerShape(22.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Surface(
            shape = shape,
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, shape),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                // 左：模型
                Box {
                    Chip(
                        text = modelName,
                        onClick = { onModelMenuOpenChange(true) },
                        trailing = Icons.Rounded.ArrowDropDown,
                    )
                    DropdownMenu(
                        expanded = modelMenuOpen,
                        onDismissRequest = { onModelMenuOpenChange(false) },
                    ) {
                        if (models.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("还没配站点") },
                                onClick = { onModelMenuOpenChange(false) },
                            )
                        } else {
                            models.forEach { m ->
                                DropdownMenuItem(
                                    text = { Text(m) },
                                    onClick = { onPickModel(m) },
                                )
                            }
                        }
                    }
                }

                // 左：联网
                IconChip(
                    icon = Icons.Rounded.Public,
                    active = webSearch,
                    onClick = onToggleWebSearch,
                )

                // 中：输入
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 38.dp)
                        .padding(vertical = 9.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 5,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        decorationBox = { inner ->
                            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                                if (value.isEmpty()) {
                                    Text(
                                        "说点什么",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.outline,
                                    )
                                }
                                inner()
                            }
                        },
                    )
                }

                // 右：加号
                Box {
                    IconButton(
                        onClick = { onPlusMenuOpenChange(true) },
                        modifier = Modifier.size(38.dp),
                    ) {
                        Icon(
                            Icons.Rounded.Add,
                            contentDescription = "更多",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    DropdownMenu(
                        expanded = plusMenuOpen,
                        onDismissRequest = { onPlusMenuOpenChange(false) },
                    ) {
                        PlusItem("图片", Icons.Rounded.Image) { onPlusPick("图片") }
                        PlusItem("文件", Icons.Rounded.AttachFile) { onPlusPick("文件") }
                        PlusItem("记忆", Icons.Rounded.Bookmarks) { onPlusPick("记忆") }
                    }
                }

                // 右：发送
                FilledIconButton(
                    onClick = onSend,
                    enabled = canSend,
                    modifier = Modifier.size(38.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.outline,
                    ),
                ) {
                    Icon(
                        Icons.Rounded.ArrowUpward,
                        contentDescription = "发送",
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun Chip(
    text: String,
    onClick: () -> Unit,
    trailing: ImageVector? = null,
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(start = 10.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.widthIn(max = 80.dp),
            )
            if (trailing != null) {
                Icon(
                    trailing,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun IconChip(
    icon: ImageVector,
    active: Boolean,
    onClick: () -> Unit,
) {
    val tint = if (active) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (active) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        border = BorderStroke(
            1.dp,
            if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
        ),
        modifier = Modifier.clickable(onClick = onClick),
    ) {
        Icon(
            icon,
            contentDescription = "联网",
            modifier = Modifier
                .padding(8.dp)
                .size(16.dp),
            tint = tint,
        )
    }
}

@Composable
private fun PlusItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    DropdownMenuItem(
        text = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp)) },
        onClick = onClick,
    )
}

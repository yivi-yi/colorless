package com.colorless.app.ui.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.colorless.app.model.ChatMessage
import com.colorless.app.model.Role

/**
 * 仿官方的样子：自己的话在右边一个浅色气泡，它的在左边不放气泡。
 * 不做头像、不做时间戳、不做状态 —— 那些以后再想。
 */
@Composable
fun MessageItem(message: ChatMessage, modifier: Modifier = Modifier) {
    when (message.role) {
        Role.USER -> Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.widthIn(max = 300.dp),
            ) {
                SelectionContainer {
                    Text(
                        text = message.text,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        Role.ASSISTANT -> SelectionContainer {
            Text(
                text = message.text,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

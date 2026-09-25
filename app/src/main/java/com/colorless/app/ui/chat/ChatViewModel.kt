package com.colorless.app.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colorless.app.model.ChatMessage
import com.colorless.app.model.Conversation
import com.colorless.app.model.Role
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatUiState(
    val conversations: List<Conversation> = emptyList(),
    val activeId: Long = 1L,
    val input: String = "",
    val sending: Boolean = false,
    /** 站点还没做，先留空；做出来之后这里就是"当前站点 + 当前模型" */
    val model: String = "",
    val models: List<String> = emptyList(),
    val webSearch: Boolean = false,
) {
    val active: Conversation?
        get() = conversations.firstOrNull { it.id == activeId }

    /** 置顶的在上面，其余按最近动过的排 */
    val ordered: List<Conversation>
        get() = conversations.sortedWith(
            compareByDescending<Conversation> { it.pinned }.thenByDescending { it.updatedAt }
        )
}

class ChatViewModel : ViewModel() {

    private val _state = MutableStateFlow(ChatUiState(conversations = listOf(Conversation(id = 1L))))
    val state: StateFlow<ChatUiState> = _state.asStateFlow()

    private var nextConvId = 2L
    private var nextMsgId = 1L

    fun onInputChange(text: String) {
        _state.update { it.copy(input = text) }
    }

    fun newConversation() {
        val id = nextConvId++
        _state.update {
            it.copy(conversations = it.conversations + Conversation(id = id), activeId = id, input = "")
        }
    }

    fun selectConversation(id: Long) {
        _state.update { it.copy(activeId = id, input = "") }
    }

    fun deleteActive() {
        val s = _state.value
        val rest = s.conversations.filterNot { it.id == s.activeId }
        if (rest.isEmpty()) {
            // 最后一个删掉了就补一个新的，不能没有窗口
            val fresh = Conversation(id = nextConvId++)
            _state.update { it.copy(conversations = listOf(fresh), activeId = fresh.id, input = "") }
        } else {
            _state.update { it.copy(conversations = rest, activeId = rest.last().id, input = "") }
        }
    }

    fun togglePin() {
        _state.update { s ->
            s.copy(
                conversations = s.conversations.map {
                    if (it.id == s.activeId) it.copy(pinned = !it.pinned) else it
                }
            )
        }
    }

    fun pickModel(name: String) {
        _state.update { it.copy(model = name) }
    }

    fun toggleWebSearch() {
        _state.update { it.copy(webSearch = !it.webSearch) }
    }

    fun send() {
        val s = _state.value
        val text = s.input.trim()
        if (text.isEmpty() || s.sending) return

        _state.update { st ->
            st.copy(
                input = "",
                conversations = st.conversations.map { c ->
                    if (c.id != st.activeId) c
                    else c.copy(
                        title = if (c.title == "新窗口") text.take(8) else c.title,
                        messages = c.messages + ChatMessage(nextMsgId++, Role.USER, text),
                        updatedAt = System.currentTimeMillis(),
                    )
                },
            )
        }

        // TODO 下一步：这里换成真的请求。现在只是把界面跑通。
        viewModelScope.launch {
            _state.update { it.copy(sending = true) }
            delay(300)
            _state.update { st ->
                st.copy(
                    sending = false,
                    conversations = st.conversations.map { c ->
                        if (c.id != st.activeId) c
                        else c.copy(
                            messages = c.messages + ChatMessage(
                                nextMsgId++, Role.ASSISTANT, "还没接模型。下一步先做站点。"
                            ),
                            updatedAt = System.currentTimeMillis(),
                        )
                    },
                )
            }
        }
    }
}

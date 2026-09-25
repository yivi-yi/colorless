package com.colorless.app.model

/**
 * 一个窗口 = 一段对话。
 *
 * 窗口名默认是「新窗口」，发第一句话之后自动取前几个字当名字（跟小行星一样），
 * 用户可以自己改（等设置页做出来）。
 */
data class Conversation(
    val id: Long,
    val title: String = "新窗口",
    val pinned: Boolean = false,
    val messages: List<ChatMessage> = emptyList(),
    val updatedAt: Long = System.currentTimeMillis(),
)

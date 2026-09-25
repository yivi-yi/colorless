package com.colorless.app.model

/** 一条消息只有这几种身份，别再加了。 */
enum class Role { USER, ASSISTANT }

/**
 * 对话里的一条。
 *
 * 规矩：**这里就是模型能看到的东西**，一比一。
 * 屏幕上不许出现库里没有的内容，也不许把库里的东西合并、去重、重排。
 * （小行星就是在这条上翻的车，记在 docs/小行星问题记录.md 里。）
 */
data class ChatMessage(
    val id: Long,
    val role: Role,
    val text: String,
    val time: Long = System.currentTimeMillis(),
)

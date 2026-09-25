package com.colorless.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

// 就改一处：正文行高给足，长段落才不挤。
val ColorlessType = Typography().let { base ->
    base.copy(
        bodyLarge = base.bodyLarge.copy(fontSize = 16.sp, lineHeight = 25.sp),
        titleLarge = TextStyle(fontSize = 18.sp),
    )
}

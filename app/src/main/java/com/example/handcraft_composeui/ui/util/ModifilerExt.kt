package com.example.handcraft_composeui.ui.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

/**
 * Modifier 扩展函数，提供无波纹点击效果
 * @param enabled 是否可点击，默认 true
 * @param onClickLabel 点击标签（用于无障碍访问）
 * @param onClick 点击回调函数
 * @return 带有无波纹点击效果的 Modifier
 */
@Composable
fun Modifier.clickNoRipple(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    onClick: () -> Unit
) : Modifier {
    return this.clickable(
        // 创建空的交互源，避免产生波纹
        interactionSource = remember { MutableInteractionSource() },
        // 设置为 null 表示不显示任何点击指示效果
        indication = null,
        enabled = enabled,
        onClickLabel = onClickLabel,
        onClick = onClick
    )
}
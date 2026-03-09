package com.zero.profile001.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 屏幕适配工具类
 * 用于将设计稿中的 px 值适配到不同屏幕尺寸
 * 设计稿宽度：1080px
 */

// 获取屏幕宽度（像素单位）
@Composable
private fun screenWidthPx(): Int {
    val context = LocalContext.current
    return context.resources.displayMetrics.widthPixels
}

// 获取屏幕宽度（dp 单位）
@Composable
private fun screenWidthDp() = LocalConfiguration.current.screenWidthDp

// 获取屏幕密度（dp 与 px 的转换比例）
@Composable
private fun density() = LocalDensity.current.density

// 设计稿宽度常量
private const val DesignWidth = 1080f

// 计算屏幕像素比例：实际屏幕宽度 / 设计稿宽度
@Composable
private fun ratioPx() = screenWidthPx() / DesignWidth

// 计算屏幕 dp 比例：实际屏幕宽度(dp) / 设计稿宽度
@Composable
private fun ratioDp() = screenWidthDp() / DesignWidth

// 将设计稿 px 值转换为当前屏幕的 dp 值（用于尺寸）
@Composable
fun Float.px() = (this * ratioPx() / density()).dp

@Composable
fun Int.px() = this.toFloat().px()

// 将设计稿 px 值直接转换为 dp（不进行密度转换）
@Composable
fun Float.realPx() = (this * ratioDp()).dp

@Composable
fun Int.realPx() = this.toFloat().realPx()

// 将设计稿 px 值转换为当前屏幕的 sp 值（用于文字大小）
@Composable
fun Float.textPx() = (this * ratioPx() / density()).sp

@Composable
fun Int.textPx() = this.toFloat().textPx()
package com.example.handcraft_composeui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import com.example.handcraft_composeui.ui.theme.HandCraft_ComposeUITheme
import com.example.handcraft_composeui.ui.util.clickNoRipple
import com.zero.profile001.ui.util.px
import com.zero.profile001.ui.util.textPx
import kotlin.math.roundToInt

// ============================================================================
// MainActivity
// ============================================================================
// 功能描述：主 Activity，应用的入口点
// 说明：继承自 ComponentActivity 以支持 Jetpack Compose UI 框架
// ============================================================================

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 启用边缘到边缘布局模式，让内容延伸到状态栏和导航栏下方
        enableEdgeToEdge()

        // 设置 Compose UI 内容
        setContent {
            // 应用自定义主题包装所有界面组件
            HandCraft_ComposeUITheme {
                // TODO: 在这里放置主界面内容
                // 目前留空，ProfileScreen 通过 Preview 独立预览
            }
        }
    }
}

// ============================================================================
// ProfileScreen - 个人中心主界面
// ============================================================================
// 功能描述：个人中心页面的主容器，包含背景、用户卡片、功能列表和底部导航
// 预览注解：showBackground = true 表示在预览时显示白色背景
// ============================================================================
// 组件结构：
//   - Box 容器（白色背景，填充整个屏幕）
//   - 背景图片层
//   - 用户卡片层
//   - 功能列表层
//   - 底部导航栏层
// ============================================================================

@Preview(showBackground = true)
@Composable
fun ProfileScreen(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(R.mipmap.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )

        UserCard()

        FeatureList()

        BottomBar()
    }
}

// ============================================================================
// BottomBar - 底部导航栏
// ============================================================================
// 功能描述：实现带动画指示器的底部导航栏，支持 4 个 Tab 的切换
// ============================================================================
// 实现要点：
//   - 使用 BoxWithConstraints 获取父容器约束，动态计算布局
//   - 指示器会根据选中项平滑滑动（通过 animateDpAsState 实现动画）
//   - 每个图标占据容器宽度的 1/4
//   - 容器位置：在父容器中底部居中，左右边距 92px，底部边距 73px
//   - 容器尺寸：宽度填满，高度固定 200px
//   - 圆角效果：50px 圆角，50px 阴影
//   - 指示器尺寸：宽度 63px，高度 8px
// ============================================================================

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BoxScope.BottomBar() {
    // --------------------------------------------------
    // 1. 定义导航图标列表
    // --------------------------------------------------
    val navTabs = remember {
        listOf(
            R.mipmap.nav_home,
            R.mipmap.nav_notification,
            R.mipmap.nav_favorite,
            R.mipmap.nav_mine,
        )
    }

    // --------------------------------------------------
    // 2. 定义状态变量
    // --------------------------------------------------
    var selectedIndex by remember { mutableIntStateOf(0) }
    val indicatorWidth = 63.px()
    val shape = RoundedCornerShape(50.px())

    // --------------------------------------------------
    // 3. 底部导航栏容器
    //    - 位置：底部居中，左右边距 92px，底部边距 73px
    //    - 尺寸：宽度填满，高度 200px
    //    - 视觉：50px 圆角，50px 阴影，白色背景
    // --------------------------------------------------
    BoxWithConstraints(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(horizontal = 92.px())
            .padding(bottom = 73.px())
            .fillMaxWidth()
            .height(200.px())
            .shadow(
                elevation = 50.px(),
                shape = shape
            )
            .clip(shape)
            .background(Color.White)
    ) {
        // --------------------------------------------------
        // 4. 计算布局参数
        // --------------------------------------------------
        val tabWidth = maxWidth / 4
        val targetOffset by animateDpAsState(
            targetValue = tabWidth * selectedIndex + tabWidth / 2 - indicatorWidth / 2
        )

        // --------------------------------------------------
        // 5. 导航按钮行
        // --------------------------------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            navTabs.fastForEachIndexed { index, tab ->
                TabButton(tab) {
                    selectedIndex = index
                }
            }
        }

        // --------------------------------------------------
        // 6. 选中指示器
        //    - 位置：底部跟随选中项，带动画平滑过渡
        //    - 尺寸：宽度 63px，高度 8px
        // --------------------------------------------------
        Image(
            painter = painterResource(R.mipmap.tab_indicator),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset { IntOffset(targetOffset.toPx().roundToInt(), 0) }
                .width(indicatorWidth)
                .height(8.px())
        )
    }
}

// ============================================================================
// TabButton - 单个导航按钮
// ============================================================================
// 功能描述：底部导航栏中的单个按钮组件
// ============================================================================
// 参数说明：
//   - tab: 图标资源 ID
//   - onClick: 点击回调函数
// 实现要点：
//   - 使用 RowScope 的 weight(1f) 均匀分配宽度
//   - 使用 clickNoRipple 扩展函数实现无波纹点击
//   - 图标居中显示，尺寸 76×76px
// ============================================================================

@Composable
fun RowScope.TabButton(tab: Int, onClick:()-> Unit){
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxSize()
            .clickNoRipple(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(tab),
            contentDescription = null,
            modifier = Modifier.size(76.px())
        )
    }
}

// ============================================================================
// UserCard - 用户信息卡片
// ============================================================================
// 功能描述：显示用户头像、昵称和用户 ID 的卡片组件
// ============================================================================
// 布局结构：
//   - Row 水平布局，垂直居中对齐，子元素间距 90px
//   - 左侧：用户头像（278×278px）
//   - 右侧：Column 垂直布局，子元素间距 16px
//     * 第一行：昵称（粗体，60px）+ 编辑图标（48×48px），底部对齐，间距 16px
//     * 第二行：用户 ID（42px，灰蓝色）
//   - 外边距：左右 92px，顶部 349px
// ============================================================================

@Composable
fun UserCard(){
    Row(
        modifier = Modifier
            .padding(horizontal = 92.px())
            .padding(top = 349.px())
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(90.px())
    ) {
        Image(
            painter = painterResource(R.mipmap.user_avatar),
            contentDescription = "用户头像",
            modifier = Modifier.size(278.px())
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.px())
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(16.px())
            ) {
                Text(
                    text = "舒欣小姐",
                    fontWeight = FontWeight.Bold,
                    fontSize = 60.textPx()
                )
                Image(
                    painter = painterResource(R.mipmap.icon_edit),
                    contentDescription = "编辑",
                    modifier = Modifier.size(48.px())
                )
            }

            Text(
                text = "ID:114514",
                fontSize = 42.textPx(),
                color = Color(0xFFA0A7BA)
            )
        }
    }
}

// ============================================================================
// Feature - 功能列表项数据类
// ============================================================================
// 功能描述：定义功能列表中单个项的数据结构
// ============================================================================
// 属性说明：
//   - icon: 功能图标的资源 ID（Int 类型）
//   - label: 功能名称（String 类型）
// ============================================================================

data class Feature(
    val icon: Int,
    val label: String
)

// ============================================================================
// FeatureList - 功能列表
// ============================================================================
// 功能描述：包含多个功能入口的列表组件
// ============================================================================
// 功能列表：
//   1. 我的 - icon_order
//   2. 记录 - icon_doc
//   3. 钱包 - icon_packet
//   4. 隐私 - icon_security
//   5. 帮助 - icon_question
//   6. 设置 - icon_setting
// 实现要点：
//   - 支持垂直滚动（当内容超出屏幕时）
//   - 每个功能项之间间距 80px
//   - 外边距：左右 92px，顶部 721px
// ============================================================================

@Composable
fun FeatureList(){
    val features = remember {
        listOf(
            Feature(R.mipmap.icon_order, "我的"),
            Feature(R.mipmap.icon_doc, "记录"),
            Feature(R.mipmap.icon_packet, "钱包"),
            Feature(R.mipmap.icon_security, "隐私"),
            Feature(R.mipmap.icon_question, "帮助"),
            Feature(R.mipmap.icon_setting, "设置"),
        )
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 92.px())
            .padding(top = 721.px())
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(80.px())
    ) {
        features.fastForEach { feature ->
            FeatureItem(feature)
        }
    }
}

// ============================================================================
// FeatureItem - 单个功能列表项
// ============================================================================
// 功能描述：功能列表中的单个可点击项
// ============================================================================
// 参数说明：
//   - feature: Feature 数据对象，包含图标和标签
// 布局结构：
//   - Row 水平布局，垂直居中对齐，子元素间距 38px
//   - 左侧：功能图标（136×136px）
//   - 中间：功能名称（半粗体，46px），占据剩余空间
//   - 右侧：箭头图标（48×48px）
// 交互效果：
//   - 点击时显示 Toast 提示，显示功能名称
//   - 使用 clickNoRipple 实现无波纹点击
// ============================================================================

@Composable
fun FeatureItem(feature: Feature) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxSize()
            .height(136.px())
            .clickNoRipple {
                Toast.makeText(context, feature.label, Toast.LENGTH_SHORT).show()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(38.px())
    ) {
        Image(
            painter = painterResource(feature.icon),
            contentDescription = feature.label,
            modifier = Modifier.size(136.px())
        )

        Text(
            text = feature.label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 46.textPx(),
            modifier = Modifier.weight(1f)
        )

        Image(
            painter = painterResource(R.mipmap.icon_right),
            contentDescription = "进入",
            modifier = Modifier.size(48.px())
        )
    }
}

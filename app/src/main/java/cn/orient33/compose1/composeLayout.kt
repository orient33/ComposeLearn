package cn.orient33.compose1

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch

const val IMG_URL = "https://img-s-msn-com.akamaized.net/tenant/amp/entityid/AAMKW3Y.img"


//图片加载库. (对比Glide) https://coil-kt.github.io/coil/ 用于kotlin下的androidx，
@Composable
fun PhotographerCard() {
    Column {
        Row(
            modifier = Modifier //注意顺序的影响
                .padding(15.dp)
                .clip(RoundedCornerShape(10.dp))
//                .background(MaterialTheme.colors.surface)
                .clickable {
                    Log.i("df", "clickable..happen.")
                }
                .fillMaxWidth()
                .padding(6.dp) //padding 与 clickable的顺序 影响了可点击区域是否包括padding
        ) {
            Surface(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.CenterVertically),
                shape = CircleShape,
//                color = MaterialTheme.colors.onSurface.copy(alpha = .2f)
            ) {                //image
                Image(
                    painter = rememberImagePainter(
                        data = IMG_URL
                    ),
                    "contentDescription"
                )
            }
            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text("Alfred Sisley", fontWeight = FontWeight.Bold)

                //隐式的传递数据给compose tree? TODO Color.luminance()?
//                CompositionLocalProvider(LocalContext provides Content) {
//                    Text("3 minutes ago", )//style = MaterialTheme.typography.bodyMedium)
//                }
            }
        }

        Image(
            painter = rememberImagePainter(data = IMG_URL),
            contentDescription = "",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )

        val s = rememberLazyListState()
        LazyColumn(state = s, modifier = Modifier.weight(1f)) {
            items(30) {
                OneRow(it)
            }
        }
        val cs = rememberCoroutineScope()//携程scope创建

        Row {
            Button(onClick = { cs.launch { s.animateScrollToItem(0) } }) {
                Text("scroll First")
            }
            Button(onClick = { cs.launch { s.animateScrollToItem(111) } }) {
                Text(text = "scroll Last")
            }
        }
//        SimpleList()
    }
}

@Composable
fun OneRow(index: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
//            .background(MaterialTheme.colors.surface)
            .clickable {}) {
        Image(
            painter = rememberImagePainter(data = "https://developer.android.google.cn/images/brand/Android_Robot.png"),
            contentDescription = "",
            modifier = Modifier.size(50.dp),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "item $index")
    }
}

@Composable
fun SimpleList() {
    val scrollState = rememberScrollState()
    Column(Modifier.verticalScroll(scrollState)) {
        repeat(100) {
            Text(text = "this is Item $it")
            Log.i("df", "render Text $it")
            //相当于 LinearLayout 竖向放了100个TextView，一次性加载出来。
        }
    }
}

//@Composable
//fun SampleScaffold() {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text("TopAppBar", modifier = Modifier.onSizeChanged { ii ->
//                        Log.i("df", "TopAppBar..Text.onSizeChanged. $ii")
//                    })
//                },
//                actions = {
//                    IconButton(onClick = { /*TODO*/ }) {
//                        Icon(Icons.Filled.Favorite, contentDescription = null)
//                    }
//                },
//            )
//        },
////        bottomBar = {
////        TextButton(onClick = {}) {
////            Text(
////                "bottomBar", modifier = Modifier
////                    .fillMaxWidth()
////                    .background(Color.Blue)
////            )
////        }
////    }
//    ) {
//        PhotographerCard()
//    }
//
//}

//7. 自定义布局 custom layout , Layout -> ViewGroup
// 不允许多次测量， 单词测量性能好，允许更深的层级
@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(content, modifier) { measurables, cons ->
        val p: List<Placeable> = measurables.map {
            it.measure(cons)
        }
        var yPos = 0
        layout(cons.maxWidth, cons.maxHeight) {
            p.forEach() {
                it.placeRelative(0, yPos)
                yPos += it.height
            }
        }
    }
}

//8. 主要在于每个子view的 xy计算
@Composable
fun MyStaggerGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(content, modifier) { meas, cons ->
        val rowWidths = IntArray(rows) { 0 }
        val rowHeights = IntArray(rows) { 0 }
        val p: List<Placeable> = meas.mapIndexed { index, m ->
            val p: Placeable = m.measure(cons)//测量后返回测量结果 用于layout
            val row = index % rows
            rowWidths[row] += p.width
            rowHeights[row] = rowHeights[row].coerceAtLeast(p.height)//same as Max(a,b)
            p
        }


        // Grid's width is the widest row
        val width = rowWidths.maxOrNull()?.coerceIn(
            cons.minWidth.rangeTo(cons.maxWidth)
        ) ?: cons.minWidth
        val height = rowHeights.sumOf { it }.coerceIn(cons.maxHeight.rangeTo(cons.maxHeight))

        // 计算每行的Y坐标
        val rowY = IntArray(rows) { 0 }
        for (ii in 1 until rows) {
            rowY[ii] = rowY[ii - 1] + rowHeights[ii - 1]
        }

        // Set the size of the parent layout
        layout(width, height) {
            val rowXX = IntArray(rows) { 0 }
            p.forEachIndexed { ii, plea ->
                val row = ii % rows
                plea.placeRelative(
                    rowXX[row], rowY[row]
                )
                rowXX[row] += plea.width
            }
        }
    }

}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp, 16.dp)
//                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = text)
        }
    }
}

val topics = listOf(
    "Arts & Crafts", "Beauty", "Book", "Business", "Comics", "Culinary",
    "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
    "Religion", "Social sciences", "Technology", "TV", "Writing"
)

@Composable
fun BodyContent8(modifier: Modifier = Modifier) {
    Column {
        Row(
            modifier = modifier
                .horizontalScroll(rememberScrollState())
                .fillMaxHeight(.5f)
        ) {
            MyStaggerGrid(modifier = modifier, rows = 5) {
                for (topic in topics) {
                    Chip(text = topic, modifier = Modifier.padding(4.dp))
                }
            }
        }
        TwoTexts(text1 = "One", text2 = "Two")
    }
}

//9.      modifier = modifier
//            .background(color = Color.LightGray, shape = RectangleShape)
//            .padding(16.dp)
//            .size(200.dp)
// padding 与size的先后顺序 影响最终大小是 200-2*16 还是 200+2*16 !!!

//10. View系统的约束布局是性能优化的有效手段，但对于compose，并不需要如此，compose因一次测量，并不关心layout层级

//11. 如何提前使用 宽高度。 intrinsicWidth/intrinsicHeight
@Composable
fun TwoTexts(modifier: Modifier = Modifier, text1: String, text2: String) {
    // 此 .height(IntrinsicSize.Min) 即为 child 的最小高度
    // 行的(满足约束)最小高度 等于 所有child 的约束最大高度， 所以行的height是Text的高度，(Divider无内容，不占空间)
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .clickable {  }
                .wrapContentWidth(Alignment.Start),
            text = text1
        )

        VerticalDivider(
            color = Color.Black, modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
                .wrapContentWidth(Alignment.End),
            text = text2
        )
    }
}

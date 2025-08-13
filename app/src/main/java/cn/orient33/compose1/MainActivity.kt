package cn.orient33.compose1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.orient33.compose1.ui.theme.Compose1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            Compose1Theme {
            // A surface container using the 'background' color from the theme
//                Surface(color = MaterialTheme.colors.background) {
//                    Greeting("Android")
//                }
            NewsStory("developer!")
//            }
        }
    }
}

@Composable
fun NewsStory(name: String) {
    Compose1Theme {
        val t = MaterialTheme.typography
        val bodyText = "hi, there! 配置文本元素，将长度上限设置为 2 行。如果文本很短，不超过此限制，则此设置没有影响；但如果文本过长，显示的文本就会被自动截短。"
        Column(Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.header),
                contentDescription = null,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .shadow(5.dp, shape = RectangleShape),
                contentScale = ContentScale.FillBounds
            )
            Spacer(Modifier.height(20.dp))
            Text(text = "Hello- $name )-(", style = t.h4)
            Text(
                text = bodyText,
                style = t.body1,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = "that's all!", style = t.body2)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NewsStory("Preview!")
}
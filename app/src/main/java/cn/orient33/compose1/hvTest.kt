package cn.orient33.compose1

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

//横竖屏 不同的布局
@Composable
fun Container(modifier: Modifier) {
    val r = LocalContext.current.display.rotation
    android.util.Log.i("df", "container r = $r, modify=$modifier")
    if (r == 0 || r == 2) {
        VContainer(modifier)
    } else {
        HContainer(modifier)
    }
}

@Composable
fun VContainer(modifier: Modifier) {
    Column(modifier) {
        Text("hello,1 ", modifier.padding(10.dp).fillMaxWidth())
        Text("hello,2 ", modifier.padding(10.dp).fillMaxWidth())
        Text("hello,3 ", modifier.padding(10.dp).fillMaxWidth())
    }
}

@Composable
fun HContainer(modifier: Modifier) {
    Column(modifier) {
        Row {
            Text("hello, 1 ", modifier.padding(10.dp))
            Text("hello, 2 ", modifier.padding(10.dp))
            Text("hello, 3 ", modifier.padding(10.dp))
        }
        Row {
            Text("hello, 4 ", modifier.padding(10.dp))
            Text("hello, 5 ", modifier.padding(10.dp))
            Text("hello, 6 ", modifier.padding(10.dp))
        }
    }

}

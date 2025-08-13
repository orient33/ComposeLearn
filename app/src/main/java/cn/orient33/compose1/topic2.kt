package cn.orient33.compose1

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//https://developer.android.com/codelabs/jetpack-compose-basics
@Composable
fun MyScreenContent(names: List<String> = List(50) { "Hello, list $it" }) {
    val counterState = remember {
        mutableStateOf(0)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .shadow(1.dp)
    ) {
        NameList(names = names, m = Modifier.weight(1f))
        Counter(counterState.value) {
            counterState.value = it
        }
    }
}

@Composable
fun NameList(names: List<String>, m: Modifier) {
    val stateList = mutableListOf<MutableState<Boolean>>()

    LazyColumn(m) {
        itemsIndexed(items = names) { index, name ->
            val state: MutableState<Boolean>
            if (index < stateList.size) {
                state = stateList[index]
            } else {
                state = remember { mutableStateOf(false) }
                stateList.add(state)
            }
            Greeting(name = name, state)
            Divider(color = Color.Blue)
        }
    }
}

//MyCounter的状态提升!! state hoist
@Composable
fun Counter(count: Int, countFun: (Int) -> Unit) {
    Button(
        onClick = { countFun(count + 1) },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (count > 10) Color.Red else Color.Green
        )
    ) {
        Text("I have been click $count times!", modifier = Modifier.padding(10.dp, 2.dp))
    }
}

@Composable
fun Greeting(name: String, state: MutableState<Boolean>) {
//    val select = remember { mutableStateOf(false) } //写法 和 下面的效果一样 但是否有区别呢？ TODO
//    var isSelect by remember { mutableStateOf(false) } // isSelect is Boolean, 上面是MutableState
    // by 关键词 需要导入  androidx.compose.runtime.getValue与setValue 否则by不生效. TODO??
    val bgColor by animateColorAsState(if (state.value) Color.Blue else Color.Transparent)

    Text(
        text = "Hi, $name", modifier = Modifier
            .padding(30.dp, 10.dp)
//        .fillMaxWidth(1f)
//        .fillMaxHeight(1f)
            .fillMaxSize()
            .background(bgColor)
//        .clickable { select.value = !select.value }
            .clickable { state.value = !state.value },
        style = MaterialTheme.typography.body1
    )
}
package com.synology.pinmenutest

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.synology.pinmenutest.ui.theme.PinMenuTestTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PinMenuTestTheme {
                val openState = remember { mutableStateOf(false) }
                var recordX by remember { mutableStateOf(0f) }
                var recordY by remember { mutableStateOf(0f) }
                var xRaw by remember { mutableStateOf(0f) }
                var yRaw by remember { mutableStateOf(0f) }
                val positionWindow = remember {
                    mutableStateOf(Offset.Zero)
                }
                val positionInRoot = remember {
                    mutableStateOf(Offset.Zero)
                }
                val size = remember {
                    mutableStateOf(IntSize.Zero)
                }

                val textColor = remember {
                    mutableStateOf(Color.White)
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column {
                            Spacer(modifier = Modifier.weight(1f))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp), horizontalArrangement = Arrangement.Center
                            ) {
                                Text(modifier = Modifier
                                    .height(50.dp)
                                    .width(300.dp)
                                    .background(Color.Gray)
                                    .pointerInteropFilter {
                                        Log.i("Teng", "it.action:${it.action}")
                                        when (it.action) {
                                            MotionEvent.ACTION_DOWN -> {
                                                openState.value = true
                                            }
                                            MotionEvent.ACTION_MOVE -> {
                                                recordX = it.x
                                                recordY = it.y
                                                xRaw = it.rawX
                                                yRaw = it.rawY

                                                if (it.inScopeOf(positionWindow, size)) {
                                                    textColor.value = Color.Red
                                                } else{
                                                    textColor.value = Color.White
                                                }
                                            }
                                            (MotionEvent.ACTION_MASK and MotionEvent.ACTION_UP) -> {
                                                if (it.inScopeOf(positionWindow, size)) {
                                                    Toast
                                                        .makeText(
                                                            this@MainActivity,
                                                            "Click success!",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                        .show()
                                                } else {
                                                    Toast
                                                        .makeText(
                                                            this@MainActivity,
                                                            "x:${it.rawX}, y:${it.rawY}",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                        .show()
                                                    Log.i(
                                                        "Teng",
                                                        "coordinates.positionInWindow(): $positionWindow, \ncoordinates.positionInRoot(): $positionInRoot \n coordinates.size:$size"
                                                    )
                                                }
                                                openState.value = false
                                            }
                                        }
                                        true
                                    }, text = "完了", textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    if (openState.value) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = Color.White.copy(alpha = 0.5f)
                        ) {
                            Column {
                                Text(text = "X= $recordX,  \ncoordinates.positionInWindow(): $positionWindow, \ncoordinates.positionInRoot(): $positionInRoot \n coordinates.size:$size")
                                Text(text = "Y= $recordY")
                                Text("xPrecision= $xRaw")
                                Text("yPrecision= $yRaw")
                                Row {
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(modifier = Modifier.onGloballyPositioned { coordinates ->
                                        // This will be the size of the Column.
                                        size.value = coordinates.size
                                        // The position of the Column relative to the application window.
                                        positionWindow.value = coordinates.positionInWindow()
                                        // The position of the Column relative to the Compose root.
                                        positionInRoot.value = coordinates.positionInRoot()
                                        // These will be the alignment lines provided to the layout (empty here for Column).
                                        coordinates.providedAlignmentLines
                                        // This will be a LayoutCoordinates instance corresponding to the parent of Column.
                                        coordinates.parentLayoutCoordinates
                                    }, text = "BBQ了", fontSize = 30.sp, color = textColor.value)
                                }
                                Button(onClick = { /*TODO*/ }) {
                                    Text(text = "T1")
                                }
                                Button(onClick = { /*TODO*/ }) {
                                    Text(text = "T2")
                                }
                                Button(onClick = { /*TODO*/ }) {
                                    Text(text = "T3")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun MotionEvent.inScopeOf(
    positionInRoot: MutableState<Offset>,
    size: MutableState<IntSize>
): Boolean {
    Log.i("Teng","rawX >= positionInRoot.value.x ${rawX >= positionInRoot.value.x}, rawX <= positionInRoot.value.x + size.value.width ${rawX <= positionInRoot.value.x + size.value.width}, " +
            "rawY >= positionInRoot.value.y ${rawY >= positionInRoot.value.y}, rawX <= positionInRoot.value.y + size.value.height ${rawY <= positionInRoot.value.y + size.value.height}")
    return (rawX >= positionInRoot.value.x && rawX <= positionInRoot.value.x + size.value.width &&
            rawY >= positionInRoot.value.y && rawY <= positionInRoot.value.y + size.value.height)
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PinMenuTestTheme {
        Greeting("Android")
    }
}
package com.nguyen.compose_basics2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nguyen.compose_basics2.ui.theme.ComposeBasics2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeBasics2Theme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier, names: List<String> = listOf("World", "Compose")) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        // use a for loop to add elements to the Column
        for (name in names) {
            Greeting(name = name)
        }
    }
}

@Composable
private fun Greeting(name: String) {
    // To add internal state to a composable, use the mutableStateOf function. State and
    // MutableState are interfaces that hold some value and trigger UI updates (recompositions)
    // whenever that value changes
    // To preserve state across recompositions, remember the mutable state using 'remember'
    val expanded = remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            // to place a composable (ElevatedButton) at the end of a Row: give some weight to the
            // composable at the start (Column)
            Column(modifier = Modifier
                .weight(1f)
                // expand an item when requested
                .padding(bottom = if (expanded.value) 48.dp else 0.dp)
            ) {
                Text(text = "Hello,")
                Text(text = name)
            }
            // Button is a composable provided by the material3 package which takes a composable as
            // the last argument.
            // toggle the value of the expanded state and show a different text depending on the value.
            ElevatedButton(onClick = { expanded.value = !expanded.value }) {
                Text(text = if (expanded.value) "Show less" else "Show more")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun DefaultPreview() {
    ComposeBasics2Theme {
        MyApp()
    }
}

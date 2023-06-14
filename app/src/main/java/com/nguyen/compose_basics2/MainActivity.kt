package com.nguyen.compose_basics2

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
fun MyApp(modifier: Modifier = Modifier) {
    // shouldShowOnboarding is hoisted from OnboardingScreen()
    // replace 'remember' with 'rememberSaveable' to save each state surviving configuration changes
    // and process death
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    Surface(modifier) {
        // share shouldShowOnboarding with OnboardingScreen() without passing it
        if (shouldShowOnboarding) {
            // notify us when the user clicked on the Continue button with a callback
            OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
        } else {
            Greetings()
        }
    }
}

@Composable
// names defaults to a list of 1000 strings, filled with the values contained in its lambda
private fun Greetings(modifier: Modifier = Modifier, names: List<String> = List(1000) { "$it" }) {
    // LazyColumn and LazyRow are equivalent to RecyclerView in Android Views
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = names) { index ->
            Greeting(name = index)
        }
    }
}

@Composable
private fun Greeting(name: String) {
    // To add internal state to a composable, use the mutableStateOf function. State and
    // MutableState are interfaces that hold some value and trigger UI updates (recompositions)
    // whenever that value changes
    // To preserve state across recompositions, remember the mutable state using 'remember'
    var expanded by remember { mutableStateOf(false) }
    // animateDpAsState composable takes a targetValue whose type is Dp
    // extraPadding depends on the expanded state
    val extraPadding by animateDpAsState(
        targetValue = if (expanded) 48.dp else 0.dp,
        // animationSpec lets you customize the animation
        // spring relies on physical properties (damping and stiffness) to make animations more natural
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
    )

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
                .padding(bottom = extraPadding.coerceAtLeast(0.dp))
            ) {
                Text(text = "Hello,")
                // use font 'headlineMedium' and style 'ExtraBold'
                Text(text = name, style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                ))
            }
            // Button is a composable provided by the material3 package which takes a composable as
            // the last argument.
            // toggle the value of the expanded state and show a different text depending on the value.
            ElevatedButton(onClick = { expanded = !expanded }) {
                Text(text = if (expanded) "Show less" else "Show more")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, uiMode = UI_MODE_NIGHT_YES, name = "DarkWing")
@Preview(showBackground = true, widthDp = 320)
@Composable
fun DefaultPreview() {
    ComposeBasics2Theme {
        Greetings()
    }
}

@Composable
fun OnboardingScreen(onContinueClicked: () -> Unit, modifier: Modifier = Modifier) {
    // Column can be configured to display its contents in the center of the screen
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(modifier = Modifier.padding(vertical = 24.dp), onClick = onContinueClicked) {
            Text("Continue")
        }
    }
}

// new preview, with a fixed height to verify that the content is aligned correctly.
@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    ComposeBasics2Theme {
        OnboardingScreen(onContinueClicked = {})
    }
}

package com.nguyen.compose_basics2

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Greeting(name: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardContent(name)
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

@Composable
private fun CardContent(name: String) {
    var expanded by remember { mutableStateOf(false) }

    Row(modifier = Modifier
        .padding(12.dp)
        // automate the process of creating the animation
        .animateContentSize(animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow)
        )
    ) {
        Column(modifier = Modifier.weight(1f).padding(12.dp)) {
            Text(text = "Hello, ")
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
            )
            // Text is displayed when the item is expanded
            if (expanded) {
                Text(text = ("Composem ipsum color sit lazy, padding theme elit, sed do bouncy. ").repeat(4),)
            }
        }
        // Use the IconButton composable together with a child Icon
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                // Use Icons.Filled.ExpandLess and Icons.Filled.ExpandMore, which are available in
                // the material-icons-extended artifact
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                // for accessibility
                contentDescription = stringResource(id = if (expanded) R.string.show_less else R.string.show_more)
            )
        }
    }
}
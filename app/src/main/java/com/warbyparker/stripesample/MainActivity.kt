package com.warbyparker.stripesample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.view.CardInputWidget
import com.stripe.android.view.CardMultilineWidget
import com.warbyparker.stripesample.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val stripe: Stripe by lazy {
        Stripe(this, publishableKey = "my_key", enableLogging = true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PaymentConfiguration.init(
            this, "my_key"
        )
        setContent {
            val stateHolder = rememberSaveableStateHolder()
            val showStripe = remember {
                mutableStateOf(false)
            }
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(it)
                            .padding(16.dp)
                            .fillMaxSize(),
                        horizontalAlignment = CenterHorizontally,
                    ) {
                        Greeting()
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("Show Stripe")
                            Spacer(modifier = Modifier.width(16.dp))
                            Switch(checked = showStripe.value, onCheckedChange = {
                                showStripe.value = it
                            })
                        }
                        if (showStripe.value) {
                            StripeWidget(stateHolder)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Text(
        text = "Hello Stripe!",
        modifier = modifier,
        textAlign = TextAlign.Center,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting()
    }
}

@Composable
private fun StripeWidget(
    stateHolder: SaveableStateHolder,
) {
    stateHolder.SaveableStateProvider("stripe_elements") {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth(),
                    factory = { context ->
                        Log.i("abcde", "factory")
                        CardInputWidget(context).apply {
                            id = R.id.warby_stripe_element_entry
                            setCardValidCallback { isValid, _ ->
                                // do something
                            }
                            postalCodeRequired = true

                        }
                    },
                    update = {
                        Log.i("abcde", "update")
                        // do nothing
                    },
                    onRelease = {
                        Log.i("abcde", "onRelease")
                    },
                )
            }
        }
    }
}

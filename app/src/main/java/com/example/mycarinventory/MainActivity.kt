package com.example.mycarinventory

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mycarinventory.ui.theme.MyCarInventoryTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel : MainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.fetchParts()
            val parts by viewModel.parts.observeAsState(initial = emptyList())

            MyCarInventoryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxWidth()) {
                    CarPartFacts("Android")
                }
            }
        }
    }
}

@Composable
fun CarPartFacts(name: String) {
    var carPartName by remember { mutableStateOf(" ") }
    var carPartModel by remember { mutableStateOf("") }
    var carPartBrand by remember { mutableStateOf("") }
    var carMake by remember { mutableStateOf("") }
    var carPartPrice by remember { mutableStateOf("") }
    val context = LocalContext.current
    Column() {

        OutlinedTextField(
            value = carPartName,
            onValueChange = { carPartName = it },
            label = { Text(stringResource(R.string.partName)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = carPartModel,
            onValueChange = { carPartModel = it },
            label = { Text(stringResource(R.string.partModel)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = carPartBrand,
            onValueChange = { carPartBrand = it },
            label = { Text(stringResource(R.string.partBrand)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = carMake,
            onValueChange = { carMake = it },
            label = { Text(stringResource(R.string.carMake)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = carPartPrice,
            onValueChange = { carPartPrice = it },
            label = { Text(stringResource(R.string.partPrice)) },
            modifier = Modifier.fillMaxWidth()
        )
        Button (
            onClick = {
                Toast.makeText(context, "$carPartName $carPartModel $carPartBrand $carMake $carPartPrice", Toast.LENGTH_LONG).show()
            },
            content = { Text(text = "Save")}
        )

    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(uiMode= Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode")
@Composable
fun DefaultPreview() {
    MyCarInventoryTheme {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxWidth()) {
            CarPartFacts("Android")
        }
    }
}
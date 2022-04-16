package com.example.mycarinventory

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mycarinventory.ui.theme.MyCarInventoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyCarInventoryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
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
            label = { Text(stringResource(R.string.partName)) }
        )
        OutlinedTextField(
            value = carPartModel,
            onValueChange = { carPartModel = it },
            label = { Text(stringResource(R.string.partModel)) }
        )
        OutlinedTextField(
            value = carPartBrand,
            onValueChange = { carPartBrand = it },
            label = { Text(stringResource(R.string.partBrand)) }
        )
        OutlinedTextField(
            value = carMake,
            onValueChange = { carMake = it },
            label = { Text(stringResource(R.string.carMake)) }
        )
        OutlinedTextField(
            value = carPartPrice,
            onValueChange = { carPartPrice = it },
            label = { Text(stringResource(R.string.partPrice)) }
        )
        Button (
            onClick = {
                Toast.makeText(context, "$carPartName $carPartModel $carPartBrand $carMake $carPartPrice", Toast.LENGTH_LONG).show()
            },
            content = { Text(text = "Save")}
        )

    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyCarInventoryTheme {
        CarPartFacts("Android")
    }
}
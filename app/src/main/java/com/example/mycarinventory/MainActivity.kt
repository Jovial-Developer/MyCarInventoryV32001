package com.example.mycarinventory

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.example.mycarinventory.dto.Part
import com.example.mycarinventory.dto.SpecificCarPart
import com.example.mycarinventory.ui.theme.MyCarInventoryTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private var userPickedPart: SpecificCarPart by mutableStateOf(SpecificCarPart())
    private var selectedPart: Part? = null
    private val viewModel: MainViewModel by viewModel<MainViewModel>()
    private var inPartName: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.fetchParts()
            //temp data
            val parts by viewModel.parts.observeAsState(initial = emptyList())
            val specifiedCarPart by viewModel.specifiedCarPart.observeAsState(initial = emptyList())

            MyCarInventoryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CarPartFacts("Android", parts, specifiedCarPart, userPickedPart)
                    //CarPartFacts("Android" , parts, parts, viewModel.selectedPart)
                }
            }
        }
    }

    @Composable
    fun CarPartFacts(name: String, parts: List<Part> = ArrayList<Part>(), specificCarPart: List<SpecificCarPart> = ArrayList<SpecificCarPart>(), userPickedPart : SpecificCarPart = SpecificCarPart()) {
        var carPartName by remember(userPickedPart.partId)  { mutableStateOf(userPickedPart.thisPartName) }
        var carPartModel by remember(userPickedPart.partId) { mutableStateOf(userPickedPart.thisPartModel) }
        var carPartBrand by remember(userPickedPart.partId) { mutableStateOf(userPickedPart.thisPartBrand) }
        var carMake by remember (userPickedPart.partId)     { mutableStateOf(userPickedPart.thisPartsCarMake) }
        var carPartPrice by remember (userPickedPart.partId){ mutableStateOf(userPickedPart.thisPartPrice) }
        val context = LocalContext.current
        Column {
            CarPartSpinner(specifiedCarParts = specificCarPart)
            TextFieldWithDropdownUsage(dataIn = parts, stringResource(R.string.partName), userPickedPart)
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
            Button(
                onClick = {
                    var specificCarPart = SpecificCarPart().apply {
                        thisPartName = carPartName
                        thisPartModel = carPartModel
                        thisPartBrand = carPartBrand
                        thisPartsCarMake = carMake
                        thisPartPrice = carPartPrice

                        partId = selectedPart?.let{
                            it.partIdFinal
                        }?: 0
                    }
                    viewModel.save(specificCarPart)
                    Toast.makeText(
                        context,
                        "$carPartName $carPartModel $carPartBrand $carMake $carPartPrice",
                        Toast.LENGTH_LONG
                    ).show()
                },
                content = { Text(text = "Save") }
            )

        }
    }
    @Composable
    fun CarPartSpinner (specifiedCarParts: List<SpecificCarPart>){
        var carPartText by remember {mutableStateOf("My Car Inventory")}
        var expanded by remember { mutableStateOf(false)}
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row(Modifier.padding(26.dp)
                .clickable {
                    expanded = !expanded
                }
                .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = carPartText, fontSize = 20.sp, modifier = Modifier.padding(end = 8.dp))
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
                DropdownMenu(expanded = expanded, onDismissRequest = {expanded = false}){
                    specifiedCarParts.forEach {
                        specificCarPart-> DropdownMenuItem(onClick = {
                           expanded = false
                        carPartText = specificCarPart.toString()
                        userPickedPart = specificCarPart
                    }) {
                            Text(text = specificCarPart.toString())
                    }
                    }
                }
            }
        }
    }

    @Composable
    fun TextFieldWithDropdownUsage(dataIn: List<Part>, label: String = "", userPickedPart: SpecificCarPart) {
        val dropDownOptions = remember { mutableStateOf(listOf<Part>()) }
        val textFieldValue = remember(userPickedPart.partId) { mutableStateOf(TextFieldValue(userPickedPart.thisPartName)) }
        val dropDownExpanded = remember { mutableStateOf(false) }

        fun onDropdownDismissRequest() {
            dropDownExpanded.value = false
        }

        fun onValueChanged(value: TextFieldValue) {
            inPartName = value.text
            dropDownExpanded.value = true
            textFieldValue.value = value
            dropDownOptions.value = dataIn.filter {
                it.toString().startsWith(value.text) && it.toString() != value.text
            }.take(3)
        }

        TextFieldWithDropdown(
            modifier = Modifier.fillMaxWidth(),
            value = textFieldValue.value,
            setValue = ::onValueChanged,
            onDismissRequest = ::onDropdownDismissRequest,
            dropDownExpanded = dropDownExpanded.value,
            list = dropDownOptions.value,
            label = label
        )
    }

   @Composable
    fun TextFieldWithDropdown(
        modifier: Modifier = Modifier,
        value: TextFieldValue,
        setValue: (TextFieldValue)-> Unit,
        onDismissRequest: ()-> Unit,
        dropDownExpanded: Boolean,
        list: List<Part>,
        label: String = ""
    ) {
        Box(modifier) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused)
                            onDismissRequest()
                    },
                value = value,
                onValueChange = setValue,
                label = { Text(label) },
                colors = TextFieldDefaults.outlinedTextFieldColors()
            )
            DropdownMenu(
                expanded = dropDownExpanded,
                properties = PopupProperties(
                    focusable = false,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                ),
                onDismissRequest = onDismissRequest
            ) {
                list.forEach { text->
                    DropdownMenuItem(onClick = {
                        setValue(
                            TextFieldValue(
                                text.toString(),
                                TextRange(text.toString().length)
                            )
                        )
                        selectedPart = text
                    }) {
                        Text(text = text.toString())


                    }
                }
            }
        }
    }



    @Preview(name = "Light Mode", showBackground = true)
    @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode")
    @Composable
    fun DefaultPreview() {
        MyCarInventoryTheme {
            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxWidth()
            ) {
                CarPartFacts("Android")
            }
        }
    }
}
package com.midterm.billsplit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.midterm.billsplit.ui.theme.BillSplitTheme
import kotlin.math.floor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BillSplitTheme {
                val totalPerPerson = remember {
                    mutableStateOf(0f)
                }
                val total = remember {
                    mutableStateOf(0f)
                }
                val sliderValue = remember {
                    mutableStateOf(0f)
                }
                val numPerson = remember {
                    mutableStateOf(0)
                }
                val tipAmount = remember {
                    mutableStateOf(0f)
                }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    color = MaterialTheme.colors.background
                ) {
                    tipAmount.value = floor(total.value * sliderValue.value / 100)
                    totalPerPerson.value = if (numPerson.value > 0) (tipAmount.value + total.value) / numPerson.value else 0f
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        TopHeader(totalPerPerson.value)
                        MainContent(sliderValue,
                                    numPerson,
                                    total,
                                    tipAmount)
                    }
                }
            }
        }
    }
}

@Composable
fun TopHeader(total: Float) {
    Card(modifier = Modifier
        .padding(start = 25.dp, top = 30.dp, end = 25.dp, bottom = 10.dp)
        .fillMaxWidth()
        .height(150.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(corner = CornerSize(15.dp)),
        backgroundColor = Color(0xffdfc2f2)) {
        Box(contentAlignment = Alignment.Center) {
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Total Per Person",
                    style = MaterialTheme.typography.h5,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Black
                )
                Text(text = "$%.2f".format(total),
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
fun MainContent(sliderValue: MutableState<Float>,
                numPerson: MutableState<Int>,
                initCost: MutableState<Float>,
                tipAmount: MutableState<Float>,) {
    var isTyping = remember {
        mutableStateOf(false)
    }
    var text = remember {
        mutableStateOf("")
    }
    Card(modifier = Modifier
        .padding(15.dp)
        .fillMaxWidth(),
        elevation = 4.dp,
        shape = RoundedCornerShape(corner = CornerSize(10.dp))) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
            OutlinedTextField(value = text.value,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                label = { Text(text = "Enter Bill",
                    color = Color(0xff575557),
                    fontWeight = FontWeight.Black)},
                onValueChange = {
                    text.value = it
                    initCost.value = if (text.value != "") text.value.toFloat() else 0f
                    isTyping.value = text.value != "" },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                leadingIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.AttachMoney, contentDescription = "enter bill")
                    }
                })
            if (isTyping.value) {
                CreateSplit(numPerson)
                CreateTip(tipAmount)
                CreateSlider(sliderValue)
            }
        }
    }
}

@Composable
fun CreateSplit(numPerson: MutableState<Int>) {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Split", modifier = Modifier.padding(10.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(end = 90.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically) {
            Card(modifier = Modifier
                .size(35.dp),
                shape = CircleShape,
                elevation = 4.dp) {
                Icon(imageVector = Icons.Filled.Remove,
                    contentDescription = "remove",
                    modifier = Modifier.clickable {
                        if (numPerson.value > 0) {
                            numPerson.value -= 1
                        }
                    })
            }
            Text(text = "${numPerson.value}",
                modifier = Modifier.padding(6.dp))
            Card(modifier = Modifier
                .size(35.dp),
                shape = CircleShape,
                elevation = 4.dp) {
                Icon(imageVector = Icons.Filled.Add,
                    contentDescription = "add",
                    modifier = Modifier.clickable {
                        numPerson.value += 1
                    })
            }
        }
    }
}

@Composable
fun CreateTip(tipAmount: MutableState<Float>) {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Tip", modifier = Modifier.padding(10.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(end = 90.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "$%.1f".format(tipAmount.value), modifier = Modifier.padding(10.dp))
        }
    }
}

@Composable
fun CreateSlider(sliderValue: MutableState<Float>) {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = "${sliderValue.value.toInt()} %")
    }
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        Slider(value = sliderValue.value.toFloat(),
            onValueChange = { sliderValue.value = it },
            valueRange = 0f..100f,
            steps = 5,
            modifier = Modifier.padding(20.dp, 10.dp,20.dp))
    }
}
@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BillSplitTheme {
        Greeting("Android")
    }
}
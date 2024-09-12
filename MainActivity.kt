package com.example.heartfailure

import NetworkViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HeartFailurePredictionScreen(viewModel = NetworkViewModel())
        }
    }
}

@Composable
fun HeartFailurePredictionScreen(viewModel: NetworkViewModel) {
    var age by remember { mutableStateOf("") }
    var anaemia by remember { mutableStateOf("") }
    var creatininePhosphokinase by remember { mutableStateOf("") }
    var diabetes by remember { mutableStateOf("") }
    var ejectionFraction by remember { mutableStateOf("") }
    var highBloodPressure by remember { mutableStateOf("") }
    var platelets by remember { mutableStateOf("") }
    var serumCreatinine by remember { mutableStateOf("") }
    var serumSodium by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }
    var smoking by remember { mutableStateOf("") }
    var observingTime by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    // Remember scroll state
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState), // Make column scrollable
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = anaemia,
            onValueChange = { anaemia = it },
            label = { Text("Anaemia") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = creatininePhosphokinase,
            onValueChange = { creatininePhosphokinase = it },
            label = { Text("Creatinine Phosphokinase") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = diabetes,
            onValueChange = { diabetes = it },
            label = { Text("Diabetes") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = ejectionFraction,
            onValueChange = { ejectionFraction = it },
            label = { Text("Ejection Fraction") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = highBloodPressure,
            onValueChange = { highBloodPressure = it },
            label = { Text("High Blood Pressure") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = platelets,
            onValueChange = { platelets = it },
            label = { Text("Platelets") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = serumCreatinine,
            onValueChange = { serumCreatinine = it },
            label = { Text("Serum Creatinine") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = serumSodium,
            onValueChange = { serumSodium = it },
            label = { Text("Serum Sodium") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = sex,
            onValueChange = { sex = it },
            label = { Text("Sex (0 female, 1 male)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = smoking,
            onValueChange = { smoking = it },
            label = { Text("Smoking") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = observingTime,
            onValueChange = { observingTime = it },
            label = { Text("Observing Time") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Button(onClick = {
            viewModel.predictHeartFailure(
                age, anaemia, creatininePhosphokinase, diabetes, ejectionFraction,
                highBloodPressure, platelets, serumCreatinine, serumSodium, sex, smoking, observingTime
            ) {
                result = "%.2f".format(it.toDouble() * 100)
            }
        }) {
            Text("Submit Data")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Patient has $result% chance to suffer heart failure",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
    }
}


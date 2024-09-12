import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.round

class NetworkViewModel : ViewModel() {
    private val url = "http://518a4e54-8ae1-42bd-8259-fcfefa06835f.germanywestcentral.azurecontainer.io/score"
    private val apiKey = "rUu02VvyK735N3Q30gk2CvYAwdmDPNLe"

    val result = mutableStateOf("")

    fun predictHeartFailure(
        age: String,
        anaemia: String,
        creatininePhosphokinase: String,
        diabetes: String,
        ejectionFraction: String,
        highBloodPressure: String,
        platelets: String,
        serumCreatinine: String,
        serumSodium: String,
        sex: String,
        smoking: String,
        observingTime: String,
        onResult: (String) -> Unit
    ) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    val connection = URL(url).openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.setRequestProperty("Authorization", "Bearer $apiKey")
                    connection.doOutput = true

                    val jsonBody = """
                        {
                          "Inputs": {
                            "input1": [
                              {
                                "age": ${age.toDoubleOrNull() ?: 0.0},
                                "anaemia": ${anaemia.toIntOrNull() ?: 0},
                                "creatinine_phosphokinase": ${creatininePhosphokinase.toIntOrNull() ?: 0},
                                "diabetes": ${diabetes.toIntOrNull() ?: 0},
                                "ejection_fraction": ${ejectionFraction.toIntOrNull() ?: 0},
                                "high_blood_pressure": ${highBloodPressure.toIntOrNull() ?: 0},
                                "platelets": ${platelets.toDoubleOrNull() ?: 0.0},
                                "serum_creatinine": ${serumCreatinine.toDoubleOrNull() ?: 0.0},
                                "serum_sodium": ${serumSodium.toIntOrNull() ?: 0},
                                "sex": ${sex.toIntOrNull() ?: 0},
                                "smoking": ${smoking.toIntOrNull() ?: 0},
                                "time": ${observingTime.toIntOrNull() ?: 0},
                                "DEATH_EVENT": 0
                              }
                            ]
                          },
                          "GlobalParameters": {}
                        }
                    """.trimIndent()

                    connection.outputStream.use { outputStream ->
                        OutputStreamWriter(outputStream).use { writer ->
                            writer.write(jsonBody)
                            writer.flush()
                        }
                    }

                    val responseCode = connection.responseCode
                    val responseBody = if (responseCode == HttpURLConnection.HTTP_OK) {
                        connection.inputStream.bufferedReader().use { it.readText() }
                    } else {
                        "Error: $responseCode, ${connection.errorStream?.bufferedReader()?.use { it.readText() }}"
                    }
                    connection.disconnect()
                    responseBody
                } catch (e: Exception) {
                    e.printStackTrace()
                    "Exception: ${e.message}"
                }
            }

            val probability = parseProbability(response)
            onResult(probability)
        }
    }

    private fun parseProbability(response: String): String {
        return try {
            val jsonResponse = JSONObject(response)
            val results = jsonResponse.getJSONObject("Results")
            val webServiceOutput = results.getJSONArray("WebServiceOutput0")
            val output = webServiceOutput.getJSONObject(0)
            val scoredProbabilities = output.getDouble("Scored Probabilities")
            scoredProbabilities.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            "Error parsing response: ${e.message}"
        }
    }
}

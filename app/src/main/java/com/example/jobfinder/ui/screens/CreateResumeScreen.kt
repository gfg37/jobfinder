import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobfinder.data.RetrofitClient
import com.example.jobfinder.data.UserSession
import com.example.jobfinder.data.model.ResumeRequest
import kotlinx.coroutines.launch

@Composable
fun CreateResumeScreen(navController: NavController) {
    val context = LocalContext.current
    val session = remember { UserSession(context) }
    val scope = rememberCoroutineScope()

    var position by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var education by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Создание резюме", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = position,
            onValueChange = { position = it },
            label = { Text("Желаемая должность") },
            modifier = Modifier.fillMaxWidth()
        )

        // ... остальные поля ввода ...

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                scope.launch {
                    try {
                        val token = session.getToken() ?: ""
                        val response = RetrofitClient.api.createResume(
                            "Bearer $token",
                            ResumeRequest(position, experience, skills, education)
                        )
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Резюме создано", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        } else {
                            Toast.makeText(context, "Ошибка при создании", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Создать")
        }
    }
}
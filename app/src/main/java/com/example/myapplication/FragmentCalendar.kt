package com.example.myapplication

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import java.time.LocalDate

class CalendarFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                        CalendarScreen()
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen() {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val imageMap = remember { mutableStateMapOf<String, Uri>() }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val key = selectedDate.toString()
            imageMap[key] = it
            selectedImageUri = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 23.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logoimage),
                contentDescription = "Logo",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "SHOP!",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }

        AndroidView(factory = { android.widget.CalendarView(it) }, update = {
            it.setOnDateChangeListener { _, year, month, dayOfMonth ->
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                selectedImageUri = imageMap[selectedDate.toString()]
            }
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp))

        Button(
            onClick = {
                galleryLauncher.launch("image/*")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDDDDDD))
        ) {
            Text("이 날에 입을 옷 추가하기", color = Color.Black)
        }
    }
}

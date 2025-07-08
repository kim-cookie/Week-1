package com.example.myapplication

import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen() {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val imageMap = remember { mutableStateMapOf<String, SnapshotStateList<Uri>>() }

    // 유저 ID 불러오기 (기본값 guest)
    val userId = remember {
        UserManager.getLoggedInUser(context)?.id ?: "guest"
    }

    // 저장된 이미지 로딩
    LaunchedEffect(userId) {
        val savedMap = ImageStorage.loadAllUris(context, userId)
        savedMap.forEach { (date, uriList) ->
            imageMap[date] = uriList.toMutableStateList()
        }
    }

    // 갤러리 런처
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val key = selectedDate.toString()
            val currentList = imageMap[key] ?: mutableStateListOf()
            currentList.add(it)
            imageMap[key] = currentList
            ImageStorage.saveUriList(context, userId, key, currentList)
        }
    }

    // UI 구성
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    )
    {
        AndroidView(
            factory = { inflaterContext ->
                LayoutInflater.from(inflaterContext).inflate(R.layout.header_logo, null)
            },
            modifier = Modifier
                .fillMaxWidth()
        )


        // 캘린더
        AndroidView(
            factory = { android.widget.CalendarView(it) },
            update = {
                it.setOnDateChangeListener { _, year, month, dayOfMonth ->
                    selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // 버튼
        Button(
            onClick = { galleryLauncher.launch("image/*") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDDDDDD))
        ) {
            Text("이 날에 입을 옷 추가하기", color = Color.Black)
        }

        // 이미지 리스트
        val imagesForDate = imageMap[selectedDate.toString()] ?: emptyList()

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 60.dp)
        ) {
            items(imagesForDate) { uri ->
                Box(modifier = Modifier.aspectRatio(1f)) {
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                    )

                    IconButton(
                        onClick = {
                            val key = selectedDate.toString()
                            val list = imageMap[key]
                            list?.remove(uri)
                            if (list != null) {
                                imageMap[key] = list
                                ImageStorage.saveUriList(context, userId, key, list)
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(20.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.delete),
                            contentDescription = "삭제",
                            tint = colorResource(id = R.color.orange),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

        }
    }
}

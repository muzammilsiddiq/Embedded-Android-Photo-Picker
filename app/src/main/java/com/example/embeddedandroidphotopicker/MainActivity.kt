package com.example.embeddedandroidphotopicker

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.ext.SdkExtensions
import android.widget.photopicker.EmbeddedPhotoPickerFeatureInfo
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import coil.compose.AsyncImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.photopicker.compose.EmbeddedPhotoPicker
import androidx.photopicker.compose.ExperimentalPhotoPickerComposeApi
import androidx.photopicker.compose.rememberEmbeddedPhotoPickerState
import coil.compose.AsyncImage
import com.example.embeddedandroidphotopicker.ui.theme.EmbeddedAndroidPhotoPickerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.UPSIDE_DOWN_CAKE, version = 15)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EmbeddedAndroidPhotoPickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (isEmbeddedPhotoPickerAvailable()) {
                        EmbeddedPickerHost(maxSelection = 5) {

                        }
                    }
                }
            }
        }
    }
}

fun isEmbeddedPhotoPickerAvailable(): Boolean {
    // Embedded picker requires Android 14+ anyway.
    if (Build.VERSION.SDK_INT < 34) return false

    // SDK Extensions are the actual gate for embedded support.
    return SdkExtensions.getExtensionVersion(Build.VERSION_CODES.UPSIDE_DOWN_CAKE) >= 15
}

@RequiresExtension(extension = Build.VERSION_CODES.UPSIDE_DOWN_CAKE, version = 15)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPhotoPickerComposeApi::class)
@Composable
fun EmbeddedPickerHost(
    maxSelection: Int = 5,
    onDone: (List<Uri>) -> Unit,
) {
    var attachments by remember { mutableStateOf(emptyList<Uri>()) }
    val scope = rememberCoroutineScope()

    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )

    // Feature configuration is explicit in the embedded picker sample:
    // max limit + ordered selection + accent color.
    // Keep this object stable.
    val featureInfo = remember {
        EmbeddedPhotoPickerFeatureInfo.Builder()
            .setMaxSelectionLimit(maxSelection)
            .setOrderedSelection(true)
            .build()
    }

    val pickerState = rememberEmbeddedPhotoPickerState(
        onSelectionComplete = {
            scope.launch {
                sheetState.bottomSheetState.hide()
            }
            onDone(attachments)
        },
        onUriPermissionGranted = { granted ->
            attachments = attachments + granted
        },
        onUriPermissionRevoked = { revoked ->
            attachments = attachments - revoked.toSet()
        }
    )

    // Keep picker expansion in sync with the container.
    SideEffect {
        val expanded = sheetState.bottomSheetState.targetValue == SheetValue.Expanded
        pickerState.setCurrentExpanded(expanded)
    }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = if (sheetState.bottomSheetState.isVisible) 400.dp else 0.dp,
        sheetContent = {
            // Dedicated picker surface area.
            EmbeddedPhotoPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 240.dp),
                state = pickerState,
                embeddedPhotoPickerFeatureInfo = featureInfo
            )
        },
        topBar = {
            TopAppBar(title = { Text("Composer") })
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { scope.launch { sheetState.bottomSheetState.partialExpand() } }) {
                    Text("Add from gallery")
                }
                Button(
                    enabled = attachments.isNotEmpty(),
                    onClick = { onDone(attachments) }
                ) {
                    Text("Send")
                }
            }

            Spacer(Modifier.height(16.dp))

            // Your own attachment UI is separate from the picker surface.
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 88.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(attachments) { uri ->
                    AttachmentTile(
                        uri = uri,
                        onRemove = {
                            scope.launch {
                                // Inform the picker that the host UI removed something.
                                pickerState.deselectUri(uri)
                                // Keep host state consistent (deselectUri won't auto-update your list).
                                attachments = attachments - uri
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AttachmentTile(
    uri: Uri,
    onRemove: () -> Unit
) {
    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier
            .size(88.dp)
            .clickable { onRemove() }
    ) {
        AsyncImage(
            model = uri,
            contentDescription = "Attachment Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

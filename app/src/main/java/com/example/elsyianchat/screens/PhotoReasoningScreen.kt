package com.example.elsyianchat.screens

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Precision
import com.commandiron.compose_loading.ChasingTwoDots
import com.example.elsyianchat.R
import com.example.elsyianchat.appComponent.LoaderComponent
import com.example.elsyianchat.factory.GenerativeViewModelFactory
import com.example.elsyianchat.ui.theme.inter
import com.example.elsyianchat.uiStates.PhotoReasoningUiState
import com.example.elsyianchat.utils.UriSaver
import com.example.elsyianchat.viewModel.PhotoReasoningViewModel
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoReasoningScreen(navController: NavHostController) {

    val photoReasoningViewModel: PhotoReasoningViewModel =
        viewModel(factory = GenerativeViewModelFactory)

    val photoReasoningUiState by photoReasoningViewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val imageRequestBuilder = ImageRequest.Builder(LocalContext.current)
    val imageLoader = ImageLoader.Builder(LocalContext.current).build()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Photo-Reasoning",
                        fontFamily = inter
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBackIos,
                            contentDescription = null,
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        PhotoReasoningScreen(
            paddingValues = paddingValues,
            uiState = photoReasoningUiState,
            onReasonClicked = { inputText, selectedItems ->
                coroutineScope.launch {
                    val bitmaps = selectedItems.mapNotNull {
                        val imageRequest = imageRequestBuilder
                            .data(it)
                            // Scale the image down to 768px for faster uploads
                            .size(size = 768)
                            .precision(Precision.EXACT)
                            .build()
                        try {
                            val result = imageLoader.execute(imageRequest)
                            if (result is SuccessResult) {
                                return@mapNotNull (result.drawable as BitmapDrawable).bitmap
                            } else {
                                return@mapNotNull null
                            }
                        } catch (e: Exception) {
                            return@mapNotNull null
                        }
                    }
                    photoReasoningViewModel.reason(inputText, bitmaps)
                }
            }
        )

    }

}

@Composable
fun PhotoReasoningScreen(
    uiState: PhotoReasoningUiState = PhotoReasoningUiState.Loading,
    onReasonClicked: (String, List<Uri>) -> Unit = { _, _ -> },
    paddingValues: PaddingValues
) {

    val coroutineScope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var userQuestion by rememberSaveable { mutableStateOf("") }
    val imageUris = rememberSaveable(saver = UriSaver()) { mutableStateListOf() }

    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { imageUri ->
        imageUri?.let {
            imageUris.add(it)
        }
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(all = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow(
            modifier = Modifier.padding(all = 8.dp).align(Alignment.Start),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(imageUris) { imageUri ->
                Box {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier
                            .height(80.dp)
                            .width(65.dp)
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.FillBounds
                    )
                    ElevatedCard(
                        shape = CircleShape,
                        onClick = {
                            coroutineScope.launch {
                                imageUris.remove(imageUri)
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(
                                x = 10.dp,
                                y = 12.dp
                            ),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(all = 4.dp)
                        )

                    }
                }
            }
        }
        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextField(
                shape = MaterialTheme.shapes.large,
                value = userQuestion,
                placeholder = {
                    Text(
                        text = "Enter your message",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = inter
                    )
                },
                onValueChange = {
                    userQuestion = it
                },
                modifier = Modifier
                    .fillMaxWidth().weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    cursorColor = MaterialTheme.colorScheme.onSurface,

                    ),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            pickMedia.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier
                    ) {
                        Icon(
                            Icons.Rounded.Add,
                            contentDescription = null,
                        )
                    }
                }
            )


            Surface(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(42.dp)
                    .clip(CircleShape)
                    .then(
                        if (userQuestion.isNotEmpty()) Modifier.clickable {
                            onReasonClicked(userQuestion, imageUris.toList())
                            keyboardController?.hide()
                            focusManager.clearFocus()

                        } else Modifier
                    ),
                shape = CircleShape,
                color =  MaterialTheme.colorScheme.secondary ,

                ) {

                Icon(
                    imageVector =  Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(8.dp)
                )
            }
        }
        when (uiState) {
            PhotoReasoningUiState.Initial -> {
                Column(
                    Modifier
                        .padding(vertical = 15.dp, horizontal = 10.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Surface(
                        shape = CircleShape,
                        shadowElevation = 10.dp
                    ) {
                        LoaderComponent(modifier = Modifier.size(120.dp), rawRes = R.raw.bot)

                    }

                    Text(
                        text ="Upload an image and ask a question to get a response",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = inter,
                        modifier = Modifier.padding(top = 10.dp),
                        color = MaterialTheme.colorScheme.onSecondary,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        lineHeight = 30.sp
                    )

                }
            }

            PhotoReasoningUiState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    ChasingTwoDots(color = MaterialTheme.colorScheme.onSecondary)
                }
            }

            is PhotoReasoningUiState.Success -> {
                Card(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(all = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_ai),
                            contentDescription = "Person Icon",
                            modifier = Modifier
                                .requiredSize(36.dp)
                                .clip(CircleShape)
                        )
                        MarkdownText(
                            markdown = uiState.outputText,
                            color = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .fillMaxWidth()
                        )
                    }
                }

            }

            is PhotoReasoningUiState.Error -> {
                Card(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = uiState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(all = 16.dp)
                    )
                }
            }
        }
    }
}
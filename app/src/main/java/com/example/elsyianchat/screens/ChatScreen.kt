package com.example.elsyianchat.screens

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.elsyianchat.R
import com.example.elsyianchat.appComponent.HorizontalSpace
import com.example.elsyianchat.appComponent.LoaderComponent
import com.example.elsyianchat.appComponent.VerticalSpace
import com.example.elsyianchat.factory.GenerativeViewModelFactory
import com.example.elsyianchat.ui.theme.inter
import com.example.elsyianchat.utils.ChatMessage
import com.example.elsyianchat.utils.Role
import com.example.elsyianchat.viewModel.ChatViewModel
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavHostController) {

    val chatViewModel: ChatViewModel = viewModel(factory = GenerativeViewModelFactory)

    val chatUiState = chatViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Chat with Elsyian AI",
                        fontFamily = inter
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBackIos,
                            contentDescription = null,
                        )
                    }
                }
            )
        }
    ) {

        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(it)
        ) {

            ChatList(
                chatMessages = chatUiState.value.messages,
                listState = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )

            VerticalSpace(height = 15.dp)

            MessageInput(
                onMessageSend = { userInput ->
                    chatViewModel.sendMessage(userInput)
                },
                resetScroll = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                }
            )

        }

    }

}

@Composable
fun MessageInput(
    onMessageSend: (String)-> Unit,
    resetScroll: () -> Unit,
    modifier: Modifier = Modifier
) {

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val userMessage = rememberSaveable {
        mutableStateOf("")
    }

    Row(

        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TextField(
            value = userMessage.value,
            onValueChange = {
                userMessage.value = it
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
            ),
            modifier = Modifier
                .padding(top = 4.dp, bottom = 4.dp, start = 8.dp)
                .fillMaxWidth()
                .weight(1f),
            colors =  TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                cursorColor = MaterialTheme.colorScheme.onSurface,

                ),
            placeholder = {
                Text(
                    text = "Type a message",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = inter
                )
            },
            shape = MaterialTheme.shapes.large,

            )
        Surface(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(42.dp)
                .clip(CircleShape)
                .then(
                    if (userMessage.value.isNotEmpty()) Modifier.clickable {
                        onMessageSend.invoke(userMessage.value)
                        userMessage.value = ""
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        resetScroll.invoke()


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


}

@Composable
fun ChatList(
    chatMessages: List<ChatMessage>,
    listState: LazyListState,
    modifier: Modifier = Modifier,

    ) {

    val context = LocalContext.current

    LazyColumn(
        state = listState,
        reverseLayout = true,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (chatMessages.size > 2) {
            items(chatMessages.reversed()) { message ->
                ChatBubbleItem(
                    message = message,
                    onLongPressed = {
                        val clipboardManager =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clipData: ClipData = ClipData.newPlainText("text", message.text)
                        clipboardManager.setPrimaryClip(clipData)
                        Toast.makeText(context, "Copied to Clipboard", Toast.LENGTH_SHORT).show()
                    }
                )

            }
        } else {
            item {
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
                        text ="Hi, I'm Elsyian AI,\nI can help you with your queries. Ask me anything!",
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
        }
    }

}

@Composable
fun ChatBubbleItem(message: ChatMessage, onLongPressed: () -> Unit) {

    val isBotMessage = message.role == Role.BOT || message.role == Role.ERROR

    if (message.text.isNotEmpty()) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {


                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Image(
                        painter = painterResource(id = if (isBotMessage) R.drawable.ic_ai else R.drawable.ic_user),
                        contentDescription = null,
                        modifier = Modifier
                            .size(35.dp)
                            .clip(CircleShape)

                    )
                    HorizontalSpace(width = 10.dp)
                    BoxWithConstraints {
                        Card(
                            shape = MaterialTheme.shapes.large,
                            modifier = Modifier
                                .widthIn(0.dp, maxWidth * 0.9f),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isBotMessage) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary,
                            )
                        ) {
                            MarkdownText(
                                markdown = message.text,
                                modifier = Modifier.padding(10.dp),
                                color = MaterialTheme.colorScheme.onSecondary,

                                )
                        }
                    }
                }
            }
            if (message.isPending) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onSecondary,
                        strokeWidth = 0.5.dp,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 10.dp)
                    )
                }

            }
        }

    }

}
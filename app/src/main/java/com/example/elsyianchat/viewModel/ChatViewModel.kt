package com.example.elsyianchat.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elsyianchat.uiStates.ChatUiState
import com.example.elsyianchat.utils.ChatMessage
import com.example.elsyianchat.utils.Role
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.asTextOrNull
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ChatViewModel (
    generativeModel: GenerativeModel
):ViewModel(){

    private val chat = generativeModel.startChat(
        history = listOf(
            content(role = "user") { text("") },
            content(role = "model") { text("") }
        )
    )


    private val _uiState: MutableStateFlow<ChatUiState> =
        MutableStateFlow(
            ChatUiState(
                chat.history.map { content ->
                    ChatMessage(
                        text = content.parts.first().asTextOrNull() ?: "",
                        role = if (content.role == "user") Role.USER else Role.BOT,
                        isPending = false
                    )
                }
            )
        )

    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun sendMessage(userMessage: String) {
        // Adding a pending message
        _uiState.value.addMessage(
            ChatMessage(
                text = userMessage,
                role = Role.USER,
                isPending = true
            )
        )

        viewModelScope.launch {
            try {
                val response = chat.sendMessage(userMessage)
                _uiState.value.replaceLastPendingMessage()
                response.text?.let { modelResponse ->
                    _uiState.value.addMessage(
                        ChatMessage(
                            text = modelResponse,
                            role = Role.BOT,
                            isPending = false
                        )
                    )
                }
            } catch (e: Exception) {
                _uiState.value.replaceLastPendingMessage()
                _uiState.value.addMessage(
                    ChatMessage(
                        text = e.localizedMessage?:"Something went wrong! Try again",
                        role = Role.ERROR
                    )
                )
            }
        }
    }

}

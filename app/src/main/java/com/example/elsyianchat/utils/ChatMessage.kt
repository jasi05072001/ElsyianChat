package com.example.elsyianchat.utils

import java.util.UUID

enum class  Role{
    USER, BOT, ERROR
}

data class ChatMessage(
    val id :String = UUID.randomUUID().toString(),
    var text :String = "",
    var role : Role = Role.USER,
    var isPending : Boolean = false
)
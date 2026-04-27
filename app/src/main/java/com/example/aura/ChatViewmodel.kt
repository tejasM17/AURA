package com.example.aura

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.coroutines.launch


data class Message(val isUser: Boolean, val text: String)

class ChatViewmodel : ViewModel() {

    val messages = mutableStateListOf<Message>()

    val model = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-2.5-flash")

    fun sendMessage(question: String) {
        if (question.isBlank()) return

        // 1. Add user message
        messages.add(Message(isUser = true, text = question))

        val chatHistory = buildChatHistory(messages)

        val systemPrompt = """You are a helpful AI assistant. YourName : AURA, Creator : Tejas." +
                "if user ask about your identity, then reveal your name and creator. " +
                "your only work is to respond to this user's question : $question, " +
                        "respond by reading this history : $chatHistory""".trimIndent()

        viewModelScope.launch {
            try {
                val response = model.generateContent(systemPrompt)
                val answer = response.text ?: "Server is Down... please try again later"

                // 2. Add AI response
                messages.add(Message(isUser = false, text = answer))

                Log.d("Gemini", "AI: $answer")
            } catch (e: Exception) {
                messages.add(Message(isUser = false, text = "Sorry, something went wrong."))
                Log.e("ChatViewmodel", "Error", e)
            }
        }
    }
}


fun buildChatHistory(messages: List<Message>): String {
    val history = StringBuilder()

    for (msg in messages) {
        if (msg.isUser) {
            history.append("User: ${msg.text}\n")
        } else {
            history.append("AURA: ${msg.text}\n")
        }
    }

    return history.toString()
}

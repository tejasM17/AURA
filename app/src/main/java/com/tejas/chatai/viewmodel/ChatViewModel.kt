package com.tejas.chatai.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.content
import com.tejas.chatai.data.MessageModel
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {

    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }
    val generativeModel = Firebase.ai()
        .generativeModel("gemini-2.5-flash-lite")

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun sendMsg(question: String) {
        Log.i("In ChatViewModel", question)
        viewModelScope.launch {
            try {
                val chat = generativeModel.startChat(
                    history = messageList.map {
                        content(it.role) { text(it.message) }
                    }.toList()
                )

                messageList.add(MessageModel(question,"user"))
                messageList.add(MessageModel("Typing","model"))

                val response = chat.sendMessage(question)
                messageList.removeLast()
                messageList.add(MessageModel(response.text.toString(),"model"))
            } catch(e: Exception) {
                messageList.removeLast()
                messageList.add(MessageModel("Error : "+e.message.toString(), "model"))
            }

        }
    }
}
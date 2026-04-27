package com.example.aura

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ChatScreen(modifier: Modifier = Modifier) {
    val viewModel: ChatViewmodel = ChatViewmodel()

    val messages = viewModel.messages

    Column(
        modifier = modifier.fillMaxSize()
            .imePadding()
    ) {
        // Simple message list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            items(messages) { msg ->
                MessageBubble(msg)
            }
        }
        MessageInput { text ->
            viewModel.sendMessage(text)
        }
    }
}

@Composable
fun MessageBubble(msg: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            modifier = Modifier.widthIn(max = 330.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Text(
                text = msg.text,
                fontSize = 20.sp,
                modifier = Modifier.padding(12.dp),
                color = if (msg.isUser) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun MessageInput(onSend: (String) -> Unit) {
    var message by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(25.dp),
            placeholder = {
                Text(
                    "Ask anything...",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            },
            trailingIcon = {
                IconButton(onClick = {
                    onSend(message.trim())
                    message = ""
                }) {
                    Icon(Icons.Default.Send, contentDescription = "Send")
                }
            },
            singleLine = false
        )
    }
}

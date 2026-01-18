package com.tejas.chatai.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tejas.chatai.R
import com.tejas.chatai.R.drawable
import com.tejas.chatai.data.MessageModel
import com.tejas.chatai.ui.theme.ColorAIMessage
import com.tejas.chatai.ui.theme.ColorUserMessage
import com.tejas.chatai.ui.theme.Purple80
import com.tejas.chatai.viewmodel.ChatViewModel

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun ChatScreen(modifier: Modifier = Modifier, viewModel: ChatViewModel) {
    Column(
        modifier = modifier
    ) {
        TopAppBar()
        MessageList(
            modifier = modifier.weight(1f),
            messageList = viewModel.messageList
        )
        MessageInput(
            onSend = {
                viewModel.sendMsg(it)
            }
        )
    }
}

@Composable
fun MessageList(modifier: Modifier = Modifier,messageList : List<MessageModel>) {
    if (messageList.isEmpty()){
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = drawable.outline_chat_24),
                contentDescription = "Icon",
                tint = Purple80
            )
            Text("Ask Me Anything...", fontSize = 24.sp)
        }
    }else {
        LazyColumn(
            modifier = modifier,
            reverseLayout = true
        ) {
            items(messageList.reversed()){
                MessageRow(messageModel = it)
            }
        }
    }
}
@Composable
fun MessageRow(modifier: Modifier = Modifier,
               messageModel: MessageModel
) {
    val isModel = messageModel.role=="model"

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = modifier.fillMaxWidth()
        ){
            Box(
                modifier = modifier.align(if (isModel) Alignment.BottomStart else Alignment.BottomEnd
                ).padding(
                    start = if (isModel) 8.dp else 70.dp,
                    end = if(isModel) 70.dp else 8.dp,
                    top = 8.dp,
                    bottom = 8.dp
                ).clip(RoundedCornerShape(30f))
                    .background(if (isModel) ColorAIMessage else ColorUserMessage)
                    .padding(5.dp)

            ) {
                SelectionContainer() {
                    Text(

                        text = messageModel.message,
                        fontWeight = FontWeight.W500
                    )
                }
            }
        }
    }
}


@Composable
fun MessageInput(
    modifier: Modifier = Modifier,
    onSend: (String) -> Unit
) {
    var message by rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        value = message,
        onValueChange = { message = it },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(25.dp),
        placeholder = { Text("Ask any…") },

        trailingIcon = {
            IconButton(
                onClick = {
                    if (message.isNotEmpty()){
                        onSend(message)
                        message = ""
                    }
                },
                enabled = message.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send"
                )
            }
        }
    )
}


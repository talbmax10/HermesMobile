package com.hermesmobile.ui.agent

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel for the Agent chat screen.
 * This is a mock implementation that simulates chatting with an AI agent.
 */
class AgentViewModel(application: Application) : AndroidViewModel(application) {

    private val _messages = MutableLiveData<List<ChatMessage>>(emptyList())
    val messages: LiveData<List<ChatMessage>> = _messages

    fun sendMessage(text: String) {
        // Add user message
        val currentMessages = _messages.value ?: emptyList()
        val userMessage = ChatMessage(text, isUser = true)
        _messages.value = currentMessages + userMessage

        // Simulate thinking
        viewModelScope.launch {
            delay(1000) // Simulate network delay

            // Add typing indicator
            val typingMessage = ChatMessage("", isUser = false, isTyping = true)
            _messages.value = _messages.value ?: emptyList() + typingMessage

            // Simulate processing
            delay(2000)

            // Remove typing indicator and add bot response
            val updatedMessages = _messages.value ?: emptyList()
            val botResponse = when (text.lowercase()) {
                "hello" -> "Hello! How can I assist you today?"
                "what can you do" -> "I can help you create Android apps, manage files, run terminal commands, and more."
                "create an android app" -> "Sure! What kind of app would you like to create?"
                else -> "I'm still learning. Please try a different command or check the documentation."
            }
            val botMessage = ChatMessage(botResponse, isUser = false)
            // Remove typing message and add bot message
            val finalMessages = updatedMessages.filterNot { it.isTyping } + botMessage
            _messages.value = finalMessages
        }
    }

    data class ChatMessage(
        val text: String,
        val isUser: Boolean,
        val isTyping: Boolean = false
    )
}
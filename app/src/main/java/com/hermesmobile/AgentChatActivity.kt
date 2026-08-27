package com.hermesmobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AgentChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSend: ImageButton
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agent_chat)

        recyclerView = findViewById(R.id.recyclerViewChat)
        editTextMessage = findViewById(R.id.editTextMessage)
        buttonSend = findViewById(R.id.buttonSend)

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        chatAdapter = ChatAdapter()
        recyclerView.adapter = chatAdapter

        // Send button click
        buttonSend.setOnClickListener {
            val message = editTextMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                // Add user message
                chatAdapter.addMessage(ChatMessage(message, isUser = true))
                editTextMessage.text.clear()
                // Simulate bot response (for now just echo) with a slight delay
                editTextMessage.post {
                    chatAdapter.addMessage(ChatMessage(" recibido: $message", isUser = false))
                }
            } else {
                Toast.makeText(this, "الرجاء كتابة رسالة", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Simple data class for chat messages
    data class ChatMessage(val text: String, val isUser: Boolean)

    // Simple adapter for chat messages
    inner class ChatAdapter :
        RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

        private val messages = mutableListOf<ChatMessage>()

        inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.textViewMessage)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_message, parent, false)
            return ChatViewHolder(view)
        }

        override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
            val message = messages[position]
            holder.textView.text = message.text
        }

        override fun getItemCount(): Int = messages.size

        fun addMessage(message: ChatMessage) {
            messages.add(message)
            notifyItemInserted(messages.lastIndex)
            recyclerView.scrollToPosition(messages.lastIndex)
        }
    }
}
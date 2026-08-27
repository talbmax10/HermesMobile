package com.hermesmobile

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
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
        chatAdapter = ChatAdapter(mutableListOf())
        recyclerView.adapter = chatAdapter

        // Send button click
        buttonSend.setOnClickListener {
            val message = editTextMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                // Add user message
                chatAdapter.addMessage(ChatMessage(message, isUser = true))
                editTextMessage.text.clear()
                // Simulate bot response (for now just echo)
                // In real implementation, we would call Hermes Agent here
                post { 
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
    inner class ChatAdapter(private var messages: List<ChatMessage>) :
        RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

        inner class ChatViewHolder(val binding: androidx.databinding.ViewDataBinding) :
            RecyclerView.ViewHolder(binding.root)

        // Since we are not using databinding for simplicity, we'll inflate layout manually
        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ChatViewHolder {
            // Inflate a simple layout for chat item (we'll create it later)
            val view = layoutInflater.inflate(R.layout.item_chat_message, parent, false)
            return ChatViewHolder(androidx.databinding.DataBindingUtil.bind(view))
        }

        override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
            val message = messages[position]
            // For now we just set text via finding views by id (we'll implement properly later)
            val textView = holder.itemView.findViewById<android.widget.TextView>(R.id.textViewMessage)
            textView.text = message.text
            // We could adjust gravity based on isUser, but skip for now
        }

        override fun getItemCount(): Int = messages.size

        fun addMessage(message: ChatMessage) {
            messages += message
            notifyItemInserted(messages.lastIndex)
            recyclerView.scrollToPosition(messages.lastIndex)
        }
    }
}
package com.hermesmobile.ui.agent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hermesmobile.R
import com.hermesmobile.databinding.FragmentAgentBinding

/**
 * Fragment for the Agent chat screen.
 */
class AgentFragment : Fragment() {

    private var _binding: FragmentAgentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AgentViewModel by viewModels()
    private lateinit var adapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        adapter = ChatAdapter()
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.chatRecyclerView.adapter = adapter

        // Observe messages from ViewModel
        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            adapter.submitList(messages)
            // Scroll to bottom
            binding.chatRecyclerView.scrollToPosition(messages.size - 1)
        }

        // Setup send button
        binding.sendButton.setOnClickListener {
            val text = binding.messageEditText.text.toString().trim()
            if (text.isNotEmpty()) {
                viewModel.sendMessage(text)
                binding.messageEditText.text.clear()
            }
        }

        // Send message on IME action
        binding.messageEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action == android.view.KeyEvent.ACTION_DOWN &&
                    keyCode == android.view.KeyEvent.KEYCODE_ENTER) {
                val text = binding.messageEditText.text.toString().trim()
                if (text.isNotEmpty()) {
                    viewModel.sendMessage(text)
                    binding.messageEditText.text.clear()
                    return@setOnKeyListener true
                }
            }
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class ChatAdapter :
            RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

        private var messages: List<AgentViewModel.ChatMessage> = emptyList()

        fun submitList(newList: List<AgentViewModel.ChatMessage>) {
            messages = newList
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_message, parent, false)
            return ChatViewHolder(view)
        }

        override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
            holder.bind(messages[position])
        }

        override fun getItemCount(): Int = messages.size

        inner class ChatViewHolder(itemView: View) :
                RecyclerView.ViewHolder(itemView) {

            private val userMessageView = itemView.findViewById<View>(R.id.userMessageContainer)
            private val botMessageView = itemView.findViewById<View>(R.id.botMessageContainer)
            private val userMessageText = itemView.findViewById<android.widget.TextView>(R.id.userMessageText)
            private val botMessageText = itemView.findViewById<android.widget.TextView>(R.id.botMessageText)
            private val typingIndicator = itemView.findViewById<View>(R.id.typingIndicator)

            fun bind(message: AgentViewModel.ChatMessage) {
                if (message.isUser) {
                    userMessageView.visibility = View.VISIBLE
                    botMessageView.visibility = View.GONE
                    typingIndicator.visibility = View.GONE
                    userMessageText.text = message.text
                } else if (message.isTyping) {
                    userMessageView.visibility = View.GONE
                    botMessageView.visibility = View.GONE
                    typingIndicator.visibility = View.VISIBLE
                } else {
                    userMessageView.visibility = View.GONE
                    botMessageView.visibility = View.VISIBLE
                    typingIndicator.visibility = View.GONE
                    botMessageText.text = message.text
                }
            }
        }
    }
}
<script setup lang="ts">
import { ref, nextTick, watch } from 'vue'
import { 
  SendOutlined, 
  UserOutlined, 
  RobotOutlined, 
  LoadingOutlined
} from '@ant-design/icons-vue'
import { chatService, type ChatMessage } from '../services/chatService'
import { message as antMessage } from 'ant-design-vue'
import { marked } from 'marked'

const WELCOME_MESSAGE = 'Hello! I am your personal InvestmentAI assistant. How can I help you manage your portfolio today?'
const ERROR_MESSAGE = 'Sorry, I encountered an error determining the best way to help you. Please try again.'

const messages = ref<ChatMessage[]>([
  {
    id: 'welcome',
    role: 'assistant',
    content: WELCOME_MESSAGE,
    timestamp: new Date()
  }
])
const inputValue = ref('')
const isLoading = ref(false)
const messagesContainer = ref<HTMLElement | null>(null)

const scrollToBottom = async () => {
  await nextTick()
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

watch(() => messages.value.length, scrollToBottom)

const handleSendMessage = async () => {
  const text = inputValue.value.trim()
  if (!text || isLoading.value) return

  inputValue.value = ''
  
  messages.value.push({
    id: Date.now().toString(),
    role: 'user',
    content: text,
    timestamp: new Date()
  })

  isLoading.value = true

  try {
    const rawResponse = await chatService.sendMessage(text)
    const { content } = chatService.parseResponse(rawResponse)
    
    messages.value.push({
      id: (Date.now() + 1).toString(),
      role: 'assistant',
      content: content,
      timestamp: new Date()
    })
  } catch (error) {
    antMessage.error('Failed to get response from server')
    messages.value.push({
      id: Date.now().toString(),
      role: 'assistant',
      content: ERROR_MESSAGE,
      timestamp: new Date()
    })
  } finally {
    isLoading.value = false
  }
}

const renderMarkdown = (content: string) => {
  return marked.parse(content)
}
</script>

<template>
  <div class="chat-layout">
    <!-- Header -->
    <header class="header">
      <div class="header-content">
        <h1>Investment Manager</h1>
      </div>
    </header>

    <div class="main-body">
      <!-- Main Chat Area -->
      <main class="chat-area">
        <div ref="messagesContainer" class="messages-display">
          <div v-for="message in messages" :key="message.id" 
               class="message-row" :class="message.role">
            
            <div class="avatar">
               <UserOutlined v-if="message.role === 'user'" />
               <RobotOutlined v-else />
            </div>

            <div class="message-content">
                <div class="sender-name">{{ message.role === 'user' ? 'You' : 'Assistant' }} <span class="time">{{ message.timestamp.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'}) }}</span></div>
                <!-- We use v-html for the rich content (tables, and Markdown) -->
                <div v-if="message.role === 'assistant'" class="bubble" v-html="renderMarkdown(message.content)"></div>
                <div v-else class="bubble">{{ message.content }}</div>
            </div>
          </div>

          <div v-if="isLoading" class="message-row assistant">
             <div class="avatar"><LoadingOutlined spin /></div>
             <div class="message-content">
                <div class="sender-name">Assistant</div>
                <div class="bubble loading">Thinking<span>.</span><span>.</span><span>.</span></div>
             </div>
          </div>
        </div>

        <!-- Input Area -->
        <div class="input-container">
            <div class="input-box">
                <a-textarea
                    v-model:value="inputValue"
                    placeholder="Ask about your portfolio, trades, or market analysis..."
                    :auto-size="{ minRows: 1, maxRows: 6 }"
                    @pressEnter.prevent="handleSendMessage"
                    class="main-input"
                    :bordered="false"
                />
                <button 
                  @click="handleSendMessage"
                  :disabled="!inputValue.trim() || isLoading"
                  class="send-btn"
                >
                  <SendOutlined />
                </button>
            </div>
             <p class="disclaimer">AI responses may be inaccurate. Verify financial data independently.</p>
        </div>
      </main>
    </div>
  </div>
</template>

<style scoped>
.chat-layout {
  display: flex;
  flex-direction: column;
  height: 100vh;
  width: 100vw;
  background-color: #ffffff;
}

/* Header */
.header {
  padding: 1.5rem 2rem;
  border-bottom: 1px solid #f3f4f6;
}

.header h1 {
  font-family: var(--font-serif);
  font-size: 2rem;
  margin-bottom: 0;
  color: #1f2937;
}

/* Main Body */
.main-body {
  display: flex;
  flex: 1;
  overflow: hidden;
}


/* Chat Area */
.chat-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  position: relative;
  background-color: #fff;
}

.messages-display {
  flex: 1;
  overflow-y: auto;
  padding: 2rem;
  display: flex;
  flex-direction: column;
  gap: 2rem;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}

.message-row {
    display: flex;
    gap: 1rem;
    max-width: 900px;
}
.message-row.user {
    /* No distinct alignment for user in this clean style, relying on avatar/label */
}

.avatar {
    width: 32px;
    height: 32px;
    border-radius: 4px; /* Square-ish for professional look? or Circle */
    background: #f3f4f6;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #4b5563;
    flex-shrink: 0;
    margin-top: 4px;
}
.assistant .avatar {
    background: #eef2ff; /* Very light blue */
    color: #4f46e5;
}
.user .avatar {
    background: #f0fdf4; /* Very light green */
    color: #166534;
}

.message-content {
    flex: 1;
}

.sender-name {
    font-size: 0.75rem;
    font-weight: 600;
    color: #374151;
    margin-bottom: 0.25rem;
}
.time {
    font-weight: 400;
    color: #9ca3af;
    margin-left: 0.5rem;
    font-size: 0.7rem;
}

.bubble {
    font-family: var(--font-sans); /* Use sans for better readability? or keep serif if requested */
    line-height: 1.6;
    color: #1f2937;
    font-size: 0.95rem;
    white-space: pre-wrap; /* Ensure new lines in plain text are preserved if any */
}

.assistant .bubble {
    font-family: var(--font-serif);
}

.user .bubble {
    background: #f9fafb;
    padding: 0.75rem 1rem;
    border-radius: 12px;
    display: inline-block;
}

/* Loader */
.loading span {
    animation: blink 1.4s infinite both;
}
.loading span:nth-child(2) { animation-delay: 0.2s; }
.loading span:nth-child(3) { animation-delay: 0.4s; }
@keyframes blink { 0% { opacity: 0.2; } 20% { opacity: 1; } 100% { opacity: 0.2; } }


/* Input Area */
.input-container {
    padding: 1.5rem 2rem;
    border-top: 1px solid #f3f4f6;
    background: #fff;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.input-box {
    display: flex;
    align-items: flex-end;
    background: #ffffff;
    border: 1px solid #e5e7eb;
    border-radius: 8px;
    padding: 0.5rem;
    box-shadow: 0 2px 4px rgba(0,0,0,0.02);
    transition: border-color 0.2s;
    max-width: 1200px;
    width: 100%;
}
.input-box:focus-within {
    border-color: #9ca3af; /* Darker gray focus */
    box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.main-input {
    font-family: var(--font-sans);
    font-size: 0.95rem;
    color: #1f2937;
    resize: none;
}

.send-btn {
    background: transparent;
    border: none;
    cursor: pointer;
    color: #9ca3af;
    padding: 0.5rem;
    transition: color 0.2s;
}
.send-btn:hover:not(:disabled) {
    color: #4f46e5; /* Accent color */
}
.send-btn:disabled {
    cursor: not-allowed;
    opacity: 0.5;
}

.disclaimer {
    margin-top: 0.5rem;
    font-size: 0.7rem;
    color: #9ca3af;
    text-align: center;
    max-width: 1200px;
    width: 100%;
}

/* Styles for HTML content from backend */
:deep(h1), :deep(h2), :deep(h3) {
    margin-top: 1rem;
    margin-bottom: 0.5rem;
    font-weight: 600;
}
:deep(ul), :deep(ol) {
    padding-left: 1.5rem;
    margin-bottom: 1rem;
}
:deep(p) {
    margin-bottom: 0.75rem;
}
:deep(table) {
    width: 100%;
    border-collapse: collapse;
    margin: 1rem 0;
    font-size: 0.9rem;
    font-family: var(--font-sans);
}
:deep(th) {
    text-align: left;
    padding: 0.5rem;
    border-bottom: 2px solid #e5e7eb;
    color: #6b7280;
    font-weight: 600;
    font-size: 0.75rem;
    text-transform: uppercase;
}
:deep(td) {
    padding: 0.5rem;
    border-bottom: 1px solid #f3f4f6;
    color: #374151;
}
:deep(.positive) { color: #16a34a; }
:deep(.negative) { color: #dc2626; }

</style>

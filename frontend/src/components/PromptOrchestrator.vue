<script setup lang="ts">
import { ref, computed } from 'vue'
import { chatService } from '../services/chatService'
import { 
  CopyOutlined, 
  LoadingOutlined,
  NumberOutlined 
} from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'

// State
const topic = ref('')
const selectedStyle = ref('Without Opinion')
const includeContext = ref(false)
const selectedLanguage = ref('English')
const selectedTone = ref('Practical & Direct')

const isLoading = ref(false)
const result = ref<string | null>(null)

// Options
const styleOptions = [
  { value: 'Without Opinion', label: 'Without Opinion' },
  { value: 'Opinionated', label: 'Opinionated' },
  { value: 'Descriptive', label: 'Descriptive' }
]

const languageOptions = [
  { value: 'English', label: 'English' },
  { value: 'Spanish', label: 'Spanish' },
  { value: 'French', label: 'French' },
  { value: 'German', label: 'German' }
]

const toneOptions = [
  { value: 'Practical & Direct', label: 'Practical & Direct' },
  { value: 'Friendly', label: 'Friendly' },
  { value: 'Professional', label: 'Professional' }
]

// Actions
const handleOrchestrate = async () => {
  if (!topic.value.trim()) {
    message.warning('Please enter an analysis topic.')
    return
  }

  isLoading.value = true
  result.value = null

  // Construct the prompt based on inputs
  // We append these instructions so the backend agent (which accepts natural language)
  // can react to them.
  const fullPrompt = `
    Topic: ${topic.value}
    Style: ${selectedStyle.value}
    Language: ${selectedLanguage.value}
    Tone: ${selectedTone.value}
    Include Background Context: ${includeContext.value}
  `.trim()

  try {
    const rawResponse = await chatService.sendMessage(fullPrompt)
    const { content } = chatService.parseResponse(rawResponse)
    result.value = content
  } catch (err) {
    message.error('Failed to generate orchestration.')
    console.error(err)
  } finally {
    isLoading.value = false
  }
}

const copyToClipboard = () => {
  if (result.value) {
    // Strip HTML tags for copying text, or copy raw? usually text.
    const text = result.value.replace(/<[^>]*>/g, '')
    navigator.clipboard.writeText(text)
    message.success('Copied to clipboard')
  }
}
</script>

<template>
  <div class="orchestrator-layout">
    <!-- Header -->
    <header class="header">
      <div class="header-content">
        <h1>Prompt Orchestrator</h1>
        <div class="tabs">
          <div class="tab active">
            <span class="icon">✨</span> Prompt Orchestrator
          </div>
          <div class="tab">
            <span class="icon">code</span> HTML Viewer
          </div>
        </div>
      </div>
    </header>

    <div class="main-body">
      <!-- Sidebar -->
      <aside class="sidebar">
        <div class="sidebar-section">
          <label class="section-label">ANALYSIS TOPIC <span class="char-count">{{ topic.length }} CHARACTERS</span></label>
          <a-textarea 
            v-model:value="topic"
            placeholder="What shall we deep dive into today? Paste your long analysis data here..."
            :auto-size="{ minRows: 6, maxRows: 12 }"
            class="topic-input"
          />
          <div class="action-row">
             <a-button type="primary" :loading="isLoading" @click="handleOrchestrate" block class="run-btn">
               Orchestrate
             </a-button>
          </div>
        </div>

        <div class="sidebar-section">
          <label class="section-label">STYLE</label>
          <a-select v-model:value="selectedStyle" :options="styleOptions" style="width: 100%" />
        </div>

        <div class="sidebar-section">
          <label class="section-label">CONTEXT</label>
          <a-checkbox v-model:checked="includeContext">Include background context</a-checkbox>
        </div>

        <div class="sidebar-section">
          <label class="section-label">LANGUAGE</label>
          <a-select v-model:value="selectedLanguage" :options="languageOptions" style="width: 100%" />
        </div>

        <div class="sidebar-section">
          <label class="section-label">TONE</label>
          <a-select v-model:value="selectedTone" :options="toneOptions" style="width: 100%" />
        </div>
      </aside>

      <!-- Main Content -->
      <main class="content-area">
        <div class="content-header" v-if="result">
          <a-button class="copy-btn" shape="round" @click="copyToClipboard">
            <template #icon><CopyOutlined /></template>
            Copy Prompt
          </a-button>
        </div>
        
        <div class="content-display">
          <!-- Empty State -->
          <div v-if="!result && !isLoading" class="empty-state">
            <div class="hash-icon">#</div>
            <p>Waiting for your topic to begin the orchestration...</p>
          </div>

          <!-- Loading State -->
          <div v-else-if="isLoading" class="loading-state">
            <LoadingOutlined :style="{ fontSize: '48px', color: '#e5e7eb' }" spin />
            <p>Orchestrating...</p>
          </div>

          <!-- Result State -->
          <div v-else class="result-container" v-html="result"></div>
        </div>
      </main>
    </div>
  </div>
</template>

<style scoped>
.orchestrator-layout {
  display: flex;
  flex-direction: column;
  height: 100vh;
  width: 100vw;
  background-color: #ffffff;
}

/* Header */
.header {
  padding: 2rem 3rem 0 3rem;
  border-bottom: 1px solid transparent; /* Image doesn't show a hard line immediately maybe? */
}

.header h1 {
  font-family: var(--font-serif);
  font-size: 2.5rem;
  margin-bottom: 1.5rem;
  color: #1f2937;
}

.tabs {
  display: flex;
  gap: 2rem;
  border-bottom: 1px solid #e5e7eb;
}

.tab {
  padding-bottom: 0.75rem;
  cursor: pointer;
  font-size: 0.9rem;
  font-weight: 500;
  color: #6b7280;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  border-bottom: 2px solid transparent;
  transition: all 0.2s;
}

.tab.active {
  color: #1f2937;
  border-bottom-color: var(--accent-color);
}

.tab .icon {
  font-family: 'Material Icons', sans-serif; /* Fallback if using icon font */
  font-size: 1.1em;
}

/* Main Body */
.main-body {
  display: flex;
  flex: 1;
  overflow: hidden;
}

/* Sidebar */
.sidebar {
  width: 320px;
  padding: 2rem;
  border-right: 1px solid #f3f4f6;
  display: flex;
  flex-direction: column;
  gap: 2rem;
  overflow-y: auto;
}

.sidebar-section {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.section-label {
  font-size: 0.7rem;
  font-weight: 700;
  color: #9ca3af; /* Gray-400 */
  letter-spacing: 0.05em;
  text-transform: uppercase;
  display: flex;
  justify-content: space-between;
}

.char-count {
  font-weight: 400;
  background: #f3f4f6;
  padding: 2px 6px;
  border-radius: 4px;
}

/* Inputs Override */
.topic-input {
  background-color: #f9fafb;
  border-color: #f3f4f6;
  padding: 1rem;
  font-family: var(--font-sans);
  color: #374151;
  font-size: 0.9rem;
  resize: none;
}
.topic-input:hover, .topic-input:focus {
  background-color: #fff;
}

.run-btn {
  margin-top: 1rem;
}

/* Content Area */
.content-area {
  flex: 1;
  padding: 2rem;
  background-color: #fff;
  position: relative;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
}

.content-header {
  position: absolute;
  top: 2rem;
  left: 2rem;
  z-index: 10;
}

.copy-btn {
  background-color: #6b7280;
  color: white;
  border: none;
  font-size: 0.8rem;
  font-weight: 500;
}
.copy-btn:hover {
  background-color: #4b5563;
  color: white;
}

.content-display {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.empty-state, .loading-state {
  text-align: center;
  color: #e5e7eb; /* Very light gray */
}

.hash-icon {
  font-size: 4rem;
  font-family: var(--font-serif);
  font-weight: 300;
  margin-bottom: 1rem;
}

.empty-state p, .loading-state p {
  font-family: var(--font-serif);
  font-style: italic;
  color: #d1d5db; /* Gray-300 */
  font-size: 1.1rem;
}

.result-container {
  width: 100%;
  max-width: 800px;
  margin: 0 auto;
  font-family: var(--font-serif); /* Serif for the content usually looks nice */
  line-height: 1.6;
  color: #374151;
}

/* Scrollbar for sidebar */
.sidebar::-webkit-scrollbar {
  width: 4px;
}
.sidebar::-webkit-scrollbar-thumb {
  background-color: #e5e7eb;
  border-radius: 4px;
}
</style>

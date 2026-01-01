import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api/investment'

export interface ChatMessage {
    id: string
    role: 'user' | 'assistant'
    content: string
    timestamp: Date
    type?: string
    data?: any
}



class ChatService {
    async sendMessage(question: string): Promise<string> {
        try {
            const response = await axios.get(API_BASE_URL, {
                params: { question },
            })
            return response.data
        } catch (error) {
            console.error('Error sending message:', error)
            throw new Error('Failed to get response from the server')
        }
    }

    parseResponse(response: string): { content: string } {
        return { content: response }
    }
}

export const chatService = new ChatService()

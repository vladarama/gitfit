
import { defineStore } from 'pinia';
import { createSession } from '../api.js';

export const useSessionStore = defineStore({
  id: 'sessions',
  state: () => ({
    sessions: [],
  }),
  actions: {
    async createSession(sessionData) {
      try {
        const response = await createSession(sessionData);
        this.sessions.push(response.data);
        return response; // Assuming success case returns response directly
      } catch (error) {
        if (error.response && error.response.data.errors) {
          // Assuming error.response.data.errors is populated based on ErrorDto
          const errorMessages = error.response.data.errors.join(', '); // Join all errors into a single string
          throw new Error(errorMessages);
        } else {
          // Handle other types of errors (network issues, etc.)
          throw new Error("Failed to create session due to network or configuration error.");
        }
      }
    },
  },
});

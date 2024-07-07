import { defineStore } from 'pinia';
import { fetchSessions, fetchFilteredSessions, fetchSessionById } from '../api.js';

export const useSessionStore = defineStore({
    id: 'session',
    state: () => ({
        sessions: [],
        session: null
    }),
    actions: {
        async fetchAndSetSessions() {
            try {
                const response = await fetchSessions();
                this.sessions = response.data;
            } catch (error) {
                console.error(error);
            }
        },
        async fetchAndSetFilteredSessions(filter) {
            try {
                const response = await fetchFilteredSessions(filter);
                this.sessions = response.data;
            } catch (error) {
                console.error(error);
            }
        },
        async fetchAndSetSessionById(id) {
            try {
                const response = await fetchSessionById(id);
                this.session = response.data;
            } catch (error) {
                console.error(error);
            }
        }
    },
});
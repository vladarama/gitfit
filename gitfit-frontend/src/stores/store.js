import { defineStore } from 'pinia';
import { fetchFitnessClasses, createCustomer, checkLogin } from '../api.js'; // Update the import route to api.js

export const useStore = defineStore({
    id: 'login',
    state: () => ({
        fitnessClasses: [],
        loggedInUser: null,
        userRole: '',
    }),
    actions: {
        async fetchAndSetFitnessClasses() {
            try {
                const response = await fetchFitnessClasses();
                console.log(response.data);
                this.fitnessClasses = response.data;
            } catch (error) {
                console.error(error);
            }
        },
        async checkLogin(username, password) {
            try {
                const response = await checkLogin(username, password);
                return response;
            } catch (error) {
                return error.response;
            }
        },
        updateUserRole(role) {
            this.userRole = role;
        }
    },
});
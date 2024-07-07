import { defineStore } from 'pinia';
import { createRegistration, fetchRegistrations, fetchRegistrationsByCustomerUsername, deleteRegistation } from '../api.js';

export const useRegistrationStore = defineStore({
  id: 'registrations',
  state: () => ({
    registrations: [],
  }),
  actions: {
    async fetchRegistrations() {
      try {
        const response = await fetchRegistrations();
        this.registrations = response.data;
      } catch (error) {
        console.error(error);
      }
    },
    async fetchRegistrationsByCustomerUsername(username) {
      try {
        const response = await fetchRegistrationsByCustomerUsername(username);
        this.registrations = response.data;
      } catch (error) {
        console.error(error);
      }
    },
    async createRegistration(registrationData) {
        try {
            const response = await createRegistration(registrationData);
            this.registrations.push(response.data);
            return true;
        } catch (error) {
            console.error(error);
            if (error.response && error.response.data.errors) {
              // Assuming error.response.data.errors is populated based on ErrorDto
              const errorMessages = error.response.data.errors.join(', '); // Join all errors into a single string
              throw new Error(errorMessages);
            }
        }
    },
    async deleteRegistration(registrationId) {
      try {
        await deleteRegistation(registrationId);
      } catch (error) {
        console.error(error);
      }
    }
  },
});

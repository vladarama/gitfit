
import { defineStore } from 'pinia';
import { createFitnessClass } from '../api.js';

export const useFitnessClassStore = defineStore({
  id: 'fitnessClasses',
  state: () => ({
    fitnessClasses: [],
  }),
  actions: {
    async createFitnessClass(fitnessClassData) {
      try {
        const response = await createFitnessClass(fitnessClassData);
        this.fitnessClasses.push(response.data);
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

import { defineStore } from 'pinia';
import {
    fetchFitnessClasses,
    deleteFitnessClass,
    fetchApprovedFitnessClasses,
    fetchPendingFitnessClasses,
} from '../api.js'; // Update the import route to api.js


export const useHomeStore = defineStore({
    id: 'main',
    state: () => ({
        fitnessClasses: [],
        approvedFitnessCLasses: [],
        pendingFitnessClasses: [],
    }),
    actions: {
        async fetchAndSetFitnessClasses() {
            try {
                const response = await fetchFitnessClasses();
                this.fitnessClasses = response.data;
            } catch (error) {
                console.error(error);
            }
        },
        async deleteFitnessClass(name) {
            try {
                await deleteFitnessClass(name);
                this.fetchAndSetFitnessClasses();
            } catch (error) {
                console.error(error);
            }
        },
        async fetchAndSetApprovedFitnessClasses() {
            try {
                const response = await fetchApprovedFitnessClasses();
                this.approvedFitnessClasses = response.data;
            } catch (error) {
                console.error(error);
            }
        },

        async fetchAndSetPendingFitnessClasses() {
            try {
                const response = await fetchPendingFitnessClasses();
                this.pendingFitnessClasses = response.data;
            } catch (error) {
                console.error(error);
            }
        }
    },
});
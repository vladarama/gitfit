import {defineStore} from 'pinia';
import { createInstructor, fetchInstructors, fetchInstructor, updateInstructorPassword, deleteInstructor } from '@/api';

export const useInstructorStore = defineStore({
    id: 'instructor',
    state: () => ({
        instructor: JSON.parse(localStorage.getItem('instructor')) || null,
        instructorLookup: {},
    }),
    actions: {
        async createInstructor(instructor) {
            try {
                const response = await createInstructor(instructor);
                return response;
            } catch (error) {
                console.log(error);
                console.log("error store");
                return error.response;
            }
        },
        async updateInstructorPassword(password) {
            try {
                console.log('Updating instructor password');
                const response = await updateInstructorPassword(password);
                console.log(response);
                console.log("REACHED HERE");
                localStorage.setItem('instructor', JSON.stringify(response.data));
                this.updateInstructorFromLocalStorage();
                return response;
            } catch (error) {
                return error.response;
            }
        },
        updateInstructorFromLocalStorage() {
            this.instructor = JSON.parse(localStorage.getItem('instructor'))||null;
        },
        async fetchAndSetInstructor(username) {
            try {
                const response = await fetchInstructor(username);
                localStorage.setItem('instructor', JSON.stringify(response.data));
                localStorage.setItem('userType', 'Instructor');
                this.updateInstructorFromLocalStorage();
            } catch (error) {
                console.error(error);
            }
        },
        async fetchInstructors() {
            try {
              const response = await fetchInstructors();
              this.instructors = response.data;
              this.instructors.forEach(instructor => {
                this.instructorLookup[instructor.username] = instructor;
            });
            } catch (error) {
              console.error(error);
            }
          },
          async deleteInstructor(username) {
            try {
              await deleteInstructor(username);
              this.fetchInstructors();
            } catch (error) {
              console.error(error);
            }
          },
    },
});

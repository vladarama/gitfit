import {defineStore} from 'pinia';
import { createCustomer, updateCustomerPassword, fetchCustomer } from '@/api';

export const useCustomerStore = defineStore({
    id: 'customer',
    state: () => ({
        customer: JSON.parse(localStorage.getItem('customer')) || null,
    }),
    actions: {
        async fetchAndSetCustomer(username) {
            try {
                console.log("[[[[[[[[[[");
                console.log(username);
                const response = await fetchCustomer(username);
                localStorage.setItem('customer', JSON.stringify(response.data));
                localStorage.setItem('userType', 'Customer');
                this.updateCustomerFromLocalStorage();
                console.log("update localstorage complete")
            } catch (error) {
                console.log("***********************")
                console.error(error);
            }
        },
        async createCustomer(customer) {
            try {
                console.log('Creating customer', customer);
                const response = await createCustomer(customer);
                console.log("no error in creating customer");
                localStorage.setItem('customer', JSON.stringify(response.data));
                localStorage.setItem('userType', 'Customer');
                this.updateCustomerFromLocalStorage();
                // this.customer = response.data;
                console.log(this.customer);
                return response;
            } catch (error) {
                console.log(error);
                console.log("error store");
                return error.response;
            }
        },
        async updateCustomerPassword(username, password) {
            try {
                console.log('Updating customer password');
                const response = await updateCustomerPassword(username, password);
                console.log(response)
                localStorage.setItem('customer', JSON.stringify(response.data));
                this.updateCustomerFromLocalStorage();
                return response;
            } catch (error) {
                return error.response;
            }
        },
        updateCustomerFromLocalStorage() {
            this.customer = JSON.parse(localStorage.getItem('customer'))||null;
        },
    },
});
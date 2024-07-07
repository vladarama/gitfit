<template>
    <ErrorModal :show="showModal" :message="errorMessage" @update:show="showModal = $event" />
    <div class="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
      <div class="sm:mx-auto sm:w-full sm:max-w-sm">
        <h2 class="mt-32 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">Billing Information</h2>
      </div>
  
      <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
        <form id="billingForm" class="space-y-6" action="#" method="POST" @submit.prevent="createBilling">
          <div>
            <label for="billingCardNumber" class="block text-sm font-medium leading-6 text-gray-900">Card Number</label>
            <div class="mt-2">
              <input id="billingCardNumber" name="billingCardNumber" type="text" autocomplete="email" required="" class="p-2 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-custom-blue focus:outline-none sm:text-sm sm:leading-6" />
            </div>
          </div>
  
          <div>
            <div class="flex items-center justify-between">
              <label for="billingCountry" class="block text-sm font-medium leading-6 text-gray-900">Country</label>
            </div>
            <div class="mt-2">
              <input id="billingCountry" name="billingCountry" type="text" autocomplete="current-password" required="" class="p-2 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-custom-blue focus:outline-none sm:text-sm sm:leading-6" />
            </div>
          </div>

          <div>
            <label for="billingState" class="block text-sm font-medium leading-6 text-gray-900">Province/State</label>
            <div class="mt-2">
              <input id="billingState" name="billingState" type="text" required="" class="p-2 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-custom-blue focus:outline-none sm:text-sm sm:leading-6" />
            </div>
          </div>

          <div>
            <label for="billingPostalCode" class="block text-sm font-medium leading-6 text-gray-900">Postal Code</label>
            <div class="mt-2">
              <input id="billingPostalCode" name="billingPostalCode" type="text" required="" class="p-2 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-custom-blue focus:outline-none sm:text-sm sm:leading-6" />
            </div>
          </div>
  
          <div>
            <label for="billingAddress" class="block text-sm font-medium leading-6 text-gray-900">Street Address</label>
            <div class="mt-2">
              <input id="billingAddress" name="billingAddress" type="text" required="" class="p-2 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-custom-blue focus:outline-none sm:text-sm sm:leading-6" />
            </div>
          </div>
          <div>
            <button type="submit" class="mt-10 flex w-full justify-center rounded-md bg-custom-blue px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-custom-dark-blue focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Comfirm</button>
          </div>
          
        </form>
        <div>
            <button type="submit" class="mt-10 flex w-full justify-center rounded-md bg-red-500 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-red-600 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600" @click="editAccount">Cancel</button>
        </div>
        
      </div>
    </div>
  </template>

<script setup>
import ErrorModal from '@/components/ErrorModal.vue';
import { useCustomerStore } from '@/stores/customerStore';
import { useBillingStore } from '@/stores/billingStore';
import { defineEmits, ref } from 'vue';

const showModal = ref(false);
const errorMessage = ref('');
const emit = defineEmits(['editAccount']);

const editAccount = () => {
    var billingForm = document.getElementById("billingForm");
    billingForm.reset();
    emit('editAccount');
}

const createBilling = async () => {
    const customerStore = useCustomerStore();
    const billingStore = useBillingStore();
    const billing = {
        country: billingCountry.value,
        state: billingState.value,
        postalCode: billingPostalCode.value,
        address: billingAddress.value,
        cardNumber: billingCardNumber.value,
        username: customerStore.customer.username
    }
    const response = await billingStore.createBilling(billing);
    if (response.status===200) {
        editAccount();
    } else {
        errorMessage.value = response.data.errors[0];
        showModal.value = true;
    }



}


</script>

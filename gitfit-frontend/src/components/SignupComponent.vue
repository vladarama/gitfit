<template>
    <ErrorModal :show="showModal" :message="errorMessage" @update:show="showModal = $event" />    
    <div class="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
      <div class="sm:mx-auto sm:w-full sm:max-w-sm">
        <h2 class="mt-32 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">Create your account</h2>
      </div>
  
      <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
        <form id="signupForm" class="space-y-6" action="#" method="POST" @submit.prevent="signup">
          <div>
            <label for="customerSignupEmail" class="block text-sm font-medium leading-6 text-gray-900">Email address</label>
            <div class="mt-2">
              <input id="customerSignupEmail" name="customerSignupEmail" type="email" autocomplete="email" required="" class="p-2 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-custom-blue focus:outline-none sm:text-sm sm:leading-6" />
            </div>
          </div>
  
          <div>
            <div class="flex items-center justify-between">
              <label for="customerSignupPassword" class="block text-sm font-medium leading-6 text-gray-900">Password</label>
            </div>
            <div class="mt-2">
              <input id="customerSignupPassword" name="customerSignupPassword" type="password" autocomplete="current-password" required="" class="p-2 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-custom-blue focus:outline-none sm:text-sm sm:leading-6" />
            </div>
          </div>

          <div>
            <label for="customerSignupFirstName" class="block text-sm font-medium leading-6 text-gray-900">First Name</label>
            <div class="mt-2">
              <input id="customerSignupFirstName" name="customerSignupFirstName" type="text" required="" class="p-2 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-custom-blue focus:outline-none sm:text-sm sm:leading-6" />
            </div>
          </div>

          <div>
            <label for="customerSignupLastName" class="block text-sm font-medium leading-6 text-gray-900">Last name</label>
            <div class="mt-2">
              <input id="customerSignupLastName" name="customerSignupLastName" type="text" required="" class="p-2 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-custom-blue focus:outline-none sm:text-sm sm:leading-6" />
            </div>
          </div>
  
          <div>
            <label for="customerSignupUsername" class="block text-sm font-medium leading-6 text-gray-900">Username</label>
            <div class="mt-2">
              <input id="customerSignupUsername" name="customerSignupUsername" type="text" required="" class="p-2 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-custom-blue focus:outline-none sm:text-sm sm:leading-6" />
            </div>
          </div>
          <div>
            <button type="submit" class="mt-10 flex w-full justify-center rounded-md bg-custom-blue px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-custom-dark-blue focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Create account</button>
          </div>
        </form>
  
        <p class="mt-5 text-center text-sm text-gray-500">
          Already have an account?
          {{ ' ' }}
          <a href="#" class="font-semibold leading-6 text-custom-blue hover:text-custom-dark-blue" @click="updateForm">Log In!</a>
        </p>
      </div>
    </div>
  </template>

<script setup>
import router from '@/router';
import ErrorModal from '@/components/ErrorModal.vue';
import { useCustomerStore } from '@/stores/customerStore';
import { useStore } from '@/stores/store';
import { defineEmits, ref } from 'vue';

const showModal = ref(false);
const errorMessage = ref('');
const emit = defineEmits(['updateForm']);

const updateForm = () => {
    emit('updateForm');
};

const store = useStore();

const signup = async () => {
    localStorage.clear();
    const customer = {
        email: customerSignupEmail.value,
        password: customerSignupPassword.value,
        firstName: customerSignupFirstName.value,
        lastName: customerSignupLastName.value,
        username: customerSignupUsername.value
    };
    const customerStore = useCustomerStore();
    const response = await customerStore.createCustomer(customer);
    if (response.status === 200) {
        store.updateUserRole('Customer');
        var signupForm = document.getElementById("signupForm");
        signupForm.reset();
        router.push('/');
    } else {
        errorMessage.value = response.data.errors[0];
        showModal.value = true;
    }
    
}

</script>

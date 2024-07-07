<template>
    <ErrorModal :show="showModal" :message="errorMessage" @update:show="showModal = $event" />
    <div class="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
      <div class="sm:mx-auto sm:w-full sm:max-w-sm">
        <h2 class="mt-32 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">Modify your password</h2>
      </div>
  
      <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
        <form class="space-y-6" action="#" method="POST" @submit.prevent="update">
          <div>
            <label for="updatePassword" class="block text-sm font-medium leading-6 text-gray-900">New password</label>
            <div class="mt-2">
              <input id="newPassword" name="newPassword" type="password" required="" class="p-2 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-custom-blue focus:outline-none sm:text-sm sm:leading-6" />
            </div>
          </div>
  
          <div>
            <button type="submit" class="mt-10 flex w-full justify-center rounded-md bg-custom-blue px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-custom-dark-blue focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600" @click="updatePassword">Update Password</button>
          </div>
        </form>
        <div>
            <button type="submit" class="mt-10 flex w-full justify-center rounded-md bg-red-500 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-red-600 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600" @click="editAccount">Cancel</button>
        </div>
      </div>
    </div>
  </template>


<script setup>
import { useCustomerStore } from '@/stores/customerStore';
import { useInstructorStore } from '@/stores/instructorStore';
import { useOwnerStore } from '@/stores/ownerStore';
import { defineEmits, ref } from 'vue';
import ErrorModal from '@/components/ErrorModal.vue';

const showModal = ref(false);
const errorMessage = ref('');
const emit = defineEmits(['editAccount']);

const editAccount = () => {
    emit('editAccount');
}

const updatePassword = async () => {
    const customerStore = useCustomerStore();
    const ownerStore = useOwnerStore();
    const instructorStore = useInstructorStore();
    const userType = localStorage.getItem('userType');
    if (userType === 'Customer') {
        const password = {
            password: newPassword.value,
            username: customerStore.customer.username
        }
        const response = await customerStore.updateCustomerPassword(password);
        if (response.status===200) {
            editAccount();
        } else {
            errorMessage.value = response.data.errors[0];
            showModal.value = true;
        }
    } else if (userType === 'Owner') {
        const password = newPassword.value;
        const response = await ownerStore.updateOwnerPassword(password);
        if (response.status===200) {
            editAccount();
        } else {
            errorMessage.value = response.data.errors[0];
            showModal.value = true;
        }
    } else if (userType === 'Instructor') {
        const password = {
            password: newPassword.value,
            username: instructorStore.instructor.username
        }
        const response = await instructorStore.updateInstructorPassword(password);
        if (response.status===200) {
            editAccount();
        } else {
            errorMessage.value = response.data.errors[0];
            showModal.value = true;
        }
    }
}


</script>

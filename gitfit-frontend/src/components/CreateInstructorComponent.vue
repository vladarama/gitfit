<template>
  <ErrorModal :show="showModal" :message="errorMessage" @update:show="showModal = $event" />
  <div class="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
    <div class="sm:mx-auto sm:w-full sm:max-w-sm">
      <h2 class="mt-32 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">Create new instructor
        account</h2>
    </div>

    <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
      <form class="space-y-6" action="#" method="POST" @submit.prevent="create">
        <div>
          <label for="createInstructorEmail" class="block text-sm font-medium leading-6 text-gray-900">Email
            address</label>
          <div class="mt-2">
            <input id="createInstructorEmail" name="createInstructorEmail" type="email" autocomplete="email" required=""
              class="p-2 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-custom-blue focus:outline-none sm:text-sm sm:leading-6" />
          </div>
        </div>

        <div>
          <div class="flex items-center justify-between">
            <label for="createInstructorPassword"
              class="block text-sm font-medium leading-6 text-gray-900">Password</label>
          </div>
          <div class="mt-2">
            <input id="createInstructorPassword" name="createInstructorPassword" type="password"
              autocomplete="current-password" required=""
              class="p-2 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-custom-blue focus:outline-none sm:text-sm sm:leading-6" />
          </div>
        </div>

        <div>
          <label for="createInstructorFirstName" class="block text-sm font-medium leading-6 text-gray-900">First
            Name</label>
          <div class="mt-2">
            <input id="createInstructorFirstName" name="createInstructorFirstName" type="text" required=""
              class="p-2 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-custom-blue focus:outline-none sm:text-sm sm:leading-6" />
          </div>
        </div>

        <div>
          <label for="createInstructorLastName" class="block text-sm font-medium leading-6 text-gray-900">Last
            name</label>
          <div class="mt-2">
            <input id="createInstructorLastName" name="createInstructorLastName" type="text" required=""
              class="p-2 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-custom-blue focus:outline-none sm:text-sm sm:leading-6" />
          </div>
        </div>

        <div>
          <label for="createInstructorUsername"
            class="block text-sm font-medium leading-6 text-gray-900">Username</label>
          <div class="mt-2">
            <input id="createInstructorUsername" name="createInstructorUsername" type="text" required=""
              class="p-2 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-custom-blue focus:outline-none sm:text-sm sm:leading-6" />
          </div>
        </div>
        <div>
          <button type="submit"
            class="mt-10 flex w-full justify-center rounded-md bg-custom-blue px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-custom-dark-blue focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Create
            Instructor Account</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import router from '@/router';
import { useInstructorStore } from '@/stores/instructorStore';
import { defineEmits, ref } from 'vue';
import ErrorModal from '@/components/ErrorModal.vue';

const showModal = ref(false);
const errorMessage = ref('');

const create = async () => {
  const instructor = {
    email: createInstructorEmail.value,
    password: createInstructorPassword.value,
    firstName: createInstructorFirstName.value,
    LastName: createInstructorLastName.value,
    username: createInstructorUsername.value
  };
  const instructorStore = useInstructorStore();
  const response = await instructorStore.createInstructor(instructor);
  if (response.status === 200) {
    router.push('/');
  } else {
    errorMessage.value = response.data.errors[0];
    showModal.value = true;
  }

}

</script>
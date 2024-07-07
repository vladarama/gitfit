<template>
  <div class="instructor-card flex flex-col bg-white rounded-xl shadow-md overflow-hidden md:max-w-2xl m-3 relative" @click.stop="toggleDropdown">
    <button class="absolute top-0 right-0 m-2 text-xl font-semibold" @click.stop="deleteInstructorHandler">X</button>
    <div class="p-8">
      <div class="uppercase tracking-wide text-sm text-indigo-500 font-semibold">
        {{ instructor.username }}
      </div>
      <!-- Additional details shown when expanded -->
      <p class="mt-2 text-gray-500">First Name: {{ instructor.firstName }}</p>
      <p class="mt-2 text-gray-500">Last Name: {{ instructor.lastName }}</p>
      <p class="mt-2 text-gray-500">Email: {{ instructor.email }}</p>
      <p class="mt-2 text-gray-500">Password: {{ instructor.password }}</p>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from 'vue';
import { useInstructorStore } from '@/stores/instructorStore';

const props = defineProps({
  instructor: {
    type: Object,
    required: true,
  },
});

const deleteInstructorHandler = async () => {
  const store = useInstructorStore();
  await store.deleteInstructor(props.instructor.username);
};

</script>

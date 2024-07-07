<script setup>
import { ref } from 'vue';
import { useInstructorStore } from '@/stores/instructorStore';


const emits = defineEmits(['create-session']);

const instructorStore = useInstructorStore();
const instructor = instructorStore.instructor;

const formData = ref({
  price: '',
  startTime: '',
  endTime: '',
  date: '',
  instructorUsername: instructor.username,
  fitnessClassName: '',
});

const handleSubmit = () => {
  emits('create-session', formData.value);
};

</script>

<template>
    <div class="w-full max-w-lg p-8 bg-white rounded-xl shadow-xl">
      <form @submit.prevent="handleSubmit" class="space-y-6">
        <!-- Input for Price with a label -->
        <div>
          <label for="price" class="text-sm font-medium text-gray-700">Price</label>
          <input v-model="formData.price" type="number" id="price" placeholder="Enter price" class="mt-1 block w-full px-4 py-2 rounded-md border border-spindle focus:border-persianblue focus:ring focus:ring-persianblue focus:ring-opacity-50" />
        </div>
        <!-- Input for Start Time with a label -->
        <div>
          <label for="startTime" class="text-sm font-medium text-gray-700">Start Time</label>
          <input v-model="formData.startTime" type="time" id="startTime" class="mt-1 block w-full px-4 py-2 rounded-md border border-spindle focus:border-persianblue focus:ring focus:ring-persianblue focus:ring-opacity-50" />
        </div>
        <!-- Input for End Time with a label -->
        <div>
          <label for="endTime" class="text-sm font-medium text-gray-700">End Time</label>
          <input v-model="formData.endTime" type="time" id="endTime" class="mt-1 block w-full px-4 py-2 rounded-md border border-spindle focus:border-persianblue focus:ring focus:ring-persianblue focus:ring-opacity-50" />
        </div>
        <!-- Input for Date with a label -->
        <div>
          <label for="date" class="text-sm font-medium text-gray-700">Date</label>
          <input v-model="formData.date" type="date" id="date" class="mt-1 block w-full px-4 py-2 rounded-md border border-spindle focus:border-persianblue focus:ring focus:ring-persianblue focus:ring-opacity-50" />
        </div>
        <!-- Input for Fitness Class Name with a label -->
        <div>
          <label for="fitnessClass" class="text-sm font-medium text-gray-700">Fitness Class</label>
          <input v-model="formData.fitnessClassName" type="text" id="fitnessClass" placeholder="Enter fitness class name" class="mt-1 block w-full px-4 py-2 rounded-md border border-spindle focus:border-persianblue focus:ring focus:ring-persianblue focus:ring-opacity-50" />
        </div>
        <!-- Submit button -->
        <button type="submit" class="mt-4 px-4 py-2 bg-persianblue text-white font-semibold rounded-md w-full hover:bg-moodyblue focus:outline-none focus:ring-2 focus:ring-spindle focus:ring-offset-2 transition-colors">
          Create Session
        </button>
      </form>
    </div>
</template>

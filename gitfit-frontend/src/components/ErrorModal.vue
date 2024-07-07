<template>
    <div v-if="show" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full" @click.self="close">
      <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
        <div class="mt-3 text-center">
          <h3 class="text-lg leading-6 font-medium text-gray-900">Error</h3>
          <div class="mt-2 px-7 py-3">
            <p class="text-sm text-gray-500">{{ message }}</p>
          </div>
          <div class="items-center px-4 py-3">
            <button id="ok-btn" @click="close" class="px-4 py-2 bg-persianblue text-white text-base font-medium rounded-md w-full shadow-sm hover:bg-moodyblue focus:outline-none focus:ring-2 focus:ring-spindle">
              OK
            </button>
          </div>
        </div>
      </div>
    </div>
  </template>

  <script setup>
  import { ref, defineProps, watch } from 'vue';
  
  const props = defineProps({
    show: Boolean,
    message: String,
  });

  const emits = defineEmits(['update:show']);
  
  const show = ref(props.show);
  
  const close = () => {
    show.value = false;
    emits('update:show', false);
  };
  
  watch(() => props.show, (newShow) => {
    show.value = newShow;
  });
  </script>
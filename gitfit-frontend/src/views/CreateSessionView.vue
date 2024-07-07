<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import ErrorModal from '@/components/ErrorModal.vue';
import SessionCreationSuccessModal from '@/components/SessionCreationSuccessModal.vue';
import SessionCreationForm from '@/components/SessionCreationForm.vue';
import { useSessionStore } from '@/stores/sessionCreationStore';

const router = useRouter();


const sessionStore = useSessionStore();
const showModal = ref(false);
const showSuccessModal = ref(false);
const errorMessage = ref('');
const message = ref('Successfully created session.');

const handleSessionCreation = async (sessionData) => {
  try {
    const response = await sessionStore.createSession(sessionData);
    if (response) {
      showSuccessModal.value = true;
    }
  } catch (error) {
    errorMessage.value = error.message;
    showModal.value = true;
  }
};

const closeSuccessModalAndRedirect = () => {
  showSuccessModal.value = false;
  router.push('/sessions'); // Redirect to sessions
};

</script>



<template>
  <div class="min-h-screen bg-linkwater pt-12 pb-8 px-4 sm:px-6 lg:px-8">
    <div class="grid grid-cols-1 md:grid-cols-2 gap-8 w-full max-w-6xl mx-auto">
      
      <!-- Left Column for title and image -->
      <div class="justify-center items-center md:items-start">
        <!-- Title -->
        <h1 class="text-4xl font-bold text-persianblue mb-20 md:mb-6 lg:mb-8 md:text-5xl lg:text-6xl">
          Create New Session
        </h1>
        <!-- Image container -->
        <div>
          <img src="@/assets/sessionCreationImg.jpg" alt="Session" class="w-full h-auto rounded-lg shadow-lg">
        </div>
      </div>
      
      <!-- Right Column for form -->
      <div class="flex flex-col justify-center">
        <SessionCreationForm @create-session="handleSessionCreation" />
        <!-- Modals -->
        <ErrorModal :show="showModal" :message="errorMessage" @update:show="showModal = $event" />
        <SessionCreationSuccessModal :show="showSuccessModal" :message="message" @update:show="closeSuccessModalAndRedirect" />
      </div>

    </div>
  </div>
</template>
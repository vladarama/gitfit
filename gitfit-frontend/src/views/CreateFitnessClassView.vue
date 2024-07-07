<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import ErrorModal from '@/components/ErrorModal.vue';
import FitnesClassCreationSuccessModal from '@/components/FitnessClassCreationSuccessModal.vue';
import FitnessClassCreationForm from '@/components/FitnessClassCreationForm.vue';
import { useFitnessClassStore } from '@/stores/fitnessClassCreationStore';

const router = useRouter();

const fitnessClassStore = useFitnessClassStore();
const showModal = ref(false);
const showSuccessModal = ref(false);
const errorMessage = ref('');
const message = ref('Fitness class proposed. Owner will review it.');

const handleFitnessClassCreation = async (fitnessClassData) => {
  try {
    const response = await fitnessClassStore.createFitnessClass(fitnessClassData);
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
  router.push('/'); // Redirect to home page
};

</script>

<template>
  <div class="min-h-screen bg-linkwater pt-12 pb-8 px-4 sm:px-6 lg:px-8">
    <div class="w-full max-w-6xl mx-auto">

      <!-- Title with reduced margin-bottom or no margin -->
      <h1 class="text-4xl font-bold text-persianblue mb-12 md:text-5xl lg:text-6xl">
        Propose New Class
      </h1>
      
      <!-- Content container for image and form -->
      <div class="flex flex-col md:flex-row items-center justify-between gap-8">

        <!-- Image container -->
        <div class="md:w-1/2">
          <img src="@/assets/fitnessClassCreationImg.jpg" alt="Fitness Class" class="w-full h-auto rounded-lg shadow-lg">
        </div>

        <!-- Form container -->
        <div class="md:w-1/2">
          <FitnessClassCreationForm @create-fitnessClass="handleFitnessClassCreation" />
        </div>

      </div>
    </div>
    <ErrorModal :show="showModal" :message="errorMessage" @update:show="showModal = $event" />
    <FitnesClassCreationSuccessModal :show="showSuccessModal" :message="message" @update:show="closeSuccessModalAndRedirect" />
  </div>
</template>

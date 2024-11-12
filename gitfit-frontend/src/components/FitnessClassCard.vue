<script setup>
import { updateFitnessClassStatus } from "@/api";
import { useStore } from "@/stores/store";
import { defineProps, computed, ref, onMounted } from "vue";

const props = defineProps({
  fitnessClass: {
    type: Object,
    required: true,
  },
});

const emit = defineEmits(["update"]);

const store = useStore();
const userRole = computed(() => store.userRole);

const approveClass = async () => {
  await updateFitnessClassStatus(props.fitnessClass.name, "APPROVED");
  emit("update");
};

const rejectClass = async () => {
  await updateFitnessClassStatus(props.fitnessClass.name, "REJECTED");
  emit("update");
};

const imageUrl = ref("");

async function fetchImage() {
  try {
    const response = await fetch(
      `https://api.pexels.com/v1/search?query=${encodeURIComponent(
        props.fitnessClass.name
      )}+fitness&per_page=1`,
      {
        headers: {
          Authorization: import.meta.env.VITE_PEXELS_API_KEY,
        },
      }
    );
    const data = await response.json();
    imageUrl.value = data.photos[0]?.src.large || "/default-fitness-image.jpg";
  } catch (error) {
    console.error("Error fetching image:", error);
    imageUrl.value = "/default-fitness-image.jpg";
  }
}

onMounted(() => {
  fetchImage();
});
</script>

<template>
  <div
    class="overflow-hidden shadow-lg rounded-lg h-auto w-full md:w-full cursor-pointer m-auto transform transition-transform duration-500 hover:scale-105 hover:shadow-xl"
  >
    <a href="#" class="w-full block h-full">
      <img
        :src="imageUrl"
        alt="fitness class"
        class="max-h-80 w-full object-cover rounded-t-lg transform transition-transform duration-500 hover:scale-110"
      />
      <div class="bg-linkwater w-full p-8">
        <p class="text-persianblue text-2xl font-medium">
          {{ fitnessClass.name }}
        </p>
        <p class="text-moodyblue text-xl font-medium mb-2">
          {{ fitnessClass.description }}
        </p>
        <div v-if="userRole === 'Owner'" class="flex items-center mt-4">
          <font-awesome-icon
            icon="heart"
            class="text-red-500 mr-4 cursor-pointer text-2xl"
            @click="approveClass"
          />
          <font-awesome-icon
            icon="thumbs-down"
            class="text-gray-500 cursor-pointer text-2xl"
            @click="rejectClass"
          />
        </div>
      </div>
    </a>
  </div>
</template>

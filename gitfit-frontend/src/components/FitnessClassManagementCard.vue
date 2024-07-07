<script setup>
import { defineProps } from 'vue';
import { useHomeStore } from '@/stores/fitnessClassStore';

const props = defineProps({
    fitnessClass: {
        type: Object,
        required: true,
    },
});

const deleteCourseHandler = async () => {
    const store = useHomeStore();
    await store.deleteFitnessClass(props.fitnessClass.name);
};
</script>

<template>
    <div
        class="overflow-hidden shadow-lg rounded-lg h-auto w-full md:w-full cursor-pointer m-auto transform transition-transform duration-500 hover:scale-105 hover:shadow-xl">
        <a href="#" class="w-full block h-full">
            <img :src="'https://source.unsplash.com/featured/?' + fitnessClass.name" alt="fitness class"
                class="max-h-40 w-full object-cover rounded-t-lg transform transition-transform duration-500 hover:scale-110" />
            <div class="bg-linkwater w-full p-8">
                <p class="text-persianblue text-md font-medium">
                    {{ fitnessClass.name }}
                </p>
                <p class="text-moodyblue text-xl font-medium mb-2">
                    {{ fitnessClass.description }}
                </p>
                <p class="text-moodyblue text-S font-small mb-2">
                    {{ fitnessClass.approvalStatus }}
                </p>
                <div class="flex items-center mt-4">
                    <font-awesome-icon icon="thumbs-down" class="text-gray-500 cursor-pointer text-2xl"
                        @click="deleteCourseHandler" />
                </div>
            </div>
        </a>
    </div>
</template>
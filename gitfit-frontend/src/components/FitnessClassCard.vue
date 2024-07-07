<script setup>
import { updateFitnessClassStatus } from '@/api';
import { useStore } from '@/stores/store';
import { defineProps, computed } from 'vue';

const props = defineProps({
    fitnessClass: {
        type: Object,
        required: true
    }
})

const emit = defineEmits(['update'])

const store = useStore();
const userRole = computed(() => store.userRole)

const approveClass = async () => {
    await updateFitnessClassStatus(props.fitnessClass.name, 'APPROVED')
    emit('update')
}

const rejectClass = async () => {
    await updateFitnessClassStatus(props.fitnessClass.name, 'REJECTED')
    emit('update')
}
</script>

<template>
    <div
        class="overflow-hidden shadow-lg rounded-lg h-auto w-full md:w-full cursor-pointer m-auto transform transition-transform duration-500 hover:scale-105 hover:shadow-xl">
        <a href="#" class="w-full block h-full">
            <img :src="'https://source.unsplash.com/featured/?' + fitnessClass.name" alt="fitness class"
                class="max-h-80 w-full object-cover rounded-t-lg transform transition-transform duration-500 hover:scale-110" />
            <div class="bg-linkwater w-full p-8">
                <p class="text-persianblue text-2xl font-medium">
                    {{ fitnessClass.name }}
                </p>
                <p class="text-moodyblue text-xl font-medium mb-2">
                    {{ fitnessClass.description }}
                </p>
                <div v-if="userRole === 'Owner'" class="flex items-center mt-4">
                    <font-awesome-icon icon="heart" class="text-red-500 mr-4 cursor-pointer text-2xl"
                        @click="approveClass" />
                    <font-awesome-icon icon="thumbs-down" class="text-gray-500 cursor-pointer text-2xl"
                        @click="rejectClass" />
                </div>
            </div>
        </a>
    </div>
</template>
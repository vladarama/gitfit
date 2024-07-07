<script setup>
import { ref, defineProps, watch } from 'vue';
import { fetchSessionBySessionId } from '@/api';

const props = defineProps({
    show: Boolean,
    sessionId: Number
});

const emit = defineEmits(['update:show']);

const sessionDetails = ref({});

const fetchDetails = async (sessionId) => {
    try {
        const response = await fetchSessionBySessionId(sessionId);
        sessionDetails.value = response.data;
    } catch (error) {
        console.error(error);
    }
};

watch(() => props.show, async (newShow) => {
    if (newShow) {
        await fetchDetails(props.sessionId);
    }
});

const closeModal = () => {
    sessionDetails.value = {};
    emit('update:show', false);
};
</script>


<template>
    <div v-if="show" class="z-10 fixed inset-0 bg-linkwater bg-opacity-50 overflow-y-auto h-full w-full"
        @click.self="closeModal">
        <div class="z-11 relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
            <div class="mt-3 text-center">
                <h3 class="text-xl leading-6 font-medium text-persianblue">Session Details</h3>
                <div class="mt-2 px-7 py-3">
                    <p class="text-md text-gray-500">Price: {{ sessionDetails.price }}</p>
                    <p class="text-md text-gray-500">Start Time: {{ sessionDetails.startTime }}</p>
                    <p class="text-md text-gray-500">End Time: {{ sessionDetails.endTime }}</p>
                    <p class="text-md text-gray-500">Date: {{ sessionDetails.date }}</p>
                    <p class="text-md text-gray-500">Instructor: {{ sessionDetails.instructorUsername }}</p>
                    <p class="text-md text-gray-500">Fitness Class: {{ sessionDetails.fitnessClassName }}</p>
                </div>
                <div class="items-center px-4 py-3">
                    <button @click.stop="closeModal"
                        class="px-4 py-2 bg-persianblue text-white text-base font-medium rounded-md w-full shadow-sm hover:bg-linkwater focus:outline-none focus:ring-2 focus:ring-moodyblue">
                        Close
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

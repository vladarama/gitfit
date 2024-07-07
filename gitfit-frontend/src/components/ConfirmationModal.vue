<script setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
    show: Boolean,
    registrationId: [String, Number]
});

const emits = defineEmits(['confirm', 'update:show']);

const closeModal = () => {
    emits('update:show', false);
};

const confirm = () => {
    emits('confirm', props.registrationId);
};
</script>

<template>
    <div v-if="show" class="z-10 fixed inset-0 bg-linkwater bg-opacity-50 overflow-y-auto h-full w-full"
        @click.self="closeModal">
        <div class="z-11 relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
            <div class="mt-3 text-center">
                <h3 class="text-xl leading-6 font-medium text-persianblue">Cancel Registration</h3>
                <div class="mt-2 px-7 py-3">
                    <p class="text-md text-gray-500">Are you sure you want to cancel this registration?</p>
                </div>
                <div class="flex items-center justify-center space-x-4 px-4 py-3">
                    <button @click="confirm"
                        class="px-4 py-2 bg-persianblue text-white text-base font-medium rounded-md shadow-sm hover:bg-moodyblue focus:outline-none focus:ring-2 focus:ring-spindle">
                        Yes
                    </button>
                    <button @click="closeModal"
                        class="px-4 py-2 bg-spindle text-white text-base font-medium rounded-md shadow-sm hover:bg-linkwater focus:outline-none focus:ring-2 focus:ring-moodyblue">
                        No
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>
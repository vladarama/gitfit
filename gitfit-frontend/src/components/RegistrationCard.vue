<script setup>
import { ref } from 'vue';
import ConfirmationModal from './ConfirmationModal.vue';
import SessionDetailsModal from './SessionDetailsModal.vue';
import { useRegistrationStore } from '@/stores/registrationStore';
import { useCustomerStore } from '@/stores/customerStore';
import { defineProps } from 'vue';

const props = defineProps({
    registration: {
        type: Object,
        required: true
    }
});

const showModal = ref(false);
const store = useRegistrationStore();
const customerStore = useCustomerStore();
const customer = customerStore.customer;

const confirmDelete = async () => {
    await store.deleteRegistration(props.registration.id);
    await store.fetchRegistrationsByCustomerUsername(customer.username);
    showModal.value = false;
};

const deleteRegistrationHandler = () => {
    showModal.value = true;
};

const showSessionModal = ref(false);

const viewSessionDetailsHandler = () => {
    showSessionModal.value = true;
};

</script>

<template>
    <div class="instructor-card flex flex-col bg-white rounded-xl shadow-md overflow-hidden md:max-w-2xl m-3 relative"
        @click.stop="toggleDropdown">
        <button class="absolute top-0 right-0 m-2 text-xl font-semibold"
            @click.stop="deleteRegistrationHandler">X</button>
        <div class="p-8">
            <div class="uppercase tracking-wide text-xl text-persianblue font-semibold">
                ID: {{ registration.id }}
            </div>
            <p class="mt-2 text-gray-500">Registration Date: {{ registration.date }}</p>
            <button class="mt-2 text-moodyblue hover:text-persianblue font-bold"
                @click.stop="viewSessionDetailsHandler">
                View Session Details
            </button>
        </div>
    </div>
    <ConfirmationModal :show="showModal" :registrationId="registration.id" @confirm="confirmDelete"
        @update:show="showModal = $event" />
    <SessionDetailsModal :show="showSessionModal" :sessionId="registration.sessionId"
        @update:show="showSessionModal = $event" />
</template>

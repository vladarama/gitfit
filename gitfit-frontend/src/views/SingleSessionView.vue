<script setup>
import ErrorModal from '@/components/ErrorModal.vue';
import SessionCard from '@/components/SessionCard.vue';
import SessionCreationSuccessModal from '@/components/SessionCreationSuccessModal.vue';
import { useRegistrationStore } from '@/stores/registrationStore';
import { useSessionStore } from '@/stores/sessionStore';
import { useCustomerStore } from '@/stores/customerStore';
import { useStore } from '@/stores/store';
import { onBeforeMount, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';

const registrationStore = useRegistrationStore();
const sessionStore = useSessionStore();
const customerStore = useCustomerStore();
const router = useRouter();
const route = useRoute();
const showSuccessModal = ref(false);
const showModal = ref(false);
const errorMessage = ref('');
const message = ref('Session registered successfully.');
const session = ref(null);
const { customer } = storeToRefs(customerStore);
const store = useStore();
const { userRole } = storeToRefs(store);

const registrationData = ref({
    "date": `${new Date().toISOString().slice(0, 10)}`,
    "sessionId": `${session.id}`,
    "customerUsername": `${customer.username}`
});

onBeforeMount(async () => {
    const id = route.params.id;
    await sessionStore.fetchAndSetSessionById(id);
    session.value = sessionStore.session;
    console.log(customer.value)
    registrationData.value.sessionId = session.value.id;
    if (customer.value) {
        registrationData.value.customerUsername = customer.value.username;
    } else {
        registrationData.value.customerUsername = '';
    }
});

const handleRegister = async () => {
    try {
        console.log(registrationData.value);
        const response = await registrationStore.createRegistration(registrationData.value);
        if (response) {
            console.log("Registration successful");
            showSuccessModal.value = true;
            setTimeout(() => {
                closeSuccessModalAndRedirect();
            }, 3000); // Wait for 3 seconds before redirecting
        }
    } catch (error) {
        errorMessage.value = error.message;
        showModal.value = true;
    }
};

const closeSuccessModalAndRedirect = () => {
    showSuccessModal.value = false;
    router.push('/sessions'); // Redirect to sessions page
};

</script>

<template>
    <div class="bg-spindle min-h-screen flex flex-col items-center">
        <SessionCard :session="session">
        </SessionCard>
        <button v-if="userRole === 'Customer'" @click="handleRegister(registrationData)" type="button"
            class="mt-10 mx-auto px-4 py-2 bg-persianblue text-white font-semibold rounded-md hover:bg-moodyblue focus:outline-none focus:ring-2 focus:ring-spindle focus:ring-offset-2 transition-colors">
            Register
        </button>
        <ErrorModal :show="showModal" :message="errorMessage" @update:show="showModal = $event" />
        <!-- TODO: rename modal -->
        <SessionCreationSuccessModal :show="showSuccessModal" :message="message"
            @update:show="closeSuccessModalAndRedirect" />
    </div>
</template>
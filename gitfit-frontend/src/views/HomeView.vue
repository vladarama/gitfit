<script setup>
import FitnessClassCard from '@/components/FitnessClassCard.vue';
import { onMounted, computed } from 'vue';
import { useHomeStore } from '@/stores/fitnessClassStore';
import { useCustomerStore } from '@/stores/customerStore';
import { useInstructorStore } from '@/stores/instructorStore';
import { useOwnerStore } from '@/stores/ownerStore';
import { useSessionStore } from '@/stores/sessionStore';
import { useStore } from '@/stores/store';
import { storeToRefs } from 'pinia';
import { useRouter } from 'vue-router';

const router = useRouter();

const customerStore = useCustomerStore();
const instructorStore = useInstructorStore();
const ownerStore = useOwnerStore();
const homeStore = useHomeStore();
const sessionStore = useSessionStore();
const store = useStore();

const { userRole } = storeToRefs(store);

const firstName = computed(() => {
    switch (store.userRole) {
        case 'Customer':
            return customerStore.customer?.firstName;
        case 'Instructor':
            return instructorStore.instructor?.firstName;
        case 'Owner':
            return ownerStore.owner?.firstName;
        default:
            return '';
    }
});

onMounted(async () => {
    await homeStore.fetchAndSetPendingFitnessClasses();
    await homeStore.fetchAndSetApprovedFitnessClasses();
});

const fitnessClasses = computed(() => {
    return store.userRole === 'Owner' ? homeStore.pendingFitnessClasses : homeStore.approvedFitnessClasses;
});

const refreshClasses = async () => {
    await homeStore.fetchAndSetPendingFitnessClasses();
    await homeStore.fetchAndSetApprovedFitnessClasses();
}

const greeting = computed(() => {
    const hour = new Date().getHours();
    if (hour < 12) {
        return 'Good morning';
    } else if (hour < 18) {
        return 'Good afternoon';
    } else {
        return 'Good evening';
    }
});

const viewRelatedSessions = async (fitnessClass) => {
    if (userRole.value === 'Owner') {
        return;
    }
    await sessionStore.fetchAndSetFilteredSessions(`fitnessClassName${fitnessClass.name}`);
    router.push('/sessions');
}
</script>

<template>
    <div class="bg-gradient-to-r from-persianblue via-moodyblue to-spindle text-white text-center py-20">
        <h1 class="text-6xl font-bold">{{ greeting }}, {{ firstName }}</h1>
    </div>
    <div class="grid grid-cols-3 gap-8 p-8">
        <FitnessClassCard v-for="fclass in fitnessClasses" :key="fclass.name" :fitnessClass="fclass"
            @click="viewRelatedSessions(fclass)" @update="refreshClasses"
            class="transform transition-transform duration-500 hover:scale-105 bg-linkwater rounded-lg shadow-md" />
    </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;700&display=swap');

body {
    font-family: 'Inter', sans-serif;
}
</style>
<template>
    <ErrorModal :show="showModal" :message="errorMessage" @update:show="showModal = $event" />
    <div class="flex min-h-full flex-col justify-center items-center ">
        <div class="flex flex-col min-h-screen items-center">
        <div class="sm:mx-auto sm:w-full sm:max-w-sm">
            <h2 class="mt-20 mb-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">User Profile</h2>
        </div>
        <div class="w500 bg-white overflow-hidden shadow rounded-lg border mb-10">
        
        <div class="border-t border-gray-200 px-4 py-5 sm:p-0">
            <dl class="sm:divide-y sm:divide-gray-200">
                <div class="flex px-4 py-5 sm:px-6">
                    <h3 class="text-lg leading-6 font-medium text-gray-900">
                        Basic Information
                    </h3>
                </div>
                <div class="py-3 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                    <dt class="text-sm font-medium text-gray-500">
                        First name
                    </dt>
                    <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                        {{ user.firstName }}
                    </dd>
                </div>
                <div class="py-3 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                    <dt class="text-sm font-medium text-gray-500"> 
                        Last name
                    </dt>
                    <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                        {{ user.lastName }}
                    </dd>
                </div>
                <div class="py-3 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                    <dt class="text-sm font-medium text-gray-500">
                        Email
                    </dt>
                    <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                        {{ user.email }}
                    </dd>
                </div>
                <div class="py-3 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                    <dt class="text-sm font-medium text-gray-500">
                        Username
                    </dt>
                    <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                        {{ user.username }}
                    </dd>
                </div>
                <div class="flex px-4 py-5 sm:px-6">
                    <h3 class="text-lg leading-6 font-medium text-gray-900">
                        Login Information
                    </h3>
                    <div class="flex px-8 items-center cursor-pointer" @click="editPassword">
                        <img src="../assets/edit.png" alt="edit" class="w-4 h-4">
                        <div class="px-1">Edit</div>
                    </div>
                </div>
                <div class="py-3 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                    <dt class="text-sm font-medium text-gray-500">
                        Password
                    </dt>
                    <dd class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                        <b>••••••••</b>
                    </dd>
                </div>
                <div v-if="userType==='Customer'" class="flex px-4 py-5 sm:px-6">
                    <h3 class="text-lg leading-6 font-medium text-gray-900">
                        Billing Information
                    </h3>
                    <div class="flex px-8 items-center cursor-pointer" @click="editBilling">
                        <img src="../assets/edit.png" alt="edit" class="w-4 h-4">
                        <div class="px-1">Edit</div>
                    </div>
                    <div v-if="billing" class="flex px-8 items-center cursor-pointer" @click="deleteBilling">
                        <img src="../assets/remove.png" alt="edit" class="w-4 h-4">
                        <div class="px-1">Remove</div>
                    </div>
                </div>
                <div v-if="userType==='Customer'" class="py-3 sm:py-5 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-6">
                    <dt class="text-sm font-medium text-gray-500">
                        Card
                    </dt>
                    <dd v-if="billing" class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                        **** **** **** {{ billing.cardNumberEnd }}
                    </dd>
                    <dd v-else class="mt-1 text-sm text-gray-900 sm:mt-0 sm:col-span-2">
                        No card on file
                    </dd>
                </div>
            </dl>
        </div>
    </div>
    </div>
    </div>
</template>

<script setup>
import { computed } from 'vue';
import { useCustomerStore } from '@/stores/customerStore';
import { useBillingStore } from '@/stores/billingStore';
import { useOwnerStore } from '@/stores/ownerStore';
import { useInstructorStore } from '@/stores/instructorStore';
import { defineEmits, ref } from 'vue';
import ErrorModal from '@/components/ErrorModal.vue';

const showModal = ref(false);
const errorMessage = ref('');

const emit = defineEmits(['editBilling', 'editPassword']);

const billingStore = useBillingStore();

const billing = computed(() => billingStore.billing);
const userType = localStorage.getItem('userType');
const user = computed(() => {
    if (userType === 'Customer') {
        const customerStore = useCustomerStore();
        return customerStore.customer;
    } else if (userType === 'Instructor') {
        const instructorStore = useInstructorStore();
        return instructorStore.instructor;
    } else if (userType === 'Owner') {
        const ownerStore = useOwnerStore();
        return ownerStore.owner;
    }
    return null;
});



const editBilling = () => {
    emit('editBilling');
};

const editPassword = () => {
    emit('editPassword');
};

const deleteBilling = async () => {
    const customerStore = useCustomerStore();
    const customer = customerStore.customer;
    const billingStore = useBillingStore();
    const response = await billingStore.deleteBilling(customer.username);
    if (response.status === 200) {
        billingStore.billing = null;
    } else {
        errorMessage.value = response.data.errors[0];
        showModal.value = true;
    }


};

</script>
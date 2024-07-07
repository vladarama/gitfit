<script setup>
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';
import { ref, onMounted } from 'vue';

const date = ref();
const format = 'yyyy-MM-dd';

const emits = defineEmits(['update-dates']);

onMounted(() => {
    const startDate = new Date();
    const endDate = new Date();
    date.value = [startDate, endDate];
})

const handleDate = (dates) => {
    emits('update-dates', dates);
}

</script>

<template>
  <VueDatePicker :model-value="date" @update:model-value="handleDate" v-model="date" range :format="format" />
</template>
<script setup>
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';
import { ref, onMounted } from 'vue';

const time = ref({
  hours: 0,
  minutes: 0,
});
const format = 'HH:mm';

const emits = defineEmits(['update-time']);

const handleTime = (time) => {
  var formattedTime = '';
  if (time !== null) {
    const { hours, minutes, seconds } = time;
    // Format hours, minutes, and seconds with leading zeros
    const formattedHours = hours.toString().padStart(2, '0');
    const formattedMinutes = minutes.toString().padStart(2, '0');
    const formattedSeconds = seconds.toString().padStart(2, '0');
    // Concatenate the formatted values into the desired string format
    formattedTime = `${formattedHours}:${formattedMinutes}:${formattedSeconds}`;
  }
  emits('update-time', formattedTime);
}

</script>

<template>
  <VueDatePicker :model-value="time" @update:model-value="handleTime" v-model="time" time-picker :format="format" />
</template>
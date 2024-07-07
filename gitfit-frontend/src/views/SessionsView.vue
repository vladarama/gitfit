<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import IconToggleSort from '@/components/icons/IconToggleSort.vue';
import DatePicker from '@/components/DatePicker.vue';
import TimePicker from '@/components/TimePicker.vue';
import { useSessionStore } from '@/stores/sessionStore';
import { useInstructorStore } from '@/stores/instructorStore';
import { useHomeStore } from '@/stores/fitnessClassStore';
import { useRouter } from 'vue-router';

var filters = ref();
var selectedInstructor = ref('');
var selectedFitnessClass = ref('');
var selectedPrice = ref('');
var selectedStartDate = ref('');
var selectedEndDate = ref('');
var selectedStartTime = ref('');
var selectedEndTime = ref('');

const sortAttribute = ref('id');
const sortDirection = ref('asc');

const sessionStore = useSessionStore();
const instructorStore = useInstructorStore();
const store = useHomeStore();

onMounted(async () => {
    if (!sessionStore.sessions.length) {
        await sessionStore.fetchAndSetSessions();
    }
    await instructorStore.fetchInstructors();
    await store.fetchAndSetFitnessClasses();
});

var sessions = computed(() => sessionStore.sessions);

var instructorLookup = computed(() => instructorStore.instructorLookup);
var instructors = computed(() => instructorStore.instructors);
var fitnessClasses = computed(() => store.fitnessClasses);

const router = useRouter();

const selectSession = async (session) => {
    await sessionStore.fetchAndSetSessionById(session.id);
    console.log(sessionStore.session)
    router.push({ name: 'SessionDetails', params: { id: session.id } });
}

// updates the filtered sessions when any of the filters change
watch([selectedInstructor, selectedFitnessClass, selectedPrice, selectedStartDate, selectedEndDate, selectedStartTime, selectedEndTime],
    async ([newInstructor, newFClass, newPrice, newStartDate, newEndDate, newStartTime, newEndTime]) => {
        filters.value = {
            'instructorUsername': newInstructor,
            'fitnessClassName': newFClass,
            'maxPrice': newPrice,
            'startDate': newStartDate,
            'endDate': newEndDate,
            'startTime': newStartTime,
            'endTime': newEndTime
        };
        console.log(filters.value);
        var params = '';
        if (newInstructor !== '') {
            params += `instructorUsername=${newInstructor}&`;
        }
        if (newFClass !== '') {
            params += `fitnessClassName=${newFClass}&`;
        }
        if (newPrice !== '') {
            params += `maxPrice=${newPrice}&`;
        }
        if (newStartDate !== '') {
            params += `startDate=${newStartDate}&`;
        }
        if (newEndDate !== '') {
            params += `endDate=${newEndDate}&`;
        }
        if (newStartTime !== '') {
            params += `startTime=${newStartTime}&`;
        }
        if (newEndTime !== '') {
            params += `endTime=${newEndTime}&`;
        }
        await sessionStore.fetchAndSetFilteredSessions(params);
        sessions = computed(() => sessionStore.sessions);
    });

const sortAttributes = ref([
    { value: 'id', label: 'ID' },
    { value: 'date', label: 'Date' },
    { value: 'startTime', label: 'Start Time' },
    { value: 'endTime', label: 'End Time' },
    { value: 'price', label: 'Price' },
    { value: 'instructorUsername', label: 'Instructor' },
    { value: 'fitnessClassName', label: 'Fitness Class' }
])

// sorts the sessions based on the selected attribute and direction
const sortedAndFilteredSessions = computed(() => {
    let sortedSessions = [...sessions.value];
    if (sortAttribute.value) {
        sortedSessions.sort((a, b) => {
            if (sortDirection.value === 'asc') {
                return a[sortAttribute.value] > b[sortAttribute.value] ? 1 : -1;
            } else {
                return a[sortAttribute.value] < b[sortAttribute.value] ? 1 : -1;
            }
        });
    }
    return sortedSessions;
});

const toggleSortDirection = () => {
    sortDirection.value = sortDirection.value === 'asc' ? 'desc' : 'asc';
}

const updateDates = (dates) => {
    if (dates === null) {
        selectedStartDate.value = ''
        selectedEndDate.value = ''
    } else {
        selectedStartDate.value = dates[0].toISOString().slice(0, 10)
        selectedEndDate.value = dates[1].toISOString().slice(0, 10)
    }
}

const updateStartTime = (time) => {
    if (time === null) {
        selectedStartTime.value = ''
    } else {
        selectedStartTime.value = time
    }
}

const updateEndTime = (time) => {
    if (time === null) {
        selectedEndTime.value = ''
    } else {
        selectedEndTime.value = time
    }
}

const updatePrice = (event) => {
    selectedPrice.value = event.target.value
}

</script>

<template>
    <div class="min-h-screen bg-linkwater pt-12 pb-8 px-4 sm:px-6 lg:px-8">
        <!-- title -->
        <h1 class="text-4xl text-center font-bold text-persianblue mb-5 ">
            Sessions List
        </h1>
        <!-- filters -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-8 w-full max-w-6xl mx-auto mb-4">
            <!-- left side of filters -->
            <div class="justify-center items-center md:items-start">
                <label for="dates" class="text-sm font-medium text-gray-700">Date Range</label>
                <DatePicker class="z-3" @update-dates="updateDates" id="dates" placeholder="Select date range" />
                <label for="start" class="text-sm font-medium text-gray-700">Start Time</label>
                <TimePicker class="z-2" @update-time="updateStartTime" id="start" placeholder="Select start time" />
                <label for="end" class="text-sm font-medium text-gray-700">End Time</label>
                <TimePicker class="z-1" @update-time="updateEndTime" id="end" placeholder="Select end time" />
            </div>
            <!-- right side of filters -->
            <div class="flex flex-col justify-center h-full">
                <label for="fclass" class="text-sm font-medium text-gray-700">Fitness Class</label>
                <!-- dropdown menu for fitness class -->
                <select id="fclass" v-model="selectedFitnessClass"
                    class="block w-full px-4 py-2 rounded-md border border-spindle focus:border-persianblue focus:ring focus:ring-persianblue focus:ring-opacity-50">
                    <option disabled value="">Filter by Fitness Class</option>
                    <option value="">No Filter</option>
                    <option v-for="fitnessClass in fitnessClasses" :key="fitnessClass.name">{{ fitnessClass.name }}
                    </option>
                </select>
                <!-- dropdown menu for instructor -->
                <label for="instructor" class="text-sm font-medium text-gray-700">Instructor</label>
                <select id="instructor" v-model="selectedInstructor"
                    class="block w-full px-4 py-2 rounded-md border border-spindle focus:border-persianblue focus:ring focus:ring-persianblue focus:ring-opacity-50">
                    <option disabled value="">Filter by Instructor</option>
                    <option value="">No Filter</option>
                    <option v-for="instructor in instructors" :key="instructor.username" :value="instructor.username">{{
                    instructor.firstName + ' ' + instructor.lastName }}
                    </option>
                </select>
                <!-- input for max price -->
                <label for="price" class="text-sm font-medium text-gray-700">Max Price</label>
                <input id="price" type="number" @keyup.enter="updatePrice" placeholder="Max Price"
                    class="block w-full px-4 py-2 rounded-md border border-spindle focus:border-persianblue focus:ring focus:ring-persianblue focus:ring-opacity-50" />
            </div>
        </div>
        <!-- sorting -->
        <div class="text-s text-gray-700 bg-moodyblue dark:bg-gray-700 dark:text-gray-400">
            <!-- dropdown menu for sorting attribute -->
            <label for="sortAttribute" class="text-sm font-medium text-gray-700 ml-5 mr-2">Sort by:</label>
            <select class="sort-dropdown" id="sortAttribute" v-model="sortAttribute">
                <option v-for="attribute in sortAttributes" :key="attribute.value" :value="attribute.value">
                    {{ attribute.label }}
                </option>
            </select>
            <!-- button to toggle direction -->
            <button @click="toggleSortDirection" type="button" class="px-2 py-2 text-persianblue
            hover:bg-spindle hover:text-white font-medium rounded-lg text-sm p-2.5 text-center 
            inline-flex items-center me-2 dark:border-blue-500 dark:text-blue-500 dark:hover:text-white 
            dark:focus:ring-blue-800 dark:hover:bg-blue-500">
                <IconToggleSort></IconToggleSort>
                <span class="sr-only">Icon description</span>
            </button>
        </div>
        <!-- sessions table -->
        <table class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
            <thead class="text-xs text-gray-700 uppercase bg-moodyblue dark:bg-gray-700 dark:text-gray-400">
                <tr>
                    <th scope="col" class="px-6 py-3">ID</th>
                    <th scope="col" class="px-6 py-3">Date</th>
                    <th scope="col" class="px-6 py-3">Start Time</th>
                    <th scope="col" class="px-6 py-3">End Time</th>
                    <th scope="col" class="px-6 py-3">Price</th>
                    <th scope="col" class="px-6 py-3">Instructor</th>
                    <th scope="col" class="px-6 py-3">Fitness Class</th>
                </tr>
            </thead>
            <tbody>
                <tr class="bg-spindle border-b dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600"
                    v-for="session in sortedAndFilteredSessions" :key="session.id" @click="selectSession(session)"
                    style="cursor: pointer;">
                    <td class="px-6 py-4">{{ session.id }}</td>
                    <td class="px-6 py-4">{{ session.date }}</td>
                    <td class="px-6 py-4">{{ session.startTime }}</td>
                    <td class="px-6 py-4">{{ session.endTime }}</td>
                    <td class="px-6 py-4">{{ session.price }}</td>
                    <td class="px-6 py-4">{{
                    (instructorLookup[session.instructorUsername] || {}).firstName
                    + ' '
                    + (instructorLookup[session.instructorUsername] || {}).lastName
                }}</td>
                    <td class="px-6 py-4">{{ session.fitnessClassName }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<style scoped>
.sort-dropdown {
    margin-right: 1rem;
}
</style>
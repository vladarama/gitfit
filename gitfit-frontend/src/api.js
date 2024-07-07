
import { data } from 'autoprefixer';
import axios from 'axios'


const frontendUrl = 'http://' + import.meta.env.VITE_APP_FRONTEND_HOST + ':' + import.meta.env.VITE_APP_FRONTEND_PORT;
const backendUrl = 'http://' + import.meta.env.VITE_APP_BACKEND_HOST + ':' + import.meta.env.VITE_APP_BACKEND_PORT + '/api';

const AXIOS = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
});

export function fetchFitnessClasses() {
    return AXIOS.get('/fitnessclasses');
}

export function checkLogin(username, password) {
    return AXIOS.post('/login', { username: username, password: password });
}

export function fetchOwner() {
    return AXIOS.get(`/owner`);
}

export function updateOwnerPassword(password) {
    console.log("entereing api.js");
    console.log(password);
    return AXIOS.put('/owner/password', password, {
        headers: {
            'Content-Type': 'text/plain'
        }
    });
}
export function deleteFitnessClass(name) {
    const encodedName = encodeURIComponent(name);
    return AXIOS.delete(`/fitnessclasses/${encodedName}`);
}

export function fetchApprovedFitnessClasses() {
    return AXIOS.get('/fitnessclasses/approved');
}

export function fetchPendingFitnessClasses() {
    return AXIOS.get('/fitnessclasses/pending');
}

export function updateFitnessClassStatus(name, status) {
    return AXIOS.put(`/fitnessclasses/${name}/approval`, JSON.stringify(status), {
        headers: {
            'Content-Type': 'application/json'
        }
    });
}

export function createCustomer(customer) {
    console.log("entereing api.js");
    return AXIOS.post('/customer', customer);
}

export function fetchCustomer(username) {
    return AXIOS.get(`/customer/${username}`);
}

export function updateCustomerPassword(password) {
    console.log("entereing api.js");
    return AXIOS.put(`/customer/password`, password);
}

export function createBilling(billing) {
    return AXIOS.put(`/customers/${billing.username}/billing`, billing);
}

export function getBilling(username) {
    return AXIOS.get(`/customers/${username}/billing`);
}

export function deleteBilling(username) {
    console.log("entereing deleteBilling apijs");
    console.log(username);
    return AXIOS.delete(`/customers/${username}/billing`);
}

export function createInstructor(instructor) {
    console.log("entereing api.js");
    return AXIOS.post('/instructor', instructor);
}
// return AXIOS.post('/customer', {data: customer, headers: { 'Content-Type': 'application/json' }});

export function createSession(sessionData) {
    return AXIOS.post('/sessions/', sessionData)
}

export function fetchSessionBySessionId(sessionId) {
    return AXIOS.get('/sessions/' + sessionId);
}

export function createFitnessClass(fitnessClassData) {
    return AXIOS.post('/fitnessclasses/', fitnessClassData)
}
export function fetchInstructors() {
    return AXIOS.get('/instructors/');
}

export function fetchInstructor(username) {
    return AXIOS.get(`/instructor/${username}`);
}

export function updateInstructorPassword(password) {
    return AXIOS.put(`/instructor/password`, password);
}

export function deleteInstructor(username) {
    return AXIOS.delete('/instructor/', { 
        data: username,
        headers: { 'Content-Type': 'text/plain' }
    });
}

export function fetchSportCenter() {
    return AXIOS.get('/sportcenter');
}

export function updateSportCenterName(name) {
    return AXIOS.put('/sportcenter/name', name, {
        headers: { 'Content-Type': 'application/json' }
    });
}

export function updateSportCenterMaxCapacity(maxCapacity) {
    return AXIOS.put('/sportcenter/capacity', maxCapacity, {
        headers: { 'Content-Type': 'application/json' }
    });
}

export function updateSportCenterHours(openingTime, closingTime) {
    // Ensure the payload structure matches what the backend expects
    const payload = {
        openingTime,
        closingTime
    };
    return AXIOS.put('/sportcenter/hours', payload, {
        headers: { 'Content-Type': 'application/json' }
    });
}

export function fetchSessions() {
    return AXIOS.get('/sessions');
}

export function fetchFilteredSessions(filter) {
    return AXIOS.get('/sessions/filter?' + filter);
}

export function fetchSessionById(id) {
    return AXIOS.get('/sessions/' + id);
}

export function createRegistration(registrationData) {
    return AXIOS.post('/registrations/', registrationData);
}

export function fetchRegistrations() {
    return AXIOS.get('/registrations');
}

export function fetchRegistrationsByCustomerUsername(username) {
    return AXIOS.get('/registrations/customer/' + username);
}

export function deleteRegistation(registrationId) {
    return AXIOS.delete('/registrations/' + registrationId);
}

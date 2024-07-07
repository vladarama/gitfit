<script setup>
import SigninComponent from '../components/SigninComponent.vue';
import SignupComponent from '../components/SignupComponent.vue';
</script>

<template>
  <div v-if = "showSignup">
    <SignupComponent @updateForm='updateForm' />
  </div>
  <div v-else>
    <SigninComponent @updateForm='updateForm' />
  </div>

</template>
  

<script>


export default {
    data() {
        return {
            email: '',
            password: '',
            showSignup: false,
            signupEmail: '',
            signupPassword: ''
        };
    },
    methods: {
        updateForm() {
            this.showSignup = !this.showSignup;
        }, 
    }
};
</script>

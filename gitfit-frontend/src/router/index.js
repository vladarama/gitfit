import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import ProfileView from '../views/ProfileView.vue'
import CreateInstructorComponent from '../components/CreateInstructorComponent.vue'
import { useOwnerStore } from '@/stores/ownerStore'
import { useInstructorStore } from '@/stores/instructorStore'
import { useCustomerStore } from '@/stores/customerStore'
import CreateSessionView from '../views/CreateSessionView.vue'
import CreateFitnessClassView from '../views/CreateFitnessClassView.vue'
import InstructorManagementView from '../views/InstructorManagementView.vue'
import FitnessClassManagementView from '../views/FitnessClassManagementView.vue'
import RegistrationView from '../views/RegistrationView.vue';
import SportCenterManagementView from '../views/SportCenterManagementView.vue';




const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
        path: '/login',
        name: 'login',
        component: LoginView,
        meta: { hideNavbar: true }
    },
    {
        path: '/profile',
        name: 'profile',
        component: ProfileView
    },
    {
        path: '/createinstructor',
        name: 'createinstructor',
        component: CreateInstructorComponent
    },
    {
      path: '/sessions',
      name: 'sessions',
      component: () => import('../views/SessionsView.vue')
    },
    {
      path: '/sessions/:id',
      name: 'SessionDetails',
      component: () => import('../views/SingleSessionView.vue')
    },
    {
      path: '/InstructorManagementView',
      path: '/new-session',
      name: 'SessionCreation',
      component: CreateSessionView
    },
    {
      path: '/new-fitness-class',
      name: 'FitnessClasssCreation',
      component: CreateFitnessClassView
    },
    {
      path: '/instructors',
      name: 'InstructorManagement',
      component: InstructorManagementView
    },
    {
      path: '/fitness-classes',
      name: 'FitnessClassManagement',
      component: FitnessClassManagementView
    },
    {
      path: "/registrations",
      name: "registrations",
      component: RegistrationView
    },
    {
      path: '/about',
      name: 'about',
      component: SportCenterManagementView
    }
  ]
});



router.beforeEach(async (to, from, next) => {
    try {
        const loggedIn = await isLoggedIn();
        if (loggedIn && (to.name==='login'|| 
        (localStorage.getItem('userType')==='Customer') && (to.name==='createinstructor' || to.name==='InstructorManagement' || to.name==='FitnessClassManagement' || to.name==='SessionCreation' || to.name==='FitnessClasssCreation') ||
        (localStorage.getItem('userType')==='Instructor') && (to.name==='createinstructor' || to.name==='InstructorManagement' || to.name==='FitnessClassManagement')
    )) {
            next({ name: 'home' });
        }
        else if (loggedIn || to.name==='login') {
            next();
        } else {
            next({ name: 'login' });
        }
    } catch (error){
        next({ name: 'login' });
    }

  })
  
  async function isLoggedIn() {
    const ownerStore = useOwnerStore();
    const instructorStore = useInstructorStore();
    const customerStore = useCustomerStore();
    if (localStorage.getItem('userType') === 'Owner') {
        try {
            const owner = localStorage.getItem('owner');
            if (owner === null) {
                localStorage.clear();
                return false;
            }
            const response = await ownerStore.fetchAndSetOwner();
            return true;
        }   catch (error) {
            localStorage.clear();
            return false;
        }
    } else if (localStorage.getItem('userType') === 'Instructor') {
        try {
            const instructor = localStorage.getItem('instructor');
            const response = await instructorStore.fetchAndSetInstructor(instructor.username);
            return true;
        }   catch (error) {
            localStorage.clear();
            return false;
        }
    } else if (localStorage.getItem('userType') === 'Customer') {
        try {
            const customer = JSON.parse(localStorage.getItem('customer'));
            const response = await customerStore.fetchAndSetCustomer(customer.username);
            return true;
        }   catch (error) {
            localStorage.clear();
            return false;
        }
    } else {
        localStorage.clear();
        return false;
    }
  }

export default router;

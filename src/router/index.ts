import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import UserManagement from '../views/UserManagement.vue'
import UserRegistration from '../views/UserRegistration.vue'
const routes = [
  { 
    path: '/', 
    component: Login 
  },
  { 
    path: '/users', 
    name: 'UserManagement',
    component: UserManagement 
  },
  { 
    path: '/users/register', 
    component: UserRegistration 
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
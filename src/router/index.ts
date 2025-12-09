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
    component: UserManagement,
    meta: { requiresAuth: true }
  },
  { 
    path: '/users/register', 
    component: UserRegistration,
    meta: { requiresAuth: true }
  },
  { path: '/users/edit/:id', 
    component: UserRegistration,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('accessToken')
  
  if (to.meta.requiresAuth && !token) {
    next('/')
  } else if (to.path === '/' && token) {
    next('/users')
  } else {
    next()
  }
})

export default router
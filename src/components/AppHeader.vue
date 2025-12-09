<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { logout } from '../apis/authApi';

const router = useRouter();

const userCode = ref('Loading...');
const fullName = ref('Loading...');

onMounted(() => {
  const storedUserCode = localStorage.getItem('userCode');
  const storedFullName = localStorage.getItem('fullName');

  if (storedUserCode) userCode.value = storedUserCode;
  if (storedFullName) fullName.value = storedFullName;
});

const handleLogout = async () => {
  try {
    await logout();
  } catch (error) {
    console.error("Logout error (server side):", error);
  } finally {
    localStorage.clear();
    router.push('/');
  }
};
</script>

<template>
  <header class="header">
    <div class="breadcrumb">
      User Management &gt; User List
    </div>
    <div class="user-info">
      <div class="info-item">
        <span>User ID: </span>
        <span class="value">{{ userCode }}</span>
      </div>
      <div class="info-item">
        <span>User Name: </span>
        <span class="value">{{ fullName }}</span>
      </div>
      <button class="logout-btn" @click="handleLogout">
        Logout
      </button>
    </div>
  </header>
</template>

<style scoped>
.header {
  border-bottom: 2px solid #000;
  padding: 10px 20px;
  background: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  font-family: Arial, sans-serif;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.value {
  margin-left: 10px;
}

.logout-btn {
  background: #00a650;
  color: #fff;
  border: none;
  padding: 6px 20px;
  font-size: 13px;
  cursor: pointer;
}
</style>
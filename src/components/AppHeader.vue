<script setup lang="ts">
// Logic xử lý logout nếu cần
const handleLogout = () => {
  console.log('Logout clicked');
  // router.push('/login')
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
        <span class="value">123456</span>
      </div>
      <div class="info-item">
        <span>User Name: </span>
        <span class="value">〇〇〇</span>
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
<script setup lang="ts">
import { defineProps, defineEmits } from 'vue';

// Định nghĩa Props nhận từ cha
defineProps<{
  activeMenu: string
}>();

// Định nghĩa sự kiện gửi ngược lại cho cha
const emit = defineEmits(['update:menu']);

const menuItems = [
  { id: 'dashboard', label: 'Dashboard Display', color: '#708090' },
  { id: 'user', label: 'User Management', color: '#808080' },
  { id: 'contract', label: 'Contract Management', color: '#696969' },
  { id: 'chip', label: 'Chip Registration', color: '#5c5c5c' },
  { id: 'data', label: 'Data Import', color: '#505050' }
];

const handleMenuClick = (id: string) => {
  emit('update:menu', id);
};
</script>

<template>
  <div class="sidebar">
    <div
      v-for="item in menuItems"
      :key="item.id"
      @click="handleMenuClick(item.id)"
      class="menu-item"
      :style="{
        background: activeMenu === item.id ? item.color : '#fff',
        color: activeMenu === item.id ? '#fff' : '#000'
      }"
    >
      {{ item.label }}
    </div>
  </div>
</template>

<style scoped>
.sidebar {
  width: 175px;
  border-right: 1px solid #000;
  background: #f5f5f5;
  min-height: calc(100vh - 50px);
  font-family: Arial, sans-serif;
}

.menu-item {
  padding: 12px 15px;
  cursor: pointer;
  font-size: 13px;
  border-bottom: 1px solid #ddd;
  transition: background 0.2s;
}
</style>
<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

defineProps<{
  activeMenu: string
}>();

const emit = defineEmits(['update:menu']);

const menuItems = [
  { id: 'dashboard', label: 'Dashboard Display', color: '#708090', path: '/dashboard' },
  { id: 'user', label: 'User Management', color: '#808080', path: '/users' }
];

const handleMenuClick = (item: any) => {
  emit('update:menu', item.id);
  
  if (item.path) {
    router.push(item.path);
  }
};
</script>

<template>
  <div class="sidebar">
    <div
      v-for="item in menuItems"
      :key="item.id"
      @click="handleMenuClick(item)"
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
.menu-item:hover {
    opacity: 0.8;
}
</style>
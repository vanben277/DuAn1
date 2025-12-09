<script setup lang="ts">

import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import AppHeader from '../components/AppHeader.vue';
import AppSidebar from '../components/AppSidebar.vue';
import { getUsers } from '../apis/userApi';
import { getStores } from '../apis/storeApi';
import { deleteUser } from '../apis/userApi';


interface Store {
  storeCode: string;
  storeName: string;
  address?: string;
  isActive: boolean;
}

interface User {
  id: number;
  userCode: string;
  fullName: string;
  storeCode: string | null;
  storeName: string | null;
  role: string;
}

const router = useRouter();
const activeMenu = ref('user');

// State Form Search
const storeCode = ref('');
const fullName = ref('');
const typeStaff = ref(false);
const typeManager = ref(false);

// Data
const users = ref<User[]>([]);
const stores = ref<Store[]>([]);
const totalItems = ref<number>(0);
const currentPage = ref<number>(0);
const pageSize = ref<number>(10);
const totalPages = ref<number>(0);

// Delete Modal State
const showDeleteModal = ref(false);
const userToDelete = ref<User | null>(null);

// Load Stores
const loadStores = async () => {
  try {
    const res = await getStores();
    stores.value = res.data.data;
  } catch (error) {
    console.error("Error loading stores", error);
  }
};

// Search
const handleSearch = async () => {
  try {
    let roleParam = null;
    if (typeStaff.value && !typeManager.value) roleParam = 'STAFF';
    if (!typeStaff.value && typeManager.value) roleParam = 'MANAGER';

    const params = {
      storeCode: storeCode.value || null,
      fullName: fullName.value || null,
      role: roleParam,
      pageNumber: currentPage.value,
      pageSize: pageSize.value
    };

    const res = await getUsers(params);
    users.value = res.data.data.content;
    totalItems.value = res.data.data.totalElements;
    totalPages.value = res.data.data.totalPages;

  } catch (error) {
    console.error("Search error", error);
  }
};

// Show Delete Modal
const confirmDelete = (user: User) => {
  userToDelete.value = user;
  showDeleteModal.value = true;
}

// Cancel Delete
const cancelDelete = () => {
  showDeleteModal.value = false;
  userToDelete.value = null;
}

// Execute Delete
const executeDelete = async () => {
  if (!userToDelete.value) return;
  
  try {
    await deleteUser(userToDelete.value.id);
    showDeleteModal.value = false;
    userToDelete.value = null;
    alert("Deleted successfully");
    handleSearch();
  } catch (error: any) {
    alert(error.response?.data?.message || "Error deleting");
  }
}

// Edit Handler
const handleEdit = (id: number) => {
  router.push(`/users/edit/${id}`);
}

// Pagination
const goToPage = (page: number) => {
  if (page >= 0 && page < totalPages.value) {
    currentPage.value = page;
    handleSearch();
  }
}

const nextPage = () => {
  if (currentPage.value < totalPages.value - 1) {
    currentPage.value++;
    handleSearch();
  }
}

const prevPage = () => {
  if (currentPage.value > 0) {
    currentPage.value--;
    handleSearch();
  }
}

// Mount
onMounted(() => {
  loadStores();
  handleSearch();
});

const handleAction = () => {
  router.push('/users/register');
}
</script>

<template>
  <div class="app-container">
    <AppHeader />
    
    <div class="main-layout">
      <AppSidebar 
        :activeMenu="activeMenu" 
        @update:menu="(id) => activeMenu = id" 
      />
      
      <div class="content-area">
        <!-- Search Section -->
        <div class="search-box">
          <table class="search-table">
            <tbody>
              <tr>
                <td class="label-col">Store Name</td>
                <td class="input-col">
                  <select v-model="storeCode" class="form-control select-box">
                    <option value="">Select</option>
                    <option v-for="s in stores" :key="s.storeCode" :value="s.storeCode">
                      {{ s.storeName }}
                    </option>
                  </select>
                </td>
              </tr>
              <tr>
                <td class="label-col">User Name</td>
                <td class="input-col flex-row">
                  <input type="text" v-model="fullName" class="form-control" />
                </td>
              </tr>
              <tr>
                <td class="label-col">Type</td>
                <td class="input-col">
                  <label class="checkbox-label">
                    <input type="checkbox" v-model="typeStaff" /> Staff
                  </label>
                  <label class="checkbox-label">
                    <input type="checkbox" v-model="typeManager" /> Manager
                  </label>
                  <button class="btn-search" @click="handleSearch">Search</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Pagination Info -->
        <div class="pagination-info">
          <span>Total Items: {{ totalItems }} items</span>
          <span class="float-right">
            {{ currentPage * pageSize + 1 }} - {{ Math.min((currentPage + 1) * pageSize, totalItems) }} / {{ totalItems }} items 
            <button @click="prevPage" :disabled="currentPage === 0" class="pagination-btn">&lt;</button>
            <button @click="nextPage" :disabled="currentPage >= totalPages - 1" class="pagination-btn">&gt;</button>
          </span>
        </div>

        <!-- Data Table -->
        <table class="data-table">
          <thead>
            <tr>
              <th>User ID</th>
              <th>User Name</th>
              <th>Store ID</th>
              <th>Store Name</th>
              <th>Type</th>
              <th>Edit</th>
              <th>Delete</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in users" :key="user.userCode">
              <td class="text-center">{{ user.userCode }}</td>
              <td>{{ user.fullName }}</td>
              <td class="text-center">{{ user.storeCode || 'N/A' }}</td>
              <td>{{ user.storeName || 'N/A' }}</td>
              <td class="text-center">{{ user.role }}</td>
              <td class="text-center">
                <button class="btn-action" @click="handleEdit(user.id)">Edit</button>
              </td>
              <td class="text-center">
                <button class="btn-action" @click="confirmDelete(user)">Delete</button>
              </td>
            </tr>
            <tr v-if="users.length === 0">
              <td colspan="7" class="text-center">No data available</td>
            </tr>
          </tbody>
        </table>

        <!-- New Registration -->
        <div class="action-footer">
          <button class="btn-new" @click="handleAction">New Registration</button>
        </div>
      
      </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div v-if="showDeleteModal" class="modal-overlay" @click.self="cancelDelete">
      <div class="modal-container">
        <div class="modal-header">
          Delete Confirmation
        </div>
        <div class="modal-body">
          <p>Are you sure you want to delete User ID [{{ userToDelete?.userCode }}]?</p>
        </div>
        <div class="modal-footer">
          <button class="btn-cancel" @click="cancelDelete">Cancel</button>
          <button class="btn-delete" @click="executeDelete">Delete</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.app-container {
  font-family: Arial, sans-serif;
  font-size: 13px;
}
.main-layout {
  display: flex;
}
.content-area {
  flex: 1;
  padding: 20px;
  background: #fff;
  position: relative;
}

.search-box {
  margin-bottom: 20px;
  border: 1px solid #ccc;
  padding: 15px;
}
.search-table {
  width: 100%;
  border-collapse: collapse;
}
.label-col {
  padding: 8px;
  width: 120px;
  text-align: right;
  padding-right: 15px;
}
.input-col {
  padding: 8px;
}
.flex-row {
  display: flex;
  gap: 20px;
  align-items: center;
}
.form-control {
  padding: 4px 6px;
  border: 1px solid #000;
  font-size: 13px;
  width: 200px;
}
.select-box {
  width: 250px;
}
.checkbox-label {
  margin-right: 30px;
  cursor: pointer;
}
.btn-search {
  margin-left: 250px;
  background: #dc143c;
  color: #fff;
  border: none;
  padding: 6px 30px;
  font-size: 13px;
  cursor: pointer;
}

.pagination-info {
  margin-bottom: 10px;
}
.float-right {
  float: right;
}
.pagination-btn {
  margin-left: 5px;
  cursor: pointer;
  padding: 2px 8px;
  border: 1px solid #999;
  background: #fff;
  border: none;
}
.pagination-btn:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  border: 1px solid #000;
  margin-top: 10px;
}
.data-table th, .data-table td {
  border: 1px solid #000;
  padding: 8px;
}
.data-table th {
  background: #f0f0f0;
}
.text-center {
  text-align: center;
}
.btn-action {
  border: 1px solid #000;
  background: #fff;
  padding: 4px 12px;
  font-size: 12px;
  cursor: pointer;
}

.action-footer {
  margin-top: 20px;
  text-align: center;
}
.btn-new {
  background: #dc143c;
  color: #fff;
  border: none;
  padding: 8px 40px;
  font-size: 13px;
  cursor: pointer;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.modal-container {
  background: #fff;
  border: 2px solid #d1d1d1;
  min-width: 500px;
}

.modal-header {
  background: #f0f0f0;
  padding: 12px 20px;
  border-bottom: 1px solid #d1d1d1;
  font-size: 14px;
  font-weight: normal;
  text-align: center;
  font-weight: 600;
}

.modal-body {
  padding: 40px 30px;
  text-align: center;
  font-size: 13px;
  background: #f0f0f0;
}

.modal-body p {
  margin: 0;
  line-height: 1.6;
}

.modal-footer {
  padding: 20px;
  display: flex;
  justify-content: center;
  background: #f0f0f0;
}

.btn-cancel {
  background: #999;
  color: #fff;
  border: none;
  padding: 8px 40px;
  font-size: 13px;
  cursor: pointer;
}

.btn-cancel:hover {
  background: #777;
}

.btn-delete {
  background: #dc143c;
  color: #fff;
  border: none;
  padding: 8px 40px;
  font-size: 13px;
  cursor: pointer;
}

.btn-delete:hover {
  background: #b01030;
}
</style>
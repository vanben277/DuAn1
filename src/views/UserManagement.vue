<script setup lang="ts">
import { ref } from 'vue';
import AppHeader from '../components/AppHeader.vue';
import AppSidebar from '../components/AppSidebar.vue';
import { useRouter } from 'vue-router';

const router = useRouter();
// State quản lý Menu
const activeMenu = ref('user');

// State Form Search
const storeName = ref('');
const userName = ref('');
const userNameKana = ref('');
const typeStaff = ref(false);
const typeManager = ref(false);

// Mock Data
const users = ref([
  { id: '111111', name: 'Taro 1', nameKana: 'Taro 1', storeId: '609', storeName: 'Saitama Main Store', type: 'Staff' },
  { id: '222222', name: 'Taro 2', nameKana: 'Taro 2', storeId: '608', storeName: 'Saitama Chuo Store', type: 'Staff' },
  { id: '333333', name: 'Taro 3', nameKana: 'Taro 3', storeId: '608', storeName: 'Saitama Chuo Store', type: 'Manager' },
  { id: '444444', name: 'Taro 4', nameKana: 'Taro 4', storeId: '608', storeName: 'Saitama Chuo Store', type: 'Manager' }
]);

const handleSearch = () => {
  console.log('Search params:', {
    store: storeName.value,
    user: userName.value,
    kana: userNameKana.value,
    staff: typeStaff.value,
    manager: typeManager.value
  });
};

const handleAction = () => {
  router.push('/users/register')
}

</script>

<template>
  <div class="app-container">
    <AppHeader />
    
    <div class="main-layout">
      <!-- Sidebar nhận prop activeMenu và lắng nghe sự kiện update -->
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
                  <select v-model="storeName" class="form-control select-box">
                    <option value="">Select</option>
                    <option value="store1">Store 1</option>
                    <option value="store2">Store 2</option>
                  </select>
                </td>
              </tr>
              <tr>
                <td class="label-col">User Name</td>
                <td class="input-col flex-row">
                  <input type="text" v-model="userName" class="form-control" />
                  <span>User Name (Kana)</span>
                  <input type="text" v-model="userNameKana" class="form-control" />
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
          <span>Total Items: 10 items</span>
          <span class="float-right">1 - 10 / 10 items &lt; &gt;</span>
        </div>

        <!-- Data Table -->
        <table class="data-table">
          <thead>
            <tr>
              <th>User ID</th>
              <th>User Name</th>
              <th>User Name (Kana)</th>
              <th>Store ID</th>
              <th>Store Name</th>
              <th>Type</th>
              <th>Edit</th>
              <th>Delete</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in users" :key="user.id">
              <td class="text-center">{{ user.id }}</td>
              <td>{{ user.name }}</td>
              <td>{{ user.nameKana }}</td>
              <td class="text-center">{{ user.storeId }}</td>
              <td>{{ user.storeName }}</td>
              <td class="text-center">{{ user.type }}</td>
              <td class="text-center">
                <button class="btn-action">Edit</button>
              </td>
              <td class="text-center">
                <button class="btn-action">Delete</button>
              </td>
            </tr>
          </tbody>
        </table>

        <!-- New Registration -->
        <div class="action-footer">
          <button class="btn-new" @click="handleAction">New Registration</button>
        </div>
      
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Reset & Layout */
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

/* Search Box */
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

/* Table */
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

/* Footer & Tooltips */
.pagination-info {
  margin-bottom: 10px;
}
.float-right {
  float: right;
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
</style>
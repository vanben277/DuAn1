<script setup lang="ts">
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import AppHeader from '../components/AppHeader.vue';
import AppSidebar from '../components/AppSidebar.vue';

// Router để chuyển trang
const router = useRouter();

// State quản lý Menu
const activeMenu = ref('user');

// State Form Data
const formData = reactive({
  userId: '',
  password: '',
  userName: '',
  userNameKana: '',
  passwordConfirm: '',
  email: '',
  storeId: '',
  userType: 'staff' // Default value
});

// Xử lý sự kiện
const handleSubmit = () => {
  console.log('Register Payload:', formData);
  alert('Đăng ký thành công (Xem console log)');
};

const handleBack = () => {
  // Quay lại trang danh sách (giả sử đường dẫn là /users)
  router.push('/users');
};
</script>

<template>
  <div class="app-container">
    <AppHeader />
    
    <div class="main-layout">
      <!-- Sidebar -->
      <AppSidebar 
        :activeMenu="activeMenu" 
        @update:menu="(id) => activeMenu = id" 
      />
      
      <div class="content-area">
        <div class="form-wrapper">
          <table class="form-table">
            <tbody>
              <!-- Row 1: ID & Password -->
              <tr>
                <td class="label-col">User ID</td>
                <td class="input-col">
                  <input type="text" v-model="formData.userId" class="form-input" />
                </td>
                <td class="label-col">Password</td>
                <td class="input-col">
                  <input type="password" v-model="formData.password" class="form-input" />
                </td>
              </tr>

              <!-- Row 2: Name & Confirm Password -->
              <tr>
                <td class="label-col">User Name</td>
                <td class="input-col flex-align">
                  <input type="text" v-model="formData.userName" class="form-input" />
                  <!-- Icon placeholder -->
                </td>
                <td class="label-col">Password (Confirm)</td>
                <td class="input-col">
                  <input type="password" v-model="formData.passwordConfirm" class="form-input" />
                </td>
              </tr>

              <!-- Row 3: Name Kana & Email -->
              <tr>
                <td class="label-col">User Name (Kana)</td>
                <td class="input-col">
                  <input type="text" v-model="formData.userNameKana" class="form-input" />
                </td>
                <td class="label-col">Email Address</td>
                <td class="input-col">
                  <input type="email" v-model="formData.email" class="form-input" />
                </td>
              </tr>

              <!-- Row 4: Store -->
              <tr>
                <td class="label-col">Store</td>
                <td colspan="3" class="input-col">
                  <select v-model="formData.storeId" class="form-input">
                    <option value="">Select</option>
                    <option value="store1">Store 1</option>
                    <option value="store2">Store 2</option>
                  </select>
                </td>
              </tr>

              <!-- Row 5: Type -->
              <tr>
                <td class="label-col">Type</td>
                <td colspan="3" class="input-col">
                  <label class="radio-label">
                    <input 
                      type="radio" 
                      value="staff" 
                      v-model="formData.userType" 
                    />
                    Staff
                  </label>
                  <label class="radio-label">
                    <input 
                      type="radio" 
                      value="manager" 
                      v-model="formData.userType" 
                    />
                    Manager
                  </label>
                </td>
              </tr>
            </tbody>
          </table>

          <!-- Button Actions -->
          <div class="action-buttons">
            <button class="btn btn-back" @click="handleBack">Back</button>
            <button class="btn btn-register" @click="handleSubmit">Register</button>
          </div>

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
  padding: 40px;
  background: #fff;
}

.form-wrapper {
  max-width: 700px;
}

/* Table Styles */
.form-table {
  width: 100%;
  border-collapse: collapse;
}

.label-col {
  padding: 12px 20px;
  text-align: right;
  width: 180px;
  white-space: nowrap;
}

.input-col {
  padding: 12px 20px;
}

/* Inputs */
.form-input {
  width: 200px;
  padding: 4px 6px;
  border: 1px solid #000;
  font-size: 13px;
}

.input-short {
  width: 150px;
}

.select-box {
  width: 250px;
}

.flex-align {
  display: flex;
  align-items: center;
  gap: 10px;
}

.icon-placeholder {
  width: 18px;
  height: 18px;
  border: 1px solid #000;
  background: #fff;
  cursor: pointer;
}

/* Radio Buttons */
.radio-label {
  margin-right: 30px;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 5px;
}

/* Buttons */
.action-buttons {
  margin-top: 40px;
  text-align: center;
  display: flex;
  justify-content: center;
  gap: 20px;
}

.btn {
  padding: 8px 40px;
  font-size: 13px;
  cursor: pointer;
}

.btn-back {
  border: 1px solid #000;
  background: #fff;
  color: #000;
}

.btn-register {
  background: #dc143c;
  color: #fff;
  border: none;
}
</style>
<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import AppHeader from '../components/AppHeader.vue';
import AppSidebar from '../components/AppSidebar.vue';
import { getStores } from '../apis/storeApi';
import { registerUser, updateUser, getRoles, getUserDetail } from '../apis/userApi';

interface Store {
  storeCode: string;
  storeName: string;
  address?: string;
  isActive: boolean;
}

const router = useRouter();
const route = useRoute();
const activeMenu = ref<string>('user');
const stores = ref<Store[]>([]);
const roles = ref<string[]>([]);

const currentUserId = route.params.id; 
const isEditMode = computed(() => !!currentUserId);

// Modal state
const showSuccessModal = ref(false);
const successMessage = ref('');

const formData = reactive({
  userCode: '',
  password: '',
  fullName: '',
  passwordConfirm: '',
  email: '',
  storeCode: '',
  role: 'STAFF',
  isActive: true
});

onMounted(async () => {
  try {
    // Load Stores & Roles
    const [resStores, resRoles] = await Promise.all([
      getStores(),
      getRoles()
    ]);
    stores.value = resStores.data.data;
    roles.value = resRoles.data.data;

    if (isEditMode.value) {
      const resUser = await getUserDetail(currentUserId);
      const data = resUser.data.data;
      
      formData.userCode = data.userCode;
      formData.fullName = data.fullName;
      formData.email = data.email;
      formData.storeCode = data.storeCode;
      formData.role = data.role;
      formData.isActive = data.isActive;
      
      formData.password = '';
      formData.passwordConfirm = '';
    }

  } catch (e) {
    console.error(e);
  }
});

const handleSubmit = async () => {
  try {
    if (isEditMode.value) {
      await updateUser(currentUserId, {
        fullName: formData.fullName,
        email: formData.email,
        storeCode: formData.storeCode,
        role: formData.role,
        isActive: formData.isActive
      });
      successMessage.value = `User [${formData.userCode}] has been updated successfully!`;
      showSuccessModal.value = true;
    } else {
      if (formData.password !== formData.passwordConfirm) {
        alert("Password confirmation does not match!");
        return;
      }
      
      await registerUser({
        userCode: formData.userCode,
        password: formData.password,
        fullName: formData.fullName,
        email: formData.email,
        storeCode: formData.storeCode,
        role: formData.role
      });
      successMessage.value = `User [${formData.userCode}] has been registered successfully!`;
      showSuccessModal.value = true;
    }

  } catch (error: any) {
    alert(error.response?.data?.message || "Operation failed");
  }
};

const closeModal = () => {
  showSuccessModal.value = false;
  router.push('/users');
};

const handleBack = () => {
  router.push('/users');
};
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
        <div class="form-wrapper">
          <h2 class="page-title">{{ isEditMode ? 'Edit User' : 'User Registration' }}</h2>

          <table class="form-table">
            <tbody>
              <tr>
                <td class="label-col">User ID</td>
                <td class="input-col">
                  <input 
                    type="text" 
                    v-model="formData.userCode" 
                    class="form-input" 
                    :disabled="isEditMode"
                    :class="{ 'disabled-input': isEditMode }"
                  />
                </td>
                <td class="label-col">Password</td>
                <td class="input-col">
                  <input 
                    type="password" 
                    v-model="formData.password" 
                    class="form-input" 
                    :disabled="isEditMode"
                    :class="{ 'disabled-input': isEditMode }"
                    :placeholder="isEditMode ? '********' : ''"
                  />
                </td>
              </tr>

              <tr>
                <td class="label-col">User Name</td>
                <td class="input-col flex-align">
                  <input type="text" v-model="formData.fullName" class="form-input" />
                </td>
                <td class="label-col">Password (Confirm)</td>
                <td class="input-col">
                  <input 
                    type="password" 
                    v-model="formData.passwordConfirm" 
                    class="form-input" 
                    :disabled="isEditMode"
                    :class="{ 'disabled-input': isEditMode }"
                    :placeholder="isEditMode ? '********' : ''"
                  />
                </td>
              </tr>

              <tr>
                <td class="label-col">Email Address</td>
                <td class="input-col">
                  <input type="email" v-model="formData.email" class="form-input" />
                </td>
              </tr>

              <tr>
                <td class="label-col">Store</td>
                <td colspan="3" class="input-col">
                  <select v-model="formData.storeCode" class="form-input select-box">
                    <option value="">Select</option>
                    <option v-for="s in stores" :key="s.storeCode" :value="s.storeCode">
                      {{ s.storeName }}
                    </option>
                  </select>
                </td>
              </tr>

              <tr>
                <td class="label-col">Type</td>
                <td colspan="3" class="input-col">
                  <label v-for="r in roles" :key="r" class="radio-label">
                    <input type="radio" :value="r" v-model="formData.role" />
                    {{ r }}
                  </label>
                </td>
              </tr>
            </tbody>
          </table>

          <!-- Button Actions -->
          <div class="action-buttons">
            <button class="btn btn-back" @click="handleBack">Back</button>
            <button class="btn btn-register" @click="handleSubmit">
              {{ isEditMode ? 'Update' : 'Register' }}
            </button>
          </div>

        </div>
      </div>
    </div>

    <!-- Success Modal -->
    <div v-if="showSuccessModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal-container">
        <div class="modal-header">
          {{ isEditMode ? 'Update Successful' : 'Registration Successful' }}
        </div>
        <div class="modal-body">
          <p>{{ successMessage }}</p>
        </div>
        <div class="modal-footer">
          <button class="btn-ok" @click="closeModal">OK</button>
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

.page-title {
  margin-bottom: 20px;
  font-size: 18px;
  border-bottom: 1px solid #ccc;
  padding-bottom: 10px;
}

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

.form-input {
  width: 200px;
  padding: 4px 6px;
  border: 1px solid #000;
  font-size: 13px;
}

.select-box {
  width: 250px;
}

.flex-align {
  display: flex;
  align-items: center;
  gap: 10px;
}

.radio-label {
  margin-right: 30px;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 5px;
}

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

.btn-back:hover {
  background: #f5f5f5;
}

.btn-register {
  background: #dc143c;
  color: #fff;
  border: none;
}

.btn-register:hover {
  background: #b01030;
}

.disabled-input {
  background-color: #e0e0e0;
  cursor: not-allowed;
  color: #666;
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

.btn-ok {
  background: #00a650;
  color: #fff;
  border: none;
  padding: 8px 50px;
  font-size: 13px;
  cursor: pointer;
}

.btn-ok:hover {
  background: #008d43;
}
</style>
<template>
  <div style="font-family: Arial, sans-serif; font-size: 14px">
    <!-- Header -->
    <div
      style="
        border: 1px solid #000;
        padding: 8px 12px;
        background: #fff;
      "
    >
      Login Screen
    </div>

    <!-- Main Content -->
    <div
      style="
        border: 1px solid #000;
        border-top: none;
        min-height: 500px;
        padding: 40px 20px;
        background: #fff;
      "
    >
      <div
        style="
          max-width: 500px;
          margin: 0 auto;
          text-align: center;
        "
      >
        <h2
          style="
            font-size: 18px;
            margin-bottom: 40px;
            font-weight: normal;
          "
        >
          Login
        </h2>

        <div style="margin-bottom: 20px">
          <table style="margin: 0 auto; border-collapse: collapse">
            <tbody>
              <!-- User ID -->
              <tr>
                <td
                  style="
                    text-align: right;
                    padding-right: 20px;
                    padding-bottom: 15px;
                    vertical-align: middle;
                  "
                >
                  User ID<span style="color: red">*</span>
                </td>
                <td style="padding-bottom: 15px">
                  <input
                    type="text"
                    v-model="userId"
                    style="
                      width: 300px;
                      padding: 4px 6px;
                      border: 1px solid #000;
                      font-size: 14px;
                    "
                  />
                </td>
              </tr>

              <!-- Password -->
              <tr>
                <td
                  style="
                    text-align: right;
                    padding-right: 20px;
                    padding-bottom: 15px;
                    vertical-align: middle;
                  "
                >
                  Password<span style="color: red">*</span>
                </td>
                <td style="padding-bottom: 15px">
                  <input
                    type="password"
                    v-model="password"
                    style="
                      width: 300px;
                      padding: 4px 6px;
                      border: 1px solid #000;
                      font-size: 14px;
                    "
                  />
                </td>
              </tr>

              <!-- Store ID -->
              <tr>
                <td
                  style="
                    text-align: right;
                    padding-right: 20px;
                    padding-bottom: 5px;
                    vertical-align: middle;
                  "
                >
                  Store ID
                </td>
                <td style="padding-bottom: 5px">
                  <input
                    type="text"
                    v-model="storeId"
                    style="
                      width: 300px;
                      padding: 4px 6px;
                      border: 1px solid #000;
                      font-size: 14px;
                    "
                  />
                </td>
              </tr>

              <!-- Note -->
              <tr>
                <td></td>
                <td
                  style="
                    text-align: left;
                    padding-bottom: 20px;
                    font-size: 12px;
                    color: red;
                  "
                >
                  * Staff accounts require store ID
                </td>
              </tr>

              <!-- Remember me -->
              <tr>
                <td></td>
                <td style="text-align: left; padding-bottom: 20px">
                  <label
                    style="
                      display: flex;
                      align-items: center;
                      gap: 6px;
                    "
                  >
                    <input
                      type="checkbox"
                      v-model="rememberMe"
                      style="
                        width: 16px;
                        height: 16px;
                        cursor: pointer;
                      "
                    />
                    <span>Save login information</span>
                  </label>
                </td>
              </tr>

              <!-- Login button -->
              <tr>
                <td></td>
                <td style="text-align: center">
                  <button
                    @click="handleLogin"
                    style="
                      background: #00a650;
                      color: #fff;
                      border: none;
                      padding: 8px 60px;
                      font-size: 14px;
                      cursor: pointer;
                      font-weight: normal;
                    "
                  >
                    Login
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- Footer -->
    <div
      style="
        border: 1px solid #000;
        border-top: none;
        padding: 20px;
        background: #fff;
        min-height: 50px;
      "
    ></div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";

const userId = ref("");
const password = ref("");
const storeId = ref("");
const rememberMe = ref(false);

const handleLogin = () => {
  console.log("Login clicked", {
    userId: userId.value,
    password: password.value,
    storeId: storeId.value,
    rememberMe: rememberMe.value,
  });
};
</script>

<template>
  <div>
    <Header />
    <Sidebar :activePage="'equipmentRentals'" />

    <main id="main" class="main">
      <div class="pagetitle">
        <h1>设备租借情况</h1>
        <nav>
          <ol class="breadcrumb">
            <li class="breadcrumb-item"><router-link to="/home">首页</router-link></li>
            <li class="breadcrumb-item active">设备租借情况</li>
          </ol>
        </nav>
      </div>

      <section class="section dashboard">
        <div class="row">
          <div class="col-lg-12">
            <div class="card">
              <div class="card-body">
                <h5 class="card-title">当前设备租借情况</h5>
                <button type="button" class="btn btn-primary" @click="showAddModal">添加设备</button>

                <!-- Success message -->
                <div v-if="message" class="alert alert-success" role="alert">
                  <span>{{ message }}</span>
                </div>

                <!-- Error message -->
                <div v-if="error" class="alert alert-danger" role="alert">
                  <span>{{ error }}</span>
                </div>

                <!-- Table displaying equipment rentals -->
                <table class="table mt-3">
                  <thead>
                  <tr>
                    <th>用户名</th>
                    <th>设备类型</th>
                    <th>数量</th>
                    <th>租借日期</th>
                    <th>归还日期</th>
                    <th>状态</th>
                    <th>图片</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="rental in equipmentRentals" :key="rental.rentalID">
                    <td>{{ rental.username }}</td>
                    <td>{{ rental.equipmentType }}</td>
                    <td>{{ rental.quantity }}</td>
                    <td>{{ formatDate(rental.rentalDate) }}</td>
                    <td>{{ formatDate(rental.returnDate) }}</td>
                    <td>{{ rental.status }}</td>
                    <td>
                      <img :src="`http://localhost:8080${rental.imagePath}`" alt="设备图片" class="img-thumbnail" style="max-width: 100px; max-height: 100px;" />
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- Add Equipment Modal -->
      <div class="modal fade" id="addEquipmentModal" tabindex="-1" aria-labelledby="addEquipmentModalLabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="addEquipmentModalLabel">添加设备</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form @submit.prevent="addEquipmentRental">
              <div class="modal-body">
                <div class="mb-3">
                  <label for="equipmentType" class="form-label">设备类型</label>
                  <input type="text" class="form-control" id="equipmentType" v-model="newRental.equipmentType" required />
                </div>
                <div class="mb-3">
                  <label for="quantity" class="form-label">数量</label>
                  <input type="number" class="form-control" id="quantity" v-model="newRental.quantity" required />
                </div>
                <div class="mb-3">
                  <label for="rentalDate" class="form-label">租借日期</label>
                  <input type="date" class="form-control" id="rentalDate" v-model="newRental.rentalDate" required />
                </div>
                <div class="mb-3">
                  <label for="returnDate" class="form-label">归还日期</label>
                  <input type="date" class="form-control" id="returnDate" v-model="newRental.returnDate" required />
                </div>
                <div class="mb-3">
                  <label for="status" class="form-label">状态</label>
                  <select class="form-control" id="status" v-model="newRental.status" required>
                    <option value="rented">借用中</option>
                    <option value="returned">已归还</option>
                  </select>
                </div>
                <div class="mb-3">
                  <label for="image" class="form-label">设备图片</label>
                  <input type="file" class="form-control" id="image" @change="handleFileUpload" required />
                </div>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
                <button type="submit" class="btn btn-primary">提交租借</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import {Modal} from 'bootstrap';
import axios from '@/api/axios';
import Header from '@/components/Header.vue';
import Sidebar from '@/components/Sidebar.vue';
import {getUserInfo} from '@/api/user';

export default {
  name: 'EquipmentRentals',
  components: {
    Header,
    Sidebar
  },
  data() {
    return {
      equipmentRentals: [],
      newRental: {
        equipmentType: '',
        quantity: '',
        rentalDate: '',
        returnDate: '',
        status: 'rented',
        image: null
      },
      message: '',
      error: '',
      userInfo: {
        username: '',
        userID: ''
      }
    };
  },
  async created() {
    await this.fetchUserInfo();
    await this.fetchEquipmentRentals();
  },
  methods: {
    async fetchUserInfo() {
      try {
        const data = await getUserInfo();
        this.userInfo = data;
        this.newRental.username = data.username;
        this.newRental.userID = data.userID || 10;
      } catch (error) {
        console.error('Failed to fetch user info:', error);
      }
    },
    async fetchEquipmentRentals() {
      try {
        const response = await axios.get('/equipment-rentals');
        if (response.data.code === 0) {
          this.equipmentRentals = response.data.data;
        } else {
          this.error = '获取设备租借信息失败：' + response.data.message;
        }
      } catch (error) {
        this.error = '获取设备租借信息失败：' + error.message;
      }
    },
    showAddModal() {
      this.newRental.username = this.userInfo.username;
      this.newRental.userID = this.userInfo.userID || 10;
      const modal = new Modal(document.getElementById('addEquipmentModal'));
      modal.show();
    },
    handleFileUpload(event) {
      this.newRental.image = event.target.files[0];
    },
    async addEquipmentRental() {
      try {
        const formData = new FormData();
        formData.append('equipmentType', this.newRental.equipmentType);
        formData.append('quantity', this.newRental.quantity);
        formData.append('rentalDate', this.newRental.rentalDate);
        formData.append('returnDate', this.newRental.returnDate);
        formData.append('status', this.newRental.status);
        formData.append('username', this.newRental.username);
        formData.append('image', this.newRental.image);

        const response = await axios.post('/equipment-rentals', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        });

        if (response.data.code === 0) {
          this.message = response.data.message;
          await this.fetchEquipmentRentals();
          const modal = Modal.getInstance(document.getElementById('addEquipmentModal'));
          modal.hide();
        } else {
          this.error = '设备租借失败：' + response.data.message;
        }
      } catch (error) {
        this.error = '设备租借失败：' + error.message;
        console.error('Error response:', error.response.data);
      }
    },
    formatDate(dateString) {
      return new Date(dateString).toLocaleDateString('zh-CN');
    }
  }
};
</script>

<style scoped>
@import '/assets/vendor/bootstrap/css/bootstrap.min.css';
@import '/assets/vendor/bootstrap-icons/bootstrap-icons.css';
@import '/assets/css/style.css';
</style>

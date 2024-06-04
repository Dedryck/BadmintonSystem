<template>
  <div>
    <Header />
    <Sidebar :activePage="'courtBookings'" />

    <main id="main" class="main">
      <div class="pagetitle">
        <h1>球场预订情况</h1>
        <nav>
          <ol class="breadcrumb">
            <li class="breadcrumb-item"><router-link to="/home">首页</router-link></li>
            <li class="breadcrumb-item active">球场预订情况</li>
          </ol>
        </nav>
      </div>

      <section class="section dashboard">
        <div class="row">
          <div class="col-lg-12">
            <div class="card">
              <div class="card-body">
                <h5 class="card-title">当前球场预订情况</h5>
                <button type="button" class="btn btn-primary" @click="showModal">添加预订</button>

                <!-- Success message -->
                <div v-if="message" class="alert alert-success" role="alert">
                  <span>{{ message }}</span>
                </div>

                <!-- Error message -->
                <div v-if="error" class="alert alert-danger" role="alert">
                  <span>{{ error }}</span>
                </div>

                <!-- Table displaying court bookings -->
                <table class="table mt-3">
                  <thead>
                  <tr>
                    <th>位置</th>
                    <th>状态</th>
                    <th>预订详情</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="courtBooking in courtBookingViewModels" :key="courtBooking.court.courtID">
                    <td>{{ courtBooking.court.location }}</td>
                    <td>{{ courtBooking.court.status }}</td>
                    <td>
                      <ul>
                        <li v-for="booking in courtBooking.bookings" :key="booking.bookingID">
                          <span>{{ formatDate(booking.startTime) }} - {{ formatDate(booking.endTime) }} {{ booking.status }}</span>
                        </li>
                      </ul>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>

    <!-- Add Booking Modal -->
    <div class="modal fade" id="addBookingModal" tabindex="-1" aria-labelledby="addBookingModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="addBookingModalLabel">添加预订</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <form @submit.prevent="addBooking">
            <div class="modal-body">
              <div class="mb-3">
                <label for="username" class="form-label">用户名</label>
                <input type="text" class="form-control" id="username" v-model="userInfo.username" readonly>
              </div>
              <div class="mb-3">
                <label for="courtID" class="form-label">场地名</label>
                <select class="form-select" id="courtID" v-model="newBooking.courtID">
                  <option v-for="court in courts" :value="court.courtID" :key="court.courtID">{{ court.location }}</option>
                </select>
              </div>
              <div class="mb-3">
                <label for="bookingDate" class="form-label">预定日期</label>
                <input type="date" class="form-control" id="bookingDate" v-model="newBooking.bookingDate" required>
              </div>
              <div class="mb-3">
                <label for="startTime" class="form-label">开始时间</label>
                <input type="datetime-local" class="form-control" id="startTime" v-model="newBooking.startTime" required>
              </div>
              <div class="mb-3">
                <label for="endTime" class="form-label">结束时间</label>
                <input type="datetime-local" class="form-control" id="endTime" v-model="newBooking.endTime" required>
              </div>
              <div class="mb-3">
                <label for="status" class="form-label">状态</label>
                <select class="form-control" id="status" v-model="newBooking.status">
                  <option value="confirmed">confirmed</option>
                  <option value="pending">pending</option>
                  <option value="cancelled">cancelled</option>
                </select>
              </div>
              <input type="hidden" v-model="newBooking.userID">  <!-- 隐藏字段 -->
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
              <button type="submit" class="btn btn-primary">提交预订</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from '@/api/axios';
import Header from '@/components/Header.vue';
import Sidebar from '@/components/Sidebar.vue';
import { Modal } from 'bootstrap';
import { getUserInfo } from '@/api/user'; // 引入获取用户信息的方法

export default {
  name: 'CourtBookings',
  components: {
    Header,
    Sidebar
  },
  data() {
    return {
      courtBookingViewModels: [],
      courts: [],
      message: '',
      error: '',
      userInfo: '', // 添加 userInfo 对象来保存用户信息，默认 userID 为 10
      newBooking: {
        userID: '',  // 添加 userID
        courtID: '',
        bookingDate: '',
        startTime: '',
        endTime: '',
        status: 'confirmed'
      }
    };
  },
  async created() {
    await this.fetchUserInfo(); // 添加获取用户信息的方法
    await this.fetchCourtBookings();
    await this.fetchCourts();
  },
  methods: {
    async fetchUserInfo() {
      try {
        const data = await getUserInfo();
        this.userInfo = data; // 保存整个用户信息对象
        this.newBooking.userID = data.userID ;
      } catch (error) {
        console.error('获取用户信息失败', error);
      }
    },
    async fetchCourtBookings() {
      try {
        const response = await axios.get('/court-bookings');
        this.courtBookingViewModels = response.data.singleData; // 修正为 singleData
      } catch (error) {
        this.error = '获取球场预订情况失败';
      }
    },
    async fetchCourts() {
      try {
        const response = await axios.get('/court-management');
        if (response.data.code === 0) {
          this.courts = response.data.singleData.map(court => {
            court.location = `${court.location}`;
            return court;
          });
        } else {
          this.error = '获取球场信息失败：' + response.data.message;
        }
      } catch (error) {
        this.error = '获取球场信息失败：' + error.message;
      }
    },
    showModal() {
      this.newBooking.bookingDate = this.getCurrentDate(); // 打开模态框时自动填充当前日期
      this.newBooking.userID = this.userInfo.userID || 10; // 默认 userID 为 10
      const modal = new Modal(document.getElementById('addBookingModal'));
      modal.show();
    },
    async addBooking() {
      try {
        console.log('newBooking:', this.newBooking);
        const response = await axios.post('/court-bookings/add', this.newBooking);
        this.message = response.data.message;
        await this.fetchCourtBookings();
        const modal = Modal.getInstance(document.getElementById('addBookingModal'));
        modal.hide();
      } catch (error) {
        this.error = '添加预订失败: ' + error.response.data.message;
        console.error('Error response:', error.response.data);
      }
    },
    formatDate(dateString) {
      const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' };
      return new Date(dateString).toLocaleDateString('zh-CN', options).replace(/\//g, '-');
    },
    getCurrentDate() {
      const date = new Date();
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    }
  }
};
</script>

<style scoped>
@import '/assets/vendor/bootstrap/css/bootstrap.min.css';
@import '/assets/vendor/bootstrap-icons/bootstrap-icons.css';
@import '/assets/vendor/boxicons/css/boxicons.min.css';
@import '/assets/vendor/quill/quill.snow.css';
@import '/assets/vendor/quill/quill.bubble.css';
@import '/assets/vendor/remixicon/remixicon.css';
@import '/assets/vendor/simple-datatables/style.css';
@import '/assets/css/style.css';
</style>

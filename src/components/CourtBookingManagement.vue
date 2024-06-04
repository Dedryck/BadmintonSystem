<template>
  <div>
    <Header />
    <Sidebar :activePage="'courtBookingManagement'" />

    <main id="main" class="main">
      <div class="pagetitle">
        <h1>球场预订管理</h1>
        <nav>
          <ol class="breadcrumb">
            <li class="breadcrumb-item"><router-link to="/home">首页</router-link></li>
            <li class="breadcrumb-item active">球场预订管理</li>
          </ol>
        </nav>
      </div>

      <section class="section dashboard">
        <div class="row">
          <div class="col-lg-12">
            <div class="card">
              <div class="card-body">
                <h5 class="card-title">当前球场预订情况</h5>
                <button type="button" class="btn btn-primary" @click="showAddModal">添加预订</button>

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
                    <th>操作</th>
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
                    <td>
                      <ul>
                        <li v-for="booking in courtBooking.bookings" :key="booking.bookingID">
                          <button class="btn btn-warning btn-sm" @click="showEditModal(courtBooking.court.courtID, booking)">修改</button>
                          <button class="btn btn-danger btn-sm" @click="deleteBooking(booking.bookingID)">删除</button>
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
              <input type="hidden" v-model="newBooking.userID">
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
              <button type="submit" class="btn btn-primary">提交预订</button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Edit Booking Modal -->
    <div class="modal fade" id="editBookingModal" tabindex="-1" aria-labelledby="editBookingModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="editBookingModalLabel">修改预订</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <form @submit.prevent="updateBooking">
            <div class="modal-body">
              <div class="mb-3">
                <label for="editCourtID" class="form-label">场地名</label>
                <select class="form-select" id="editCourtID" v-model="editBooking.courtID">
                  <option v-for="court in courts" :value="court.courtID" :key="court.courtID">{{ court.location }}</option>
                </select>
              </div>
              <div class="mb-3">
                <label for="editBookingDate" class="form-label">预定日期</label>
                <input type="date" class="form-control" id="editBookingDate" v-model="editBooking.bookingDate" required>
              </div>
              <div class="mb-3">
                <label for="editStartTime" class="form-label">开始时间</label>
                <input type="datetime-local" class="form-control" id="editStartTime" v-model="editBooking.startTime" required>
              </div>
              <div class="mb-3">
                <label for="editEndTime" class="form-label">结束时间</label>
                <input type="datetime-local" class="form-control" id="editEndTime" v-model="editBooking.endTime" required>
              </div>
              <div class="mb-3">
                <label for="editStatus" class="form-label">状态</label>
                <select class="form-control" id="editStatus" v-model="editBooking.status">
                  <option value="confirmed">confirmed</option>
                  <option value="pending">pending</option>
                  <option value="cancelled">cancelled</option>
                </select>
              </div>
              <input type="hidden" v-model="editBooking.userID">
              <input type="hidden" v-model="editBooking.bookingID"> <!-- 确保包含 bookingID 字段 -->
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
              <button type="submit" class="btn btn-primary">更新预订</button>
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
import { getUserInfo } from '@/api/user';

export default {
  name: 'CourtBookingManagement',
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
      userInfo: { userID: 10 },
      newBooking: {
        userID: 10,
        courtID: '',
        bookingDate: '',
        startTime: '',
        endTime: '',
        status: 'confirmed'
      },
      editBooking: {
        bookingID: '',  // 确保包含 bookingID 字段
        userID: 10,
        courtID: '',
        bookingDate: '',
        startTime: '',
        endTime: '',
        status: 'confirmed'
      }
    };
  },
  async created() {
    await this.fetchUserInfo();
    await this.fetchCourtBookings();
    await this.fetchCourts();
  },
  methods: {
    async fetchUserInfo() {
      try {
        const data = await getUserInfo();
        this.userInfo = data;
        this.newBooking.userID = data.userID || 10;
        this.editBooking.userID = data.userID || 10;
      } catch (error) {
        console.error('获取用户信息失败', error);
      }
    },
    async fetchCourtBookings() {
      try {
        const response = await axios.get('/court-bookings');
        this.courtBookingViewModels = response.data.singleData;
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
    showAddModal() {
      this.newBooking.bookingDate = this.getCurrentDate();
      this.newBooking.userID = this.userInfo.userID || 10;
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
    showEditModal(courtID, booking) {
      this.editBooking = { ...booking, courtID };
      this.editBooking.userID = this.userInfo.userID || 10;
      this.editBooking.bookingDate = booking.startTime.substring(0, 10); // 提取日期部分
      // 转换时间格式以符合 input[type="datetime-local"]
      this.editBooking.startTime = new Date(booking.startTime).toISOString().slice(0, 16);
      this.editBooking.endTime = new Date(booking.endTime).toISOString().slice(0, 16);

      const modal = new Modal(document.getElementById('editBookingModal'));
      modal.show();
    },
    async updateBooking() {
      // 强制使用 UTC 格式提交时间数据
      this.editBooking.startTime = new Date(this.editBooking.startTime).toISOString();
      this.editBooking.endTime = new Date(this.editBooking.endTime).toISOString();

      try {
        console.log('Updating booking:', this.editBooking); // 添加日志
        const response = await axios.post('/court-bookings/edit', this.editBooking);
        console.log('Updated booking response:', response.data);
        this.message = response.data.message;
        await this.fetchCourtBookings();
        const modal = Modal.getInstance(document.getElementById('editBookingModal'));
        modal.hide();
      } catch (error) {
        this.error = '修改预订失败: ' + error.response.data.message;
        console.error('Error response:', error.response.data);
      }
    },
    async deleteBooking(bookingID) {
      try {
        const response = await axios.delete(`/court-bookings/delete/${bookingID}`);
        this.message = response.data.message;
        await this.fetchCourtBookings();
      } catch (error) {
        this.error = '删除预订失败: ' + error.response.data.message;
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
@import '/assets/css/style.css';
</style>

<template>
  <div>
    <Header />
    <Sidebar :activePage="'courtManagement'" />

    <main id="main" class="main">
      <div class="pagetitle">
        <h1>球场管理</h1>
        <nav>
          <ol class="breadcrumb">
            <li class="breadcrumb-item"><router-link to="/home">首页</router-link></li>
            <li class="breadcrumb-item active">球场管理</li>
          </ol>
        </nav>
      </div>

      <section class="section dashboard">
        <div class="row">
          <div class="col-lg-12">
            <div class="card">
              <div class="card-body">
                <h5 class="card-title">当前球场信息</h5>
                <button type="button" class="btn btn-primary" @click="showAddModal">添加场地</button>

                <!-- Success message -->
                <div v-if="message" class="alert alert-success" role="alert">
                  <span>{{ message }}</span>
                </div>

                <!-- Error message -->
                <div v-if="error" class="alert alert-danger" role="alert">
                  <span>{{ error }}</span>
                </div>

                <!-- Table displaying courts -->
                <table class="table mt-3">
                  <thead>
                  <tr>
                    <th>场地位置</th>
                    <th>状态</th>
                    <th>操作</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="court in courts" :key="court.courtID">
                    <td>{{ court.location }}</td>
                    <td>{{ court.status }}</td>
                    <td>
                      <button class="btn btn-warning btn-sm" @click="showEditModal(court)">修改</button>
                      <button class="btn btn-danger btn-sm" @click="deleteCourt(court.courtID)">删除</button>
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

    <!-- Add Court Modal -->
    <div class="modal fade" id="addCourtModal" tabindex="-1" aria-labelledby="addCourtModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="addCourtModalLabel">添加场地</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <form @submit.prevent="addCourt">
            <div class="modal-body">
              <div class="mb-3">
                <label for="location" class="form-label">场地位置</label>
                <input type="text" class="form-control" id="location" v-model="newCourt.location" required>
              </div>
              <div class="mb-3">
                <label for="status" class="form-label">状态</label>
                <select class="form-select" id="status" v-model="newCourt.status" required>
                  <option value="available">available</option>
                  <option value="booked">booked</option>
                  <option value="maintenance">maintenance</option>
                </select>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
              <button type="submit" class="btn btn-primary">提交</button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Edit Court Modal -->
    <div class="modal fade" id="editCourtModal" tabindex="-1" aria-labelledby="editCourtModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="editCourtModalLabel">修改场地</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <form @submit.prevent="updateCourt">
            <div class="modal-body">
              <div class="mb-3">
                <label for="editLocation" class="form-label">场地位置</label>
                <input type="text" class="form-control" id="editLocation" v-model="editCourt.location" required>
              </div>
              <div class="mb-3">
                <label for="editStatus" class="form-label">状态</label>
                <select class="form-select" id="editStatus" v-model="editCourt.status" required>
                  <option value="available">available</option>
                  <option value="booked">booked</option>
                  <option value="maintenance">maintenance</option>
                </select>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
              <button type="submit" class="btn btn-primary">更新</button>
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
import { Modal } from 'bootstrap';  // Importing Modal from bootstrap
import { getUserInfo } from '@/api/user';

export default {
  name: 'CourtManagement',
  components: {
    Header,
    Sidebar
  },
  data() {
    return {
      courts: [],
      newCourt: {
        location: '',
        status: 'available'
      },
      editCourt: {
        courtID: '',
        location: '',
        status: 'available'
      },
      message: '',
      error: '',
    };
  },
  async created() {
    await this.fetchCourts();
  },
  methods: {
    async fetchCourts() {
      try {
        const response = await axios.get('/court-management/list');
        this.courts = response.data.singleData;
      } catch (error) {
        this.error = '获取场地信息失败：' + error.message;
      }
    },
    showAddModal() {
      this.newCourt = {
        location: '',
        status: 'available'
      };
      const modal = new Modal(document.getElementById('addCourtModal'));
      modal.show();
    },
    async addCourt() {
      try {
        const response = await axios.post('/court-management/add', this.newCourt);
        this.message = response.data.message;
        await this.fetchCourts();
        const modal = Modal.getInstance(document.getElementById('addCourtModal'));
        modal.hide();
      } catch (error) {
        this.error = '添加场地失败: ' + error.response.data.message;
      }
    },
    showEditModal(court) {
      this.editCourt = { ...court };
      const modal = new Modal(document.getElementById('editCourtModal'));
      modal.show();
    },
    async updateCourt() {
      try {
        const response = await axios.post('/court-management/edit', this.editCourt);
        this.message = response.data.message;
        await this.fetchCourts();
        const modal = Modal.getInstance(document.getElementById('editCourtModal'));
        modal.hide();
      } catch (error) {
        this.error = '修改场地失败: ' + error.response.data.message;
      }
    },
    async deleteCourt(courtID) {
      try {
        const response = await axios.delete(`/court-management/delete/${courtID}`);
        this.message = response.data.message;
        await this.fetchCourts();
      } catch (error) {
        this.error = '删除场地失败: ' + error.response.data.message;
      }
    }
  }
};
</script>

<style scoped>
@import '/assets/vendor/bootstrap/css/bootstrap.min.css';
@import '/assets/vendor/bootstrap-icons/bootstrap-icons.css';
@import '/assets/css/style.css';
</style>

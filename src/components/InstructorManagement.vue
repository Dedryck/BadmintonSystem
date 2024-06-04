<template>
  <div>
    <Header />
    <Sidebar :activePage="'instructorManagement'" />

    <main id="main" class="main">
      <div class="pagetitle">
        <h1>教练信息管理</h1>
        <nav>
          <ol class="breadcrumb">
            <li class="breadcrumb-item"><router-link to="/home">首页</router-link></li>
            <li class="breadcrumb-item active">教练信息管理</li>
          </ol>
        </nav>
      </div>

      <section class="section dashboard">
        <div class="row">
          <div class="col-lg-12">
            <div class="card">
              <div class="card-body">
                <h5 class="card-title">教练信息管理</h5>
                <button type="button" class="btn btn-primary" @click="showAddModal">添加教练</button>

                <!-- Success message -->
                <div v-if="message" class="alert alert-success" role="alert">
                  <span>{{ message }}</span>
                </div>

                <!-- Table displaying instructors -->
                <table class="table mt-3">
                  <thead>
                  <tr>
                    <th>照片</th>
                    <th>姓名</th>
                    <th>电话</th>
                    <th>邮箱</th>
                    <th>专长</th>
                    <th>状态</th>
                    <th>操作</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="instructor in instructors" :key="instructor.instructorID">
                    <td>
                      <img :src="`http://localhost:8080${instructor.photoPath}`" alt="教练图片" class="img-thumbnail" style="max-width: 100px; max-height: 100px;" />
                    </td>
                    <td>{{ instructor.name }}</td>
                    <td>{{ instructor.phone }}</td>
                    <td>{{ instructor.email }}</td>
                    <td>{{ instructor.specialization }}</td>
                    <td>{{ instructor.available ? '可用' : '不可用' }}</td>
                    <td>
                      <button type="button" class="btn btn-warning" @click="showEditModal(instructor)">修改</button>
                      <button type="button" class="btn btn-danger" @click="deleteInstructor(instructor.instructorID)">删除</button>
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

    <!-- Add Instructor Modal -->
    <div class="modal fade" id="addInstructorModal" tabindex="-1" aria-labelledby="addInstructorModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="addInstructorModalLabel">添加教练</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <form @submit.prevent="addInstructor">
            <div class="modal-body">
              <div class="mb-3">
                <label for="name" class="form-label">姓名</label>
                <input type="text" class="form-control" id="name" v-model="newInstructor.name" required>
              </div>
              <div class="mb-3">
                <label for="phone" class="form-label">电话</label>
                <input type="text" class="form-control" id="phone" v-model="newInstructor.phone" required>
              </div>
              <div class="mb-3">
                <label for="email" class="form-label">邮箱</label>
                <input type="email" class="form-control" id="email" v-model="newInstructor.email" required>
              </div>
              <div class="mb-3">
                <label for="specialization" class="form-label">专长</label>
                <input type="text" class="form-control" id="specialization" v-model="newInstructor.specialization" required>
              </div>
              <div class="mb-3">
                <label for="available" class="form-label">状态</label>
                <select class="form-control" id="available" v-model="newInstructor.available">
                  <option value="true">可用</option>
                  <option value="false">不可用</option>
                </select>
              </div>
              <div class="mb-3">
                <label for="photo" class="form-label">照片</label>
                <input type="file" class="form-control" id="photo" @change="onFileChange">
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

    <!-- Edit Instructor Modal -->
    <div class="modal fade" id="editInstructorModal" tabindex="-1" aria-labelledby="editInstructorModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="editInstructorModalLabel">修改教练信息</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <form @submit.prevent="updateInstructor">
            <div class="modal-body">
              <input type="hidden" v-model="editInstructor.instructorID">
              <div class="mb-3">
                <label for="editName" class="form-label">姓名</label>
                <input type="text" class="form-control" id="editName" v-model="editInstructor.name" required>
              </div>
              <div class="mb-3">
                <label for="editPhone" class="form-label">电话</label>
                <input type="text" class="form-control" id="editPhone" v-model="editInstructor.phone" required>
              </div>
              <div class="mb-3">
                <label for="editEmail" class="form-label">邮箱</label>
                <input type="email" class="form-control" id="editEmail" v-model="editInstructor.email" required>
              </div>
              <div class="mb-3">
                <label for="editSpecialization" class="form-label">专长</label>
                <input type="text" class="form-control" id="editSpecialization" v-model="editInstructor.specialization" required>
              </div>
              <div class="mb-3">
                <label for="editAvailable" class="form-label">状态</label>
                <select class="form-control" id="editAvailable" v-model="editInstructor.available">
                  <option value="true">可用</option>
                  <option value="false">不可用</option>
                </select>
              </div>
              <div class="mb-3">
                <label for="editPhoto" class="form-label">照片</label>
                <input type="file" class="form-control" id="editPhoto" @change="onEditFileChange">
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
  </div>
</template>

<script>
import { Modal } from 'bootstrap';
import Header from '@/components/Header.vue';
import Sidebar from '@/components/Sidebar.vue';
import axios from '@/api/axios';

export default {
  name: 'InstructorManagement',
  components: {
    Header,
    Sidebar
  },
  data() {
    return {
      instructors: [],
      newInstructor: {
        name: '',
        phone: '',
        email: '',
        specialization: '',
        available: 'true',
        photo: null
      },
      editInstructor: {
        instructorID: '',
        name: '',
        phone: '',
        email: '',
        specialization: '',
        available: 'true',
        photo: null
      },
      message: '',
      error: ''
    };
  },
  async created() {
    await this.fetchInstructors();
  },
  methods: {
    async fetchInstructors() {
      try {
        const response = await axios.get('/instructor-management');
        this.instructors = response.data.singleData;
      } catch (error) {
        console.error('Failed to fetch instructors:', error);
      }
    },
    onFileChange(event) {
      this.newInstructor.photo = event.target.files[0];
    },
    onEditFileChange(event) {
      this.editInstructor.photo = event.target.files[0];
    },
    showAddModal() {
      this.newInstructor = {
        name: '',
        phone: '',
        email: '',
        specialization: '',
        available: 'true',
        photo: null
      };
      const modal = new Modal(document.getElementById('addInstructorModal'));
      modal.show();
    },
    async addInstructor() {
      const formData = new FormData();
      for (const key in this.newInstructor) {
        formData.append(key, this.newInstructor[key]);
      }

      try {
        const response = await axios.post('/instructor-management/add', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        });

        this.message = response.data.message || '教练添加成功';
        await this.fetchInstructors();
        const modal = Modal.getInstance(document.getElementById('addInstructorModal'));
        modal.hide();
      } catch (error) {
        console.error('Failed to add instructor:', error);
        this.error = error.response?.data?.message || '教练添加失败';
      }
    },



    showEditModal(instructor) {
      this.editInstructor = { ...instructor, photo: null };
      const modal = new Modal(document.getElementById('editInstructorModal'));
      modal.show();
    },
    async updateInstructor() {
      const formData = new FormData();
      formData.append('instructorID', this.editInstructor.instructorID);
      formData.append('name', this.editInstructor.name);
      formData.append('phone', this.editInstructor.phone);
      formData.append('email', this.editInstructor.email);
      formData.append('specialization', this.editInstructor.specialization);
      formData.append('available', this.editInstructor.available);
      if (this.editInstructor.photo) {
        formData.append('photo', this.editInstructor.photo);
      }
      // for (const key in this.editInstructor) {
      //   formData.append(key, this.editInstructor[key]);
      // }

      try {
        const response = await axios.post('/instructor-management/edit', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        });

        this.message = response.data.message || '教练修改成功';
        await this.fetchInstructors();
        const modal = Modal.getInstance(document.getElementById('editInstructorModal'));
        modal.hide();
      } catch (error) {
        console.error('Failed to update instructor:', error);
        this.error = error.response?.data?.message || '教练修改失败';
      }
    },



    async deleteInstructor(id) {
      if (confirm('确定要删除此教练信息吗？')) {
        try {
          const response = await axios.delete(`/instructor-management/delete/${id}`, {
            headers: {
              'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
          });
          this.message = response.data.message || '删除反馈成功';
          await this.fetchInstructors();
        } catch (error) {
          console.error('Failed to delete instructor:', error);
          this.error = error.response?.data?.message || '删除反馈失败';
        }
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

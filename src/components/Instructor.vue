<template>
  <div>
    <Header />
    <Sidebar :activePage="'instructors'" />

    <main id="main" class="main">
      <div class="pagetitle">
        <h1>教练信息</h1>
        <nav>
          <ol class="breadcrumb">
            <li class="breadcrumb-item"><router-link to="/home">首页</router-link></li>
            <li class="breadcrumb-item active">教练信息</li>
          </ol>
        </nav>
      </div>

      <section class="section dashboard">
        <div class="row">
          <div class="col-lg-12">
            <div class="card">
              <div class="card-body">
                <h5 class="card-title">当前教练信息</h5>

                <!-- Success message -->
                <div v-if="message" class="alert alert-success" role="alert">
                  <span>{{ message }}</span>
                </div>

                <!-- Error message -->
                <div v-if="error" class="alert alert-danger" role="alert">
                  <span>{{ error }}</span>
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
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="instructor in instructors" :key="instructor.instructorID">
                    <td>
                      <img :src="getFullImagePath(instructor.photoPath)" alt="照片" class="img-thumbnail" style="max-width: 100px; max-height: 100px;" />
                    </td>
                    <td>{{ instructor.name }}</td>
                    <td>{{ instructor.phone }}</td>
                    <td>{{ instructor.email }}</td>
                    <td>{{ instructor.specialization }}</td>
                    <td>{{ instructor.available ? '可用' : '不可用' }}</td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script>
import axios from '@/api/axios';
import Header from '@/components/Header.vue';
import Sidebar from '@/components/Sidebar.vue';
import { getUserInfo } from '@/api/user';

export default {
  name: 'Instructor',
  components: {
    Header,
    Sidebar
  },
  data() {
    return {
      instructors: [],
      message: '',
      error: '',
      userInfo: {
        username: '',
        role: ''
      }
    };
  },
  async created() {
    await this.fetchUserInfo();
    await this.fetchInstructors();
  },
  methods: {
    async fetchUserInfo() {
      try {
        const data = await getUserInfo();
        this.userInfo = data;
      } catch (error) {
        console.error('Failed to fetch user info:', error);
      }
    },
    async fetchInstructors() {
      try {
        const response = await axios.get('/instructors');
        if (response.data.code === 0) {
          this.instructors = response.data.singleData;
        } else {
          this.error = '获取教练信息失败：' + response.data.message;
        }
      } catch (error) {
        this.error = '获取教练信息失败：' + error.message;
      }
    },
    getFullImagePath(photoPath) {
      // 拼接完整的图片路径
      return `http://localhost:8080${photoPath}`;
    }
  }
};
</script>

<style scoped>
@import '/assets/vendor/bootstrap/css/bootstrap.min.css';
@import '/assets/vendor/bootstrap-icons/bootstrap-icons.css';
@import '/assets/css/style.css';
</style>

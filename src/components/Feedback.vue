<template>
  <div>
    <Header />
    <Sidebar :activePage="'feedback'" />

    <main id="main" class="main">
      <div class="pagetitle">
        <h1>反馈管理</h1>
        <nav>
          <ol class="breadcrumb">
            <li class="breadcrumb-item"><router-link to="/home">首页</router-link></li>
            <li class="breadcrumb-item active">反馈管理</li>
          </ol>
        </nav>
      </div>

      <section class="section dashboard">
        <div class="row">
          <div class="col-lg-12">
            <div class="card">
              <div class="card-body">
                <h5 class="card-title">反馈管理</h5>
                <button type="button" class="btn btn-primary" @click="showAddModal">添加反馈</button>

                <!-- Success message -->
                <div v-if="message" class="alert alert-success" role="alert">
                  <span>{{ message }}</span>
                </div>

                <!-- Table displaying feedbacks -->
                <table class="table mt-3">
                  <thead>
                  <tr>
                    <th>用户名</th>
                    <th>评论</th>
                    <th>评分</th>
                    <th>反馈日期</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="feedback in feedbacks" :key="feedback.id">
                    <td>{{ feedback.username }}</td>
                    <td>{{ feedback.comments }}</td>
                    <td>{{ feedback.rating }}</td>
                    <td>{{ formatDate(feedback.feedbackDate) }}</td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>

    <!-- Add Feedback Modal -->
    <div class="modal fade" id="addFeedbackModal" tabindex="-1" aria-labelledby="addFeedbackModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="addFeedbackModalLabel">添加反馈</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <form @submit.prevent="addFeedback">
            <div class="modal-body">
              <div class="mb-3">
                <label for="username" class="form-label">用户名</label>
                <input type="text" class="form-control" id="username" v-model="newFeedback.username" readonly>
                <input type="hidden" v-model="newFeedback.userID">
              </div>
              <div class="mb-3">
                <label for="comments" class="form-label">评论</label>
                <textarea class="form-control" id="comments" v-model="newFeedback.comments" rows="3" required></textarea>
              </div>
              <div class="mb-3">
                <label for="rating" class="form-label">评分</label>
                <select class="form-control" id="rating" v-model="newFeedback.rating" required>
                  <option value="1">1</option>
                  <option value="2">2</option>
                  <option value="3">3</option>
                  <option value="4">4</option>
                  <option value="5">5</option>
                </select>
              </div>
              <input type="hidden" v-model="newFeedback.feedbackDate"> <!-- 隐藏的反馈日期字段 -->
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
import axios from '@/api/axios';
import Header from '@/components/Header.vue';
import Sidebar from '@/components/Sidebar.vue';
import { Modal } from 'bootstrap';
import { getUserInfo } from '@/api/user';

export default {
  name: 'Feedback',
  components: {
    Header,
    Sidebar,
  },
  data() {
    return {
      feedbacks: [],
      message: '',
      error: '',
      userInfo: {
        userID: '',
        username: '',
      },
      newFeedback: {
        userID: '', // 默认 userID 为 10
        username: '',
        comments: '',
        rating: '5',
        feedbackDate: '',
      },
    };
  },
  async created() {
    await this.fetchUserInfo();
    await this.fetchFeedbacks();
  },
  methods: {
    async fetchUserInfo() {
      try {
        const data = await getUserInfo();
        this.userInfo = data;
        this.newFeedback.userID = data.userID || 10;
        this.newFeedback.username = data.username;
      } catch (error) {
        console.error('Failed to fetch user info:', error);
        this.newFeedback.userID = 10; // 获取用户信息失败时保持为 10
      }
    },
    async fetchFeedbacks() {
      try {
        const response = await axios.get('/feedback');
        if (response.data.code === 0) {
          this.feedbacks = response.data.singleData;
        } else {
          this.error = '获取反馈信息失败：' + response.data.message;
        }
      } catch (error) {
        this.error = '获取反馈信息失败：' + error.message;
      }
    },
    showAddModal() {
      const modal = new Modal(document.getElementById('addFeedbackModal'));
      modal.show();
    },
    async addFeedback() {
      try {
        this.newFeedback.feedbackDate = new Date().toISOString(); // 设置当前日期
        const response = await axios.post('/feedback/add', this.newFeedback, {
          params: {
            username: this.newFeedback.username, // 在查询参数中添加用户名
          },
        });
        this.message = response.data.message;
        await this.fetchFeedbacks();
        const modal = Modal.getInstance(document.getElementById('addFeedbackModal'));
        modal.hide();
      } catch (error) {
        this.error = '添加反馈失败: ' + error.response.data.message;
        console.error('Error response:', error.response.data);
      }
    },
    formatDate(dateString) {
      const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' };
      return new Date(dateString).toLocaleDateString('zh-CN', options).replace(/\//g, '-');
    },
  },
};
</script>

<style scoped>
@import '/assets/vendor/bootstrap/css/bootstrap.min.css';
@import '/assets/vendor/bootstrap-icons/bootstrap-icons.css';
@import '/assets/css/style.css';
</style>

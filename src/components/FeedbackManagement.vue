<template>
  <div>
    <Header />
    <Sidebar :activePage="'feedbackManagement'" />

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
                <!-- Error message -->
                <div v-if="error" class="alert alert-danger" role="alert">
                  <span>{{ error }}</span>
                </div>

                <!-- Table displaying feedbacks -->
                <table class="table mt-3">
                  <thead>
                  <tr>
                    <th>用户名</th>
                    <th>评论</th>
                    <th>评分</th>
                    <th>反馈日期</th>
                    <th>操作</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="feedback in feedbacks" :key="feedback.feedbackID">
                    <td>{{ feedback.username }}</td>
                    <td>{{ feedback.comments }}</td>
                    <td>{{ feedback.rating }}</td>
                    <td>{{ formatDate(feedback.feedbackDate) }}</td>
                    <td>
                      <button type="button" class="btn btn-warning" @click="showEditModal(feedback)">修改</button>
                      <button type="button" class="btn btn-danger" @click="deleteFeedback(feedback.feedbackID)">删除</button>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </section>

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
                  <input type="text" class="form-control" v-model="newFeedback.username" readonly>
                  <input type="hidden" v-model="newFeedback.userID">
                </div>
                <div class="mb-3">
                  <label for="comments" class="form-label">评论</label>
                  <textarea class="form-control" v-model="newFeedback.comments" rows="3" required></textarea>
                </div>
                <div class="mb-3">
                  <label for="rating" class="form-label">评分</label>
                  <select class="form-control" v-model="newFeedback.rating" required>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
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

      <!-- Edit Feedback Modal -->
      <div class="modal fade" id="editFeedbackModal" tabindex="-1" aria-labelledby="editFeedbackModalLabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="editFeedbackModalLabel">修改反馈</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form @submit.prevent="updateFeedback">
              <div class="modal-body">
                <input type="hidden" v-model="editFeedback.feedbackID">
                <input type="hidden" v-model="editFeedback.userID">
                <div class="mb-3">
                  <label for="editUsername" class="form-label">用户名</label>
                  <input type="text" class="form-control" v-model="editFeedback.username" readonly>
                </div>
                <div class="mb-3">
                  <label for="editComments" class="form-label">评论</label>
                  <textarea class="form-control" v-model="editFeedback.comments" rows="3" required></textarea>
                </div>
                <div class="mb-3">
                  <label for="editRating" class="form-label">评分</label>
                  <select class="form-control" v-model="editFeedback.rating" required>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
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
    </main>
  </div>
</template>

<script>
import { Modal } from 'bootstrap';
import Header from '@/components/Header.vue';
import Sidebar from '@/components/Sidebar.vue';
import { getUserInfo } from '@/api/user';
import axios from '@/api/axios';

export default {
  name: 'FeedbackManagement',
  components: {
    Header,
    Sidebar
  },
  data() {
    return {
      feedbacks: [],
      newFeedback: {
        username: '',
        userID: '',
        comments: '',
        rating: 5,
        feedbackDate: new Date().toISOString().split('T')[0]
      },
      editFeedback: {
        feedbackID: '',
        username: '',
        userID: '',
        comments: '',
        rating: 5,
        feedbackDate: new Date().toISOString().split('T')[0]
      },
      message: '',
      error: ''
    };
  },
  async created() {
    await this.fetchUserInfo();
    await this.fetchFeedbacks();
  },
  methods: {
    async fetchUserInfo() {
      try {
        const response = await getUserInfo();
        this.newFeedback.username = response.username;
        this.newFeedback.userID = response.userID;
      } catch (error) {
        this.error = '获取用户信息失败';
        console.error('Failed to fetch user info:', error);
      }
    },
    async fetchFeedbacks() {
      try {
        const response = await axios.get('/feedback-management');
        this.feedbacks = response.data.singleData;
      } catch (error) {
        this.error = '获取反馈信息失败';
        console.error('Failed to fetch feedbacks:', error);
      }
    },
    async showAddModal() {
      try {
        await this.fetchUserInfo(); // 确保先获取用户信息
        this.newFeedback.comments = '';
        this.newFeedback.rating = 5;
        const modal = new Modal(document.getElementById('addFeedbackModal'));
        modal.show();
      } catch (error) {
        this.error = '无法显示添加反馈模态框';
        console.error('Failed to show add modal:', error);
      }
    },
    async addFeedback() {
      try {
        // 确保 userID 已被设置
        if (!this.newFeedback.userID) {
          await this.fetchUserInfo();
        }

        const response = await axios.post('/feedback-management/add?username=admin', {
          userID: this.newFeedback.userID,
          username: this.newFeedback.username,
          comments: this.newFeedback.comments,
          rating: this.newFeedback.rating,
          feedbackDate: this.newFeedback.feedbackDate
        }, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        });

        this.message = response.data.message || '添加反馈成功';
        this.error = ''; // 清空错误消息
        await this.fetchFeedbacks();
        const modal = Modal.getInstance(document.getElementById('addFeedbackModal'));
        modal.hide();
      } catch (error) {
        console.error('Failed to add feedback:', error);
        this.error = error.response?.data?.message || '添加反馈失败';
        this.message = ''; // 清空成功消息
      }
    },
    showEditModal(feedback) {
      this.editFeedback = { ...feedback };
      const modal = new Modal(document.getElementById('editFeedbackModal'));
      modal.show();
    },
    async updateFeedback() {
      try {
        const response = await axios.post('/feedback-management/edit', {
          feedbackID: this.editFeedback.feedbackID,
          userID: this.editFeedback.userID,
          username: this.editFeedback.username,
          comments: this.editFeedback.comments,
          rating: this.editFeedback.rating,
          feedbackDate: this.editFeedback.feedbackDate
        }, {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        });
        this.message = response.data.message || '修改反馈成功';
        this.error = ''; // 清空错误消息
        await this.fetchFeedbacks();
        const modal = Modal.getInstance(document.getElementById('editFeedbackModal'));
        modal.hide();
      } catch (error) {
        console.error('Failed to update feedback:', error);
        this.error = error.response?.data?.message || '修改反馈失败';
      }
    },
    async deleteFeedback(id) {
      if (confirm('确定要删除此反馈信息吗？')) {
        try {
          const response = await axios.delete(`/feedback-management/delete/${id}`, {
            headers: {
              'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
          });
          this.message = response.data.message || '删除反馈成功';
          this.error = ''; // 清空错误消息
          await this.fetchFeedbacks();
        } catch (error) {
          console.error('Failed to delete feedback:', error);
          this.error = error.response?.data?.message || '删除反馈失败';
        }
      }
    },
    formatDate(date) {
      return new Date(date).toISOString().split('T')[0];
    }
  }
};
</script>

<style scoped>
@import '/assets/vendor/bootstrap/css/bootstrap.min.css';
@import '/assets/vendor/bootstrap-icons/bootstrap-icons.css';
@import '/assets/css/style.css';
</style>

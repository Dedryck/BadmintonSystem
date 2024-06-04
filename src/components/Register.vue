<template>
  <div class="bg">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-md-6">
          <div class="card">
            <div class="card-header text-center">
              <h3>注册新用户</h3>
            </div>
            <div class="card-body">
              <form @submit.prevent="register">
                <div class="form-group">
                  <label for="username">用户名</label>
                  <input type="text" class="form-control" id="username" v-model="username" required>
                </div>
                <div class="form-group">
                  <label for="password">密码</label>
                  <input type="password" class="form-control" id="password" v-model="password" required>
                </div>
                <div class="form-group">
                  <label for="confirmPassword">确认密码</label>
                  <input type="password" class="form-control" id="confirmPassword" v-model="confirmPassword" required>
                </div>
                <div class="form-group">
                  <label for="email">邮箱</label>
                  <input type="email" class="form-control" id="email" v-model="email" required>
                </div>
                <div class="form-group">
                  <label for="phone">电话</label>
                  <input type="text" class="form-control" id="phone" v-model="phone" required>
                </div>
                <div class="form-group">
                  <label for="role">角色</label>
                  <select class="form-control" id="role" v-model="role" required>
                    <option value="member">会员</option>
                  </select>
                </div>
                <button type="submit" class="btn btn-primary btn-block">注册</button>
              </form>
              <button @click="goToLogin" class="btn btn-secondary btn-block mt-3">返回</button>
              <div class="mt-3">
                <span v-if="error" class="text-danger">{{ errorMessage }}</span>
                <span v-if="success" class="text-success">注册成功，请登录。</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from '@/api/axios'; // 确保正确引入axios实例
export default {
  data() {
    return {
      username: '',
      password: '',
      confirmPassword: '', // 新增确认密码字段
      email: '',
      phone: '',
      role: 'member',
      error: false,
      errorMessage: '',
      success: false,
    };
  },
  methods: {
    async register() {
      // 检查密码是否匹配
      if (this.password !== this.confirmPassword) {
        this.error = true;
        this.errorMessage = '两次输入的密码不匹配';
        return;
      }

      try {
        const response = await axios.post('/users/register', { // 路径为 '/users/register'
          username: this.username,
          password: this.password,
          email: this.email,
          phone: this.phone,
          role: this.role,
        });
        console.log(response); // 打印响应以调试
        this.success = true;
        this.error = false;
        // 注册成功后，重定向到登录页面
        setTimeout(() => {
          window.location.href = "http://localhost:5174";
        }, 2000); // 等待2秒后重定向
      } catch (error) {
        console.error('Error:', error); // 打印错误以调试
        console.error('Error response:', error.response); // 打印错误响应以调试
        console.error('Error message:', error.message); // 打印错误信息以调试
        this.error = true;
        this.errorMessage = error.response?.data?.message || '注册失败';
        this.success = false;
      }
    },
    goToLogin() {
      window.location.href = "http://localhost:5174";
    },
  },
};
</script>

<style scoped>
body, html {
  height: 100%;
  margin: 0;
}

.bg {
  background-image: url('/assets/images/badminton_background.png');
  height: 100%;
  background-position: center;
  background-repeat: no-repeat;
  background-size: cover;
  display: flex;
  justify-content: center;
  align-items: center;
}

.card {
  background: rgba(255, 255, 255, 0.85);
}
</style>

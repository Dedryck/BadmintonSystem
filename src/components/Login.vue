<template>
  <div class="bg">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-md-6">
          <div class="card">
            <div class="card-header text-center">
              <h3>羽毛球场地管理系统登录</h3>
            </div>
            <div class="card-body">
              <form @submit.prevent="login">
                <div class="form-group">
                  <label for="username">用户名</label>
                  <input type="text" class="form-control" id="username" v-model="username" required>
                </div>
                <div class="form-group">
                  <label for="password">密码</label>
                  <input type="password" class="form-control" id="password" v-model="password" required>
                </div>
                <button type="submit" class="btn btn-primary btn-block">登录</button>
              </form>
              <button @click="goToRegister" class="btn btn-secondary btn-block mt-3">注册</button>
              <div class="mt-3">
                <span v-if="error" class="text-danger">用户名或密码错误</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from '@/api/axios';

export default {
  data() {
    return {
      username: '',
      password: '',
      error: false,
    };
  },
  methods: {
    async login() {
      try {
        const response = await axios.post('/login', {
          username: this.username,
          password: this.password,
        });
        localStorage.setItem('token', response.data.token);
        this.$router.push('/home');
      } catch (error) {
        this.error = true;
        console.error(error); // 添加错误日志以调试
      }
    },
    goToRegister() {
      this.$router.push('/register');
    },
  },
};
</script>

<style scoped>
body, html,  .bg {
  height: 100%;
  margin: 0;
}
.bg {
  background-image: url('/assets/images/badminton_background.png');
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

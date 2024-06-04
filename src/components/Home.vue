<template>
  <div>
    <Header />
    <Sidebar :username="username" :role="role" :activePage="'home'" />
    <main id="main" class="main">
      <div class="pagetitle">
        <h1>羽毛球场地预订系统</h1>
        <nav>
          <ol class="breadcrumb">
            <li class="breadcrumb-item"><router-link to="/home">首页</router-link></li>
            <li class="breadcrumb-item active">仪表盘</li>
          </ol>
        </nav>
      </div>

      <section class="section dashboard">
        <div class="row">
          <div class="col-lg-12">
            <div class="card">
              <div class="card-body">
                <h5 class="card-title">欢迎来到羽毛球场地预订系统</h5>
                <p>您已成功登录。</p>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script>
import Header from './Header.vue';
import Sidebar from './Sidebar.vue';
import axios from 'axios';

export default {
  name: 'Home',
  components: {
    Header,
    Sidebar,
  },
  data() {
    return {
      username: '',
      role: '',
    };
  },
  mounted() {
    this.fetchUserInfo();
  },
  methods: {
    fetchUserInfo() {
      axios.get('/api/home')
          .then(response => {
            const data = response.data;
            this.username = data.username;
            this.role = data.role;
          })
          .catch(error => {
            console.error('There was an error fetching the user info:', error);
          });
    },
  },
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

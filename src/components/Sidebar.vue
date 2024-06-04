<template>
  <aside id="sidebar" class="sidebar">
    <div class="d-flex align-items-center justify-content-between mb-3">
      <div>
        <span>{{ userInfo.username }}</span> (<span>{{ userInfo.role }}</span>)
      </div>
    </div>
    <ul class="sidebar-nav" id="sidebar-nav">
      <li class="nav-item">
        <router-link :class="{'nav-link': true, 'active': activePage === 'home'}" to="/home">
          <i class="bi bi-grid"></i>
          <span>首页</span>
        </router-link>
      </li>
      <li class="nav-item">
        <router-link :class="{'nav-link': true, 'active': activePage === 'courtBookings'}" to="/court-bookings">
          <i class="bi bi-calendar-check"></i>
          <span>球场预订情况</span>
        </router-link>
      </li>
      <li class="nav-item">
        <router-link :class="{'nav-link': true, 'active': activePage === 'equipmentRentals'}" to="/equipment-rentals">
          <i class="bi bi-bar-chart"></i>
          <span>设备租借情况</span>
        </router-link>
      </li>
      <li class="nav-item">
        <router-link :class="{'nav-link': true, 'active': activePage === 'feedback'}" to="/feedback">
          <i class="bi bi-chat-dots"></i>
          <span>用户信息反馈</span>
        </router-link>
      </li>
      <li class="nav-item">
        <router-link :class="{'nav-link': true, 'active': activePage === 'instructors'}" to="/instructors">
          <i class="bi bi-person-badge"></i>
          <span>现有教练信息</span>
        </router-link>
      </li>
      <li class="nav-item" v-if="userInfo.role === 'admin'">
        <a class="nav-link" :class="{'collapsed': activePage !== 'courtManagement' && activePage !== 'courtBookingManagement'}" data-bs-target="#court-management-nav" data-bs-toggle="collapse" href="#" :aria-expanded="activePage === 'courtManagement' || activePage === 'courtBookingManagement'">
          <i class="bi bi-gem"></i>
          <span>球场管理（管理员）</span><i class="bi bi-chevron-down ms-auto"></i>
        </a>
        <ul id="court-management-nav" class="nav-content collapse" :class="{'show': activePage === 'courtManagement' || activePage === 'courtBookingManagement'}" data-bs-parent="#sidebar-nav">
          <li>
            <router-link :class="{'active': activePage === 'courtBookingManagement'}" to="/court-booking-management">
              <i class="bi bi-circle"></i><span>球场预订管理</span>
            </router-link>
          </li>
          <li>
            <router-link :class="{'active': activePage === 'courtManagement'}" to="/court-management">
              <i class="bi bi-circle"></i><span>球场管理</span>
            </router-link>
          </li>
        </ul>
      </li>
      <li class="nav-item" v-if="userInfo.role === 'admin'">
        <a class="nav-link" :class="{'collapsed': activePage !== 'equipmentManagement'}" data-bs-target="#equipment-management-nav" data-bs-toggle="collapse" href="#" :aria-expanded="activePage === 'equipmentManagement'">
          <i class="bi bi-people"></i>
          <span>设备管理（管理员）</span><i class="bi bi-chevron-down ms-auto"></i>
        </a>
        <ul id="equipment-management-nav" class="nav-content collapse" :class="{'show': activePage === 'equipmentManagement'}" data-bs-parent="#sidebar-nav">
          <li>
            <router-link :class="{'active': activePage === 'equipmentManagement'}" to="/equipment-management">
              <i class="bi bi-circle"></i><span>设备管理</span>
            </router-link>
          </li>
        </ul>
      </li>
      <li class="nav-item" v-if="userInfo.role === 'admin'">
        <router-link :class="{'nav-link': true, 'active': activePage === 'staffManagement'}" to="/staff-management">
          <i class="bi bi-person"></i>
          <span>人员管理（管理员）</span>
        </router-link>
      </li>
      <li class="nav-item" v-if="userInfo.role === 'admin'">
        <router-link :class="{'nav-link': true, 'active': activePage === 'feedbackManagement'}" to="/feedback-management">
          <i class="bi bi-chat-dots-fill"></i>
          <span>用户信息反馈管理</span>
        </router-link>
      </li>
      <li class="nav-item" v-if="userInfo.role === 'admin'">
        <router-link :class="{'nav-link': true, 'active': activePage === 'instructorManagement'}" to="/instructor-management">
          <i class="bi bi-person-badge-fill"></i>
          <span>教练信息管理</span>
        </router-link>
      </li>
      <li class="nav-item">
        <a class="nav-link" :class="{ 'active': activePage === 'logout' }" @click="logout">
          <i class="bi bi-box-arrow-right"></i>
          <span>退出</span>
        </a>
      </li>
    </ul>
  </aside>
</template>

<script>
import { getUserInfo } from '@/api/user';

export default {
  name: 'Sidebar',
  data() {
    return {
      userInfo: {
        username: '',
        role: ''
      },
    };
  },
  props: {
    activePage: String,
  },
  async created() {
    try {
      const data = await getUserInfo();
      this.userInfo = data;
    } catch (error) {
      console.error('Failed to fetch user info:', error);
    }
  },
  methods: {
    logout() {
      localStorage.removeItem('token'); // 移除 token
      this.$router.push('/'); // 跳转到登录页面
    }
  }
};
</script>

<style scoped>
@import '/assets/vendor/bootstrap/css/bootstrap.min.css';
@import '/assets/vendor/bootstrap-icons/bootstrap-icons.css';
@import '/assets/css/style.css';
</style>

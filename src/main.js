import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js'; // 确保导入完整的 Bootstrap
import 'bootstrap-icons/font/bootstrap-icons.css';
import axios from './api/axios';

const app = createApp(App);
app.config.globalProperties.$axios = axios;
app.use(router);
app.mount('#app');

console.log("App mounted on #app");
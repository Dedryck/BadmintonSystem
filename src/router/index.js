import { createRouter, createWebHistory } from 'vue-router';
import Login from '@/components/Login.vue';
import Register from '@/components/Register.vue';
import Home from '@/components/Home.vue';
import CourtBookings from '@/components/CourtBookings.vue';
import CourtBookingManagement from '@/components/CourtBookingManagement.vue'; // 引入 CourtBookingManagement.vue
import EquipmentRentals from '@/components/EquipmentRentals.vue';
import Feedback from '@/components/Feedback.vue';
import Instructor from '@/components/Instructor.vue';
import CourtManagement from '@/components/CourtManagement.vue';
import EquipmentManagement from '@/components/EquipmentManagement.vue';
import StaffManagement from "@/components/StaffManagement.vue";
import FeedbackManagement from "@/components/FeedbackManagement.vue";
import InstructorManagement from "@/components/InstructorManagement.vue";



const routes = [
    { path: '/', component: Login },
    { path: '/register', component: Register },
    { path: '/home', component: Home },
    { path: '/court-bookings', component: CourtBookings },
    { path: '/court-booking-management', component: CourtBookingManagement }, // 添加新的路由
    { path: '/court-management', component: CourtManagement },
    { path: '/equipment-rentals', component: EquipmentRentals },
    { path: '/feedback', component: Feedback },
    { path: '/instructors', component: Instructor },
    { path: '/equipment-management', component: EquipmentManagement },
    { path: '/staff-management', component: StaffManagement },
    { path: '/feedback-management', component: FeedbackManagement },
    { path: '/instructor-management',component: InstructorManagement}

];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;

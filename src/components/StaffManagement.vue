<template>
  <div>
    <!-- 引入顶部栏 -->
    <Header />
    <!-- ======= Sidebar ======= -->
    <Sidebar :activePage="'staffManagement'" />
    <!-- ======= Main ======= -->
    <main id="main" class="main">
      <div class="pagetitle">
        <h1>人员管理</h1>
        <nav>
          <ol class="breadcrumb">
            <li class="breadcrumb-item"><router-link to="/home">首页</router-link></li>
            <li class="breadcrumb-item active">人员管理</li>
          </ol>
        </nav>
      </div><!-- End Page Title -->

      <section class="section dashboard">
        <div class="row">
          <div class="col-lg-12">
            <div class="card">
              <div class="card-body">
                <h5 class="card-title">人员管理情况</h5>
                <button type="button" class="btn btn-primary" @click="openAddUserModal">添加人员</button>

                <!-- Success message -->
                <div v-if="message" class="alert alert-success" role="alert">
                  <span>{{ message }}</span>
                </div>

                <!-- Table displaying users -->
                <table class="table mt-3">
                  <thead>
                  <tr>
                    <th>用户名</th>
                    <th>邮箱</th>
                    <th>电话</th>
                    <th>角色</th>
                    <th>操作</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="user in userList" :key="user.userID">
                    <td>{{ user.username }}</td>
                    <td>{{ user.email }}</td>
                    <td>{{ user.phone }}</td>
                    <td>{{ user.role }}</td>
                    <td>
                      <button type="button" class="btn btn-warning btn-sm" @click="openEditUserModal(user)">修改</button>
                      <button type="button" class="btn btn-danger btn-sm" @click="deleteUser(user.userID)">删除</button>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main><!-- End Main -->

    <!-- Add User Modal -->
    <div class="modal fade" id="addUserModal" tabindex="-1" aria-labelledby="addUserModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="addUserModalLabel">添加人员</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <form @submit.prevent="addUser">
            <div class="modal-body">
              <div class="mb-3">
                <label for="username" class="form-label">用户名</label>
                <input type="text" class="form-control" v-model="newUser.username" required>
              </div>
              <div class="mb-3">
                <label for="password" class="form-label">密码</label>
                <input type="password" class="form-control" v-model="newUser.password" required>
              </div>
              <div class="mb-3">
                <label for="email" class="form-label">邮箱</label>
                <input type="email" class="form-control" v-model="newUser.email" required>
              </div>
              <div class="mb-3">
                <label for="phone" class="form-label">电话</label>
                <input type="text" class="form-control" v-model="newUser.phone">
              </div>
              <div class="mb-3">
                <label for="role" class="form-label">角色</label>
                <select class="form-control" v-model="newUser.role">
                  <option value="admin">admin</option>
                  <option value="member">member</option>
                </select>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
              <button type="submit" class="btn btn-primary">提交人员</button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Edit User Modal -->
    <div class="modal fade" id="editUserModal" tabindex="-1" aria-labelledby="editUserModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="editUserModalLabel">修改人员</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <form @submit.prevent="updateUser">
            <div class="modal-body">
              <input type="hidden" v-model="editUserForm.userID">
              <div class="mb-3">
                <label for="editUsername" class="form-label">用户名</label>
                <input type="text" class="form-control" v-model="editUserForm.username" required>
              </div>
              <div class="mb-3">
                <label for="editPassword" class="form-label">密码</label>
                <input type="password" class="form-control" v-model="editUserForm.password">
              </div>
              <div class="mb-3">
                <label for="editEmail" class="form-label">邮箱</label>
                <input type="email" class="form-control" v-model="editUserForm.email" required>
              </div>
              <div class="mb-3">
                <label for="editPhone" class="form-label">电话</label>
                <input type="text" class="form-control" v-model="editUserForm.phone">
              </div>
              <div class="mb-3">
                <label for="editRole" class="form-label">角色</label>
                <select class="form-control" v-model="editUserForm.role">
                  <option value="admin">admin</option>
                  <option value="member">member</option>
                </select>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
              <button type="submit" class="btn btn-primary">修改人员</button>
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
  name: 'StaffManagement',
  components: {
    Header,
    Sidebar,
  },
  data() {
    return {
      userList: [],
      newUser: {
        username: '',
        password: '',
        email: '',
        phone: '',
        role: 'member',
      },
      editUserForm: {
        userID: '',
        username: '',
        password: '',
        email: '',
        phone: '',
        role: 'member',
      },
      message: '',
      roles: [
        { value: 'admin', text: 'admin' },
        { value: 'member', text: 'member' },
      ],
      userInfo: {
        username: '',
        role: '',
      },
    };
  },
  methods: {
    fetchUsers() {
      axios.get('/staff-management')
          .then(response => {
            if (response.data.code === 0) {
              this.userList = response.data.singleData;
            } else {
              this.message = response.data.message;
            }
          })
          .catch(error => {
            console.error('Failed to fetch users:', error);
          });
    },
    addUser() {
      axios.post('/staff-management/add-user', this.newUser)
          .then(response => {
            if (response.data.code === 0) {
              this.message = '添加成功';
              this.fetchUsers();
              this.closeAddUserModal();
            } else {
              this.message = "添加失败！！！";
            }
          })
          .catch(error => {
            console.error('Failed to add user:', error);
          });
    },
    openEditUserModal(user) {
      this.editUserForm = { ...user, password: '' };
      new Modal(document.getElementById('editUserModal')).show();
    },
    updateUser() {
      axios.post('/staff-management/edit-user', this.editUserForm)
          .then(response => {
            if (response.data.code === 0) {
              this.message = '修改成功';
              this.fetchUsers();
              this.closeEditUserModal();
            } else {
              this.message = '修改失败！！';
            }
          })
          .catch(error => {
            console.error('Failed to update user:', error);
          });
    },
    deleteUser(id) {
      if (confirm('确定要删除这个人员吗？')) {
        axios.delete(`/staff-management/delete-user/${id}`)
            .then(response => {
              if (response.data.code === 0) {
                this.message = '修改成功';
                this.fetchUsers();
              } else {
                this.message = '修改失败！！！';
              }
            })
            .catch(error => {
              console.error('Failed to delete user:', error);
            });
      }
    },
    fetchUserInfo() {
      getUserInfo()
          .then(data => {
            this.userInfo = data;
          })
          .catch(error => {
            console.error('Failed to fetch user info:', error);
          });
    },
    openAddUserModal() {
      new Modal(document.getElementById('addUserModal')).show();
    },
    closeAddUserModal() {
      const modalElement = document.getElementById('addUserModal');
      const modalInstance = Modal.getInstance(modalElement);
      if (modalInstance) {
        modalInstance.hide();
      }
    },
    closeEditUserModal() {
      const modalElement = document.getElementById('editUserModal');
      const modalInstance = Modal.getInstance(modalElement);
      if (modalInstance) {
        modalInstance.hide();
      }
    },
  },
  created() {
    this.fetchUsers();
    this.fetchUserInfo();
  },
};
</script>

<style scoped>
@import '/assets/vendor/bootstrap/css/bootstrap.min.css';
@import '/assets/vendor/bootstrap-icons/bootstrap-icons.css';
@import '/assets/css/style.css';
</style>

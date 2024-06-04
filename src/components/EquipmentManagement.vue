<template>
  <div>
    <Header />
    <Sidebar :activePage="'equipmentManagement'" />
    <main id="main" class="main">
      <div class="pagetitle">
        <h1>设备管理</h1>
        <nav>
          <ol class="breadcrumb">
            <li class="breadcrumb-item"><router-link to="/home">首页</router-link></li>
            <li class="breadcrumb-item active">设备管理</li>
          </ol>
        </nav>
      </div>

      <section class="section dashboard">
        <div class="row">
          <div class="col-lg-12">
            <div class="card">
              <div class="card-body">
                <h5 class="card-title">设备管理情况</h5>
                <button type="button" class="btn btn-primary" @click="showAddModal">添加设备</button>

                <!-- Success message -->
                <div v-if="message" class="alert alert-success" role="alert">
                  <span>{{ message }}</span>
                </div>

                <!-- Delete success message -->
                <div v-if="deleteSuccessMessage" class="alert alert-success" role="alert">
                  设备删除成功！
                </div>

                <!-- Table displaying equipment rentals -->
                <table class="table mt-3">
                  <thead>
                  <tr>
                    <th>设备图片</th>
                    <th>设备类型</th>
                    <th>数量</th>
                    <th>租借日期</th>
                    <th>归还日期</th>
                    <th>用户名</th>
                    <th>状态</th>
                    <th>操作</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="equipment in equipmentList" :key="equipment.rentalID">
                    <td>
                      <img :src="`http://localhost:8080${equipment.imagePath}`" alt="设备图片" class="img-thumbnail" style="max-width: 100px; max-height: 100px;" />
                    </td>
                    <td>{{ equipment.equipmentType }}</td>
                    <td>{{ equipment.quantity }}</td>
                    <td>{{ formatDate(equipment.rentalDate) }}</td>
                    <td>{{ formatDate(equipment.returnDate) }}</td>
                    <td>{{ equipment.username }}</td>
                    <td>{{ equipment.status }}</td>
                    <td>
                      <button type="button" class="btn btn-warning btn-sm" @click="openEditEquipmentModal(equipment)">修改</button>
                      <button type="button" class="btn btn-danger btn-sm" @click="deleteEquipment(equipment.rentalID)">删除</button>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- Add Equipment Modal -->
      <div class="modal fade" id="addEquipmentModal" tabindex="-1" aria-labelledby="addEquipmentModalLabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="addEquipmentModalLabel">添加设备</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form @submit.prevent="addEquipment">
              <div class="modal-body">
                <div class="mb-3">
                  <label for="username" class="form-label">用户名</label>
                  <input type="text" class="form-control" v-model="userInfo.username" readonly>
                </div>
                <div class="mb-3">
                  <label for="equipmentType" class="form-label">设备类型</label>
                  <input type="text" class="form-control" v-model="newEquipment.equipmentType" required>
                </div>
                <div class="mb-3">
                  <label for="quantity" class="form-label">数量</label>
                  <input type="number" class="form-control" v-model="newEquipment.quantity" required>
                </div>
                <div class="mb-3">
                  <label for="rentalDate" class="form-label">租借日期</label>
                  <input type="date" class="form-control" v-model="newEquipment.rentalDate" required>
                </div>
                <div class="mb-3">
                  <label for="returnDate" class="form-label">归还日期</label>
                  <input type="date" class="form-control" v-model="newEquipment.returnDate">
                </div>
                <div class="mb-3">
                  <label for="status" class="form-label">状态</label>
                  <select class="form-control" v-model="newEquipment.status">
                    <option value="rented">rented</option>
                    <option value="returned">returned</option>
                  </select>
                </div>
                <div class="mb-3">
                  <label for="image" class="form-label">设备图片</label>
                  <input type="file" class="form-control" @change="onImageChange" accept="image/*" required>
                </div>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
                <button type="submit" class="btn btn-primary">提交设备</button>
              </div>
            </form>
          </div>
        </div>
      </div>


      <!-- Edit Equipment Modal -->
      <div class="modal fade" id="editEquipmentModal" tabindex="-1" aria-labelledby="editEquipmentModalLabel" aria-hidden="true">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="editEquipmentModalLabel">修改设备</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form @submit.prevent="updateEquipment">
              <div class="modal-body">
                <div class="mb-3">
                  <label for="editUsername" class="form-label">用户名</label>
                  <input type="text" class="form-control" id="editUsername" v-model="editEquipmentForm.username" readonly>
                </div>
                <div class="mb-3">
                  <label for="editEquipmentType" class="form-label">设备类型</label>
                  <input type="text" class="form-control" id="editEquipmentType" v-model="editEquipmentForm.equipmentType" required>
                </div>
                <div class="mb-3">
                  <label for="editQuantity" class="form-label">数量</label>
                  <input type="number" class="form-control" id="editQuantity" v-model="editEquipmentForm.quantity" required>
                </div>
                <div class="mb-3">
                  <label for="editRentalDate" class="form-label">租借日期</label>
                  <input type="date" class="form-control" id="editRentalDate" v-model="editEquipmentForm.rentalDate" required>
                </div>
                <div class="mb-3">
                  <label for="editReturnDate" class="form-label">归还日期</label>
                  <input type="date" class="form-control" id="editReturnDate" v-model="editEquipmentForm.returnDate">
                </div>
                <div class="mb-3">
                  <label for="editStatus" class="form-label">状态</label>
                  <select class="form-control" id="editStatus" v-model="editEquipmentForm.status">
                    <option value="rented">rented</option>
                    <option value="returned">returned</option>
                  </select>
                </div>
                <div class="mb-3">
                  <label for="editImage" class="form-label">设备图片</label>
                  <input type="file" class="form-control" id="editImage" @change="onEditImageChange" accept="image/*">
                </div>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
                <button type="submit" class="btn btn-primary">提交修改</button>
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
  name: 'EquipmentManagement',
  components: {
    Header,
    Sidebar
  },
  data() {
    return {
      equipmentList: [],
      newEquipment: {
        equipmentType: '',
        quantity: 1,
        rentalDate: '',
        returnDate: '',
        status: 'rented',
        image: null,
        username: ''
      },
      editEquipmentForm: {
        rentalID: null,
        equipmentType: '',
        quantity: '',
        rentalDate: '',
        returnDate: '',
        status: 'rented',
        image: null,
        username: ''
      },
      userInfo: {
        username: '',
        userID: ''
      },
      message: '',
      deleteSuccessMessage: false,
      showEditEquipmentModal: false,
      addModal: null,
      editModal: null,
    };
  },
  async created() {
    await this.fetchUserInfo();
    await this.fetchEquipment();
    this.addModal = new Modal(document.getElementById('addEquipmentModal'));
    this.editModal = new Modal(document.getElementById('editEquipmentModal'));
    // 更新 newEquipment 和 editEquipment 中的 username
    this.newEquipment.username = this.userInfo.username;
    this.editEquipment.username = this.userInfo.username;
  },
  methods: {
    async fetchUserInfo() {
      try {
        const response = await getUserInfo();
        this.userInfo = response;
        // 打印用户信息
        console.log('用户信息:', this.userInfo);
        // 更新 newEquipment 和 editEquipment 中的 username
        this.newEquipment.username = this.userInfo.username;
        this.editEquipment.username = this.userInfo.username;
      } catch (error) {
        console.error('获取用户信息失败:', error);
      }
    },
    async fetchEquipment() {
      try {
        const response = await axios.get('/equipment-management');
        console.log('设备列表响应:', response.data);
        if (response.data.code === 0) {
          this.equipmentList = response.data.singleData;
          // 打印接收到的数据
          console.log('设备列表:', this.equipmentList);
        } else {
          console.error('获取设备信息失败:', response.data.message);
        }
      } catch (error) {
        console.error('获取设备信息失败:', error);
      }
    },
    showAddModal() {
      this.addModal.show();
    },
    async addEquipment() {
      const formData = new FormData();
      formData.append('equipmentType', this.newEquipment.equipmentType);
      formData.append('quantity', this.newEquipment.quantity);
      formData.append('rentalDate', this.newEquipment.rentalDate);
      formData.append('returnDate', this.newEquipment.returnDate);
      formData.append('status', this.newEquipment.status);
      formData.append('image', this.newEquipment.image);
      formData.append('username', this.newEquipment.username);  // 提交时附加 username

      // 打印出需要提交的数据
      console.log('提交的数据:', {
        equipmentType: this.newEquipment.equipmentType,
        quantity: this.newEquipment.quantity,
        rentalDate: this.newEquipment.rentalDate,
        returnDate: this.newEquipment.returnDate,
        status: this.newEquipment.status,
        image: this.newEquipment.image,
        username: this.newEquipment.username,
      });

      for (let pair of formData.entries()) {
        console.log(pair[0] + ': ' + pair[1]);
      }

      try {
        const response = await axios.post('/equipment-management/add', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        });
        if (response.data.code === 0) {
          this.equipmentList.push(response.data.singleData); // 更新列表
          this.message = '设备添加成功！';
          this.addModal.hide();
        } else {
          console.error('设备添加失败:', response.data.message);
        }
      } catch (error) {
        console.error('设备添加失败:', error);
      }
    },


    openEditEquipmentModal(equipment) {
      this.editEquipmentForm = {
        ...equipment,
        rentalDate: equipment.rentalDate ? new Date(equipment.rentalDate).toISOString().split('T')[0] : '',
        returnDate: equipment.returnDate ? new Date(equipment.returnDate).toISOString().split('T')[0] : '',
        image: null
      };
      new Modal(document.getElementById('editEquipmentModal')).show();
    },
    async updateEquipment() {
      const formData = new FormData();
      formData.append('rentalID', this.editEquipmentForm.rentalID); // 添加rentalID
      formData.append('equipmentType', this.editEquipmentForm.equipmentType);
      formData.append('quantity', this.editEquipmentForm.quantity);
      formData.append('rentalDate', this.editEquipmentForm.rentalDate);
      formData.append('returnDate', this.editEquipmentForm.returnDate);
      formData.append('status', this.editEquipmentForm.status);
      formData.append('username', this.editEquipmentForm.username); // 提交时附加 username

      if (this.editEquipmentForm.image) {
        formData.append('image', this.editEquipmentForm.image); // 如果有新图片，则添加到FormData
      }

      try {
        const response = await axios.post('/equipment-management/edit', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        });
        if (response.data.code === 0) {
          this.message = response.data.message;
          this.fetchEquipment();
          this.closeEditEquipmentModal();
        } else {
          this.message = response.data.message;
        }
      } catch (error) {
        console.error('Failed to update equipment:', error);
      }
    },

    closeEditEquipmentModal() {
      const modalElement = document.getElementById('editEquipmentModal');
      const modalInstance = Modal.getInstance(modalElement);
      if (modalInstance) {
        modalInstance.hide();
      }
    },

    async deleteEquipment(id) {
      try {
        const response = await axios.delete(`/equipment-management/delete/${id}`);
        if (response.data.code === 0) {
          this.equipmentList = this.equipmentList.filter(e => e.rentalID !== id);
          this.deleteSuccessMessage = true;
          setTimeout(() => {
            this.deleteSuccessMessage = false;
          }, 3000);
        } else {
          console.error('设备删除失败:', response.data.message);
        }
      } catch (error) {
        console.error('设备删除失败:', error);
      }
    },
    onImageChange(event) {
      this.newEquipment.image = event.target.files[0];
    },
    onEditImageChange(event) {
      this.editEquipmentForm.image = event.target.files[0];
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

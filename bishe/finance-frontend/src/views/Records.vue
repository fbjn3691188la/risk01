<template>
  <div class="records-container">
    <h2>收支记录管理</h2>
    
    <!-- 记录表单 -->
    <div class="record-form">
      <h3>{{ isEditing ? '编辑记录' : '添加记录' }}</h3>
      <form @submit.prevent="saveRecord">
        <div class="form-group">
          <label>类型</label>
          <select v-model="form.type" required>
            <option value="income">收入</option>
            <option value="expense">支出</option>
          </select>
        </div>
        <div class="form-group">
          <label>金额</label>
          <input type="number" step="0.01" v-model="form.amount" required />
        </div>
        <div class="form-group">
          <label>分类</label>
          <select v-model="form.categoryId" required>
            <option v-for="category in categories" :key="category.id" :value="category.id">
              {{ category.name }}
            </option>
          </select>
        </div>
        <div class="form-group">
          <label>日期</label>
          <input type="date" v-model="form.date" required />
        </div>
        <div class="form-group">
          <label>备注</label>
          <input type="text" v-model="form.remark" />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary">{{ isEditing ? '更新' : '添加' }}</button>
          <button type="button" class="btn btn-secondary" @click="resetForm">取消</button>
        </div>
      </form>
    </div>

    <!-- 记录列表 -->
    <div class="records-list">
      <div class="filter">
        <button 
          v-for="type in ['all', 'income', 'expense']" 
          :key="type"
          :class="['btn', { active: filterType === type }]"
          @click="filterType = type"
        >
          {{ type === 'all' ? '全部' : type === 'income' ? '收入' : '支出' }}
        </button>
      </div>
      <table>
        <thead>
          <tr>
            <th>类型</th>
            <th>金额</th>
            <th>分类</th>
            <th>日期</th>
            <th>备注</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="record in filteredRecords" :key="record.id">
            <td>{{ record.type === 'income' ? '收入' : '支出' }}</td>
            <td :class="record.type === 'income' ? 'income-amount' : 'expense-amount'">
              {{ record.amount }}
            </td>
            <td>{{ getCategoryName(record.categoryId) }}</td>
            <td>{{ record.date }}</td>
            <td>{{ record.remark || '-' }}</td>
            <td>
              <button class="btn btn-sm btn-edit" @click="editRecord(record)">编辑</button>
              <button class="btn btn-sm btn-delete" @click="deleteRecord(record.id)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="filteredRecords.length === 0" class="empty">暂无记录</div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      records: [],
      categories: [],
      form: {
        type: 'expense',
        amount: '',
        categoryId: '',
        date: new Date().toISOString().split('T')[0],
        remark: ''
      },
      isEditing: false,
      editingId: null,
      filterType: 'all'
    }
  },
  computed: {
    filteredRecords() {
      if (this.filterType === 'all') {
        return this.records
      }
      return this.records.filter(record => record.type === this.filterType)
    },
    userId() {
      const user = JSON.parse(localStorage.getItem('user'))
      return user ? user.id : null
    }
  },
  mounted() {
    this.loadRecords()
    this.loadCategories()
  },
  methods: {
    async loadRecords() {
      if (!this.userId) return
      try {
        const response = await axios.get(`/api/records/user/${this.userId}`)
        this.records = response.data
      } catch (err) {
        console.error('加载记录失败:', err)
      }
    },
    async loadCategories() {
      if (!this.userId) return
      try {
        const response = await axios.get(`/api/categories/user/${this.userId}`)
        this.categories = response.data
        if (this.categories.length > 0) {
          this.form.categoryId = this.categories[0].id
        }
      } catch (err) {
        console.error('加载分类失败:', err)
      }
    },
    async saveRecord() {
      if (!this.userId) return
      try {
        const recordData = {
          ...this.form,
          userId: this.userId
        }
        if (this.isEditing) {
          await axios.put(`/api/records/${this.editingId}`, recordData)
        } else {
          await axios.post('/api/records', recordData)
        }
        this.loadRecords()
        this.resetForm()
      } catch (err) {
        console.error('保存记录失败:', err)
      }
    },
    editRecord(record) {
      this.isEditing = true
      this.editingId = record.id
      this.form = {
        type: record.type,
        amount: record.amount,
        categoryId: record.categoryId,
        date: record.date,
        remark: record.remark
      }
    },
    async deleteRecord(id) {
      if (!confirm('确定要删除这条记录吗？')) return
      try {
        await axios.delete(`/api/records/${id}`)
        this.loadRecords()
      } catch (err) {
        console.error('删除记录失败:', err)
      }
    },
    resetForm() {
      this.isEditing = false
      this.editingId = null
      this.form = {
        type: 'expense',
        amount: '',
        categoryId: this.categories.length > 0 ? this.categories[0].id : '',
        date: new Date().toISOString().split('T')[0],
        remark: ''
      }
    },
    getCategoryName(categoryId) {
      const category = this.categories.find(c => c.id === categoryId)
      return category ? category.name : '未知分类'
    }
  }
}
</script>

<style scoped>
.records-container {
  max-width: 1000px;
  margin: 0 auto;
}

h2 {
  margin-bottom: 20px;
}

.record-form {
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
  border: 1px solid #ddd;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

input, select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.form-actions {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.btn {
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.btn-primary {
  background-color: #4CAF50;
  color: white;
}

.btn-secondary {
  background-color: #f0f0f0;
  color: #333;
}

.records-list {
  margin-top: 20px;
}

.filter {
  margin-bottom: 15px;
  display: flex;
  gap: 10px;
}

.filter .btn {
  background-color: #f0f0f0;
  color: #333;
}

.filter .btn.active {
  background-color: #4CAF50;
  color: white;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #f2f2f2;
  font-weight: bold;
}

.income-amount {
  color: #4CAF50;
  font-weight: bold;
}

.expense-amount {
  color: #f44336;
  font-weight: bold;
}

.btn-sm {
  padding: 5px 10px;
  font-size: 12px;
}

.btn-edit {
  background-color: #2196F3;
  color: white;
  margin-right: 5px;
}

.btn-delete {
  background-color: #f44336;
  color: white;
}

.empty {
  text-align: center;
  padding: 40px;
  color: #999;
}
</style>
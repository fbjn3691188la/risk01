<template>
  <div class="categories-container">
    <h2>财务分类管理</h2>
    
    <!-- 分类表单 -->
    <div class="category-form">
      <h3>{{ isEditing ? '编辑分类' : '添加分类' }}</h3>
      <form @submit.prevent="saveCategory">
        <div class="form-group">
          <label>分类名称</label>
          <input type="text" v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>类型</label>
          <select v-model="form.type" required>
            <option value="income">收入</option>
            <option value="expense">支出</option>
          </select>
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary">{{ isEditing ? '更新' : '添加' }}</button>
          <button type="button" class="btn btn-secondary" @click="resetForm">取消</button>
        </div>
      </form>
    </div>

    <!-- 分类列表 -->
    <div class="categories-list">
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
            <th>分类名称</th>
            <th>类型</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="category in filteredCategories" :key="category.id">
            <td>{{ category.name }}</td>
            <td>{{ category.type === 'income' ? '收入' : '支出' }}</td>
            <td>
              <button class="btn btn-sm btn-edit" @click="editCategory(category)">编辑</button>
              <button class="btn btn-sm btn-delete" @click="deleteCategory(category.id)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="filteredCategories.length === 0" class="empty">暂无分类</div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      categories: [],
      form: {
        name: '',
        type: 'expense'
      },
      isEditing: false,
      editingId: null,
      filterType: 'all'
    }
  },
  computed: {
    filteredCategories() {
      if (this.filterType === 'all') {
        return this.categories
      }
      return this.categories.filter(category => category.type === this.filterType)
    },
    userId() {
      const user = JSON.parse(localStorage.getItem('user'))
      return user ? user.id : null
    }
  },
  mounted() {
    this.loadCategories()
  },
  methods: {
    async loadCategories() {
      if (!this.userId) return
      try {
        const response = await axios.get(`/api/categories/user/${this.userId}`)
        this.categories = response.data
      } catch (err) {
        console.error('加载分类失败:', err)
      }
    },
    async saveCategory() {
      if (!this.userId) return
      try {
        const categoryData = {
          ...this.form,
          userId: this.userId
        }
        if (this.isEditing) {
          await axios.put(`/api/categories/${this.editingId}`, categoryData)
        } else {
          await axios.post('/api/categories', categoryData)
        }
        this.loadCategories()
        this.resetForm()
      } catch (err) {
        console.error('保存分类失败:', err)
      }
    },
    editCategory(category) {
      this.isEditing = true
      this.editingId = category.id
      this.form = {
        name: category.name,
        type: category.type
      }
    },
    async deleteCategory(id) {
      if (!confirm('确定要删除这个分类吗？')) return
      try {
        await axios.delete(`/api/categories/${id}`)
        this.loadCategories()
      } catch (err) {
        console.error('删除分类失败:', err)
      }
    },
    resetForm() {
      this.isEditing = false
      this.editingId = null
      this.form = {
        name: '',
        type: 'expense'
      }
    }
  }
}
</script>

<style scoped>
.categories-container {
  max-width: 800px;
  margin: 0 auto;
}

h2 {
  margin-bottom: 20px;
}

.category-form {
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

.categories-list {
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
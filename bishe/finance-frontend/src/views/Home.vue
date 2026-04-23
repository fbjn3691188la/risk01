<template>
  <div class="home-container">
    <div class="sidebar">
      <h3>个人财务管理系统</h3>
      <ul>
        <li>
          <router-link to="/home/records">收支记录</router-link>
        </li>
        <li>
          <router-link to="/home/categories">财务分类</router-link>
        </li>
        <li>
          <router-link to="/home/statistics">统计分析</router-link>
        </li>
        <li class="logout">
          <button @click="logout">退出登录</button>
        </li>
      </ul>
    </div>
    <div class="content">
      <router-view />
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    logout() {
      localStorage.removeItem('user')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.home-container {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 200px;
  background-color: #f0f0f0;
  padding: 20px;
  border-right: 1px solid #ddd;
}

.sidebar h3 {
  margin-bottom: 20px;
  text-align: center;
  color: #333;
}

.sidebar ul {
  list-style: none;
  padding: 0;
}

.sidebar li {
  margin-bottom: 10px;
}

.sidebar a {
  display: block;
  padding: 10px;
  text-decoration: none;
  color: #333;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.sidebar a:hover {
  background-color: #ddd;
}

.logout {
  margin-top: 40px;
}

.logout button {
  width: 100%;
  padding: 10px;
  background-color: #f44336;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.logout button:hover {
  background-color: #d32f2f;
}

.content {
  flex: 1;
  padding: 20px;
}
</style>
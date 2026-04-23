<template>
  <div class="statistics-container">
    <h2>统计分析</h2>
    
    <!-- 统计选择器 -->
    <div class="selector">
      <div class="form-group">
        <label>统计类型</label>
        <select v-model="statType" @change="loadStatistics">
          <option value="monthly">月度统计</option>
          <option value="yearly">年度统计</option>
        </select>
      </div>
      <div class="form-group" v-if="statType === 'monthly'">
        <label>选择月份</label>
        <input type="month" v-model="month" @change="loadStatistics" />
      </div>
      <div class="form-group" v-if="statType === 'yearly'">
        <label>选择年份</label>
        <input type="number" v-model="year" @change="loadStatistics" />
      </div>
    </div>

    <!-- 统计概览 -->
    <div class="overview" v-if="statistics">
      <div class="overview-item income">
        <h3>收入</h3>
        <p class="amount">{{ statistics.income }}</p>
      </div>
      <div class="overview-item expense">
        <h3>支出</h3>
        <p class="amount">{{ statistics.expense }}</p>
      </div>
      <div class="overview-item balance">
        <h3>结余</h3>
        <p class="amount">{{ statistics.balance }}</p>
      </div>
    </div>

    <!-- 图表容器 -->
    <div class="charts">
      <div class="chart-item" v-if="statType === 'monthly'">
        <h3>月度分类收支</h3>
        <div ref="categoryChart" class="chart"></div>
      </div>
      <div class="chart-item" v-if="statType === 'yearly'">
        <h3>年度月度收支</h3>
        <div ref="monthlyChart" class="chart"></div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import * as echarts from 'echarts'

export default {
  data() {
    return {
      statType: 'monthly',
      year: new Date().getFullYear(),
      month: new Date().toISOString().slice(0, 7),
      statistics: null,
      categoryChart: null,
      monthlyChart: null
    }
  },
  computed: {
    userId() {
      const user = JSON.parse(localStorage.getItem('user'))
      return user ? user.id : null
    }
  },
  mounted() {
    this.loadStatistics()
  },
  beforeUnmount() {
    if (this.categoryChart) {
      this.categoryChart.dispose()
    }
    if (this.monthlyChart) {
      this.monthlyChart.dispose()
    }
  },
  methods: {
    async loadStatistics() {
      if (!this.userId) return
      try {
        let response
        if (this.statType === 'monthly') {
          const [year, month] = this.month.split('-').map(Number)
          response = await axios.get(`/api/statistics/monthly/${this.userId}/${year}/${month}`)
        } else {
          response = await axios.get(`/api/statistics/yearly/${this.userId}/${this.year}`)
        }
        this.statistics = response.data
        this.renderCharts()
      } catch (err) {
        console.error('加载统计数据失败:', err)
      }
    },
    renderCharts() {
      if (!this.statistics) return
      
      if (this.statType === 'monthly') {
        this.renderCategoryChart()
      } else {
        this.renderMonthlyChart()
      }
    },
    renderCategoryChart() {
      if (!this.$refs.categoryChart) return
      
      if (this.categoryChart) {
        this.categoryChart.dispose()
      }
      
      this.categoryChart = echarts.init(this.$refs.categoryChart)
      
      const option = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          top: '5%',
          left: 'center'
        },
        series: [
          {
            name: '收入分类',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: Object.entries(this.statistics.categoryIncome || {}).map(([key, value]) => ({
              name: key,
              value: value
            }))
          }
        ]
      }
      
      this.categoryChart.setOption(option)
    },
    renderMonthlyChart() {
      if (!this.$refs.monthlyChart) return
      
      if (this.monthlyChart) {
        this.monthlyChart.dispose()
      }
      
      this.monthlyChart = echarts.init(this.$refs.monthlyChart)
      
      const months = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
      
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['收入', '支出']
        },
        xAxis: {
          type: 'category',
          data: months
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '收入',
            type: 'line',
            data: months.map((_, index) => {
              return this.statistics.monthlyIncome?.[index + 1] || 0
            }),
            itemStyle: {
              color: '#4CAF50'
            }
          },
          {
            name: '支出',
            type: 'line',
            data: months.map((_, index) => {
              return this.statistics.monthlyExpense?.[index + 1] || 0
            }),
            itemStyle: {
              color: '#f44336'
            }
          }
        ]
      }
      
      this.monthlyChart.setOption(option)
    }
  }
}
</script>

<style scoped>
.statistics-container {
  max-width: 1000px;
  margin: 0 auto;
}

h2 {
  margin-bottom: 20px;
}

.selector {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  border: 1px solid #ddd;
}

.form-group {
  flex: 1;
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

.overview {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
}

.overview-item {
  flex: 1;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
}

.overview-item.income {
  background-color: #e8f5e8;
  border: 1px solid #c8e6c9;
}

.overview-item.expense {
  background-color: #ffebee;
  border: 1px solid #ffcdd2;
}

.overview-item.balance {
  background-color: #e3f2fd;
  border: 1px solid #bbdefb;
}

.overview-item h3 {
  margin-bottom: 10px;
  font-size: 16px;
  color: #333;
}

.overview-item .amount {
  font-size: 24px;
  font-weight: bold;
}

.overview-item.income .amount {
  color: #4CAF50;
}

.overview-item.expense .amount {
  color: #f44336;
}

.overview-item.balance .amount {
  color: #2196F3;
}

.charts {
  margin-top: 30px;
}

.chart-item {
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  border: 1px solid #ddd;
  margin-bottom: 30px;
}

.chart-item h3 {
  margin-bottom: 20px;
  text-align: center;
}

.chart {
  height: 400px;
  width: 100%;
}
</style>
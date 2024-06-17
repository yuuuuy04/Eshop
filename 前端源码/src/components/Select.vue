<template>
  <div>
	<el-select v-model="selectedCategory" placeholder="全部" @change="handleCategoryClick">
	<el-option label="全部" value="0"></el-option>
	<el-option
		v-for="item in categories"
		:key="item.id"
		:label="item.name"
		:value="item.id">
	</el-option>
	</el-select>
    <div class="grid-container">
      <div v-for="item in dataList" :key="item.id" class="grid-item" @click="toDetailsPage(item.id)">
        <img :src="'http://localhost:8081/img/' + item.imgPath" alt="" style="width: 300px; height: 250px; margin-right: 10px;"/>
        <div>{{ item.name }} ￥{{ item.price }}</div>
      </div>
    </div>
    <div>
      <el-button-group>
        <el-button type="primary" icon="el-icon-arrow-left" @click="loadLess">上一页</el-button>
        <el-button type="primary" @click="loadData">下一页<i class="el-icon-arrow-right el-icon--right"></i></el-button>
      </el-button-group>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      dataList: [],
      page: 0,
      pageSize: 8, // 每次加载的数量
      currentSize: 8,
      categories: [], // 分类列表
      selectedCategory: ''
    };
  },
  //初始化函数
  created() {
    this.loadData();
    this.loadCategories();
	this.loadToken();
  },
  methods: {
	  //加载下一页
    loadData() {
      this.loading = true;
      //没有选择分类时
      if(this.selectedCategory==0 && this.currentSize == 8 ){
		this.page++;
        axios.get('http://localhost:8081/universal/page', { params: { page: this.page, size: this.pageSize } })
          .then(response => {
            const data = response.data;
            if (data.code === 200) {
              const newData = data.data;
              this.currentSize = newData.length;
              if (newData.length === 0) {
                this.moreToLoad = false;
              } else {
                this.dataList = [...newData];
              }
            } else {
              console.error('Error:', data.message);
            }
          })
          .catch(error => {
            console.error('Error fetching data:', error);
          })
          .finally(() => {
            this.loading = false;
          });
      }else{
		选择了分类时
		
      }
    },
    loadLess(){
      this.loading = true;
      //是否选择了分类
      if(this.page > 1){
        this.page--;
        axios.get('http://localhost:8081/universal/page', { params: { page: this.page, size: this.pageSize } })
          .then(response => {
            const data = response.data;
            if (data.code === 200) {
              const newData = data.data;
			  this.currentSize = 8;
              if (newData.length === 0) {
                this.moreToLoad = false;
              } else {
                this.dataList = [...newData];
              }
            } else {
              console.error('Error:', data.message);
            }
          })
          .catch(error => {
            console.error('Error fetching data:', error);
          })
          .finally(() => {
            this.loading = false;
          });
      }else{
		  
	  }
    },
	//加载分类
    loadCategories() {
		axios.get('http://localhost:8081/universal/categories')
        .then(response => {
          const data = response.data;
          if (data.code === 200) {
            this.categories = data.data;
          } else {
            console.error('Error fetching categories:', data.message);
          }
        })
        .catch(error => {
          console.error('Error fetching categories:', error);
        });
    },
	//选择分类
    handleCategoryClick() {
		if(selectedCategory == 0){
			this.dataList=[];
			this.page = 0;
			this.currentSize=8;
			this.loadData();
		}else{
			const categoryId = this.selectedCategory;
			axios.get('http://localhost:8081/universal/pagebycategory', { params: { page: this.page=1, size: this.pageSize, categoryId: categoryId} })
			.then(response => {
			const data = response.data;
			if (data.code === 200) {
				const newData = data.data;
				if (newData.length === 0) {
				this.moreToLoad = false;
				} else {
				this.currentSize = data.length;
				this.dataList = [...newData];
				this.page++; // 更新页码
				}
			} else {
				console.error('Error:', data.message);
			}
			})
			.catch(error => {
			console.error('Error fetching data:', error);
			})
			.finally(() => {
			this.loading = false;
			});
		}
    },
	//跳转到详细页
	toDetailsPage(itemId) {
	    this.$router.push({path: '/details', query: {itemId: itemId}});
	},
	loadToken(){
		if(localStorage.getItem('token') != null){
			
			this.$store.dispatch('saveToken', this.getData('token'));
			this.$store.dispatch('saveuserType', this.getData('userType'));
			this.$store.dispatch('saveAvatar', this.getData('avatarPath'));
		}
	},
	getData(key) {
	  let dataObj = JSON.parse(localStorage.getItem(key))
	  if (new Date().getTime() > dataObj.time) {
	    this.$notify({
	    	title: '',
	    	message: '账号过期,请重新登录',
	    	position: 'bottom-right'
	    });
		localStorage.removeItem('token');
		localStorage.removeItem('userType');
		localStorage.removeItem('avatar');
	    return null;
	  } else {
	   return dataObj.data
	  }
	}
  }
};
</script>

<style scoped>
/* 样式可以根据需要进行调整 */
.grid-container {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}

.grid-item {
	align-items: center;
	border: 1px solid #ccc;
	padding: 10px;
}
</style>

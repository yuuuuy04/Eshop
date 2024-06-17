<template>
  <div id="app">
    <el-container>
      <el-header>
	   <div>
	   <el-menu
	     :default-active="1"
	     class="el-menu-demo"
	     mode="horizontal"
	     @select="handleSelect"
	     background-color="#545c64"
	     text-color="#fff"
	     active-text-color="#ffd04b">
		 <el-avatar :size="60" src="https://empty" @error="errorHandler">
			<img :src="avatar"/>
		</el-avatar>
	     <el-menu-item index="1"><router-link to="/show" style="text-decoration: none;">首页</router-link></el-menu-item>
		 <el-menu-item v-if="this.$store.state.userType=='0'" index="2"><router-link to="/cart" style="text-decoration: none;">购物车</router-link></el-menu-item>
	     <el-submenu index="3">
	       <template slot="title">功能菜单</template>
		   <router-link v-if="this.$store.state.userType==''" to="/login" style="text-decoration: none;"><el-menu-item index="3-1">登录</el-menu-item></router-link>
		   <router-link v-if="this.$store.state.userType!=''" to="/order" style="text-decoration: none;"><el-menu-item index="3-2">个人设置</el-menu-item></router-link>
	       <router-link v-if="this.$store.state.userType=='0'" to="/userorder" style="text-decoration: none;"><el-menu-item index="3-2">订单</el-menu-item></router-link>
	       <el-menu-item v-if="this.$store.state.userType!=''" @click="logOut()" index="3-4">退出</el-menu-item>
	     </el-submenu>
		 <el-submenu v-if="this.$store.state.userType=='1'" index="4">
			<template slot="title">管理员功能</template>
			<router-link to="/managecategory" style="text-decoration: none;"><el-menu-item index="4-1">管理分类</el-menu-item></router-link>
			<router-link to="/managegoods" style="text-decoration: none;"><el-menu-item index="4-2">管理商品</el-menu-item></router-link>
			<router-link to="/login" style="text-decoration: none;"><el-menu-item index="4-3">管理订单</el-menu-item></router-link>
			<router-link to="/order" style="text-decoration: none;"><el-menu-item index="4-4">管理用户</el-menu-item></router-link>
		 </el-submenu>
	   </el-menu>
	   </div>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import axios from 'axios';
import Select from './components/Select.vue'
import Cart from './components/Cart.vue'
import Login from './components/Login.vue'
import Register from './components/Register.vue'
import Details from './components/Details.vue'
import ManageCategory from './components/ManageCategory.vue'
import ManageGoods from './components/ManageGoods.vue'
import AddGoods from './components/AddGoods.vue'
import CreateOrder from './components/CreateOrder.vue'
import ModifyGoods from './components/ModifyGoods.vue'
import UserOrder from './components/UserOrder.vue'
import OrderDetails from './components/OrderDetails.vue'

export default {
  name: 'App',
  components: {
    Select,
	Cart,
	Login,
	Register,
	Details,
	ManageCategory,
	ManageGoods,
	AddGoods,
	CreateOrder,
	ModifyGoods,
	UserOrder,
	OrderDetails
  },
  data() {
  	return {
  		avatar: this.$store.state.avatar
    };
  },
  methods: {
    logOut() {
		//清空localstorage
		localStorage.removeItem('token');
		localStorage.removeItem('userType');
		localStorage.removeItem('avatar');
		//退出登录
		axios.post('http://localhost:8081/user/common/logout', null, {
			headers: {
				'token': this.$store.state.token
			}
		})
		.then(response => {
			//清空状态
			this.$store.dispatch('saveToken', '');
			this.$store.dispatch('saveuserType', '');
			this.$store.dispatch('avatar', '');
		});
    }
  }
}
</script>

<style scoped>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
  .el-header, .el-footer {
    background-color: #ffffff;
    color: #333;
    text-align: center;
    line-height: 50px;
  }
  
  .el-aside {
    background-color: #D3DCE6;
    color: #333;
    text-align: center;
    line-height: 200px;
  }
  
  .el-main {
    background-color: #ffffff;
    color: #333;
    text-align: center;
    line-height: 160px;
  }
  
  body > .el-container {
    margin-bottom: 40px;
  }
  
  .el-container:nth-child(5) .el-aside,
  .el-container:nth-child(6) .el-aside {
    line-height: 260px;
  }
  
  .el-container:nth-child(7) .el-aside {
    line-height: 320px;
  }
</style>

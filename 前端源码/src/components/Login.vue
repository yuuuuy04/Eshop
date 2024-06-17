<template>
	<div>
		<el-input placeholder="请输入账号" v-model="loginParam.userName"></el-input>
		<el-input placeholder="请输入密码" v-model="loginParam.password" show-password></el-input>
		<div><el-link @click="toRegister" type="primary">注册</el-link></div>
		<el-button @click="login">登录</el-button>
	</div>
</template>

<script>
import axios from "axios";

export default {
	data() {
		return {
			loginParam: {}
		}
    },
	methods: {
		login(){
			axios.post('http://localhost:8081/universal/login', this.loginParam)
			  .then(response => {
			    //保存状态进vuex
				this.$store.dispatch('saveToken', response.data.data.jwt);
				this.$store.dispatch('saveuserType', response.data.data.userType);
				this.$store.dispatch('saveAvatar', response.data.data.avatarPath);
				//保存token进localstorage
				let currentTime = new Date().getTime()
				let pastTime = 7 * 24 * 60 * 60 * 1000
				localStorage.setItem('token', JSON.stringify({data: response.data.data.jwt, time: currentTime + pastTime}));
				localStorage.setItem('userType', JSON.stringify({data: response.data.data.userType, time: currentTime + pastTime}));
				localStorage.setItem('avatar', JSON.stringify({data: 'http://localhost:8081/avatar/' + response.data.data.avatarPath, time: currentTime + pastTime}));
				//跳转到首页
				this.$alert('已跳转到首页', '登录成功');
				this.$router.push('/show');
			  })
			  .catch(error => {
				this.loginParam = {};
			    console.error(error);
			    //显示登录失败
			  });
		},
		toRegister(){
			this.$router.push('/register');
		}
	}
}
</script>
	
<style>
</style>

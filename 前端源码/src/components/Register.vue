<template>
	<el-form :model="data" status-icon :rules="rules" ref="data" label-width="100px" class="demo-ruleForm">
		<el-form-item label="用户名">
		  <el-input v-model="data.userName"></el-input>
		</el-form-item>
		<el-form-item label="昵称">
		  <el-input v-model="data.nickName"></el-input>
		</el-form-item>
		<el-form-item label="电话">
		  <el-input v-model="data.phoneNumber"></el-input>
		</el-form-item>
		<el-form-item label="邮箱">
		  <el-input v-model="data.email"></el-input>
		</el-form-item>
		<el-form-item label="密码" prop="pass">
		  <el-input type="password" v-model="data.password" autocomplete="off"></el-input>
		</el-form-item>
		<el-form-item label="确认密码" prop="checkPass">
		  <el-input type="password" v-model="data.checkPass" autocomplete="off"></el-input>
		</el-form-item>
		<el-form-item>
			<el-button type="primary" @click="submitForm">注册</el-button>
		</el-form-item>
	</el-form>
</template>

<script>
import axios from "axios";
export default{
	data(){
		var validatePass = (rule, value, callback) => {
			if (value === '') {
				callback(new Error('请输入密码'));
			} else {
				if (this.data.checkPass !== '') {
					this.$refs.data.validateField('checkPass');
				}
				callback();
			}
		};
		var validatePass2 = (rule, value, callback) => {
			if (value === '') {
				callback(new Error('请再次输入密码'));
			} else if (value !== this.data.password) {
				callback(new Error('两次输入密码不一致!'));
			} else {
				callback();
			}
		}
		return{
			data: {
				userName: '',
				nickName: '',
				phoneNumber: '',
				email: '',
				password: '',
				checkPass: ''
			},
			rules: {
				pass: [
					{ validator: validatePass, trigger: 'blur' }
				],
				checkPass: [
					{ validator: validatePass2, trigger: 'blur' }
				]
			}
		}
	},
	methods: {
		submitForm(){
			const requestData = {
			    userName: this.data.userName,
			    nickName: this.data.nickName,
			    phoneNumber: this.data.phoneNumber,
			    email: this.data.email,
			    password: this.data.password
			};
			axios.post('http://localhost:8081/universal/register', requestData)
				.then(response => {
					if(response.data.code == 200){
						this.$alert('', '注册成功');
						this.$router.push('/login');
					}
				})
		}
	}
}
</script>

<style>
</style>

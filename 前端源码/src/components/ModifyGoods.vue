<template>
	<div>
		<el-page-header @back="goBack" content="修改页面">
		</el-page-header>
		<div>
			<el-form ref="form" :model="form" label-width="80px">
				<el-form-item label="商品名称">
				    <el-input v-model="name"></el-input>
				</el-form-item>
				<el-form-item label="商品价格">
				    <el-input v-model="price"></el-input>
				</el-form-item>
				<el-form-item label="商品库存">
				    <el-input v-model="sku"></el-input>
				</el-form-item>
				<el-form-item label="分类">
					<el-select v-model="categoryId" placeholder="选择分类">
						<el-option
							v-for="item in categories"
							:key="item.id"
							:label="item.name"
							:value="item.id">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item>
					<input type="file" @change="handleFileChange">
				</el-form-item>
				<el-form-item label="商品描述">
				    <el-input type="textarea" v-model="description"></el-input>
				</el-form-item>
				<el-form-item>
				    <el-button @click="updateGoods" type="primary">修改</el-button>
				</el-form-item>
			</el-form>
		</div>
	</div>
</template>

<script>
import axios from 'axios';
export default {
	data(){
		return{
			categories: [],
			id: '',
			name: '',
			description: '',
			sku: '',
			price: '',
			file: null,
			categoryId: ''
		}
	},
	mounted() {
		this.id = this.$route.query.itemId;
		this.loadData();
	},
	methods: {
		//返回上一页
	    goBack() {
			this.$router.go(-1);
	    },
		handleFileChange(event) {
			this.file = event.target.files[0];
		},
		loadData(){
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
			axios.get('http://localhost:8081/admin/goodsmanager/check', { params: {id: this.$route.query.itemId} ,
			headers: {
			        'token': this.$store.state.token
			    }},
			)
				.then(response => {
					this.name = response.data.data.name;
					this.description = response.data.data.description;
					this.price = response.data.data.price;
					this.imgPath = response.data.data.imgPath;
					this.sku = response.data.data.sku;
				})
		},
		updateGoods(){
			const options = {
			  method: 'POST',
			  url: 'http://localhost:8081/admin/goodsmanager/update',
			  headers: {
			    token: this.$store.state.token,
				'content-type': 'multipart/form-data; boundary=---011000010111000001101001'
			  },
			  data: {
					"id": this.id,
					"img": this.file,
					"name": this.name,
					"price": this.price,
					"sku": this.sku,
					"description": this.description,
					"categoryId": this.categoryId
			  }
			};
			
			axios.request(options).then(response => {
				this.categories = [];
				this.file = null;
				this.name = '';
				this.price = '';
				this.sku = '';
				this.description = '';
				this.categoryId = '';
				this.$notify({
					title: '',
					message: '修改成功',
					position: 'bottom-right'
				});
			}).catch(function (error) {
				console.error(error);
			});
		}
	}
}
</script>

<style>
</style>

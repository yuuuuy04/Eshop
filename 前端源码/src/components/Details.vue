<template>
	<div>
		<el-page-header @back="goBack" content="详情页面">
		</el-page-header>
		<div class="product-details">
			<!-- 左侧图片 -->
			<div class="product-image">
				<img :src="'http://localhost:8081/img/' + this.imgPath" alt="Product Image" />
			</div>
			<!-- 右侧详情 -->
			<div class="product-info">
				<h2>{{name}}</h2>
				<p>{{description}}</p>
				<p>价格: {{price}}</p>
				<p>销量: {{sales}}</p>
			</div>
		</div>
		<el-button type="danger" @click="addGoods">加入购物车</el-button>
	</div>
</template>

<script>
import axios from 'axios';
export default {
	data(){
		return{
			id: null,
			name: '',
			description: '',
			price: null,
			imgPath: '',
			sales: null
		}
	},
	created() {
	    this.loadDetails();
	},
	methods: {
		//返回上一页
	    goBack() {
			this.$router.go(-1);
	    },
		//加载数据
		loadDetails(){
			axios.get('http://localhost:8081/universal/detail', { params: {id: this.$route.query.itemId} })
				.then(response => {
					this.id = response.data.data.id;
					this.name = response.data.data.name;
					this.description = response.data.data.description;
					this.price = response.data.data.price;
					this.imgPath = response.data.data.imgPath;
					this.sales = response.data.data.sales;
				})
		},
		openSuccess() {
		    this.$notify({
				title: '',
				message: '添加成功',
				position: 'bottom-right'
		    });
		},
		openFail() {
		    this.$notify({
				title: '',
				message: '请先登录',
				position: 'bottom-right'
		    });
		},
		addGoods(){
			if(this.$store.state.token !== ''){
				const form = new FormData();
				form.append("goodsId", this.id);
				form.append("amount", parseInt("1"));
				axios.post('http://localhost:8081/user/cart/addgoods',form,{
					headers: {
						'token': this.$store.state.token
					}
				}).then(response => {
					this.openSuccess();
				})
			}else{
				this.openFail();
			}
		}
	}
}
</script>

<style scoped>
.product-details {
  display: flex;
  align-items: center;
}

.product-image {
  margin-right: 20px;
}

.product-image img {
  max-width: 300px;
  height: auto;
}
</style>

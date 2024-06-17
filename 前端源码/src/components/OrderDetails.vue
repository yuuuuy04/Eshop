<template>
	<div>
		<el-page-header @back="goBack" content="订单页"></el-page-header>
		<el-table
		:data="orderDetails"
		style="width: 100%">
				<el-table-column
					label=""
					width="120">
					<template slot-scope="scope">
						<img :src= "'http://localhost:8081/img/' + scope.row.imgPath" style="width: 100px; height: 100px;" />
					</template>
				</el-table-column>
				<el-table-column
					prop="goodsId"
					label="id"
					width="180">
				</el-table-column>
				<el-table-column
					prop="name"
					label="名字"
					width="180">
				</el-table-column>
				<el-table-column
					prop="price"
					label="单价">
				</el-table-column>
				<el-table-column
				prop="amount"
				label="数量"
				show-overflow-tooltip>
				</el-table-column>
				<el-table-column
				prop="totalPrices"
				label="总价"
				show-overflow-tooltip>
				</el-table-column>
		    </el-table>
			<p>总计: {{this.totalPrices}}</p>
	</div>
</template>

<script>
import axios from 'axios';

export default{
	data() {
	  return {
		orderDetails: [],
		totalPrices: 0
	  }
	},
	mounted() {
		this.loadDetails()
	},
	methods: {
		goBack() {
			this.$router.go(-1);
		},
		loadDetails(){
			axios.get('http://localhost:8081/user/order/orderdetail', { params: {orderId: this.$route.query.itemId} ,
			headers: {
			        'token': this.$store.state.token
			    }},
			)
				.then(response => {
					this.orderDetails = response.data.data
					this.totalPrices = this.orderDetails.reduce((total, item) => {
					    return total + item.totalPrices;
					}, 0);
			})
			.catch(function (error) {
				console.error(error);
			})
		}
	}
}
</script>

<style>
</style>

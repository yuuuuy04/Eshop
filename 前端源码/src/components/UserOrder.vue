<template>
	<div class="divclass">
		<el-table
		:data="orderList"
		border
		style="width: 100%">
		<el-table-column
			fixed
			prop="id"
			label="订单号"
			width="150">
		</el-table-column>
		<el-table-column
			prop="totalPrices"
			label="订单金额"
			width="120">
		</el-table-column>
		<el-table-column
			fixed="right"
			label="操作"
			width="150">
			<template slot-scope="scope">
				<el-button @click="getOrderDetails(scope.row.id)" type="text" size="small">详细</el-button>
				<el-button v-if="scope.row.status == '2'" type="danger" size="small" @click="payOrder(scope.row.id)">支付</el-button>
				<el-tag v-if="scope.row.status == '1'" type="success">已完成</el-tag>
				<el-tag v-if="scope.row.status == '0'" type="danger">未完成</el-tag>
			</template>
		</el-table-column>
		</el-table>
	</div>
</template>

<script>
import axios from 'axios';

export default{
	data() {
	  return {
		orderList: []
	  };
	},
	mounted() {
		this.loadOrder()
	},
	methods:{
		loadOrder(){
			axios.get('http://localhost:8081/user/order/list', {
				headers: {
					'token': this.$store.state.token
				}
			})
			.then(response => {
				this.orderList = response.data.data;
			})
			.catch(err => {
				
			})
		},
		payOrder(id){
			this.$confirm('请点击确认进行支付', '提示', {
			    confirmButtonText: '确认',
			    cancelButtonText: '取消',
			    type: 'warning'
			})
			.then(() => {
			    const options = {
			      method: 'POST',
			      url: 'http://localhost:8081/user/order/confirm',
			      headers: {
			        token: this.$store.state.token,
			    	'content-type': 'multipart/form-data; boundary=---011000010111000001101001'
			      },
			      data: {
			    		"orderId": id
			      }
			    };
			    
			    axios.request(options).then(response => {
					if(response.data.code == 200 && response.data.data == true){
						this.$notify({
							title: '',
							message: '支付成功',
							position: 'bottom-right'
						});
					}
				})
			})
			.catch(() => {
			});
		},
		getOrderDetails(id){
			this.$router.push({path: '/orderdetails', query:{itemId: id}})
		}
	}
}
</script>

<style>
.divclass {
	max-width: 450px;
	width: 100%;
}
</style>

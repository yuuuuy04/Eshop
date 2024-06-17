<template>
	<div>
		<el-table
		:data="goodsData"
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
					label="姓名"
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
			<el-button @click="addOrder" type="danger" round>下单</el-button>
	</div>
</template>

<script>
import axios from 'axios';
export default {
	data() {
	  return {
	    goodsData: [],
		totalPrices: 0,
		//购物车商品id
		id: [],
		orderId: 0
	  }
	},
	mounted() {
		this.goodsData = this.$route.query.arrayParam;
		this.unite()
	},
	methods: {
		unite(){
			this.totalPrices = this.goodsData.reduce((total, item) => {
			    return total + item.totalPrices;
			}, 0);
			
			this.id = this.goodsData.map(item => item.id);
			console.log(this.id)
		},
		addOrder() {
		    axios.post('http://localhost:8081/user/order/add', this.id, {
					headers: {
						'token': this.$store.state.token
					}
				})
		        .then(response => {
					if(response.data.code == 200){
						this.orderId = response.data.data.orderId;
						this.$confirm('订单已添加,请进行支付', '提示', {
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
						    		"orderId": this.orderId
						      }
						    };
						    
 						    axios.request(options).then(response => {
								if(response.data.code == 200 && response.data.data == true){
									this.$notify({
										title: '',
										message: '支付成功',
										position: 'bottom-right'
									});
									this.$router.push('/show');
								}
							})
						})
						.catch(() => {
							this.$router.push('/userorder');
						});
					}else{
						this.$message.error('订单添加失败，请稍后重试');
					}
		        })
		        .catch(error => {
		            
		        });
		}
	}
}
</script>

<style>
</style>

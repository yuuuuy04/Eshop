<template>
	<div>
		<el-table
		ref="multipleTable"
		:data="goodsData"
		tooltip-effect="dark"
		style="width: 100%"
		@selection-change="handleSelectionChange">
			<el-table-column
			type="selection"
			width="55">
			</el-table-column>
			<el-table-column
				label=""
				width="120">
				<template slot-scope="scope">
					<img :src= "'http://localhost:8081/img/' + scope.row.imgPath" style="width: 100px; height: 100px;" />
				</template>
			</el-table-column>
			<el-table-column
			prop="name"
			label="名字"
			width="120">
			</el-table-column>
			<el-table-column
			prop="price"
			label="单价"
			show-overflow-tooltip>
			</el-table-column>
			<el-table-column
			prop="totalPrices"
			label="总价"
			show-overflow-tooltip>
			</el-table-column>
			<el-table-column
			prop="amount"
			label="数量"
			show-overflow-tooltip>
				<template slot-scope="scope">
					<el-input-number v-model="scope.row.amount" @change="handleChange(scope.row)" :min="0" :max="10" label="描述文字"></el-input-number>
				</template>
			</el-table-column>
		</el-table>
		<div class="overlay">
			<el-button @click='createOrder()' style="margin-left: auto;" round >确认订单</el-button>
		</div>
	</div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
      return {
        goodsData: [],
        multipleSelection: [],
		totalPrices: '',
		selected: []
      }
    },
	created() {
		this.loadCart();
	},
    methods: {
		//保存选中的商品
		handleSelectionChange(selection){
			this.selected = selection;
		},
		loadCart(){
			axios.get('http://localhost:8081/user/cart/getcart', {
				headers: {
					'token': this.$store.state.token
				}
			})
			.then(response => {
				this.goodsData = response.data.data.list;
				this.totalPrices = response.data.data.totalPrices;
				this.goodsData.forEach(item => {
					item.realAmount = item.amount;
				});
			})
			.catch(err => {
				
			})
		},
		toggleSelection(rows) {
			if (rows) {
				rows.forEach(row => {
					this.$refs.multipleTable.toggleRowSelection(row);
				});
			} else {
				this.$refs.multipleTable.clearSelection();
			}
		},
		//加减商品
		handleChange(row) {
			//点击减一
			if (row.amount < row.realAmount) {
				const form = new FormData();
				form.append("goodsId", row.goodsId);
				form.append("amount", parseInt("1"));
				axios.post('http://localhost:8081/user/cart/deletegoods',form,{
					headers: {
						'token': this.$store.state.token,
						'content-type': 'multipart/form-data; boundary=---011000010111000001101001'
					}
				}).then(response => {
					row.realAmount = row.realAmount-1;
				})
			} else if(row.amount > row.realAmount){
				const form = new FormData();
				form.append("goodsId", row.goodsId);
				form.append("amount", parseInt("1"));
				axios.post('http://localhost:8081/user/cart/addgoods',form,{
					headers: {
						'token': this.$store.state.token
					}
				}).then(response => {
					row.realAmount = row.realAmount+1;
				})
			}
		},
		//创建订单
		createOrder(){
			this.$router.push({path: '/createorder', query: {arrayParam: this.selected}});
		}
	}
}
</script>

<style>
.overlay {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 50px; /* 使用固定像素值 */
  background-color: rgba(255, 255, 255, 1); /* 半透明的白色背景 */
  padding: 20px; /* 按钮周围的填充 */
  box-sizing: border-box; /* 使填充不会增加元素的实际尺寸 */
  display: flex; /* 使用 flex 布局 */
  justify-content: flex-end; /* 按钮向右对齐 */
  align-items: center; /* 按钮垂直居中 */
  z-index: 9999;
}
</style>

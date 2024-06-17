<template>
	<div>
		<el-button type="text" class="left-button" @click="addGoods">添加</el-button>
		<el-table
			:data="data"
			border
			style="width: 100%">
			<el-table-column
			fixed
			prop="id"
			label="id"
			width="50">
			</el-table-column>
			<el-table-column
			prop="img"
			label=""
			width="150">
				<template v-slot:default="scope">
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
			label="价格(元)"
			width="100">
			</el-table-column>
			<el-table-column
			prop="category"
			label="分类"
			width="120">
			</el-table-column>
			<el-table-column
			prop="sku"
			label="库存"
			width="70">
			</el-table-column>
			<el-table-column
			prop="sales"
			label="销量"
			width="120">
			</el-table-column>
			<el-table-column
			prop="description"
			label="描述"
			width="700">
			</el-table-column>
			<el-table-column
			fixed="right"
			label="启用/禁用"
			width="100">
			<template v-slot:default="scope">
				<el-switch
				v-model="scope.row.status"
				active-color="#13ce66"
				inactive-color="#afafaf"
				active-value="0"
				inactive-value="1"
				@change="switchStatus(scope.row.id)">
				</el-switch>
			</template>
			</el-table-column>
			<el-table-column
			fixed="right"
			label=""
			width="100">
			<template v-slot:default="scope">
				<el-button
					v-model="scope.row.id"
					@click="toModifyPage(scope.row.id)"
					type="text"
					size="small">
					修改
				</el-button>
			</template>
			</el-table-column>
		</el-table>
	</div>
</template>

<script>
import axios from 'axios';
export default {
    data() {
      return {
        data: []
      }
    },
	mounted() {
		this.loadData()
	},
	methods:{
		loadData(){
			const options = {
			  method: 'GET',
			  url: 'http://localhost:8081/admin/goodsmanager/get',
			  headers: {
				token: this.$store.state.token
			  }
			};
			
			axios.get('http://localhost:8081/admin/goodsmanager/get', {
							headers: {
								'token': this.$store.state.token
							}
						}).then(response => {
				this.data = response.data.data
			}).catch(function (error) {
				console.error(error);
			});
		},
		switchStatus(row){
			const form = new FormData();
			form.append("id", row.id);
			form.append("status", row.status);
			const options = {
			  method: 'POST',
			  url: 'http://localhost:8081/admin/goodsmanager/switch',
			  headers: {
				token: this.$store.state.token
			  },
			  data: form
			};
			
			axios.request(options).then(function (response) {
			  console.log(response.data);
			}).catch(function (error) {
			  console.error(error);
			});
		},
		toModifyPage(id){
			this.$router.push({path: '/modifygoods', query:{itemId: id}});
		},
		addGoods(){
			this.$router.push({path: '/addgoods'});
		}
	}
}
</script>

<style scoped>
</style>

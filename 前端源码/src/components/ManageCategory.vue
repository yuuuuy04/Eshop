<template>
	<div class="divclass">
		<div class="button-container">
			<el-button type="text" class="left-button" @click="addCate">添加分类</el-button>
		</div>
		<div class="table-container">
			<el-table
				:data="data"
				style="width: 100%"
				max-height="700px">
				<el-table-column
					fixed
					prop="id"
					label="id"
					width="150">
				</el-table-column>
				<el-table-column
					fixed
					prop="name"
					label="分类名"
					width="150">
				</el-table-column>
				<el-table-column
					fixed="right"
					label="操作"
					width="120">
					<template slot-scope="scope">
						<el-button
							v-model="scope.row.id"
							@click="handleChange(scope.row.id)"
							type="text"
							size="small">
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>
	</div>
</template>

<script>
import axios from 'axios';
export default{
	data(){
		return{
			data: []
		}
	},
	created() {
		this.loadCategory()
	},
	methods: {
		handleChange(row){
			const options = {
				method: 'DELETE',
				url: 'http://localhost:8081/admin/cate/del',
				params: {id: row.id},
				headers: {
					token: this.$store.state.token
				}
			};
			
			axios.request(options).then(function (response) {
				console.log(response.data);
			}).catch(function (error) {
				console.error(error);
			});
		},
		addCate(){
			this.$prompt('请输入分类名', '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消'
			        }).then(({ value }) => {
						const form = new FormData();
						form.append("name", value);
						axios.post('http://localhost:8081/admin/cate/add', form, 
						{
							headers: {
								'token': this.$store.state.token
							}
						})
						.then(response => {
							if(response.data.code == 200){
								this.$message({
								  type: 'success',
								  message: '添加成功'
								});
							}else{
								this.$message({
									type: 'info',
									message: '发送 POST 请求出错，请稍后重试'
								});
							}
						})
						.catch(() => {
							this.$message({
								type: 'info',
								message: '取消输入'
							});
						})   
			        }).catch(() => {
						this.$message({
						type: 'info',
						message: '取消输入'
					});       
				});
		},
		loadCategory(){
			axios.get('http://localhost:8081/universal/categories')
			.then(response => {
			  if (response.data.code === 200) {
			    this.data = response.data.data;
			  } else {
			    console.error('Error fetching categories:', data.message);
			  }
			})
			.catch(error => {
			  console.error('Error fetching categories:', error);
			});
		}
	}
}
</script>

<style>
.button-container {
	text-align: left;
}

.left-button {
	margin-right: 10px;
}
.table-container {
    max-width: 500px;
    width: 100%;
}
.divclass {
	display: flex;
	justify-content: center;
}
</style>

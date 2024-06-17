import VueRouter from "vue-router";
import Vue from "vue";
// import Select from '../components/Select.vue'
const Select = () => import('../components/Select.vue')
// import Cart from '../components/Cart.vue'
const Cart = () => import('../components/Cart.vue')
// import Login from '../components/Login.vue'
const Login = () => import('../components/Login.vue')
// import Register from '../components/Register.vue'
const Register = () => import('../components/Register.vue')
//import Details from '../components/Details.vue'
const Details = () => import('../components/Details.vue')
import ManageCategory from '../components/ManageCategory.vue'
import ManageGoods from '../components/ManageGoods.vue'
import AddGoods from '../components/AddGoods.vue'
import CreateOrder from '../components/CreateOrder.vue'
import ModifyGoods from '../components/ModifyGoods.vue'
import UserOrder from '../components/UserOrder.vue'
import OrderDetails from '../components/OrderDetails.vue'

Vue.use(VueRouter)

const router = new VueRouter({
	routes: [
		{path: '', redirect: '/show'},
		{path: '/show', component: Select},
		{path: '/cart', component: Cart},
		{path: '/login', component: Login},
		{path: '/register', component: Register},
		{path: '/details', component: Details},
		{path: '/managecategory', component: ManageCategory},
		{path: '/managegoods', component: ManageGoods},
		{path: '/addgoods', component: AddGoods},
		{path: '/createorder', component: CreateOrder},
		{path: '/modifygoods', component: ModifyGoods},
		{path: '/userorder', component: UserOrder},
		{path: '/orderdetails', component: OrderDetails}
	]
})

export default router
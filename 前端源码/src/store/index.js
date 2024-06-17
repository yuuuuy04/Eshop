import Vue from "vue";
import Vuex from 'vuex';
Vue.use(Vuex)

const store = new Vuex.Store({
	state: {
		token: '',
		userType: '',
		avatar: ''
	},
	mutations: {
	    setToken(state, token) {
	      state.token = token;
	    },
		setuserType(state, userType){
			state.userType = userType;
		},
		setAvatar(state, avatar){
			state.avatar = avatar;
		}
	},
	actions: {
	    saveToken({ commit }, token) {
	      commit('setToken', token);
	    },
		saveuserType({ commit }, userType) {
		  commit('setuserType', userType);
		},
		saveAvatar({ commit }, avatar) {
		  commit('setAvatar', 'http://localhost:8081/avatar/' + avatar);
		}
	}
})
export default store
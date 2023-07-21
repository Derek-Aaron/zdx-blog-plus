
import {useStore} from "@/stores";

export default function installPlugins(app){
	app.directive('checkPerm', (el, binding, vnode) => {
		const  { value } = binding
		if (checkPerm(value)) {
			el.parentNode && el.parentNode.removeChild(el)
		}
	})

	app.directive('checkRole', (el, binding, vnode) => {
		const  { value } = binding
		if (checkRole(value)) {
			el.parentNode && el.parentNode.removeChild(el)
		}
	})
}


export const checkPerm = (value) => {
	let flag = useStore().useUser.permissions.indexOf('*::||::*') === -1
	if (flag && typeof value === 'string') {
		flag = useStore().useUser.permissions.indexOf(value) === -1
	}
	if (flag && typeof value === 'object' && Array.isArray(value)) {
		flag = value.some(item => {
			return useStore().useUser.permissions.indexOf(item) === -1
		})
	}
	return flag;
}

export const checkRole = (value) => {
	const roleNames = useStore().useUser.roles.map(item => item.name)
	let flag = roleNames.indexOf('admin') === -1;
	if (flag && roleNames.indexOf(value) === -1) {
		flag = true;
	}
	return flag;
}
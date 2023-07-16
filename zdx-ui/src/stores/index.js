import {useAppStore} from "@/stores/modules/useAppStore";
import {useBlogStore} from "@/stores/modules/useBlogStore";
import {useUserStore} from "@/stores/modules/useUserStore";

const useStore = () => ({
	app: useAppStore(),
	blog: useBlogStore(),
	user: useUserStore(),
});

export default useStore;
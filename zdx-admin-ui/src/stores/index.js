import {useAppStore} from "./modules/useAppStore";
import {useSettingsStore} from "./modules/useSettingStore";
import {useTagsViewStore} from "./modules/useTagsViewStore";
import {useUserStore} from "@/stores/modules/useUserStore";
import {usePermissionStore} from "@/stores/modules/usePermissionStore";
import { useDictStore } from "./modules/useDictStore";


export const useStore = () => ({
    useDict: useDictStore(),
    usePermission: usePermissionStore(),
    useUser: useUserStore(),
    useApp: useAppStore(),
    useSetting: useSettingsStore(),
    useTagsView: useTagsViewStore()
})
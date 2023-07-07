
import defaultSettings from '@/settings'
import {useStore} from "../stores";

/**
 * 动态修改标题
 */
export function useDynamicTitle() {
  const settingsStore = useStore.useSetting
  if (settingsStore.dynamicTitle) {
    document.title = settingsStore.title + ' - ' + defaultSettings.title;
  } else {
    document.title = defaultSettings.title;
  }
}
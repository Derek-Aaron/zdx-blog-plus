import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useDictStore = defineStore('useDictStore', () => {
    const dicts = ref([])

    const getDict = (key) => {
        if (key == null && key === "") {
            return null;
        }
        try {
            for (let i = 0; i < dicts.value.length; i++) {
                if (dicts.value[i].key === key) {
                    return dicts.value[i].value;
                }
            }
        } catch (e) {
            return null;
        }
    }

    const setDict = (key, value) => {
        if (key !== null && key !== "") {
            dicts.value.push({
                key: key,
                value: value
            });
        }
    }

    const removeDict = (key) => {
        let bln = false;
        try {
          for (let i = 0; i < dicts.value.length; i++) {
            if (dicts.value[i].key === key) {
              dicts.value.splice(i, 1);
              return true;
            }
          }
        } catch (e) {
          bln = false;
        }
        return bln;
    }

    const cleanDict = () => {
        dicts.value = []
    }

    const initDict = () => {

    }

    return {dicts, getDict, setDict, removeDict, cleanDict, initDict}
})
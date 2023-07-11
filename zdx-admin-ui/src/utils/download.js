import {saveAs} from 'file-saver';
import {ElMessage} from "element-plus";
export async function download(res, fileName) {
    const isLogin = await blobValidate(res);
    if (isLogin) {
        const blob = new Blob([res.data]);
        saveAs(blob, fileName);
    } else {
        const resText = await res.data.text();
        const rspObj = JSON.parse(resText);
        if (rspObj.code === 500) {
            ElMessage.error(rspObj.message)
        }
    }
}

async function blobValidate(res) {
    try {
        const text = await res.data.text();
        JSON.parse(text);
        return false;
    } catch (error) {
        return true;
    }
}
import {saveAs} from 'file-saver';
import {ElMessage} from "element-plus";
export async function download(res, fileName) {
    let disposition = res.headers['content-disposition']
    if (fileName === '' || fileName === null || fileName === undefined) {
        fileName = disposition.substring(disposition.lastIndexOf('=') + 1, disposition.length)
    }
    const blob = new Blob([res.data]);
    saveAs(blob, fileName);
}


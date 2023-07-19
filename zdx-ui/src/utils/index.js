export function transToFile(blob, fileName, fileType) {
	return new File([blob], fileName, {type: fileType})
}
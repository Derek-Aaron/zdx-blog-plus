package com.zdx.utils;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.zdx.Constants;
import com.zdx.enums.FileTypeEnum;

import java.util.Locale;

public class FileUtil {

    public static String getFileType(String filename) {
        String type = "";
        if (StrUtil.isNotBlank(filename) && ObjUtil.isNotNull(filename)) {
            for (String image : Constants.IMAGE_TYPE) {
                if (filename.toUpperCase(Locale.ROOT).endsWith(image) || filename.toLowerCase(Locale.ROOT).endsWith(image)) {
                    type = FileTypeEnum.IMAGE.name();
                    break;
                }
            }
            if (StrUtil.isBlank(type)) {
                for (String audio : Constants.AUDIO_TAPE) {
                    if (filename.toUpperCase(Locale.ROOT).endsWith(audio) || filename.toLowerCase(Locale.ROOT).endsWith(audio)) {
                        type = FileTypeEnum.AUDIO.name();
                        break;
                    }
                }
            }
            if (StrUtil.isBlank(type)) {
                for (String document : Constants.DOCUMENT_TYPE) {
                    if (filename.toUpperCase(Locale.ROOT).endsWith(document) || filename.toLowerCase(Locale.ROOT).endsWith(document)) {
                        type = FileTypeEnum.DOCUMENT.name();
                        break;
                    }
                }
            }
            if (StrUtil.isBlank(type)) {
                for (String document : Constants.VIDEO_TAPE) {
                    if (filename.toUpperCase(Locale.ROOT).endsWith(document) || filename.toLowerCase(Locale.ROOT).endsWith(document)) {
                        type = FileTypeEnum.VIDEO.name();
                        break;
                    }
                }
            }
        } else {
            type = FileTypeEnum.OTHER.name();
        }
        return type;
    }
}

package com.zdx.strategy.context;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.Constants;
import com.zdx.entity.tk.Dict;
import com.zdx.entity.us.Auth;
import com.zdx.enums.AuthSourceEnum;
import com.zdx.enums.DictTypeEnum;
import com.zdx.enums.LikeTypeEnum;
import com.zdx.enums.MusicTypeEnum;
import com.zdx.model.vo.front.MusicVo;
import com.zdx.strategy.*;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StrategyContext {

    private final List<LikeStrategy> likeStrategies;

    private final List<AuthStrategy> authStrategies;

    private final List<DictStrategy> dictStrategies;

    private final List<MusicStrategy> musicStrategies;


    private final List<DataImportExportStrategy> dataImportExportStrategies;


    /**
     * 执行点赞策略
     * @param typeEnum 点赞类型
     * @param typeId 点赞id
     */
    public void executeLike(LikeTypeEnum typeEnum, String typeId) {
        for (LikeStrategy likeStrategy : likeStrategies) {
            if (likeStrategy.likeType().equals(typeEnum)) {
                likeStrategy.execute(typeId);
                break;
            }
        }
    }

    /**
     * 第三方登录
     * @param sourceEnum 登录类型
     * @param auth 实体类
     * @return 返回
     */
    public AuthRequest executeAuth(AuthSourceEnum sourceEnum, Auth auth) {
        for (AuthStrategy authStrategy : authStrategies) {
            if (authStrategy.source().equals(sourceEnum)) {
                return authStrategy.execute(auth);
            }
        }
        return null;
    }

    /**
     * 数据字典映射
     * @param typeEnum 数据字典类型
     * @param dict 数据字典
     * @return 返回
     */
    public List<Map<String, String>> executeDict(DictTypeEnum typeEnum, Dict dict) {
        for (DictStrategy dictStrategy : dictStrategies) {
            if (dictStrategy.type().equals(typeEnum)) {
                return dictStrategy.execute(dict);
            }
        }
        return null;
    }

    /**
     * 获取加载的音乐
     * @param typeEnum 类型
     * @param musicId 音乐id
     * @return 返回
     */
    @Cacheable(cacheNames = Constants.MUSIC_CACHE, keyGenerator = "zdx-key-generator")
    public List<MusicVo> executeMusic(MusicTypeEnum typeEnum, String musicId) {
        for (MusicStrategy musicStrategy : musicStrategies) {
            if (musicStrategy.musicType().equals(typeEnum)) {
                return musicStrategy.execute(musicId);
            }
        }
        return null;
    }

    /**
     * 数据导入
     * @param file 文件
     * @param type 类型
     * @param service service 类
     * @param <T> 泛型实体
     * @throws IOException 异常
     */
    @SuppressWarnings("all")
    public <T> void importData(MultipartFile file, String type, IService<T> service) throws IOException {
        for (DataImportExportStrategy dataImportExportStrategy : dataImportExportStrategies) {
            if (dataImportExportStrategy.type().equals(type)) {
                dataImportExportStrategy.importData(file, service);
                return;
            }
        }
    }

    @SuppressWarnings("all")
    public <T> void exportData(HttpServletResponse response, String type, Wrapper<T> wrapper, IService<T> service) throws IOException {
        for (DataImportExportStrategy dataImportExportStrategy : dataImportExportStrategies) {
            if (type.equals(dataImportExportStrategy.type())) {
                dataImportExportStrategy.exportData(response, wrapper, service);
                return;
            }
        }
    }
}

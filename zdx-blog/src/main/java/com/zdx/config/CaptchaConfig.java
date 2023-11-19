package com.zdx.config;

import cn.hutool.core.util.StrUtil;
import com.anji.captcha.model.common.Const;
import com.anji.captcha.service.CaptchaCacheService;
import com.anji.captcha.service.CaptchaService;
import com.anji.captcha.service.impl.CaptchaServiceFactory;
import com.anji.captcha.util.ImageUtils;
import com.zdx.config.properties.CaptchaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author zhaodengxuan
 */
@Configuration
@Slf4j
@EnableConfigurationProperties(CaptchaProperties.class)
public class CaptchaConfig {
	@Autowired
	@Qualifier("captchaProperties")
	private CaptchaProperties prop;

	@Bean
	@ConditionalOnMissingBean
	public CaptchaService captchaService() {
		Properties config = new Properties();
		config.put(Const.CAPTCHA_CACHETYPE, prop.getCacheType().name());
		config.put(Const.CAPTCHA_WATER_MARK, prop.getWaterMark());
		config.put(Const.CAPTCHA_FONT_TYPE, prop.getFontType());
		config.put(Const.CAPTCHA_TYPE, prop.getType().getCodeValue());
		config.put(Const.CAPTCHA_INTERFERENCE_OPTIONS, prop.getInterferenceOptions());
		config.put(Const.ORIGINAL_PATH_JIGSAW, prop.getJigsaw());
		config.put(Const.ORIGINAL_PATH_PIC_CLICK, prop.getPicClick());
		config.put(Const.CAPTCHA_SLIP_OFFSET, prop.getSlipOffset());
		config.put(Const.CAPTCHA_AES_STATUS, String.valueOf(prop.getAesStatus()));
		config.put(Const.CAPTCHA_WATER_FONT, prop.getWaterFont());
		config.put(Const.CAPTCHA_CACAHE_MAX_NUMBER, prop.getCacheNumber());
		config.put(Const.CAPTCHA_TIMING_CLEAR_SECOND, prop.getTimingClear());

		config.put(Const.HISTORY_DATA_CLEAR_ENABLE, prop.isHistoryDataClearEnable() ? "1" : "0");

		config.put(Const.REQ_FREQUENCY_LIMIT_ENABLE, prop.isReqFrequencyLimitEnable() ? "1" : "0");
		config.put(Const.REQ_GET_LOCK_LIMIT, String.valueOf(prop.getReqGetLockLimit()));
		config.put(Const.REQ_GET_LOCK_SECONDS, String.valueOf(prop.getReqGetLockSeconds()));
		config.put(Const.REQ_GET_MINUTE_LIMIT, String.valueOf(prop.getReqGetMinuteLimit()));
		config.put(Const.REQ_CHECK_MINUTE_LIMIT, String.valueOf(prop.getReqCheckMinuteLimit()));
		config.put(Const.REQ_VALIDATE_MINUTE_LIMIT, String.valueOf(prop.getReqVerifyMinuteLimit()));

		config.put(Const.CAPTCHA_FONT_SIZE, String.valueOf(prop.getFontSize()));
		config.put(Const.CAPTCHA_FONT_STYLE, String.valueOf(prop.getFontStyle()));
		config.put(Const.CAPTCHA_WORD_COUNT, String.valueOf(prop.getClickWordCount()));

		if ((StrUtil.isNotBlank(prop.getJigsaw()) && prop.getJigsaw().startsWith("classpath:"))
				|| (StrUtil.isNotBlank(prop.getPicClick()) && prop.getPicClick().startsWith("classpath:"))) {
			//自定义resources目录下初始化底图
			config.put(Const.CAPTCHA_INIT_ORIGINAL, "true");
			initializeBaseMap(prop.getJigsaw(), prop.getPicClick());
		}
		return CaptchaServiceFactory.getInstance(config);
	}

	private static void initializeBaseMap(String jigsaw, String picClick) {
		ImageUtils.cacheBootImage(getResourcesImagesFile(jigsaw + "/original/*.png"),
				getResourcesImagesFile(jigsaw + "/slidingBlock/*.png"),
				getResourcesImagesFile(picClick + "/*.png"));
	}

	public static Map<String, String> getResourcesImagesFile(String path) {
		Map<String, String> imgMap = new HashMap<>();
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try {
			Resource[] resources = resolver.getResources(path);
			for (Resource resource : resources) {
				byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
				String string = Base64Utils.encodeToString(bytes);
				String filename = resource.getFilename();
				imgMap.put(filename, string);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return imgMap;
	}

	@Bean
	public CaptchaCacheService captchaCacheService(){
		//缓存类型redis/local/....
		return CaptchaServiceFactory.getCache(prop.getCacheType().name());
	}
}

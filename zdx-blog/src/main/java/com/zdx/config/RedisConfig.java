package com.zdx.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zdx.Constants;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.TimeZone;

/**
 * @author zhaodengxuan
 */
@Configuration
public class RedisConfig {

	/**
	 * 默认采用Jackson序列化方式
	 */
	@Bean
	public RedisSerializer<?> Jackson2JsonRedisSerialize(){
		Jackson2JsonRedisSerializer<?> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setTimeZone(TimeZone.getDefault());
		jsonRedisSerializer.setObjectMapper(objectMapper);
		return jsonRedisSerializer;
	}

	@Bean
	public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, RedisSerializer<?> fastJson2JsonRedisSerialize){
		RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		//设置Key的序列化采用StringRedisSerializer
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		//设置值的序列化采用FastJsonRedisSerializer
		redisTemplate.setValueSerializer(fastJson2JsonRedisSerialize);
		redisTemplate.setHashValueSerializer(fastJson2JsonRedisSerialize);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	@Bean
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory,
									 RedisSerializer<?> Jackson2JsonRedisSerialize
	) {
		// 生成一个默认配置，通过config对象即可对缓存进行自定义配置
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
		// 设置缓存的默认过期时间，也是使用Duration设置
		config = config.entryTtl(Duration.ofHours(Constants.REDIS_TIME))
				.computePrefixWith(cacheName -> cacheName)
				// 设置 key为string序列化
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				// 设置value为fastJson序列化
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(Jackson2JsonRedisSerialize))
				// 不缓存空值
				.disableCachingNullValues();
		// 使用自定义的缓存配置初始化一个cacheManager
		RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
		return new RedisCacheManager(redisCacheWriter, config);
	}
}

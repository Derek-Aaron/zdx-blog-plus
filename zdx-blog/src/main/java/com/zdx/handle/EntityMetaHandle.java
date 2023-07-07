package com.zdx.handle;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author zhaodengxuan
 */
@Component
public class EntityMetaHandle implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		this.fillStrategy(metaObject, "createTime", new Date());
		this.fillStrategy(metaObject, "updateTime",  new Date());
		this.fillStrategy(metaObject, "personId", IdUtil.fastSimpleUUID());
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		this.fillStrategy(metaObject, "updateTime",  new Date());
	}
}

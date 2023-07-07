package com.zdx.utils;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author zhaodengxuan
 */
public class TreeUtil {

	/**
	 * 创建树结构
	 *
	 * @param data      数据
	 * @param id        id 字段名
	 * @param parentId  父级字段名
	 * @param recursion 递归深度
	 * @param sortName  排序字段名
	 * @param <T>
	 * @return 返回值
	 */
	@SuppressWarnings("all")
	public static <T> List<Tree<T>> createTree(List<?> data, String id, String parentId, Integer recursion, String sortName) {
		TreeNodeConfig config = new TreeNodeConfig();
		config.setIdKey(id);//默认为id可以不设置
		config.setParentIdKey(parentId);//默认为parentId可以不设置
		config.setDeep(recursion);//最大递归深度
		config.setWeightKey(sortName);//排序字段
		return cn.hutool.core.lang.tree.TreeUtil.build(data, null, config, (object, tree) -> {
			// 也可以使用 tree.setId(object.getId());等一些默认值
			if (object instanceof Map) {
				Map<String, Object> objectMap = (Map<String, Object>) object;
				objectMap.forEach(tree::putExtra);
			} else {
				List<Field> fields = List.of(ReflectUtil.getFields(object.getClass()));
				for (Field field : fields) {
					try {
						String name = field.getName();
						name = "get" + name.toUpperCase(Locale.ROOT).charAt(0) + name.substring(1);
						Method method = ReflectUtil.getPublicMethod(object.getClass(), name);
						tree.putExtra(field.getName(), method.invoke(object));
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		});
	}

	/**
	 * 数据
	 *
	 * @param data
	 * @param sortName
	 * @param <T>
	 * @return
	 */
	public static <T> List<Tree<T>> createTree(List<?> data, String sortName) {
		return createTree(data, "id", "parentId", 4, sortName);
	}

}

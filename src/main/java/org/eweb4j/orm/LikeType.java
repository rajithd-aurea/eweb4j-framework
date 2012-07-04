package org.eweb4j.orm;
/**
 * 模糊查询中的匹配类型
 * @author cfuture.aw
 * @since v1.a.0
 */
public interface LikeType {
	static int LEFT_LIKE = -1;
	static int ALL_LIKE = 0;
	static int RIGHT_LIKE = 1;
}

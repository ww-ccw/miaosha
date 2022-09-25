package com.chw.miaosha.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author CW
 */
@Component
public class UUIDUtil {
	public static String uuid() {
		/**
		 * 获得一个随机码
		 */
		return UUID.randomUUID().toString().replace("-", "");
	}
}

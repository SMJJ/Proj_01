package com.liaomj.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class getPageId {
	
	public static String getPageId(String url) {
		String pageId = "";
		if(StringUtils.isBlank(url)) {
			return pageId;
		}
		Pattern pattern = Pattern.compile("pageId=[0-9]+");  //定义匹配规则
		Matcher matcher = pattern.matcher(url);   // 执行规则返回匹配内容
		
		if (matcher.find()) {    // 判断是否匹配到内容
			pageId = matcher.group().split("pageId=")[1];
		}else {
			pageId = "-";
		}
		return pageId;
	}
	
	public static void main(String[] args) {
		System.out.println(getPageId("http://www.yihaodian.com/cms/view.do?topicId=14572"));
		System.out.println(getPageId("http://www.yihaodian.com/cms/view.do?topicId=22372&merchant=1"));
	}
}

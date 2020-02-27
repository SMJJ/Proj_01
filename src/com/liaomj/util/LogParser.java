package com.liaomj.util;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class LogParser {
	private Logger logger = LoggerFactory.getLogger(LogParser.class);
	
	public Map<String, String> parse(String log){
		Map<String, String> infoMap = new HashMap<>();
		IPParser ipParser = IPParser.getInstance();
		
		if(StringUtils.isNotBlank(log)) {
			String[] splits = log.split("\001");
			String ip = splits[13];
            String url = splits[1];
            String sessionId = splits[10];
            String time = splits[17];
            String pageId = getPageId.getPageId(url);

            infoMap.put("pageId",pageId);
            infoMap.put("ip",ip);
            infoMap.put("url",url);
            infoMap.put("sessionId",sessionId);
            infoMap.put("time",time);
            
			IPParser.RegionInfo regionInfo = ipParser.analyseIp(ip);
			
			String country = "-";
			String city = "-";
			String province = "-";
			if(regionInfo != null) {
				country = regionInfo.getCountry();
				province = regionInfo.getProvince();
				city = regionInfo.getCity();
			}

			infoMap.put("country", country);
			infoMap.put("province", province);
			infoMap.put("city", city);
		}else {
			logger.error("日志记录格式不正确"+log);
		}
		return infoMap;
	}
}

package com.geolog.web.domain;

import org.json.simple.JSONObject;

public class AddPOIResponse extends BaseResponse {

	@Override
	public String serialize() {
	JSONObject result = new JSONObject();
		
		
		
		
		
		result.put("status", getStatus());
		result.put("message", getMessage());
		
		
		
		return  result.toJSONString();
		
	}

}

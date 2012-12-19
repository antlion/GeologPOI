package activity.web.domain;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;



public class SuggestionResponse extends BaseResponse {


	
	@Override
	public String serialize() {
		
		JSONObject result = new JSONObject();
		
		
		
		
		
		result.put("status", getStatus());
		result.put("message", getMessage());
		
		
		
		return  result.toJSONString();
	
	}

	

}

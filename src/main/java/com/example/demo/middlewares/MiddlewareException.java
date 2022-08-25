package com.example.demo.middlewares;

import java.util.HashMap;
import java.util.Map;

public class MiddlewareException extends RuntimeException {
	
	   private static final long serialVersionUID = 1L;
	   
	   public String type;
	   public String error;
	   
	   public MiddlewareException(String type,String error)
	   {
		   this.type = type;
		   this.error = error;
	   }
	   
	   public Map<String, String> customError(){
		   Map<String, String> retMap = new HashMap<>();
		   retMap.put("type", this.type);
		   retMap.put("error", this.error);
		   
		   return retMap;
	   }
}
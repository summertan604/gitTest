package com.xy.Util;

import java.util.HashMap;
import java.util.Map;

public class ReadConst extends ConstDefineUtil {
    public Map<String,String> readConst(){
        Map<String,String> map=new HashMap<>();
        map.put("urlPrefix",prop.getProperty(PROPERTIES_URL));
        map.put("condition",prop.getProperty(PROPERTIES_CONDITION));
        return map;
    }
}

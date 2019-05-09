package TestCase;

import com.xy.Util.InsertSqlUtil;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test1 {
    @Test
    public void test() throws SQLException, ClassNotFoundException {
        Map<String,String> mapTemp=new HashMap<>();
        mapTemp.put("CASENAME","2565");
        mapTemp.put("PROJECTNAME","旧企业资料");
        mapTemp.put("APINAME","pubNumService");
        mapTemp.put("ENVIRONMENT","0");
        mapTemp.put("REQUESTBODY","test");
        mapTemp.put("REQUESTHEADER","test");
        mapTemp.put("RESPONSEBODY","test");
        List<Map<String,String>> list=new ArrayList<>();
        list.add(mapTemp);
        int sum=InsertSqlUtil.executeInsert(list);
        System.out.println("插入数据"+sum+"条");
    }
}

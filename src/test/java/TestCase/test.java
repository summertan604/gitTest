//package com.test.request;
//
//import static io.restassured.RestAssured.given;
//
//import com.xy.extentReport.BaseExample;
//import io.restassured.RestAssured;
//import io.restassured.parsing.Parser;
//import io.restassured.path.json.JsonPath;
//import io.restassured.response.Response;
//import io.restassured.response.ValidatableResponse;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.testng.annotations.Test;
//import org.testng.annotations.DataProvider;
//
//
//import com.relevantcodes.extentreports.LogStatus;
//
//
//
//
//public class RequestAndVerify extends BaseExample {
//    @Test(dataProvider = "dp")
//    public void f(CaseInfo caseInfo) {
//
//        String filepath = "D:\\ME\\接口自动化\\接口模板\\结果.xls";
//        BaseExcel baseExcel = new BaseExcel();
//
//        String requestParameter = ""; //拼接入参
//        Map<String,String> requestHeader = new HashMap<String,String>(); //请求头
//        int responseCode ; //响应状态吗
//        String responseData = ""; //响应数据
//        String expectedResults = ""; //预期结果
//        String actualResults = ""; //实际结果
//        String requestUri = caseInfo.getRequestUri().get("URI"); //请求uri
//        int port = Integer.parseInt(caseInfo.getPort().get("端口")); //请求端口
//        String requestAddress = caseInfo.getRequestAddress().get("地址"); //请求地址
//        String requestBody = "";//post请求，body参数
//
//        /*
//         * 设置请求url
//         */
//        RestAssured.baseURI = requestUri;
//        RestAssured.port = port;
//        RestAssured.basePath = requestAddress;
//        RestAssured.registerParser("text/plain", Parser.JSON);
//
//        ValidatableResponse resp1 = null;
//
//        /*
//         * 拼接入参、拼接请求头、发起请求
//         */
//
//        for (Entry<String, String> mapRequestBody :caseInfo.getRequestBody().entrySet()) {
//
//            if (mapRequestBody.getValue() == null || mapRequestBody.getValue().isEmpty()) { //没有body参数，则执行以下代码
//
//                /*
//                 * 拼接入参
//                 */
//                requestParameter = requestParameter + "?";
//                for (Entry<String, String> mapRequestParameter :caseInfo.getRequestParameter().entrySet()) {
//                    if (mapRequestParameter.getValue() != null && mapRequestParameter.getValue().length() > 0) {
//                        requestParameter = requestParameter + mapRequestParameter.getKey() + "=" + mapRequestParameter.getValue() + "&";
//                    }
//                }
//
//                requestParameter = requestParameter.substring(0, requestParameter.length()-1); //去掉最后一个&符号
//
//                /*
//                 * 拼接请求头
//                 */
//
//                for (Entry<String, String> mapRequestHeader :caseInfo.getRequestHeader().entrySet()) {
//                    if (mapRequestHeader.getValue() != null && mapRequestHeader.getValue().length() > 0) {
//                        requestHeader.put(mapRequestHeader.getKey(), mapRequestHeader.getValue());
//                    }
//
//                }
//
//                /*
//                 * 发起请求
//                 */
//                for (Entry<String, String> mapRequestMode :caseInfo.getRequestMode().entrySet()) {
//                    if (mapRequestMode.getValue().equals("get")) {
//                        resp1 = given()
//                                .headers(requestHeader)
//                                .when()
//                                .get(requestParameter)
//                                .then();
//                    }else if (mapRequestMode.getValue().equals("post")) {
//                        resp1 = given()
//                                .headers(requestHeader)
//                                .when()
//                                .post(requestParameter)
//                                .then();
//                    }
//                }
//
//            } else {
//
//                /*
//                 * 拼接请求头
//                 */
//
//                for (Entry<String, String> mapRequestHeader :caseInfo.getRequestHeader().entrySet()) {
//                    if (mapRequestHeader.getValue() != null && mapRequestHeader.getValue().length() > 0) {
//                        requestHeader.put(mapRequestHeader.getKey(), mapRequestHeader.getValue());
//                    }
//
//                }
//
//                requestBody = mapRequestBody.getValue();
//
//                resp1 = given()
//                        .headers(requestHeader)
//                        .when()
//                        .body(requestBody)
//                        .post()
//                        .then();
//            }
//        }
//
//
//        /*
//         * 输出请求结果到Excel
//         */
//        for (Entry<String, String> m :caseInfo.getUseCaseNumber().entrySet()) {
////baseExcel.writeExcel(filepath, 0, 0, Integer.parseInt(m.getValue()), resp1.extract().asString()); //将结果写入Excel
//        }
//
//        System.out.println(resp1.extract().asString());
//
//        /*
//         * 获取用例名
//         */
//        for (Entry<String, String> mapUseCaseName :caseInfo.getUseCaseName().entrySet()) {
//            test = extent.startTest(mapUseCaseName.getValue());
//        }
//
//        /*
//         * 判断响应码
//         */
//        Response response = resp1.extract().response();
////resp1.assertThat().statusCode(200);
//        responseCode = response.getStatusCode();
//
//        /*
//         * 判断响应结果
//         */
//
//        responseData = resp1.extract().asString();
//        boolean VerificationStr = true; //响应数据类型为String，判断结果通过，则为true，否则为false
//        boolean VerificationInt = true; //响应数据类型为int，判断结果通过，则为true，否则为false
//        JsonPath jsonPath = new JsonPath(resp1.extract().asString()); //将响应数据转化为json
//
////响应数据类型为String，判断结果是否符合预期。
//        for (Entry<String, String> mapRequestResultStr :caseInfo.getRequestResultStr().entrySet()) {
//            int contins = jsonPath.getString(mapRequestResultStr.getKey()).indexOf(mapRequestResultStr.getValue());
//            if (contins != -1) {
//                VerificationStr = true;
//            }else {
//                expectedResults = mapRequestResultStr.getKey() + "==" + mapRequestResultStr.getValue() ;
//                actualResults = mapRequestResultStr.getKey() + "==" + jsonPath.getString(mapRequestResultStr.getKey());
//                VerificationStr = false;
//                break;
//            }
//        }
//
////响应数据类型为int，判断结果是否符合预期。
//
//        for (Entry<String, String> mapRequestResultInt :caseInfo.getRequestResultInt().entrySet()) {
//            if (mapRequestResultInt.getValue().length() > 0) {
//                if (jsonPath.getInt(mapRequestResultInt.getKey()) == Integer.parseInt(mapRequestResultInt.getValue())) {
//                    VerificationInt = true;
//                }else {
//                    expectedResults = mapRequestResultInt.getKey() + "==" + Integer.parseInt(mapRequestResultInt.getValue()) ;
//                    actualResults = mapRequestResultInt.getKey() + "==" + jsonPath.getInt(mapRequestResultInt.getKey());
//                    VerificationInt = false;
//                    break;
//                }
//            }
//
//        }
//
//
//        if (requestBody.length() > 0) { //post请求，body参数
//
//            if (VerificationStr && VerificationInt) {
//
//                test.log(LogStatus.PASS, "请求：" + requestUri + ":" + port + requestAddress);
//                test.log(LogStatus.PASS, "body: " + requestBody);
//                test.log(LogStatus.PASS, "响应状态码：" + responseCode);
//                test.log(LogStatus.PASS, "响应结果：" + responseData);
//            }else {
//
//                test.log(LogStatus.PASS, "请求：" + requestUri + ":" + port + requestAddress);
//                test.log(LogStatus.PASS, "body: " + requestBody);
//
//                if (responseCode == 200) {
//                    test.log(LogStatus.PASS, "响应状态码：" + responseCode);
//                }else {
//                    test.log(LogStatus.FAIL, "响应状态码：" + responseCode);
//                }
//
//                test.log(LogStatus.FAIL, "响应结果：" + responseData);
//                test.log(LogStatus.FAIL, "预期结果：" + expectedResults);
//                test.log(LogStatus.FAIL,"实际结果：" + actualResults);
//            }
//
//        }else {
//
//            if (VerificationStr && VerificationInt) {
//
//                test.log(LogStatus.PASS, "请求：" + requestUri + ":" + port + requestAddress + requestParameter);
//                test.log(LogStatus.PASS,"响应状态码：" + responseCode);
//                test.log(LogStatus.PASS,"响应结果：" + responseData);
//            }else {
//
//                test.log(LogStatus.PASS, "请求：" + requestUri + ":" + port + requestAddress + requestParameter);
//
//                if (responseCode == 200) {
//                    test.log(LogStatus.PASS, "响应状态码：" + responseCode);
//                }else {
//                    test.log(LogStatus.FAIL, "响应状态码：" + responseCode);
//                }
//
//                test.log(LogStatus.FAIL, "响应结果：" + responseData);
//                test.log(LogStatus.FAIL, "预期结果：" + expectedResults);
//                test.log(LogStatus.FAIL, "实际结果：" + actualResults);
//            }
//        }
//
//    }
//
//    @DataProvider
//    public Object[][] dp() {
//        Object[][] myObj = null;
//        String filepath = "D:\\ME\\接口自动化\\接口模板\\接口1.xls";
//        BaseExcel baseExcel = new BaseExcel();
//        CaseHelper caseHelper = new CaseHelper();
//        List<Map<String,String>> list = baseExcel.readExcelList(filepath, 5); //读取Excel数据
//        myObj = caseHelper.getObjArrByList(list); //转化为Object[][]类型
//        return myObj;
//    }
//}
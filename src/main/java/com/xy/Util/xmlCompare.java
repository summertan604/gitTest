package com.xy.Util;

import com.google.common.collect.Sets;
import org.dom4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @Author: TANSHUFANG
 * @Description:
 * @CreateDate: 2018/12/21 14:36
 */
public class xmlCompare {
    private Logger logger=LoggerFactory.getLogger(xmlCompare.class);
    String tag="0";
    String inTest="";
    String inFormal="";

    public Map<String,String>  xmlCompare(String testXmlString,String formalXmlString){
//        oldXmlString ="<?xml version=\"1.0\" encoding=\"UTF-8\"?><QueryPubInfoRespose><rstCode>0</rstCode><rstInfo>ok</rstInfo><info pubId=\"1000003436\" rid=\"6800143\"><pubId>1000003436</pubId><pubName>小米</pubName><pubType>企业服务-品牌服务;</pubType><pubTypeCode>018006</pubTypeCode><versionCode>null</versionCode><rectLogoName>11b8f8ad9cb50288f1992b5981716166.jpg</rectLogoName><circleLogoName>ca3b237a3ffc5d08021853ba118b60e1.png</circleLogoName><backColor>#ff8400</backColor><backColorEnd>#ff6300</backColorEnd><scale>2</scale><corpLevel>0</corpLevel></info><pubNumList pubId=\"1000003436\" rid=\"6800143\"><pubNum><num>10698000009931</num><qnum>10698000009931</qnum><areaCode>CN;</areaCode><menuPriority>1</menuPriority><type>1</type></pubNum></pubNumList></QueryPubInfoRespose>";
        Map<String,String> testMap=stringToMap(testXmlString);
        Map<String,String> formalMap=stringToMap(formalXmlString);
        whichNotMatch(testMap,formalMap);
        Map<String,String> result=new HashMap<>();
        result.put("tag",tag);
        result.put("inTest",inTest);
        result.put("inFormal",inFormal);
        return result;
//        if(testMap.equals(formalMap)){
//            System.out.println("true");
//            return "true";
//        }else{
//            System.out.println("false");
//            return "false";
//        }
    }
    public void whichNotMatch(Map testMap,Map formalMap ){
        Set<String> testMapKey = testMap.keySet();
        Set<String> formalMapKey = formalMap.keySet();
        Set<String> differenceSet = Sets.difference(testMapKey, formalMapKey);//以testMapKey为标准，取与formalMapKey中不同的
        Set<String> differenceSet2 = Sets.difference(formalMapKey, testMapKey);//以formalMapKey为标准，取与testMapKey中不同的
        Set<String> differenceKey = new HashSet<String>();
        System.out.println(differenceSet);
        System.out.println(differenceSet2);

        differenceKey.addAll(differenceSet);
        differenceKey.removeAll(differenceSet2);
        differenceKey.addAll(differenceSet2); //把differenceSet、differenceSet2合并

        System.out.println(differenceKey);
        Set<String> commonKey = new HashSet<String>();
        commonKey.addAll(testMapKey);
        commonKey.retainAll(formalMapKey);
        System.out.println("交集：" + commonKey);
       //循环共有的key，判断value是否一致
        for(String key:commonKey){
            if(!testMap.get(key).equals(formalMap.get(key))){
                logger.debug(key+"值不相同");
            }
        }

        //循环差异的key，给出提示
        if(differenceKey.size()>0){
            inTest="测试存在，断言不存在的节点有：";
            inFormal="断言存在，测试不存在的节点有：";
        }
        for(String key:differenceKey){
            if(testMap.containsKey(key) && !formalMap.containsKey(key) ){
                tag="1";
                inTest=inTest+key+",";
            }else if(!testMap.containsKey(key) && formalMap.containsKey(key) ){
                inFormal=inFormal+key+",";
                tag="1";
            }

        }
        logger.debug(inTest);
        logger.debug(inFormal);
    }

    public Map<String, String> stringToMap(String xmlString){
        List<Map<String, String>> resultList = iterateWholeXML(xmlString);
        System.out.println("resultList" + resultList);
        Map<String, String> retMap = new HashMap<String, String>();
        for (int i = 0; i < resultList.size(); i++) {
            Map map = (Map) resultList.get(i);

            for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                retMap.put(key, (String) map.get(key));

            }
        }
        System.out.println("=============================================================" );
        System.out.println("转成reMap" + retMap);
        System.out.println("=============================================================" );
        return retMap;
    }

    public static List<Map<String, String>> iterateWholeXML(String xmlStr) {

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        try {
            Document document = DocumentHelper.parseText(xmlStr);
            Element root = document.getRootElement();
            recursiveNode(root, list);
            return list;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static void recursiveNode(Element root, List<Map<String, String>> list) {

        // 遍历根结点的所有孩子节点
        HashMap<String, String> map = new HashMap<String, String>();
        for (Iterator iter = root.elementIterator(); iter.hasNext();) {
            Element element = (Element) iter.next();
            if (element == null)
                continue;
            // 获取属性和它的值
            for (Iterator attrs = element.attributeIterator(); attrs.hasNext();) {
                Attribute attr = (Attribute) attrs.next();
                System.out.println(attr);
                if (attr == null)
                    continue;
                String attrName = attr.getName();
                System.out.println("attrname" + attrName);
                String attrValue = attr.getValue();

                map.put(attrName+attrValue, attrValue);
            }
            // 如果有PCDATA，则直接提出
            if (element.isTextOnly()) {
                String innerName = element.getName();
                String innerValue = element.getText();

                map.put(innerName+innerValue, innerValue);
                list.add(map);
            } else {
                // 递归调用
                recursiveNode(element, list);
            }
        }
    }
}

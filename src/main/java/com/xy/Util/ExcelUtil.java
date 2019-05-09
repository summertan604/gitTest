package com.xy.Util;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: TANSHUFANG
 * @Description:处理excel表格
 * @CreateDate: 2018/9/10 14:31
 */
public class ExcelUtil {
    public Workbook workbook;
    public Sheet sheet;
    public Cell cell;
    int rows;
    int columns;
    public String fileName;
    public String caseName;
    public ArrayList<String> arrkey = new ArrayList<String>();
    String sourceFile;

    /**
     * @param fileName excel文件名
     * @param caseName sheet名
     */
    public ExcelUtil(String fileName, String caseName) {
        super();
        this.fileName = fileName;
        this.caseName = caseName;
    }

    public List<Map<String,String>>  readExcelData() throws BiffException,IOException{
        List<Map<String,String>> listMap=new ArrayList<Map<String,String>>();
        WorkbookSettings workbookSettings = new WorkbookSettings();
//       可以设置为UTF-8或者GBK或者ISO-8859-1
        workbookSettings.setEncoding("UTF-8");
        workbook=Workbook.getWorkbook(new File(getPath()),workbookSettings);
        sheet=workbook.getSheet(caseName);
        rows=sheet.getRows();
        columns=sheet.getColumns();
        if(rows>1){
            for(int i=0;i<columns;i++){
                arrkey.add(sheet.getCell(i,0).getContents());  //把列名存入arrkey
            }
        }
        for(int r=1;r<rows;r++){
            Map<String,String> map=new HashMap<>();
            for(int c=0;c<columns;c++){
                map.put(arrkey.get(c),sheet.getCell(c,r).getContents());
            }
            listMap.add(map);
        }
        return  listMap;
    }

    /**
     * 获得excel表中的数据
     */
    public Object[][] getExcelData() throws BiffException, IOException {

        workbook = Workbook.getWorkbook(new File(getPath()));
        sheet = workbook.getSheet(caseName);
        rows = sheet.getRows();
        columns = sheet.getColumns();
        // 为了返回值是Object[][],定义一个多行单列的二维数组
        HashMap<String, String>[][] arrmap = new HashMap[rows - 1][1];
        // 对数组中所有元素hashmap进行初始化
        if (rows > 1) {
            for (int i = 0; i < rows - 1; i++) {
                arrmap[i][0] = new HashMap<String, String>();
            }
        } else {
            System.out.println("excel中没有数据");
        }

        // 获得首行的列名，作为hashmap的key值
        for (int c = 0; c < columns; c++) {
            String cellvalue = sheet.getCell(c, 0).getContents();
            arrkey.add(cellvalue);
        }
        // 遍历所有的单元格的值添加到hashmap中
        for (int r = 1; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                String cellvalue = sheet.getCell(c, r).getContents();
                arrmap[r - 1][0].put(arrkey.get(c), cellvalue);
            }
        }
        return arrmap;
    }

    /**
     * 获得excel文件的路径
     *
     * @return
     * @throws IOException
     */
    public String getPath() throws IOException {
        File directory = new File(".");
        sourceFile = directory.getCanonicalPath() + "\\src\\main\\resources\\"
                + fileName + ".xls";
        System.out.println("=========="+directory.getCanonicalPath());
        return sourceFile;
    }
}

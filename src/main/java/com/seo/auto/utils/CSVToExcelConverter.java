package com.seo.auto.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVToExcelConverter {

    public static void main(String[] args) throws IOException {
        String fName="C:\\Users\\pc\\IdeaProjects\\atmp-bpij\\sortie\\CompteEmployeurCourant\\10-12-2019-13H-29M-36s\\Compte-Consolide--10-12-2019-13H-29M-36s.csv";
        convertCSVToXLSRaw(fName,fName.replace("csv","xls"),";",false);
    }

    public static void convertCSVToXLSRaw(String fName,String outputFname,String sep, boolean b) throws IOException
    {
        ArrayList arList=null;
        ArrayList al=null;
        String thisLine;
        int count=0;
        FileInputStream fis = new FileInputStream(fName);
        DataInputStream myInput = new DataInputStream(fis);
        int i=0;
        arList = new ArrayList();
        while ((thisLine = myInput.readLine()) != null)
        {
            al = new ArrayList();
            String strar[] = thisLine.split(sep);
            for(int j=0;j<strar.length;j++)
            {
                al.add(strar[j]);
            }
            arList.add(al);
            System.out.println();
            i++;
        }

        try
        {
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("Feuille 1");
            for(int k=0;k<arList.size();k++)
            {
                ArrayList ardata = (ArrayList)arList.get(k);
                HSSFRow row = sheet.createRow((short) 0+k);
                for(int p=0;p<ardata.size();p++)
                {
                    HSSFCell cell = row.createCell((short) p);
                    String data = ardata.get(p).toString();
                    if(data.startsWith("=")){
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        data=data.replaceAll("\"", "");
                        data=data.replaceAll("=", "");
                        cell.setCellValue(data);
                    }else if(data.startsWith("\"")){
                        data=data.replaceAll("\"", "");
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellValue(data);
                    }else{
                        data=data.replaceAll("\"", "");
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(data);
                    }
                    //*/
                    // cell.setCellValue(ardata.get(p).toString());
                }
                System.out.println();
            }
            FileOutputStream fileOut = new FileOutputStream(outputFname);
            hwb.write(fileOut);
            fileOut.close();
            //System.out.println("Your excel file has been generated");
        } catch ( Exception ex ) {
            ex.printStackTrace();
        } //main method ends
    }
    static void CSVtoXLS(String CSVFile, String XLSFile, String sep, boolean isVueFiltree) throws IOException {
        ArrayList arList;
        ArrayList al;
        String thisLine;
        arList = new ArrayList();

        while ((thisLine = new DataInputStream(new FileInputStream(CSVFile)).readLine()) != null) {
            al = new ArrayList();
            String[] strar = thisLine.split(sep);
            al.addAll(Arrays.asList(strar));
            arList.add(al);
        }

        try {
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("feuille 1");

            for (int k = 0; k < arList.size(); k++) {
                ArrayList ardata = (ArrayList) arList.get(k);

                for (int p = 0; p < ardata.size(); p++) {
                    HSSFCell cell = sheet.createRow(k).createCell((short) p);
                    String data = ardata.get(p).toString();
                    if (data.startsWith("=")) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        data = data.replaceAll("\"", "");
                        data = data.replaceAll("=", "");
                        cell.setCellValue(data);
                    } else if (data.startsWith("\"")) {
                        data = data.replaceAll("\"", "");
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellValue(data);
                    } else {
                        data = data.replaceAll("\"", "");
                        if (isCorrectColumn(p, isVueFiltree, data) && k > 0 && data.length() > 0 && isLong(data)) {
                            cell.setCellValue(Long.parseLong(data));
                        } else {
                            cell.setCellValue(data);
                        }
                    }
                }
            }
            FileOutputStream fileOut = new FileOutputStream(XLSFile);
            hwb.write(fileOut);
            fileOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static boolean isLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isCorrectColumn(int col, boolean isVueFiltree, String data) {
        if (isVueFiltree) {
            if (data.contains("B") | data.contains("DCD")) {
                return false;
            }

            int[] tab = {7, 13, 14, 15, 17, 18, 19, 23};
            int decalage = 0;
            for (int p = 0; p < tab.length; p++) {
                if (p > 0 && isVueFiltree) {
                    decalage = 5;
                }

                if ((decalage + tab[p]) == col) {
                    return true;
                }
            }

            return false;
        } else {
            if (data.contains("B") | data.contains("DCD")) {
                return false;
            }

            int[] tab = {8, 14, 15, 16, 18, 19, 20, 24};
            int decalage = 0;
            for (int i : tab) {
                if ((decalage + i) == col) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    private static boolean isInteger(String s, int radix) {
        if (s.isEmpty()) {
            return false;
        }

        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) {
                    return false;
                } else {
                    continue;
                }
            }

            if (Character.digit(s.charAt(i), radix) < 0) {
                return false;
            }
        }

        return true;
    }
}
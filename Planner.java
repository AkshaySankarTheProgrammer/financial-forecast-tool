package com.example.financeplanner;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import java.awt.Color;

import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

public class Planner
{
    private final String inputFileSheet = "Templates(1).xlsx";
    private final String normalTemplateSheetName = "NormalTemplates";
    private final String recurringTemplateSheetName = "RecurringTemplates";
    private final String generatedFileName = "ExpensesSheet.xlsx";
    private final String finalSheetName = "Expenses";

    private List<NormalTemplate> normalTemplates = new ArrayList<>();
    private List<RecurrenceTemplate> recurringTemplates = new ArrayList<>();

    Map<String, List<NormalTemplate>> templates = new HashMap<>();
    Map<String, NormalTemplate> detailsNormal = new HashMap<>();
    Map<String, RecurrenceTemplate> detailsRecurring = new HashMap<>();

    public void initFinal()
    {
        createOutputFile();
    }

    public void createOutputFile()
    {
        try
        {
            Workbook workbook = new XSSFWorkbook();
            FileOutputStream fos = new FileOutputStream(generatedFileName);

            workbook.createSheet(finalSheetName);

            workbook.write(fos);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void loadNormalSheet(String fileName, String sheetName)
    {
        try
        {
            FileInputStream fis = new FileInputStream(fileName);
            Workbook workbook = new XSSFWorkbook(fis);

            Sheet sheet = workbook.getSheet(sheetName);

            if(sheet != null)
            {

                int lastRowNum = sheet.getLastRowNum();

                for (int row = 1; row <= lastRowNum; row++) {
                    String name = sheet.getRow(row).getCell(0).getStringCellValue();
                    String category = sheet.getRow(row).getCell(1).getStringCellValue();
                    double amount = sheet.getRow(row).getCell(2).getNumericCellValue();
                    Date date = sheet.getRow(row).getCell(3).getDateCellValue();
                    boolean isIncome = sheet.getRow(row).getCell(4).getBooleanCellValue();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String formatted = sdf.format(date);

                    NormalTemplate obj = new NormalTemplate(name, category, amount, formatted, isIncome);
                    normalTemplates.add(obj);
                    detailsNormal.put(obj.getName(), obj);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void loadRecurringSheet(String fileName, String sheetName)
    {
        try
        {
            FileInputStream fis = new FileInputStream(fileName);
            Workbook workbook = new XSSFWorkbook(fis);

            Sheet sheet = workbook.getSheet(sheetName);
            if(sheet != null)
            {
                int lastRowNum = sheet.getLastRowNum();

                for (int row = 1; row <= lastRowNum; row++)
                {
                    String name = sheet.getRow(row).getCell(0).getStringCellValue();
                    String category = sheet.getRow(row).getCell(1).getStringCellValue();
                    String recurrence = sheet.getRow(row).getCell(2).getStringCellValue();
                    double amount = sheet.getRow(row).getCell(3).getNumericCellValue();
                    Date date = sheet.getRow(row).getCell(4).getDateCellValue();
                    boolean isIncome = sheet.getRow(row).getCell(5).getBooleanCellValue();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String formatted = sdf.format(date);

                    RecurrenceTemplate obj = new RecurrenceTemplate(name, category, recurrence, amount, formatted, isIncome);
                    recurringTemplates.add(obj);
                    detailsRecurring.put(obj.getName(), obj);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    void mapLoader()
    {
        for(NormalTemplate obj: normalTemplates)
        {
            String cat = obj.getCategory();

            if(templates.containsKey(cat))
            {
                List<NormalTemplate> temp = templates.get(cat);
                temp.add(obj);
                templates.put(cat, temp);
            }
            else
            {
                List<NormalTemplate> temp = new ArrayList<>();
                temp.add(obj);
                templates.put(cat, temp);
            }
        }

        for(RecurrenceTemplate obj: recurringTemplates)
        {
            String cat = obj.getCategory();

            if(templates.containsKey(cat))
            {
                List<NormalTemplate> temp = templates.get(cat);
                temp.add(obj);
                templates.put(cat, temp);
            }
            else
            {
                List<NormalTemplate> temp = new ArrayList<>();
                temp.add(obj);
                templates.put(cat, temp);
            }
        }

        // System.out.println(templates);
    }

    void generator(double balance, String outputFileName, String outputSheetName)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(outputFileName);
            Workbook wb = new XSSFWorkbook();

            Sheet sheet = wb.createSheet(outputSheetName);

            Font boldFont = wb.createFont();
            boldFont.setBold(true);

            // Create a cell style and assign the bold font to it
            CellStyle boldStyle = wb.createCellStyle();
            boldStyle.setFont(boldFont);

            int rowIndex = 0;
            sheet.createRow(rowIndex++);
            Row row1 = sheet.createRow(rowIndex++);

            row1.createCell(1).setCellValue("Description");
            row1.getCell(1).setCellStyle(boldStyle);

            Row row2 = sheet.createRow(rowIndex++);
            row2.createCell(1).setCellValue("Balance At Start");
            row2.getCell(1).setCellStyle(boldStyle);

            for(String category: templates.keySet())
            {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(1).setCellValue(category);
                row.getCell(1).setCellStyle(boldStyle);

                List<NormalTemplate> temps = templates.get(category);

                for(NormalTemplate obj: temps)
                {
                    Row r = sheet.createRow(rowIndex++);
                    r.createCell(1).setCellValue(obj.getName());
                }
            }

            sheet.createFreezePane(0,2);

            sheet.autoSizeColumn(1);
            YearMonth current = YearMonth.now();


            XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();

            XSSFColor greenColor = new XSSFColor(new Color(204, 255, 204), new DefaultIndexedColorMap());
            style.setFillForegroundColor(greenColor);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Works till here

            int colIndex = 2;
            row2.createCell(2).setCellValue(balance);

            for(int i = 0; i < 6; i++)
            {
                int weekNumber = 1;
                YearMonth next = current.plusMonths(i);
                String month = next.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                int year = next.getYear();

                int start = colIndex;
                int end = colIndex + 3;

                CellRangeAddress mergedRegion = new CellRangeAddress(0, 0, start, end);
                sheet.addMergedRegion(mergedRegion);

                Row row = sheet.getRow(0);
                Cell cell = row.createCell(colIndex);
                cell.setCellValue(month + " " + year);

                Cell cell1 = row1.createCell((colIndex));
                cell1.setCellValue("Week 1");
                Cell cell2 = row1.createCell((colIndex+1));
                cell2.setCellValue("Week 2");
                Cell cell3 = row1.createCell((colIndex+2));
                cell3.setCellValue("Week 3");
                Cell cell4 = row1.createCell((colIndex+3));
                cell4.setCellValue("Month End");
                sheet.autoSizeColumn(colIndex+3);

                CellStyle styles = wb.createCellStyle();
                styles.setAlignment(HorizontalAlignment.CENTER);
                styles.setVerticalAlignment(VerticalAlignment.CENTER);
                styles.setFont(boldFont);
                cell.setCellStyle(styles);

                int lastRowNum = sheet.getLastRowNum();

                for(int j = start; j <= end; j++)
                {
                    double weekSpend;

                    rowIndex = j;

                    row2.createCell(j).setCellValue(balance);
                    for(int k = 3; k <= lastRowNum; k++)
                    {
                        weekSpend = 0;


                        Row x = sheet.getRow(k);
                        Cell c = x.getCell(1);
                        String nameOfTransaction = c.getStringCellValue();

                        NormalTemplate temp1 = detailsNormal.get(nameOfTransaction);
                        RecurrenceTemplate temp2 = detailsRecurring.get(nameOfTransaction);

                        if(temp1 == null && temp2 == null)
                            continue;
                        else if(temp2 == null)
                        {
                            String m = temp1.getMonth();
                            if(m.equalsIgnoreCase(month))
                            {
                                double amount = temp1.getAmount();
                                int day = temp1.getDay();

                                if(weekNumber == 1)
                                {
                                    if(day <= 7)
                                    {
                                        weekSpend += amount;
                                        if(amount < 0)
                                        {
                                            amount *= -1;
                                            Cell y = x.createCell(j);
                                            y.setCellValue(amount);
                                            y.setCellStyle(style);
                                        }
                                        else
                                        {
                                            Cell y = x.createCell(j);
                                            y.setCellValue(amount);
                                        }
                                    }
                                }
                                else if(weekNumber == 2)
                                {
                                    if(day > 7 && day <= 14)
                                    {
                                        weekSpend += amount;
                                        if(amount < 0)
                                        {
                                            amount *= -1;
                                            Cell y = x.createCell(j);
                                            y.setCellValue(amount);
                                            y.setCellStyle(style);
                                        }
                                        else
                                        {
                                            Cell y = x.createCell(j);
                                            y.setCellValue(amount);
                                        }
                                    }
                                }
                                else if(weekNumber == 3)
                                {
                                    if(day > 14 && day <= 21)
                                    {
                                        weekSpend += amount;
                                        if(amount < 0)
                                        {
                                            amount *= -1;
                                            Cell y = x.createCell(j);
                                            y.setCellValue(amount);
                                            y.setCellStyle(style);
                                        }
                                        else
                                        {
                                            Cell y = x.createCell(j);
                                            y.setCellValue(amount);
                                        }
                                    }
                                }
                                else
                                {
                                    if(day > 21)
                                    {
                                        weekSpend += amount;
                                        if(amount < 0)
                                        {
                                            amount *= -1;
                                            Cell y = x.createCell(j);
                                            y.setCellValue(amount);
                                            y.setCellStyle(style);
                                        }
                                        else
                                        {
                                            Cell y = x.createCell(j);
                                            y.setCellValue(amount);
                                        }
                                    }
                                }
                            }
                        }
                        else if(temp1 == null)
                        {
                            String m = temp2.getMonth();
                            double amount = temp2.getAmount();
                            String months = temp2.getMonths();
                            // System.out.println(temp2.getName() + " " + months);
                            if(months == null) months = "";

                            if(temp2.isDaily())
                            {
                                amount *= 7;
                                weekSpend += amount;
                                if(amount < 0)
                                {
                                    amount *= -1;
                                    Cell y = x.createCell(j);
                                    y.setCellValue(amount);
                                    y.setCellStyle(style);
                                }
                                else
                                {
                                    Cell y = x.createCell(j);
                                    y.setCellValue(amount);
                                }
                            }
                            else if(temp2.isWeekly())
                            {
                                weekSpend += amount;
                                if(amount < 0)
                                {
                                    amount *= -1;
                                    Cell y = x.createCell(j);
                                    y.setCellValue(amount);
                                    y.setCellStyle(style);
                                }
                                else
                                {
                                    Cell y = x.createCell(j);
                                    y.setCellValue(amount);
                                }
                            }
                            else if(m.equalsIgnoreCase(month) || months.contains(month.toUpperCase()))
                            {
                                int day = temp2.getDay();
                                if(weekNumber == 1)
                                {
                                    if(day <= 7)
                                    {
                                        weekSpend += amount;
                                        if(amount < 0)
                                        {
                                            amount *= -1;
                                            Cell y = x.createCell(j);
                                            y.setCellValue(amount);
                                            y.setCellStyle(style);
                                        }
                                        else
                                        {
                                            Cell y = x.createCell(j);
                                            y.setCellValue(amount);
                                        }
                                    }
                                }
                                else if(weekNumber == 2)
                                {
                                    if(day > 7 && day <= 14)
                                    {
                                        weekSpend += amount;
                                        if(amount < 0)
                                        {
                                            amount *= -1;
                                            Cell y = x.createCell(j);
                                            y.setCellValue(amount);
                                            y.setCellStyle(style);
                                        }
                                        else
                                        {
                                            Cell y = x.createCell(j);
                                            y.setCellValue(amount);
                                        }
                                    }
                                }
                                else if(weekNumber == 3)
                                {
                                    if(day > 14 && day <= 21)
                                    {
                                        weekSpend += amount;
                                        if(amount < 0)
                                        {
                                            amount *= -1;
                                            Cell y = x.createCell(j);
                                            y.setCellValue(amount);
                                            y.setCellStyle(style);
                                        }
                                        else
                                        {
                                            Cell y = x.createCell(j);
                                            y.setCellValue(amount);
                                        }
                                    }
                                }
                                else
                                {
                                    if(day > 21)
                                    {
                                        weekSpend += amount;
                                        if(amount < 0)
                                        {
                                            amount *= -1;
                                            Cell y = x.createCell(j);
                                            y.setCellValue(amount);
                                            y.setCellStyle(style);
                                        }
                                        else
                                        {
                                            Cell y = x.createCell(j);
                                            y.setCellValue(amount);
                                        }
                                    }
                                }
                            }
                        }

                        balance -= weekSpend;
                    }
                    weekNumber++;
                }

                colIndex += 4;
            }

            wb.write(fos);

            wb.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
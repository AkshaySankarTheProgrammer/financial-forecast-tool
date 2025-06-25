package com.example.financeplanner;

public class Tester
{
    public static void main(String[] args) //RUN THIS METHOD
    {
        new Tester().finalTestCase();
    }

    public void finalTestCase()
    {
        Planner obj = new Planner();

        obj.initFinal();
        obj.createOutputFile();

        obj.loadNormalSheet("Templates.xlsx", "NormalTemplates"); //CREATE FILENAME AND SHEET NAME AS MENTIONED HERE
        obj.loadRecurringSheet("Templates.xlsx", "RecurringTemplates"); //CREATE FILENAME AND SHEET NAME AS MENTIONED HERE

        obj.mapLoader();

        obj.generator(10000.00, "Expenses.xlsx", "Expenses");//ENTER OUTPUT FILENAME AND SHEET NAME AS WELL AS STARTING BALANCE HERE
    }
}

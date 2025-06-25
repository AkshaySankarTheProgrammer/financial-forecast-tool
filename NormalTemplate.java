package com.example.financeplanner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NormalTemplate
{
    private String name;
    private String category;
    private double amount;
    private LocalDate date;
    private int year;
    private String month;
    private int day;
    private boolean isIncome;

    NormalTemplate()
    {

    }

    NormalTemplate(String name, String category, double amt, String date, boolean isIncome)
    {
        this.name =name;
        this.category = category;
        this.isIncome = isIncome;
        if(isIncome)
            this.amount = -1 * amt;
        else
            this.amount = amt;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.date = LocalDate.parse(date, formatter);
        this.month = (this.date).getMonth().toString();
        this.day = (this.date).getDayOfMonth();
        this.year = (this.date).getYear();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public void setIncome(boolean income) {
        isIncome = income;
    }
}
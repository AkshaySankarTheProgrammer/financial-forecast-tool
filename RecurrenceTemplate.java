package com.example.financeplanner;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class RecurrenceTemplate extends NormalTemplate
{
    private String recurrence;
    private String months;
    private boolean isDaily;
    private boolean isWeekly;

    RecurrenceTemplate(String name, String category, String rec, double amt, String date, boolean isIncome)
    {
        super(name, category, amt, date, isIncome);
        this.recurrence = rec;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate d = LocalDate.parse(date, formatter);
        String month = d.getMonth().toString();

        if (month.length() > 3)
        {
            formatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("MMMM")
                    .toFormatter(Locale.ENGLISH);
        }
        else
        {
            formatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("MMM")
                    .toFormatter(Locale.ENGLISH);
        }
        Month m = Month.from(formatter.parse(month));

        int monthNumber = m.getValue();

        if(rec.equalsIgnoreCase("Monthly"))
        {
            months = "JANUARYFEBRUARYMARCHAPRILMAYJUNEJULYAUGUSTSEPTEMBEROCTOBERNOVEMBERDECEMBER";
        }
        else if(rec.equalsIgnoreCase("BiMonthly"))
        {
            if(monthNumber % 2 == 0)
                months = "FEBRUARYAPRILJUNEAUGUSTOCTOBERDECEMBER";
            else
                months = "JANUARYMARCHMAYJULYSEPTEMBERNOVEMBER";
        }
        else if(rec.equalsIgnoreCase("Quarterly"))
        {
            if(monthNumber % 3 == 0)
                months = "MARCHJUNESEPTEMBERDECEMBER";
            else if(monthNumber % 3 == 1)
                months = "JANUARYAPRILJULYOCTOBER";
            else
                months = "FEBRUARYMAYAUGUSTNOVEMBER";
        }
        else if(rec.equalsIgnoreCase("Yearly"))
        {
            months = month;
        }
        else if(rec.equalsIgnoreCase("Daily"))
        {
            isDaily = true;
        }
        else if(rec.equalsIgnoreCase("Weekly"))
        {
            isWeekly = true;
        }
    }

    public String getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }

    public String getMonths() {
        return months;
    }

    public void setMonths(String months) {
        this.months = months;
    }

    public boolean isDaily() {
        return isDaily;
    }

    public void setDaily(boolean daily) {
        isDaily = daily;
    }

    public boolean isWeekly() {
        return isWeekly;
    }

    public void setWeekly(boolean weekly) {
        isWeekly = weekly;
    }
}

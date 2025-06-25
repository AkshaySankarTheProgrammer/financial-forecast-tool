# financial-forecast-tool
This code accepts formatted input which consists of normal and recurring transaction templates and provides an excel sheet sheet containing a completely formatted and calculated output that conveys when a user's transactions are made as well as the weekly breakdown of costs and expenditure for the next 6 months.
---

# Important Instructions
The input file must be in the same folder as the folder than runs the program. The input file must be named "Templates.xlsx", and it should have two sheets in it. The first sheet must be named "NormalTemplates" and the second sheet must be named "RecurringTemplates". 

## Normal Template Sheet Format
The columns must be in the order Name -> Category -> Amount -> Date -> isIncome
Name -> Name of the transaction (Upto you)
Category -> What you want to group similar transactions by (Upto you)
Amount -> Expenditure or Income on the transaction (Upto you)
Date -> In the format dd-mm-yyyy (Must use this format)
isIncome -> TRUE or FALSE based on if the value is income or expenditure (Must use this format)

   ![image](https://github.com/user-attachments/assets/04ca20e6-5869-4a53-89da-4c55efd4c84f)

## Recurring Template Sheet Format
The columns must be in the order Name -> Category -> Recurrence -> Amount -> Date -> isIncome
Name -> Name of the transaction (Upto you)
Category -> What you want to group similar transactions by (Upto you)
Recurrence -> Can be Daily, Weekly, Monthly, BiMonthly, Quarterly and Yearly (Must use this format)
Amount -> Expenditure or Income on the transaction (Upto you)
Date -> In the format dd-mm-yyyy (Must use this format)
isIncome -> TRUE or FALSE based on if the value is income or expenditure (Must use this format)

![image](https://github.com/user-attachments/assets/7101dc9e-f0c3-4263-a581-e4e1dd27e0a5)

---

# Troubleshooting
a) Make sure the filename and sheetname are exactly as mentioned.
b) Make sure that the transaction templates you upload are exactly as expected to behave. Do not upload a monthly transaction and expect it to behave like a bimonthly transaction.
c) Verify that the titles in your sheet as well as the values in those columns match the requirements here.

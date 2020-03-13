|----------------|
|ProgramCondenser|
|----------------|

///////////\\\\\\\\\\
|By Addison Goolsbee|
\\\\\\\\\\\//////////

What it Does
------------
The ProgramCondenser condenses a .in file so that all spaces, empty lines and comments are removed, and then it creates a .out file in the same place with the same name that is the condensed version

How to Set it Up
----------------
Here are the steps to run ProgramCondenser:

1. Unzip the ProgramCondenser zip file by double-clicking on it.

2.Open the application terminal

3.Navigate into the src folder in ProgramCondenser. Assuming you placed the ProgramCondenser folder on your desktop, your terminal inputs should read:
	a. cd Desktop (press enter)
	b. cd ProgramCondenser
	c. cd src

4. Type  java programcondenser.Condenser  into terminal, and press enter. The next line should read "Enter file name"

5. Find your .in file and drag it into the terminal. It's full name should appear. Press enter.

At this point, a .out document should appear next to your .in file, and this document is the condensed version of the .in file

If you have an error in the terminal that reads:  Exception in thread "main" java.io.FileNotFoundException: [full .in file name]  (No such file or directory),   then wait a minute or two and repeat steps 4 and 5. (It worked for me)

Conclusion
----------
Yay
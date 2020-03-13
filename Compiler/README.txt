|----------------|
|Partial Compiler|
|----------------|

\\\\\\\\\\\\\\\\\\\\\
\By Addison Goolsbee\
\\\\\\\\\\\\\\\\\\\\\

What it Does
Partial Compiler takes in a file containing .jack files which contain high level coding language, and outputs a .xml file within that folder containing that jack code translated into a simplified form, where each symbol, variable name, constant, and keyword is broken down and then categorized further by marking groups of them as within things like if-statements.

How to Set it Up
----------------
1. Unzip the Compiler zip file by double-clicking on it. Make sure it is on your desktop

2. Open the application Terminal

3. In Terminal, navigate to the src Assembler file by typing:
	a. cd Desktop 
	b. cd Compiler
	c. cd src

4. Run the VM Translator in Terminal by typing:
	java compiler.Compiler

5. The next line should read:
	Enter file name
Find your .jack file in finder and drag it into Terminal. This will put the full file name into Terminal. Make sure there are no spaces before or after the file name. It should start with /Users/ and end with .jack

6. A .xml file should now appear in your folder. This is the translated file.

Troubleshooting
---------------
If you have the error 
Exception in thread "main" java.io.FileNotFoundException: ... (No such file or directory)
after entering the .vm file name, then there is likely a space before or after the file name.
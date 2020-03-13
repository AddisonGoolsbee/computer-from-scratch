|---------|
|Assembler|
|---------|

\\\\\\\\\\\\\\\\\\\\\
\By Addison Goolsbee\
\\\\\\\\\\\\\\\\\\\\\

What it Does
------------
Assembler takes in a .asm file containing hack assembly language, and outputs a .hack file containing the binary form of the hack assembly language.

How to Set it Up
----------------
1. Unzip the Assembler zip file by double-clicking on it. Make sure it is on your desktop

2. Open the application terminal

3. In terminal, navigate to the src Assembler file by typing:
	a. cd Desktop 
	b. cd Assembler
	c. cd src

4. Run the assembler in terminal by typing:
	java assembler.Assembler

5. The next line should read:
	Enter file name
Find your .asm file in finder and drag it into terminal. This will put the full file name into terminal. Make sure there are no spaces before or after the file name. It should start with /Users/ and end with .asm

6. A .hack file should appear next to your .asm file. It is in binary.

Troubleshooting
---------------
If you have the error 
Exception in thread "main" java.io.FileNotFoundException: ... (No such file or directory)
after entering the .asm file name, then there is likely a space before or after the file name.
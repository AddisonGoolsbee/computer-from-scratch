|------------|
|VMTranslator|
|------------|

\\\\\\\\\\\\\\\\\\\\\
\By Addison Goolsbee\
\\\\\\\\\\\\\\\\\\\\\

What it Does
VMTranslator takes in a folder containing .vm files which contain virtual machine language, and outputs a .asm file within that folder containing that virtual machine language translated into hack assembly language.

How to Set it Up
----------------
1. Unzip the Assembler zip file by double-clicking on it. Make sure it is on your desktop

2. Open the application Terminal

3. In Terminal, navigate to the src Assembler file by typing:
	a. cd Desktop 
	b. cd VMTranslator
	c. cd src

4. Run the VM Translator in Terminal by typing:
	java vmtranslator.VMTranslator

5. The next line should read:
	Enter file name
Find your folder containing .vm files in finder and drag it into Terminal. This will put the full folder name into Terminal. Make sure there are no spaces before or after the file name. It should start with /Users/ and end with the name of the folder

6. A .asm file should now appear in your folder. This is the translated file.

Troubleshooting
---------------
If you have the error 
Exception in thread "main" java.io.FileNotFoundException: ... (No such file or directory)
after entering the .vm file name, then there is likely a space before or after the file name.
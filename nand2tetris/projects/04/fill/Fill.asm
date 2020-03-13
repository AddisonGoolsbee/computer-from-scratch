// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

//End of screen = KBD-1
//Start of screen = ?

	@SCREEN	//screen start address
	D=A
	@pointer
	M=D	//sets pointer to start of screen
	@KBD
	D=M
	@FORWARD
	D;JGT
	@BACKWARD
	D;JEQ

(FORWARD)	//when key pressed, make next set of pixels black
	@KBD
	D=A-1
	@pointer
	D=D-M
	@SKIPF
	D;JLE	//If at max screen location, don't blacken pixels
	@pointer
	M=M+1	//iterate pointer
	A=M
	M=-1	//turn next pixels black
(SKIPF)
	@KBD
	D=M
	@FORWARD
	D;JGT
	@BACKWARD
	D;JEQ


(BACKWARD)	//whey key not pressed, make previous set of pixels white
	@SCREEN
	D=A-1
	@pointer
	D=M-D
	@SKIPF
	D;JLE	//If at min screen location, don't whiten previous pixels
	@pointer
	A=M
	M=0	//turn previous pixels white
	@pointer
	M=M-1	//decrease pointer

(SKIPB)
	@KBD
	D=M
	@FORWARD
	D;JGT
	@BACKWARD
	D;JEQ
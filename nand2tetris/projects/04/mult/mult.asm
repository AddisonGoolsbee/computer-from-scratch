// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
	@R0
	D=M
	@counter
	M=D
	@sum
	M=0
(LOOP)
	@counter
	D=M
	@sum
	D=M
	@R2
	M=D
	@counter
	D=M
	@END
	D;JEQ
	D=D-1
	@counter
	M=D
	@R1
	D=M
	@sum
	D=M+D
	M=D
	@LOOP
	0;JMP
(END)
	@END
	0;JMP

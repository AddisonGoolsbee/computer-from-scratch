@SCREEN
D=A
@pointer
M=D


(reset)
@counter
M=0

@pointer
D=M
@KBD
D=A-D
@skip
D;JLE

(loopblack)
@pointer
A=M
M=-1
@pointer
M=M+1
@counter
M=M+1
D=M
@15
D=A-D
@loopblack
D;JGE

@counter
M=0

(loopwhite)
@pointer
M=M+1
@counter
M=M+1
D=M
@14
D=A-D
@loopwhite
D;JGE


(skip)
@reset
D;JMP

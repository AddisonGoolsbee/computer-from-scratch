D=A
@502
@pointer
M=D//sets pointer to start of screen
@KBD
D=M
(POOP)
@FORWARD

D;JGT
@BACKWARD
D;JEQ
(POAP)
@KBD
D=A-1
@pointer
D=D-M
@SKIPF
D;JLE//If at max screen location, don't blacken pixels
@pointer
M=M+1//iterate pointer
A=M
M=-1//turn next pixels black
@KBD
D=M
@FORWARD
D;JGT
@BACKWARD
D;JEQ
@SCREEN
D=A-1
@pointer
D=M-D
@SKIPF
D;JLE//If at min screen location, don't whiten previous pixels
@pointer
A=M
M=0//turn previous pixels white
@pointer
M=M-1//decrease pointer
@KBD
D=M
@FORWARD
D;JGT
@BACKWARD
D;JEQ
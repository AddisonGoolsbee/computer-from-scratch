// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/03/a/Register.tst

load Countdown16.hdl,
output-file Countdown16.out,
compare-to Countdown16.cmp,
output-list time%S1.4.1 in%D1.6.1 load%B2.1.2 out%D1.6.1;

set in 0,
set load 1,
tick,
output;

tock,
output;

set in 0,
set load 0,
tick,
output;

tock,
output;

set in 3,
set load 1,
tick,
output;

tock,
output;

set in 3,
set load 0,
tick,
output;

tock,
output;

set in 3,
set load 0,
tick,
output;

tock,
output;

set in 3,
set load 0,
tick,
output;

tock,
output;

set in 3,
set load 0,
tick,
output;

tock,
output;

set in 12345,
set load 1,
tick,
output;

tock,
output;

set in 0,
set load 0,
tick,
output;

tock,
output;

set in 0,
set load 0,
tick,
output;

tock,
output;

set in 0,
set load 0,
tick,
output;

tock,
output;


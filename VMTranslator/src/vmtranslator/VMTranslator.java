/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vmtranslator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import static java.lang.Integer.toBinaryString;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 *
 * @author addisongoolsbee
 */
public class VMTranslator {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner myObj = new Scanner(System.in);
        //need to change this to folder
        System.out.println("Enter folder name");
        
        //folder stuff
        int numvmfiles = 0;
        for(int i = 1;i<numvmfiles+1;i++){
           
        }
        
        String uncondensed = "";
        
        ArrayList<String> condensedFile = new ArrayList<>();

        String folderName = myObj.nextLine();
        
        String outputName = folderName.substring(folderName.lastIndexOf("/")+1)+".asm";
        String className = outputName.substring(0,outputName.indexOf(".asm"));
        String fullOutput = folderName+"/"+outputName;
        
        File dir = new File(folderName);
        
        
        String[] fileNames = dir.list(); 
        for (String fileName : fileNames) { 
            if(fileName.substring(fileName.lastIndexOf(".")+1).equals("vm")){
                String vmFileName = folderName+"/"+fileName;
                condensedFile.addAll(condense(vmFileName));
            }
            
        } 

        //condensed file has comments/blanks removed
        
        //Order: original file, condensedFile, hackFile

        //hackFile has the finished file in assembly language
        ArrayList<String> hackFile = new ArrayList<>();
        hackFile.add("@256\nD=A\n@SP\nM=D\n"
                + "@Sys.initCall\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"   //push return-address
                + "@LCL\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"   //push LCL
                + "@ARG\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"   //push ARG
                + "@THIS\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"   //push THIS
                + "@THAT\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"   //push THAT
                + "@251\nD=A\n@ARG\nM=D\n"   //ARG = 251
                + "@SP\nD=M\n@LCL\nM=D\n"   //LCL = SP
                + "@Sys.init\n0;JMP\n"  //goto f
                + "(Sys.initCall)");
        
        //Iterates through condensedFile
        Iterator<String> iterator = condensedFile.iterator(); 
        //counter for changing label names
        int counter = 0;
        String label = "";
        int callCounter = 0;
        
        //Translation to hack
        while(iterator.hasNext()){
            String line = iterator.next(),hack = "",word1 = "", word2 = "",word4  = "";
            int word3 = 0;
            int spaces = line.length() - line.replaceAll(" ", "").length();
            if(spaces == 2){
                word1 = line.substring(0,line.indexOf(" "));
                String remain = line.substring(word1.length()+1);
                word2 = remain.substring(0,remain.indexOf(" "));
                word3 = Integer.parseInt(remain.substring(remain.indexOf(" ")+1));
            }else if(spaces > 2){
                word1 = line.substring(0,line.indexOf(" "));
                String remain = line.substring(word1.length()+1);
                word2 = remain.substring(0,remain.indexOf(" "));
                remain = remain.substring(word2.length()+1);
                word3 = Integer.parseInt(remain.substring(0,remain.indexOf(" ")));
                word4 = remain.substring(remain.lastIndexOf(" ")+1);
            } else if(spaces == 1){
                word1 = line.substring(0,line.indexOf(" "));
                word2 = line.substring(line.indexOf(" ")+1);
            } else {
                word1 = line;
            }
            
            //check if push/pop command
            if("p".equals(line.substring(0,1))){
                //Split push/pop command into   type segment number
                //segHack changes the segment to it's hack counterpart (local -> LCL)
                String type = line.substring(0,line.indexOf(" "));
                String rest = line.substring(line.indexOf(" ")+1);
                String segment = rest.substring(0,rest.indexOf(" "));
                String number = rest.substring(rest.indexOf(" ")+1);
                if(line.contains("static")){
                     number = rest.substring(rest.indexOf(" ")+1,rest.lastIndexOf(" "));
                }
                String classNameShort = line.substring(line.lastIndexOf(" ")+1);
                String segHack = "";
                if(segment.equals("local")){segHack = "LCL";}
                if(segment.equals("argument")){segHack = "ARG";}
                if(segment.equals("this")){segHack = "THIS";}
                if(segment.equals("that")){segHack = "THAT";}
                if(segment.equals("pointer")){
                    if(number.substring(0,1).equals("0")){segHack = "THIS";}
                    if(number.substring(0,1).equals("1")){segHack = "THAT";}
                }
                //temp number
                if(segment.equals("temp")){
                    number = Integer.toString(5+Integer.parseInt(number));
                }
                
                //push commands
                if("push".equals(type)){
                    if("local".equals(segment)||"argument".equals(segment)||"this".equals(segment)||"that".equals(segment)){
                        hack = "@"+segHack+"\nD=M\n@"+number+"\nA=D+A\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
                    } else if(segment.equals("constant")){
                        hack = "@"+number+"\nD=A\n@SP\nM=M+1\nA=M-1\nM=D";
                    } else if(segment.equals("pointer")){
                        hack = "@"+segHack+"\nD=M\n@SP\nM=M+1\nA=M-1\nM=D";
                    } else if(segment.equals("static")){
                        hack = "@"+classNameShort+number+"\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
                    } else if(segment.equals("temp")){
                        hack = "@"+number+"\nD=M\n@SP\nM=M+1\nA=M-1\nM=D";
                    }
                //pop commands
                } else if("pop".equals(type)){
                    if("local".equals(segment)||"argument".equals(segment)||"this".equals(segment)||"that".equals(segment)){
                        hack = "@"+segHack+"\n@"+segHack+"\nD=M\n@"+number+"\nD=D+A\n@R13\nM=D\n@SP\nAM=M-1\nD=M\n@R13\nA=M\nM=D";
                    } else if(segment.equals("pointer")){
                        hack = "@SP\nAM=M-1\nD=M\n@"+segHack+"\nM=D";
                    } else if(segment.equals("static")){
                        hack = "@SP\nAM=M-1\nD=M\n@"+classNameShort+number+"\nM=D";
                    } else if (segment.equals("temp")){
                        hack = "@SP\nAM=M-1\nD=M\n@"+number+"\nM=D";
                    }
                }
            
                
            //Loops
            } else if(word1.equals("label")){
                hack = "("+word2+")";
            } else if(word1.equals("goto")){
                hack = "@"+word2+"\n0;JMP";
            } else if(word1.equals("if-goto")){
                hack = "@SP\nAM=M-1\nD=M\n@"+word2+"\nD;JNE";
                
            //function calling commands
            //function implementation
            } else if(word1.equals("function")){
                String temp = "";
                for(int i = 0;i<word3;i++){
                    temp += "\n@SP\nA=M\nM=0\n@SP\nM=M+1";
                }
                hack = "("+word2+")"+temp;
                
            //function calling
            } else if(word1.equals("call")){
            hack = "@"+word4+callCounter+"\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"   //push return-address
                + "@LCL\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"   //push LCL
                + "@ARG\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"   //push ARG
                + "@THIS\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"   //push THIS
                + "@THAT\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n"   //push THAT
                + "@5\nD=A\n@"+word3+"\nD=D+A\n@SP\nD=M-D\n@ARG\nM=D\n"   //ARG = SP-n-5
                + "@SP\nD=M\n@LCL\nM=D\n"   //LCL = SP
                + "@"+word2+"\n0;JMP\n"  //goto f
                + "("+word4+callCounter+")";   //(Return-address
            
            callCounter++;
            
            //function return    
            } else if(word1.equals("return")){
                hack = "@LCL\nD=M\n@FRAME\nM=D\n"   //FRAME = LCL
                + "@5\nA=D-A\nD=M\n@RET\nM=D\n" //RET = *(FRAME-5)
                + "@ARG\nD=M\n@0\nD=D+A\n@R13\nM=D\n@SP\nAM=M-1\nD=M\n@R13\nA=M\nM=D\n"
                + "@ARG\nD=M\n@SP\nM=D+1\n"    //SP = ARG+1
                + "@FRAME\nD=M-1\nAM=D\nD=M\n@THAT\nM=D\n"    //THAT = *(FRAME-1)
                + "@FRAME\nD=M-1\nAM=D\nD=M\n@THIS\nM=D\n"  //THIS = *(FRAME-2)
                + "@FRAME\nD=M-1\nAM=D\nD=M\n@ARG\nM=D\n"  //ARG = *(FRAME-3)
                + "@FRAME\nD=M-1\nAM=D\nD=M\n@LCL\nM=D\n"  //LCL = *(FRAME-4)
                + "@RET\nA=M\n0;JMP";   //goto RET
                
               
                
            //Arithmetic commands
            } else {
                if ("add".equals(line)){hack = "@SP\nAM=M-1\nD=M\nA=A-1\nM=D+M";}
                else if ("sub".equals(line)){hack = "@SP\nAM=M-1\nD=M\nA=A-1\nM=M-D";}
                else if ("neg".equals(line)){hack = "@SP\nA=M-1\nM=-M";}
                else if ("and".equals(line)){hack = "@SP\nAM=M-1\nD=M\nA=A-1\nM=M&D";}
                else if ("or".equals(line)){hack = "@SP\nAM=M-1\nD=M\nA=A-1\nM=M|D";}
                else if ("not".equals(line)){hack = "@SP\nA=M-1\nM=!M";}
                else if ("gt".equals(line)){
                    label = "continue"+counter;
                    hack = "@SP\nAM=M-1\nA=A-1\nD=M\nM=0\nA=A+1\nD=M-D\n@"+label+"\nD;JGE\n@SP\nA=M-1\nM=-1\n("+label+")";
                    counter++;}
                else if ("lt".equals(line)){
                    label = "continue"+counter;
                    hack = "@SP\nAM=M-1\nA=A-1\nD=M\nM=0\nA=A+1\nD=M-D\nM=0\n@"+label+"\nD;JLE\n@SP\nA=M-1\nM=-1\n("+label+")";
                    counter++;}
                else if ("eq".equals(line)){
                    label = "continue"+counter;
                    hack = "@SP\nAM=M-1\nA=A-1\nD=M\nM=0\nA=A+1\nD=M-D\n@"+label+"\nD;JNE\n@SP\nA=M-1\nM=-1\n("+label+")";
                    counter++;}
            }
            
            //adds several hack lines of code to hackFile, blank if it can't read the line of VM code
            hackFile.add(hack+"\n");
            //System.out.println(hack);
        }
        //write hackFile to .hack
        File fout = new File(fullOutput);
	FileOutputStream fos = new FileOutputStream(fout);
	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
	for (int i = 0; i < hackFile.size(); i++) {
		bw.write(hackFile.get(i));
		bw.newLine();
	}
	bw.close();
    }
    
    
    public static ArrayList<String> condense(String file) throws FileNotFoundException, IOException{   
        int check = 0;
        BufferedReader in = new BufferedReader(new FileReader(file));
        String vmShortName = file.substring(file.lastIndexOf("/")+1);
        
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<String> condensed = new ArrayList<>();
        
        String temp = in.readLine();
        while(temp!=null){
            lines.add(temp);
            temp = in.readLine();
        }
        
        for(String test: lines){
            if(test.length() >= 2 && test.substring(0,2).equals("/*")){
                check = 1;
            } else if (test.length() >= 2 && test.substring(0,2).equals("*/")){
                check = 2;
                
            }
            if (check != 1 && !test.contains("//")){
                if (check == 2){
                    test=test.substring(test.indexOf("*/")+2);
                    check = 0;
                }
                if(!test.trim().isEmpty()){
                    //System.out.println(test.replace(" ", ""));
                    if(test.length() >= 4 && test.substring(0,4).equals("call") || test.contains("static")){
                        condensed.add(test.trim()+" "+vmShortName);
                    } else {
                        condensed.add(test.trim());
                    }
                    
                }            
            } else if(check != 1 && test.indexOf("//")>=1){
                test=test.substring(0,test.indexOf("//"));
                //System.out.println(test.replace(" ", ""));
                if(test.length() >= 4 && test.substring(0,4).equals("call")){
                        condensed.add(test.trim()+" "+vmShortName);
                    } else {
                        condensed.add(test.trim());
                    }
            }
            
            if(check !=1 && test.length() == 1){
                if(!test.trim().isEmpty()){
                    //System.out.println(test.replace(" ", ""));
                    if(test.length() >= 4 && test.substring(0,4).equals("call")){
                        condensed.add(test.trim()+" "+vmShortName);
                    } else {
                        condensed.add(test.trim());
                    }
                }
            }
        }
        //System.out.println(condensed);
    return condensed;
    }
}
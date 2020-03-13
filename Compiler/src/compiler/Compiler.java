/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author addisongoolsbee
 */
public class Compiler {
    
    static ArrayList<String> t = new ArrayList<>();
    static ArrayList<String> a = new ArrayList<>();
    static int tpointer = 0;
    static int apointer = 0;
    static String spaces = "";
    static String[] optemp = {"+","-","*","/","&","|","<",">","="};
    static List<String> op = Arrays.asList(optemp);
    static String[][] st1 = new String[1000][4];
    static String[][] st2 = new String[1000][4];
    static String classN = "";
    static boolean subTable = false;
    static String letVar = "";
    static int st1pointer = 0;
    static int st2pointer = 0;
    static int expListCounter = 0;
    static int labelCounter = 0;
    

    public static void main(String[] args) throws IOException {
        
        
        Scanner myObj = new Scanner(System.in);
        //will be changed to folder
        System.out.println("Enter file name");
        
        //folder stuff
        int numvmfiles = 0;
        for(int i = 1;i<numvmfiles+1;i++){
           
        }
        
        ArrayList<String> condensedFile = new ArrayList<>();

        String fileName = myObj.nextLine();
        condensedFile = condense(fileName);
        
        String outputName = fileName.substring(0,fileName.indexOf(".jack"))+".vm";
                
        Iterator<String> iterator = condensedFile.iterator(); 
        while(iterator.hasNext()){
            String line = iterator.next();
            t.addAll(tokenize(line));
        }
        
        analyze();
        //ArrayList a has the full xml code
        
        File fout = new File(outputName);
	FileOutputStream fos = new FileOutputStream(fout);
	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
	for (int i = 0; i < a.size(); i++) {
		bw.write(a.get(i));
		bw.newLine();
	}
	bw.close();
    }
    
    public static ArrayList<String> condense(String file) throws FileNotFoundException, IOException{   
        int check = 0;
        BufferedReader in = new BufferedReader(new FileReader(file));
        
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<String> condensed = new ArrayList<>();
        
        String temp = in.readLine();
        while(temp!=null){
            lines.add(temp);
            temp = in.readLine();
        }
        
        for(String test: lines){
            
            if(!(test.trim().isEmpty())){
                if(test.contains("/*")&&check==0){
                    check = 1;
                    if(test.substring(0,test.indexOf("/*")).trim().length()>0){
                        condensed.add(test.substring(0,test.indexOf("/*")).trim());
                    } 
                } else if(test.contains("//")&&check==0){
                    if(test.substring(0,test.indexOf("//")).trim().length()>0){
                        condensed.add(test.substring(0,test.indexOf("//")).trim());
                    }
                } else {
                    condensed.add(test.trim());
                }

                if(test.contains("*/")&&check==1){
                    check=0;
                    if(test.substring(test.indexOf("*/")+2).trim().length()>0){
                        condensed.add(test.substring(test.indexOf("*/")+2).trim());
                    }
                }
            }
                
        }
        return condensed;
    }
    
    public static ArrayList<String> tokenize(String input){
        String[] keywords = {"class","constructor","function","method","field","static","var","int","char","boolean","void","true","false","null","this","let","do","if","else","while","return"};
        String[] symbols = {"{","}","(",")","[","]",".",",",";","+","-","*","/","&","|","<",">","=","~"};
        String numbers = "1234567890";

        String output = "";
        ArrayList<String> xml = new ArrayList<>();
        
        //expansion of symbols
        for(int i=0;i<input.length();i++){
            int num = 0;
            for(String s : symbols){
                if(input.substring(i,i+1).equals(s)){
                    if(i>0&&!input.substring(i-1,i).equals(" ")){
                        output+=" ";
                    }
                    output += s;
                    if(i!=input.length()-1&&!input.substring(i+1,i+2).equals(" ")){
                        output+=" ";
                    }
                    num = 1;
                }
                
            }
            if(num==0){
                output+=input.substring(i,i+1);
            }
        }
        //System.out.println(output);
        //end of symbol expansion
        
        //breaking into smallest parts
        String[] temp = output.split(" ");
        
        ArrayList<String> atoms = new ArrayList<>();
        String tempString = "";
        boolean checker = false;
        for(String s: temp){
            if(!checker&&s.length()>2&&s.substring(0,1).equals("\"")){
                checker = true;
                tempString = tempString + s;
            } else if(s.contains("\"")&&checker){
                checker = false;
                tempString = tempString + s;
                atoms.add(tempString);
            } else if(checker){

                tempString = tempString + " " + s;
            } else {
                atoms.add(s);
            }
            
        }
        //System.out.println(atoms);
        
        for(String s : atoms){
            if(s.length()!=0){
                if(numbers.contains(s.substring(0,1))){
                    xml.add("<integerConstant> "+s+" </integerConstant>");
                } else if(s.substring(0,1).equals("\"")){
                    xml.add("<stringConstant> "+s.substring(1,s.length()-1)+" </stringConstant>");
                } else if(Arrays.asList(symbols).contains(s)){
                    xml.add("<symbol> "+s+" </symbol>");
                } else if(Arrays.asList(keywords).contains(s)){
                    xml.add("<keyword> "+s+" </keyword>");
                } else {
                    xml.add("<identifier> "+s+" </identifier>");
                }
            }
            
        }
        
        return xml;
    }
    
    public static void analyze(){
        classA();
    }
    
    public static String value(String s){
        if(s.contains(">")&&s.contains("<")){
            return s.substring(s.indexOf(">")+2,s.lastIndexOf("<")-1);
        }  
        return "";
    }
    
    public static String token(String s){
        return s.substring(1,s.indexOf(">"));
    }
    
    public static void doWord(){
        //a.add(spaces+t.get(tpointer));
        apointer++;
        tpointer++;
    }
    
    public static String getVar(String s){
        if(subTable){
            for(int i = 0;i<st2.length;i++){
                if(st2[i][0].equals(s)){
                    return st2[i][2]+" "+st2[i][3];
                }
            }
        }
        for(int i = 0;i<st1.length;i++){
            if(st1[i][0].equals(s)){
                return st1[i][2]+" "+st1[i][3];
            }
        }
        
        return "FAIL";
    }
    
    public static String changeOp(String s){
        if(s.equals("+")){return "add";}
        else if(s.equals("+")){return "add";}
        else if(s.equals("-")){return "sub";}
        else if(s.equals("*")){return "call math.multiply 2";}
        else if(s.equals("/")){return "call math.divide 2";}
        else if(s.equals("&")){return "and";}
        else if(s.equals("|")){return "or";}
        else if(s.equals("<")){return "lt";}
        else if(s.equals(">")){return "gt";}
        else if(s.equals("=")){return "eq";}
        else{return "CHANGEOP PROBLEM";}
    }
    
    public static void stringDef(){
            if(value(t.get(tpointer+1)).equals("String")){
                a.add("push constant "+getVar(value(t.get(tpointer+4))));
                a.add("call String.new 1");
                a.add("pop "+getVar(value(t.get(tpointer+2))));
        }
    }
    //Program Structure
    
    public static void classA(){
        classN = value(t.get(tpointer+1));
        a.add(spaces+"<class>");apointer++;spaces+="  ";
        doWord(); //class
        className(); //className
        doWord(); //{
        while(value(t.get(tpointer)).equals("static")||value(t.get(tpointer)).equals("field")){ //classVarDec*
            classVarDec();
        }
        String su = value(t.get(tpointer));
        while(value(t.get(tpointer)).equals("constructor")||value(t.get(tpointer)).equals("method")||value(t.get(tpointer)).equals("function")){ //subroutineDec*
            subroutineDec();
        }
        doWord(); //}
        spaces=spaces.substring(0,spaces.length()-2);a.add(spaces+"</class>");apointer++;
        for(int i = 0;i<st1.length;i++){
            for(int j = 0;j<4;j++){
                st1[i][j] = null;
            }
        }
    }
    
    
    
    public static void classVarDec(){
        a.add(spaces+"<classVarDec>");apointer++;spaces+="  ";
        String kindTemp;
        if(value(t.get(tpointer+2)).equals("field")){
            kindTemp = "this";
        } else {
            kindTemp = "static";
        }
        if(subTable){
            st2[st2pointer][0] = value(t.get(tpointer+2));st2[st2pointer][1] = value(t.get(tpointer+1));st2[st2pointer][2] = kindTemp;st2[st2pointer][3] = Integer.toString(st2pointer);
            st2pointer++;
        } else {
            st1[st1pointer][0] = value(t.get(tpointer+2));st1[st1pointer][1] = value(t.get(tpointer+1));st1[st1pointer][2] = kindTemp;st1[st1pointer][3] = Integer.toString(st1pointer);
            st1pointer++;
        }
        stringDef();
        
        
        doWord(); //static/field
        typeA();
        varName();
        while(value(t.get(tpointer)).equals(",")){
            if(subTable){
                st2[st2pointer][0] = value(t.get(tpointer+1));st2[st2pointer][1] = st2[st2pointer-1][1];st2[st2pointer][2] = st2[st2pointer-1][2];st2[st2pointer][3] = Integer.toString(st2pointer);
                st2pointer++;
            } else {
                st1[st1pointer][0] = value(t.get(tpointer+1));st1[st1pointer][1] = st2[st2pointer-1][1];st1[st1pointer][2] = st2[st2pointer-1][2];st1[st1pointer][3] = Integer.toString(st1pointer);
                st1pointer++;
            }
            stringDef();
            doWord(); //,
            varName();
        }
        doWord(); //;
        spaces=spaces.substring(0,spaces.length()-2);a.add(spaces+"</classVarDec>");apointer++;
    }
    
    public static void typeA(){
        doWord(); //int/char/boolean/className
    }
    
    public static void subroutineDec(){
        subTable = true;
        st2[0][0] = "this";st2[0][1] = classN;st2[0][2] = "argument";st2[0][3] = "0";
        st2pointer ++;
        int alloc = 0;
        a.add(spaces+"<subroutineDec>");apointer++;spaces+="  ";
        String temp = "";
        int k = 0;
        
        if(value(t.get(tpointer)).equals("constructor")){
            doWord(); //constructor
            doWord(); //void/type
            subroutineName();
            doWord(); //(
            
            if(!value(t.get(tpointer)).equals(")")){
                alloc++;
                int i = 2;
                while(value(t.get(tpointer)+i).equals(",")){
                    alloc++;
                    i+=3;
                }
            }
            a.add("push constant "+Integer.toString(alloc));
            a.add("call Memory.alloc 1");
            a.add("pop pointer 0");
            parameterList();
            doWord(); //)
            subroutineBody();
        } else if(value(t.get(tpointer)).equals("method")){
            doWord(); //method
            doWord(); //void/type
            subroutineName();
            doWord(); //(
            temp += "function "+value(t.get(tpointer-2))+" ";
            parameterList();
            doWord(); //)
            if(value(t.get(tpointer+1)).equals("var")){
                alloc++;
                k = 2;
                while(value(t.get(tpointer)+k).equals(",")){
                    alloc++;
                    k+=3;
                }
                while(value(t.get(tpointer)+k).equals("var")){
                    alloc++;
                    k = 2;
                    while(value(t.get(tpointer)+k).equals(",")){
                        alloc++;
                        k+=3;
                    }
                }
                
            }
            temp += alloc;
            a.add(temp);
            a.add("push argument 0");
            a.add("pop pointer 0");
            subroutineBody();
        } else if(value(t.get(tpointer)).equals("function")){
            doWord(); //function
            doWord(); //void/type
            subroutineName();
            doWord(); //(
            temp += "function "+value(t.get(tpointer-2))+" ";
            parameterList();
            doWord(); //)
            if(value(t.get(tpointer+1)).equals("var")){
                alloc++;
                k = 2;
                while(value(t.get(tpointer)+k).equals(",")){
                    alloc++;
                    k+=3;
                }
                while(value(t.get(tpointer)+k).equals("var")){
                    alloc++;
                    k = 2;
                    while(value(t.get(tpointer)+k).equals(",")){
                        alloc++;
                        k+=3;
                    }
                }
                
            }
            temp += alloc;
            a.add(temp);
            subroutineBody();
        }
        spaces=spaces.substring(0,spaces.length()-2);a.add(spaces+"</subroutineDec>");apointer++;
        for(int i = 0;i<st2.length;i++){
            for(int j = 0;j<4;j++){
                st2[i][j] = null;
            }
        }
        st2pointer = 0;
        subTable = false;
    }
    
    public static void parameterList(){
        a.add(spaces+"<parameterList>");apointer++;spaces+="  ";
        if(value(t.get(tpointer)).equals("int")||value(t.get(tpointer)).equals("boolean")||value(t.get(tpointer)).equals("char")){
            if(subTable){
                st2[st2pointer][0] = value(t.get(tpointer+1));st2[st2pointer][1] = value(t.get(tpointer));st2[st2pointer][2] = "argument";st2[st2pointer][3] = Integer.toString(st2pointer);
                st2pointer++;
            } else {
                st1[st1pointer][0] = value(t.get(tpointer+1));st1[st1pointer][1] = value(t.get(tpointer));st1[st1pointer][2] = "argument";st1[st1pointer][3] = Integer.toString(st1pointer);
                st1pointer++;
            }
            stringDef();
            typeA();
            varName();
            while(value(t.get(tpointer)).equals(",")){
                doWord(); //,
                if(subTable){
                    st2[st2pointer][0] = value(t.get(tpointer+1));st2[st2pointer][1] = value(t.get(tpointer));st2[st2pointer][2] = "argument";st2[st2pointer][3] = Integer.toString(st2pointer);
                    st2pointer++;
                } else {
                    st1[st1pointer][0] = value(t.get(tpointer+1));st1[st1pointer][1] = value(t.get(tpointer));st1[st1pointer][2] = "argument";st1[st1pointer][3] = Integer.toString(st1pointer);
                    st1pointer++;
                }
                stringDef();
                typeA();
                varName();
            }
        }
        spaces=spaces.substring(0,spaces.length()-2);a.add(spaces+"</parameterList>");apointer++;
    }
    
    public static void subroutineBody(){
        a.add(spaces+"<subroutineBody>");apointer++;spaces+="  ";
        doWord(); //{
        while(value(t.get(tpointer)).equals("var")){
            varDec();
        }
        statements();
        doWord(); //}
        spaces=spaces.substring(0,spaces.length()-2);a.add(spaces+"</subroutineBody>");apointer++;
    }
    
    public static void varDec(){
        if(subTable){
            st2[st2pointer][0] = value(t.get(tpointer+2));st2[st2pointer][1] = value(t.get(tpointer+1));st2[st2pointer][2] = "local";st2[st2pointer][3] = Integer.toString(st2pointer);
            st2pointer++;
        } else {
            st1[st1pointer][0] = value(t.get(tpointer+2));st1[st1pointer][1] = value(t.get(tpointer+1));st1[st1pointer][2] = "local";st1[st1pointer][3] = Integer.toString(st1pointer);
            st1pointer++;
        }
        stringDef();
        a.add(spaces+"<varDec>");apointer++;spaces+="  ";
        doWord(); //var
        typeA();
        varName();
        while(value(t.get(tpointer)).equals(",")){
            if(subTable){
                st2[st2pointer][0] = value(t.get(tpointer+1));st2[st2pointer][1] = value(t.get(tpointer));st2[st2pointer][2] = "local";st2[st2pointer][3] = Integer.toString(st2pointer);
                st2pointer++;
            } else {
                st1[st1pointer][0] = value(t.get(tpointer+1));st1[st1pointer][1] = value(t.get(tpointer));st1[st1pointer][2] = "local";st1[st1pointer][3] = Integer.toString(st1pointer);
                st1pointer++;
            }
            stringDef();
            doWord(); //,
            varName();
        }
        doWord(); //;
        spaces=spaces.substring(0,spaces.length()-2);a.add(spaces+"</varDec>");apointer++;
    }
    
    public static void className(){
        doWord(); //identifier
    }
    
    public static void subroutineName(){
        doWord(); //identifier
    }
    
    public static void varName(){
        doWord(); //indentifier
    }
    
    //Statements
    
    public static void statements(){
        a.add(spaces+"<statements>");apointer++;spaces+="  ";
        while(value(t.get(tpointer)).equals("let")||value(t.get(tpointer)).equals("if")||value(t.get(tpointer)).equals("while")||value(t.get(tpointer)).equals("do")||value(t.get(tpointer)).equals("return")){
            if(value(t.get(tpointer)).equals("let")){
                letStatement();
            } else if(value(t.get(tpointer)).equals("if")){
                ifStatement();
            } else if(value(t.get(tpointer)).equals("while")){
                whileStatement();
            } else if(value(t.get(tpointer)).equals("do")){
                doStatement();
            } else if(value(t.get(tpointer)).equals("return")){
                returnStatement();
            }
        }
        spaces=spaces.substring(0,spaces.length()-2);a.add(spaces+"</statements>");apointer++;
    }
    
    public static void letStatement(){
        a.add(spaces+"<letStatement>");apointer++;spaces+="  ";
        int tpointertemp;
        int tpointertemp2;
        
        doWord(); //let
        letVar = value(t.get(tpointer));
        varName();
        if(value(t.get(tpointer)).equals("[")){
            int i=0;
            while(true){
                if(value(t.get(tpointer+i)).equals("=")){
                    tpointertemp = tpointer;
                    tpointertemp2 = tpointer;
                    expression();
                    tpointertemp2 = tpointer;
                    tpointer = tpointertemp;
                    break;
                }
                i++;
            }
            a.add("push "+getVar(value(t.get(tpointer-1))));
            doWord(); //[
            expression();
            doWord(); //]
            a.add("add");
            a.add("pop pointer 1");
            a.add("pop that 0");
            doWord(); //=
            tpointer = tpointertemp2;
            doWord(); //;
        } else {
            doWord(); //=
            expression();
            doWord(); //;
            a.add("pop"+getVar(letVar));
        }
        
        
        spaces=spaces.substring(0,spaces.length()-2);a.add(spaces+"</letStatement>");apointer++;
    }
    
    public static void ifStatement(){
        a.add(spaces+"<ifStatement>");apointer++;spaces+="  ";
        String L1 = classN+".label"+Integer.toString(labelCounter);
        labelCounter++;
        String L2 = classN+".label"+Integer.toString(labelCounter);
        labelCounter++;
        
        doWord(); //if
        doWord(); //(
        
        expression();
        a.add("not");
        a.add("if-goto "+L1);
        
        doWord(); //)
        doWord(); //{
        
        statements();
        a.add("goto "+L2);
        a.add("label "+L1);
        
        doWord(); //}
        if(value(t.get(tpointer)).equals("else")){
            doWord(); //else
            doWord(); //{
            statements();
            doWord(); //}
        }
        a.add("label "+L2);
        spaces=spaces.substring(0,spaces.length()-2);a.add(spaces+"</ifStatement>");apointer++;
    }
    
    public static void whileStatement(){
        a.add(spaces+"<whileStatement>");apointer++;spaces+="  ";
        String L1 = classN+".label"+Integer.toString(labelCounter);
        labelCounter++;
        String L2 = classN+".label"+Integer.toString(labelCounter);
        labelCounter++;
        
        a.add("label " +L1);
        
        doWord(); //while
        doWord(); //(
        
        expression();
        a.add("not");
        a.add("if-goto "+L2);
        
        doWord(); //)
        doWord(); //{
        statements();
        a.add("goto "+L1);
        a.add("label "+L2);
        doWord(); //}
        spaces=spaces.substring(0,spaces.length()-2);a.add(spaces+"</whileStatement>");apointer++;
    }
    
    public static void doStatement(){
        a.add(spaces+"<doStatement>");apointer++;spaces+="  ";
        doWord(); //do
        subroutineCall();
        doWord(); //;
        a.add("pop temp 0");
        spaces=spaces.substring(0,spaces.length()-2);a.add(spaces+"</doStatement>");apointer++;
    }
    
    public static void returnStatement(){
        //HEY
        a.add(spaces+"<returnStatement>");apointer++;spaces+="  ";
        doWord(); //return
        if(!value(t.get(tpointer)).equals(";")){
            expression();
        } else {
            a.add("push constant 0");
        }
        a.add("return");
        doWord(); //;
        spaces=spaces.substring(0,spaces.length()-2);a.add(spaces+"</returnStatement>");apointer++;
    }
    
    //Expressions
    
    public static void expression(){
        a.add(spaces+"<expression>");apointer++;spaces+="  ";
        term();
        while(op.contains(value(t.get(tpointer)))){
            String curOp = value(t.get(tpointer));
            op();
            term();
            
            a.add(changeOp(curOp));
        }
        spaces=spaces.substring(0,spaces.length()-2);a.add(spaces+"</expression>");apointer++;
    }
    
    public static void term(){
        a.add(spaces+"<term>");apointer++;spaces+="  ";
        if(value(t.get(tpointer+1)).equals("[")){
            a.add("push "+getVar(value(t.get(tpointer))));
            varName();
            doWord(); //[
            expression();
            doWord(); //]  
            a.add("add");
            a.add("pop pointer 1");
            a.add("push that 0");
        } else if(value(t.get(tpointer)).equals("(")){
            doWord(); //(
            expression();
            doWord(); //)
        } else if (value(t.get(tpointer+1)).equals("(")||value(t.get(tpointer+1)).equals(".")){
            subroutineCall();
        } else if(value(t.get(tpointer)).equals("-")){
            unaryOp();
            term();
            a.add("neg");           
        } else if(value(t.get(tpointer)).equals("~")){
            unaryOp();
            term();           
            a.add("not");            
        } else if(token(t.get(tpointer)).equals("integerConstant")){
            a.add("push constant "+value(t.get(tpointer)));
            doWord();
        } else if (token(t.get(tpointer)).equals("StringConstant")){
            a.add("push "+getVar(letVar));
            String temp = value(t.get(tpointer));
            int stringLength = temp.lastIndexOf("\"")-temp.indexOf("\"");
            for(int i = 0;i<stringLength;i++){
                int ascii = temp.charAt(i);
                a.add("push constant "+Integer.toString(ascii));
                a.add("call String.appendChar 2");
                a.add("pop "+getVar(letVar));
            }
            doWord();
        } else if (token(t.get(tpointer)).equals("keyword")){
           if(value(t.get(tpointer)).equals("true")){
               a.add("push constant 1");
               a.add("neg");
           } else if(value(t.get(tpointer)).equals("false")){
               a.add("push constant 0");
           } else if(value(t.get(tpointer)).equals("null")){
               a.add("push constant 0");
           } else if(value(t.get(tpointer)).equals("this")){
               a.add("push pointer 0");
           }
            doWord();
        } else if (token(t.get(tpointer)).equals("identifier")){
            String varCall = "";
            if(subTable){
                for(int i = 0;i<st1.length;i++){
                    if(st1[i].equals(value(t.get(tpointer)))){
                        varCall += st1[i][2] +" "+ st1[i][3];
                        break;
                    } 
                }
                
                for(int i = 0;i<st2.length;i++){
                    if(st2[i].equals(value(t.get(tpointer)))){
                        varCall += st2[i][2] +" "+ st2[i][3];
                        break;
                    }
                }
            } else {
                for(int i = 0;i<st1.length;i++){
                    if(st1[i].equals(value(t.get(tpointer)))){
                        varCall += st1[i][2] +" "+ st1[i][3];
                        break;
                    }
                }
            }
            a.add("push "+ varCall);
            doWord();
        }
        spaces=spaces.substring(0,spaces.length()-2);a.add(spaces+"</term>");apointer++;
    }
    
    public static void subroutineCall(){
        //Check this
        //a.add(spaces+"<subroutineCall>");apointer++;spaces+="  ";
        if(value(t.get(tpointer+1)).equals("(")){
            String tempName = value(t.get(tpointer));
            subroutineName();
            doWord(); //(
            expressionList();
            doWord(); //)
            
            a.add("call "+tempName+" "+expListCounter);
        } else {
            a.add("push pointer 0");
            
            doWord(); //className/varName
            doWord(); //.
            String tempName = value(t.get(tpointer));
            subroutineName();
            doWord(); //(
            expressionList();
            doWord(); //)
            
            a.add("call "+tempName+" "+expListCounter);
        }
        expListCounter = 0;
        //spaces=spaces.substring(0,spaces.length()-2);a.add(spaces+"</subroutineCall>");apointer++;
    }
    
    public static void expressionList(){
        a.add(spaces+"<expressionList>");apointer++;spaces+="  ";
        if(!value(t.get(tpointer)).equals(")")){
            expression();
            expListCounter++;
            while(value(t.get(tpointer)).equals(",")){
                doWord(); //,
                expression();
                expListCounter++;
            }
        }
        spaces=spaces.substring(0,spaces.length()-2);a.add(spaces+"</expressionList>");apointer++;
    }
    
    public static void op(){
        doWord(); //+/-/*///&/|/</>/=
    }
    
    public static void unaryOp(){
        doWord(); //-/~
    }
    
    public static void KeywordConstant(){
        doWord(); //true/false/null/this
    }
    
}

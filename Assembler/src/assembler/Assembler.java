package assembler;

import java.util.*; 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import static java.lang.Integer.toBinaryString;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author addisongoolsbee
 */
public class Assembler {

    public static void main(String[] args) throws IOException {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter file name");

        String file = myObj.nextLine();
        
        String location = file.substring(0,file.lastIndexOf("asm"));
        String outFile = location + "hack";
        
        ArrayList<String> condensedFile = new ArrayList<>();
        condensedFile = condense(file);
        //condensed file has comments/blanks removed
        
        //Order: original file, condensedFile, labelsGone, binaryFile
        
        //labelsGone has label removed and stored in the hashmap
        Iterator<String> labelIter = condensedFile.iterator(); 
        ArrayList<String> labelsGone = new ArrayList<>();


        //binaryFile has the finished file in binary
        ArrayList<String> binaryFile = new ArrayList<>();

        //This hashmap is a dictionary that holds all variables and their values
        HashMap<String, Integer> variables = new HashMap<>(); 
        //default values (not sorted)
        variables.put("SP",0);
        variables.put("LCL",1);
        variables.put("ARG",2);
        variables.put("THIS",3);
        variables.put("THAT",4);
        variables.put("SCREEN",16384);
        variables.put("KBD",24576);
        for(int i=0;i<16;i++){
            variables.put("R"+String.valueOf(i),i);
        }
        int nextVar = 16;
                
        //Removal of lables
        int newLine = 0;
        while(labelIter.hasNext()){
            String line = labelIter.next();
            //check if line is a label
            if("(".equals(line.substring(0,1))){
                String label = line.substring(1,line.indexOf(")"));
                variables.put(label,newLine);
            } else {
                labelsGone.add(line);
                newLine++;
            }
        }
        
        Iterator<String> iterator = labelsGone.iterator(); 
        //Translation to binary
        while(iterator.hasNext()){
            String line = iterator.next();
            String binary = "";
            
            //A-instruction
            if("@".equals(line.substring(0,1))){
                //non-symbols
                if("0".equals(line.substring(1,2)) || "1".equals(line.substring(1,2)) || "2".equals(line.substring(1,2)) || "3".equals(line.substring(1,2)) || "4".equals(line.substring(1,2)) || "5".equals(line.substring(1,2)) || "6".equals(line.substring(1,2)) || "7".equals(line.substring(1,2)) || "8".equals(line.substring(1,2)) || "9".equals(line.substring(1,2))){
                    binary+="0";
                    String aValue = line.substring(1);
                    String endBinary = Integer.toBinaryString(Integer.parseInt(aValue));
                    while(endBinary.length()<15){
                        endBinary="0"+endBinary;
                    }
                    binary+=endBinary;
                    //System.out.println(binary);
                    
                //A-instruction for variables    
                } else {
                    String vname = line.substring(1);
                    if(variables.containsKey(vname)){
                        binary="0";
                        String endBinary = toBinaryString(variables.get(vname));
                        while(endBinary.length()<15){
                            endBinary="0"+endBinary;
                        }
                        binary+=endBinary;
                        //System.out.println(binary);
                    } else {
                        variables.put(vname, nextVar);
                        binary="0";
                        String endBinary = toBinaryString(variables.get(vname));
                        while(endBinary.length()<15){
                            endBinary="0"+endBinary;
                        }
                        binary+=endBinary;
                        //System.out.println(binary);
                        nextVar += 1;
                    }
                    
                    
                }
               
            //C-instruction
            } else {
                binary+="111";
                int equal = line.indexOf("=");
                int semicolon = line.indexOf(";");
                String comp;
                String dest = "000";
                String jump = "000";
                
                //computation
                if(equal !=-1 && semicolon !=-1){
                    comp = line.substring(equal+1,semicolon);
                } else if(equal != -1){
                    comp = line.substring(equal+1);                    
                } else if(semicolon != -1){
                    comp = line.substring(0,semicolon);                    
                } else {
                    comp = line;
                }
                
                //jump
                if(semicolon !=-1){
                    jump = line.substring(semicolon+1);
                }
                
                //destination
                if(equal !=-1){
                    dest = line.substring(0,equal);
                }
                
                //binary translation
                //comp
                String a;
                String c="0";
                if(comp.contains("M")){
                    a = "1";
                } else {
                    a = "0";
                }
                if("0".equals(comp)){c = "101010";}
                if("1".equals(comp)){c = "111111";}
                if("-1".equals(comp)){c = "111010";}
                if("D".equals(comp)){c = "001100";}
                if("A".equals(comp)||"M".equals(comp)){c = "110000";}
                if("!D".equals(comp)){c = "001101";}
                if("!A".equals(comp)||"!M".equals(comp)){c = "110001";}
                if("-D".equals(comp)){c = "001111";}
                if("-A".equals(comp)||"-M".equals(comp)){c = "110011";}
                if("D+1".equals(comp)){c = "011111";}
                if("A+1".equals(comp)||"M+1".equals(comp)){c = "110111";}
                if("D-1".equals(comp)){c = "001110";}
                if("A-1".equals(comp)||"M-1".equals(comp)){c = "110010";}
                if("D+A".equals(comp)||"D+M".equals(comp)){c = "000010";}
                if("D-A".equals(comp)||"D-M".equals(comp)){c = "010011";}
                if("A-D".equals(comp)||"M-D".equals(comp)){c = "000111";}
                if("D&A".equals(comp)||"D&M".equals(comp)){c = "000000";}
                if("D|A".equals(comp)||"D|M".equals(comp)){c = "010101";}
                comp = a+c;
                
                //destination
                if("M".equals(dest)){dest="001";}
                if("D".equals(dest)){dest="010";}
                if("MD".equals(dest)){dest="011";}
                if("A".equals(dest)){dest="100";}
                if("AM".equals(dest)){dest="101";}
                if("AD".equals(dest)){dest="110";}
                if("AMD".equals(dest)){dest="111";}
                
                //jump
                if("JGT".equals(jump)){jump="001";}
                if("JEQ".equals(jump)){jump="010";}
                if("JGE".equals(jump)){jump="011";}
                if("JLT".equals(jump)){jump="100";}
                if("JNE".equals(jump)){jump="101";}
                if("JLE".equals(jump)){jump="110";}
                if("JMP".equals(jump)){jump="111";}
                
                
                binary=binary+comp+dest+jump;
                //System.out.println(binary);                
            }
            //adds one 16-bit line of translated binary code
            binaryFile.add(binary);
            
        }
        //write binaryFile to .hack file
        File fout = new File(outFile);
	FileOutputStream fos = new FileOutputStream(fout);
	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
	for (int i = 0; i < binaryFile.size(); i++) {
		bw.write(binaryFile.get(i));
		bw.newLine();
	}
	bw.close();
    }
	

    //Removes blank lines, spaces and comments
    //      hi/*hi will not change check to 1 - fix if you want to
    //      ...*/hi will return hi - this is fixed already
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
                    condensed.add(test.replace(" ", ""));
                }            
            } else if(check != 1 && test.indexOf("//")>=1){
                test=test.substring(0,test.indexOf("//"));
                //System.out.println(test.replace(" ", ""));
                condensed.add(test.replace(" ", ""));
            }
            
            if(check !=1 && test.length() == 1){
                if(!test.trim().isEmpty()){
                    //System.out.println(test.replace(" ", ""));
                    condensed.add(test.replace(" ", ""));
                }
            }
        }
    return condensed;
    }
}

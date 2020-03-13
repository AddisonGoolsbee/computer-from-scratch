/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programcondenser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;  // Import the Scanner class

/**
 *
 * @author addisongoolsbee
 */
public class Condenser {
// /Users/addisongoolsbee/Desktop/foo.in
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter file name");

        String file = myObj.nextLine();  // Read user input
        condense(file);
        
    }
    public static void condense(String file) throws FileNotFoundException, IOException{   
        int location = file.lastIndexOf("in");
        String outfile = file.substring(0,location);
        int check = 0;
        BufferedReader in = new BufferedReader(new FileReader(file));
        
        ArrayList<String> lines = new ArrayList<String>();
        
        ListIterator<String> listItr = lines.listIterator();
        
        String temp = in.readLine();
        while(temp!=null){
            lines.add(temp);
            temp = in.readLine();
        }
//        FileWriter fileWriter = new FileWriter(outfile+"out");
//        PrintWriter printWriter = new PrintWriter(fileWriter);
        for(String test: lines){
            
            if(test.length() >= 2 && test.substring(0,2).equals("/*")){
                check = 1;
            } else if (test.length() >= 2 && test.substring(0,2).equals("*/")){
                check = 2;
                
            }
            if (check != 1 && test.length() >= 2 && !test.substring(0,2).equals("//")){
                if (check == 2){
                    test=test.substring(2);
                    check = 0;
                }
                if(!test.trim().isEmpty()){
                    System.out.println(test.replace(" ", ""));
//                    printWriter.println(test.replace(" ", ""));
                }            
            }
        }
//        printWriter.close();
    } 
    
}
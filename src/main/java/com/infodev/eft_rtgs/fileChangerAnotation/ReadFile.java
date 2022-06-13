package com.infodev.eft_rtgs.fileChangerAnotation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadFile {


    public String readfile(String fileName){
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            String data="";
            while (myReader.hasNextLine()) {
                 data =data+ myReader.nextLine()+"\r\n";

            }
             myReader.close();
            return data;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }
    }




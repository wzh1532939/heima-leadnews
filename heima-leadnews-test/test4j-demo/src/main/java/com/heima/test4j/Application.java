package com.heima.test4j;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;


public class Application {
    public static void main(String[] args) throws TesseractException {
        ITesseract tesseract=new Tesseract();
        tesseract.setDatapath("D:\\OCR");
        tesseract.setLanguage("chi_sim");
        File file=new File("D:\\OCR\\1.jpg");
        String result = tesseract.doOCR(file);
        System.out.println(result);
    }
}

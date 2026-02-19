package com.example.demo;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OCRService {
    public String extractAmount(File imageFile) {
        Tesseract tesseract = new Tesseract();
        // Tera exact Tesseract path
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata"); 
        
        try {
            String text = tesseract.doOCR(imageFile);
            System.out.println("--- DEBUG: Bill Text Start ---");
            System.out.println(text); 
            System.out.println("--- DEBUG: Bill Text End ---");

            // Naya Flexible Regex: Jo bill ke aakhir ka decimal amount uthayega
            Pattern pattern = Pattern.compile("(\\d+[.,]\\d{2})");
            Matcher matcher = pattern.matcher(text);

            String lastAmount = "0.00";
            while (matcher.find()) {
                lastAmount = matcher.group(1).replace(",", ".");
            }
            return lastAmount;
        } catch (Exception e) {
            e.printStackTrace();
            return "0.00";
        }
    }
}

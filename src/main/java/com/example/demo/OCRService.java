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
        
        // 🔥 FIX 1: Render (Linux) ke liye sahi path set karo
        // Dockerfile mein Tesseract yahan install hota hai
        tesseract.setDatapath("/usr/share/tesseract-ocr/4.00/tessdata"); 
        
        // 🔥 FIX 2: Image quality aur accuracy badhane ke liye settings
        tesseract.setTessVariable("user_defined_dpi", "300");
        tesseract.setPageSegMode(3); // 3 = Fully automatic page segmentation (Best for receipts)

        try {
            String text = tesseract.doOCR(imageFile);
            System.out.println("--- DEBUG: Bill Text Start ---");
            System.out.println(text); 
            System.out.println("--- DEBUG: Bill Text End ---");

            // Agar text khali hai toh debug mein pata chal jayega
            if (text == null || text.trim().isEmpty()) {
                System.out.println("ERROR: OCR ne kuch bhi read nahi kiya!");
                return "0.00";
            }

            // Naya Flexible Regex: Jo bill ke aakhir ka decimal amount uthayega
            Pattern pattern = Pattern.compile("(\\d+[.,]\\d{2})");
            Matcher matcher = pattern.matcher(text);

            String lastAmount = "0.00";
            while (matcher.find()) {
                lastAmount = matcher.group(1).replace(",", ".");
            }
            return lastAmount;
        } catch (Exception e) {
            System.err.println("OCR Error: " + e.getMessage());
            e.printStackTrace();
            return "0.00";
        }
    }
}

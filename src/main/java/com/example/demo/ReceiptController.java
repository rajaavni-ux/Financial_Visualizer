package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ReceiptController {
    @Autowired private OCRService ocrService;
    @Autowired private ReceiptRepository repository;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws Exception {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();

        String amountStr = ocrService.extractAmount(convFile);
        double amount = Double.parseDouble(amountStr);

        if (amount > 0) {
            Receipt receipt = new Receipt();
            receipt.setAmount(amount);
            receipt.setScanDate(LocalDateTime.now());
            repository.save(receipt);
            return ResponseEntity.ok("Success");
        }
        return ResponseEntity.badRequest().body("Scan Failed: Value was 0");
    }

    @GetMapping("/all")
    public List<Receipt> getAll() { return repository.findAll(); }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.ok("Deleted");
    }
}

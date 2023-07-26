package org.example;

import com.aspose.pdf.Document;
import com.aspose.pdf.ExcelSaveOptions;
import com.aspose.pdf.ExcelSaveOptions.ExcelFormat;
import com.aspose.pdf.SaveFormat;


public class PdfConverter {
    public static void main(String[] args) throws Exception {// Main method to perform conversion

        // Applying product license to create XLSX from PDF in Java

        // Create a Document Class object to load PDF and saving as XLSX
        Document document = new Document("test2.pdf");

        // Set ExcelSaveOptions
        ExcelSaveOptions saveOptions = new ExcelSaveOptions();
        saveOptions.setFormat(ExcelFormat.XLSX);
        saveOptions.setInsertBlankColumnAtFirst(true);

        // Convert the PDF to XLSX format
        document.save("output_pdfToXls.xlsx", SaveFormat.Excel);
    }
}


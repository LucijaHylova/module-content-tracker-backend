package com.bfh.moduletracker.ai.service.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.bfh.moduletracker.ai.common.HtmlCleaningTransformer;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class ModuleReader {

    @Value("${isa-modules.path}")
    private Resource excelFile;

    @Getter
    private final List<String> headerOrder;

    public ModuleReader(List<String> headerOrder) {
        this.headerOrder = headerOrder;
    }


    public List<Document> read() {
        List<Document> documents = new ArrayList<>();

        try (InputStream inputStream = excelFile.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            for (int n = 1; n < sheet.getPhysicalNumberOfRows(); n++) {
                Row row = sheet.getRow(n);
                if (row == null) continue;

                Document document = new Document("");

                for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                    this.headerOrder.add(headerRow.getCell(i).getStringCellValue().trim());
                    String key = headerRow.getCell(i).getStringCellValue().trim();
                    String value = row.getCell(i) != null ? String.valueOf(row.getCell(i)) : "";

                    document.getMetadata().put(key, value);

                }

                documents.add(document);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file", e);
        }
        HtmlCleaningTransformer.clean(documents);
        return documents;
    }


    public List<Document> read(InputStream excelFileInputStream) {
        List<Document> documents = new ArrayList<>();

        try {
            Workbook workbook = new XSSFWorkbook(excelFileInputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            for (int n = 1; n < sheet.getPhysicalNumberOfRows(); n++) {
                Row row = sheet.getRow(n);
                if (row == null) continue;

                Document document = new Document("");

                for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                    this.headerOrder.add(headerRow.getCell(i).getStringCellValue().trim());
                    String key = headerRow.getCell(i).getStringCellValue().trim();
                    String value = row.getCell(i) != null ? String.valueOf(row.getCell(i)) : "";

                    document.getMetadata().put(key, value);

                }

                documents.add(document);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file", e);
        }
        HtmlCleaningTransformer.clean(documents);
        return documents;
    }
}


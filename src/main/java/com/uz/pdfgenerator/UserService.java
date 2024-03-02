package com.uz.pdfgenerator;

import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public Long create(UserEntity user) {
        return repository.save(user).getId();
    }

    public Page<UserEntity> getList(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    public UserEntity update(Long id, UserEntity user) {
        UserEntity userEntity = repository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("Entity with ID " + id + "  found"));
        return repository.save(userEntity);
    }

    public UserEntity get(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity with ID " + id + " not found"));
    }

    @SneakyThrows
    public void createPDF(Integer page, Integer size, HttpServletResponse response) {

        Page<UserEntity> list = getList(page, size);
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTitle.setSize(20);
        Paragraph paragraph1 = new Paragraph("List of the Users", fontTitle);
        paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph1);
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{3, 3, 3, 3, 3, 3, 3});
        table.setSpacingBefore(5);
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(CMYKColor.BLUE);
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.WHITE);
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Full Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Passport series", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Birthdate", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Gender", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Phone", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Address", font));
        table.addCell(cell);
        for (UserEntity user : list) {
            table.addCell(String.valueOf(user.getId()));
            table.addCell(user.getFullName());
            table.addCell(user.getPassSeries());
            table.addCell(String.valueOf(user.getBirthDate()));
            table.addCell(user.getGender());
            table.addCell(user.getPhone());
            table.addCell(user.getAddress());
        }
        document.add(table);
        document.close();


    }

    public Boolean delete(Long id) {
        UserEntity userEntity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity with ID " + id + " not found"));
        userEntity.setDeletedAt(Timestamp.valueOf(LocalDateTime.now()));
        repository.save(userEntity);
        return true;
    }
}

package com.uz.pdfgenerator;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.UserTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
//@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService service;

    @PostMapping("/users")
    public ResponseEntity<Long> create(@RequestBody UserEntity user) {
        return ResponseEntity.ok(service.create(user));
    }

    @GetMapping("/users")
    public Page<UserEntity> getAll(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                   @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return service.getList(page, size);
    }

    @PutMapping("/users")
    public ResponseEntity<UserEntity> update(@RequestParam Long id, @RequestBody UserEntity user) {
        return ResponseEntity.ok(service.update(id, user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.delete(id));
    }

    @GetMapping("/generate")
    public void generate(@RequestParam(name = "page", defaultValue = "0") Integer page,
                         @RequestParam(name = "size", defaultValue = "10") Integer size, HttpServletResponse response) throws IOException {
        {
            response.setContentType("application/pdf");
            DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
            String currentDateTime = dateFormat.format(new Date());
            String headerkey = "Content-Disposition";
            String headervalue = "attachment; filename=student" + currentDateTime + ".pdf";
            response.setHeader(headerkey, headervalue);
            service.createPDF(page, size, response);
        }
    }
}


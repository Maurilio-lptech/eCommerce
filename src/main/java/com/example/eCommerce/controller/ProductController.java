package com.example.eCommerce.controller;

import com.example.eCommerce.dto.ProductDto;
import com.example.eCommerce.service.ProductService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = "*")
public class ProductController {

    @Value("${file.upload-dir:./uploads/}")
    private String uploadDir;

    @Autowired
    private ProductService service;

    @PostConstruct
    public void init() {
        Path fullPath = Paths.get(uploadDir).toAbsolutePath();
        System.out.println("🛠️ Cartella upload: " + fullPath);
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder!");
        }
    }

    //CRUD
    @PostMapping(value="/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> createProduct(@RequestPart("product")  ProductDto EntityToCreate,
                                                    @RequestPart("image") MultipartFile image) throws IOException {
        if (EntityToCreate.getId() != null) {
            EntityToCreate.setId(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProduct(EntityToCreate, image));
    }

    // Endpoint GET per recuperare un'immagine dal server
    @GetMapping("/images/{filename:.+}")  // (il ".+" serve per catturare l'estensione)
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            // Costruisce il percorso completo del file unendo:
            // - la directory base (uploadDir)
            // - il nome del file passato nell'URL
            Path path = Paths.get(uploadDir + filename);
            System.out.println("Percorso cercato: " + Paths.get(uploadDir).resolve(filename).toAbsolutePath());

            // Carica la risorsa (immagine) come Resource (interfaccia Spring per risorse)
            Resource resource = new UrlResource(path.toUri());

            // Se tutto va bene, restituisce:
            // - Status HTTP 200 (OK)
            // - Header Content-Type appropriato (es. image/jpeg)
            // - Il contenuto dell'immagine come corpo della risposta
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
                    .body(resource);

        } catch (MalformedURLException e1) {
            return ResponseEntity.badRequest().body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable UUID id, @RequestBody ProductDto ProductToUpdate) {
        return ResponseEntity.ok(service.updateProduct(id, ProductToUpdate));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getProductById(id));
    }

    @GetMapping("/searchName")
    public Page<ProductDto> findProductByName(@RequestParam String name, Pageable pageable) {
        return service.getAllProductByName(name, pageable);
    }

    @GetMapping("/Products")
    public Page<ProductDto> findAllProducts(Pageable pageable) {
        return service.getAllProducts(pageable);
    }


    @GetMapping("/search")
    public Page<ProductDto> searchProduct(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double price,
            Pageable pageable
    ) {
        return service.searchProduct(name,category,price,pageable);
    }


}

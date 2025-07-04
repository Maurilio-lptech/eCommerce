package com.example.eCommerce.serviceTest;

import com.example.eCommerce.dto.ProductDto;
import com.example.eCommerce.entity.Category;
import com.example.eCommerce.entity.Product;
import com.example.eCommerce.entity.User;
import com.example.eCommerce.mapper.ProductMapper;
import com.example.eCommerce.repository.CategoryRepository;
import com.example.eCommerce.repository.ProductRepository;
import com.example.eCommerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Value("${file.upload-dir:./uploads/}")
    private String uploadDir;

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ProductMapper mapper;

    //CRUD

    @Transactional(readOnly = true)
    public ProductDto getProductById(UUID id) {
        return mapper.toDto(repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nessun product trovato con id" + id)));
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getAllProductByName(String name, Pageable pageable) {

        Page<Product> productByName = repository.findByNameContains(name, pageable);

        return productByName.map(mapper::toDto);
    }

    @Transactional
    public ProductDto createProduct(
           ProductDto productDto, MultipartFile image) throws IOException {

        if (productDto.getId() != null) {
            throw new IllegalArgumentException("Passa un id nullo durante la creazione");
        }

        Category category = categoryRepository.findById(productDto.getCategory_id())
                .orElseThrow(() -> new EntityNotFoundException("Category con id" + productDto.getCategory_id() + "non trovata"));
        //TODO: aggiungere il check ruolo utente venditore ( si potrebbe usare delle funzioni del service anzichè usare il repository?)
        User seller = userRepository.findById(productDto.getSeller_id())
                .orElseThrow(() -> new EntityNotFoundException("Venditore con id" + productDto.getSeller_id() + "non trovato"));

        Product newProduct = mapper.toEntity(productDto);

        newProduct.setCategory(category);
        newProduct.setSeller(seller);

        // Salvataggio immagine
        String imagePath = saveImage(image);
        newProduct.setImageName(imagePath);


        return mapper.toDto(repository.save(newProduct));
    }

    // Funzione per salvare un'immagine sul filesystem e restituire il nome del file
    private String saveImage(MultipartFile file) throws IOException {
        // Genera un nome file univoco concatenando: UUId e estensione del immagine
        String fileName = "prod-" + UUID.randomUUID() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());

        // Crea un oggetto Path combinando:
        // - La directory di upload (uploadDir, definita in application proprieties)
        // - Il nome file generato
        Path path = Paths.get(uploadDir + fileName);

        // Crea tutte le directory necessarie (se non esistono già)
        // per il percorso specificato
        Files.createDirectories(path.getParent());

        // Copia il contenuto del file multipart (l'immagine uploadata)
        // nel path specificato, sovrascrivendo se esiste già
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // Restituisce solo il nome del file (percorso relativo)
        // che può essere poi usato per riferimento nel database
        return fileName;
    }

    @Transactional
    public void deleteProduct(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Id non trovato nel DB");
        }

        repository.deleteById(id);
    }

    @Transactional
    public ProductDto updateProduct( @NotNull ProductDto productDto, MultipartFile image) throws IOException {
        //TODO:check ruolo venditore
        Product existingProduct = repository.findById(productDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("product con id" + productDto.getId() + " non trovato nel db"));

        Category category = categoryRepository.findById(productDto.getCategory_id())
                .orElseThrow(() -> new EntityNotFoundException("Category con id" + productDto.getCategory_id() + "non trovata"));

        User seller = userRepository.findById(productDto.getSeller_id())
                .orElseThrow(() -> new EntityNotFoundException("Venditore con id" + productDto.getSeller_id() + "non trovato"));


        existingProduct.setName(productDto.getName());
        existingProduct.setCategory(category);
        existingProduct.setSeller(seller);
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setQuantity_available(productDto.getQuantity_available());
        existingProduct.setDescription(productDto.getDescription());

        if (image != null && !image.isEmpty()) {
            String imagePath = saveImage(image);
            existingProduct.setImageName(imagePath);
        }
        return mapper.toDto(repository.save(existingProduct));
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        Page<Product> productPage = repository.findAll(pageable);

        return productPage.map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> searchProduct(String name, String category, Double price, Pageable pageable) {
        return repository.search(name, category, price, pageable).map(mapper::toDto);
    }

}

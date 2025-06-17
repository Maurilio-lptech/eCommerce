package com.example.eCommerce.service;

import com.example.eCommerce.dto.ProductDto;
import com.example.eCommerce.entity.Category;
import com.example.eCommerce.entity.Product;
import com.example.eCommerce.entity.User;
import com.example.eCommerce.mapper.ProductMapper;
import com.example.eCommerce.repository.CategoryRepository;
import com.example.eCommerce.repository.ProductRepository;
import com.example.eCommerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ProductMapper mapper;

    //CRUD

    public ProductDto getProductById(UUID id) {
        return mapper.toDto(repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nessun product trovato con id" + id)));
    }

    public Page<ProductDto> getAllProductByName(String name, Pageable pageable) {

        Page<Product> productByName= repository.findByNameContains(name, pageable);

        return productByName.map(mapper::toDto);
    }

    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        //TODO: aggiungere il check ruolo venditore ( si potrebbe usare delle funzioni del service anzichè usare il repository?)
        if (productDto.getId() != null) {
            throw new IllegalArgumentException("Passa un id nullo durante la creazione");
        }

        Category category=  categoryRepository.findById(productDto.getCategory_id())
                .orElseThrow(()->new EntityNotFoundException("Category con id"+productDto.getCategory_id()+"non trovata"));

        User seller=  userRepository.findById(productDto.getSeller_id())
                .orElseThrow(()->new EntityNotFoundException("Venditore con id"+productDto.getSeller_id()+"non trovato"));

        Product newProduct= mapper.toEntity(productDto);

        newProduct.setCategory(category);
        newProduct.setSeller(seller);
        return mapper.toDto(repository.save(newProduct));
    }

    @Transactional
    public void deleteProduct(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Id non trovato nel DB");
        }

        repository.deleteById(id);
    }

    @Transactional
    public ProductDto updateProduct(UUID id, ProductDto productDto) {
        //TODO:check ruolo venditore
        Product existingProduct = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("product con id" + productDto.getId() + " non trovato nel db"));

        Category category=  categoryRepository.findById(productDto.getCategory_id())
                .orElseThrow(()->new EntityNotFoundException("Category con id"+productDto.getCategory_id()+"non trovata"));

        User seller=  userRepository.findById(productDto.getSeller_id())
                .orElseThrow(()->new EntityNotFoundException("Venditore con id"+productDto.getSeller_id()+"non trovato"));


        Product productToUpdate= mapper.toEntity(productDto);
        productToUpdate.setCategory(category);
        productToUpdate.setSeller(seller);
        return mapper.toDto(repository.save(productToUpdate));
    }

    public Page<ProductDto> getAllProducts(Pageable pageable) {
        Page<Product> productPage = repository.findAll(pageable);

        return productPage.map(mapper::toDto);
    }

    public Page<ProductDto> searchProduct(String name, String category, Double price, Pageable pageable ){
        return repository.search(name,category,price,pageable).map(mapper::toDto);
    }

}

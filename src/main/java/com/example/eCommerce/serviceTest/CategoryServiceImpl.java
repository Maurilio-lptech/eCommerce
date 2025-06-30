package com.example.eCommerce.serviceTest;


import com.example.eCommerce.dto.CategoryDto;
import com.example.eCommerce.entity.Category;
import com.example.eCommerce.mapper.CategoryMapper;
import com.example.eCommerce.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    //CRUD
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(UUID id){
        return mapper.toDto(repository.findById(id)
                .orElseThrow( ()-> new EntityNotFoundException("Nessun utente trovato con id" + id)));
    }

    @Transactional
    public CategoryDto createCategory(@NotNull CategoryDto categoryDto) {
        if(categoryDto.getId()!=null){
            throw  new IllegalArgumentException("Passa un id nullo durante la creazione");
        }

        //check categoria name è presente o no
        if(repository.existsByName(categoryDto.getName())){
            throw new IllegalArgumentException("Nome categoria già presente nel db");
        };

        return mapper.toDto(repository.save(mapper.toEntity(categoryDto)));
    }

    @Transactional
    public void deleteCategory(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Id non trovato nel DB");
        }

        repository.deleteById(id);
    }

    @Transactional
    public CategoryDto updateCategory(UUID id, @NotNull CategoryDto categoryDto) {

        Category existingCategory= repository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException ("category con id"+ categoryDto.getId()+ " non trovato nel db"));

        //check categoria name è presente o no
        if(repository.existsByName(categoryDto.getName())){
            throw new IllegalArgumentException("Nome categoria già presente nel db");
        }

        existingCategory.setName(categoryDto.getName());
        existingCategory.setDescription(categoryDto.getDescription());

        return mapper.toDto(repository.save(existingCategory));
    }

    @Transactional(readOnly = true)
    public Page<CategoryDto> getAllCategories(Pageable pageable){
        Page<Category> categoryPage = repository.findAll(pageable);

        return categoryPage.map(mapper::toDto);
    }
}

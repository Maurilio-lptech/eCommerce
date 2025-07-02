package com.example.eCommerce.controllerTest;

import com.example.eCommerce.controller.ProductController;
import com.example.eCommerce.dto.ProductDto;
import com.example.eCommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @TempDir
    Path tempUploadDir;

    private ProductDto sampleProduct;
    private UUID sampleProductId;

    @BeforeEach
    public void setup() throws IOException {
        // Initialize test directory and controller
        ReflectionTestUtils.setField(productController, "uploadDir", tempUploadDir.toString() + "/");
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        // Create sample product
        sampleProductId = UUID.randomUUID();
        sampleProduct = new ProductDto();
        sampleProduct.setId(sampleProductId);
        sampleProduct.setName("Test Product");
        sampleProduct.setPrice(100.0);
    }

    // CRUD Tests
    @Test
    @WithMockUser(roles = "ADMIN")
    public void createProduct_ShouldReturnCreated_WhenValidRequest() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image".getBytes()
        );

        MockMultipartFile productJson = new MockMultipartFile(
                "product",
                "",
                "application/json",
                ("{\"name\":\"Test Product\",\"price\":100.0}").getBytes()
        );

        when(productService.createProduct(any(ProductDto.class), any()))
                .thenReturn(sampleProduct);

        mockMvc.perform(multipart("/api/product/new")
                        .file(image)
                        .file(productJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(sampleProductId.toString()))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteProduct_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/product/delete/{id}", sampleProductId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getProductById_ShouldReturnProduct() throws Exception {
        when(productService.getProductById(sampleProductId))
                .thenReturn(sampleProduct);

        mockMvc.perform(get("/api/product/{id}", sampleProductId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleProductId.toString()));
    }

    // Image Tests
    @Test
    @WithMockUser(roles = "ADMIN")
    public void getImage_ShouldReturnImage_WhenFileExists() throws Exception {
        String filename = "test-image.jpg";
        Path imagePath = tempUploadDir.resolve(filename);
        Files.write(imagePath, "test image content".getBytes());

        mockMvc.perform(get("/api/product/images/{filename}", filename))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.CONTENT_TYPE))
                .andExpect(content().bytes(Files.readAllBytes(imagePath)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getImage_ShouldReturnInternalServerError_WhenFileNotFound() throws Exception {
        mockMvc.perform(get("/api/product/images/prod-df7bb33c-25d3-4d54-b8bb-9315762a5725345.jpg"))
                .andExpect(status().isInternalServerError());
    }

//    @Test
//    @WithMockUser(roles = "ADMIN")
//    public void updateProduct_ShouldReturnOk_WhenValidRequest() throws Exception {
//        MockMultipartFile image = new MockMultipartFile(
//                "image",
//                "update.jpg",
//                "image/jpeg",
//                "updated image".getBytes()
//        );
//
//        MockMultipartFile productJson = new MockMultipartFile(
//                "product",
//                "",
//                "application/json",
//                ("{\"id\":\"" + sampleProductId + "\",\"name\":\"Updated Product\",\"price\":150.0}").getBytes()
//        );
//
//        ProductDto updatedProduct = new ProductDto();
//        updatedProduct.setId(sampleProductId);
//        updatedProduct.setName("Updated Product");
//        updatedProduct.setPrice(150.0);
//
//        when(productService.updateProduct(any(ProductDto.class), any()))
//                .thenReturn(updatedProduct);
//
//        mockMvc.perform(multipart("/api/product/update")
//                        .file(image)
//                        .file(productJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Updated Product"));
//    }






}
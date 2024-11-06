package com.example.StockManagement.controller;

import com.example.StockManagement.data.model.Product;
import com.example.StockManagement.service.ProductService;
import com.example.StockManagement.util.XMLToJSONConverter; // Import the utility class
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Method to serve the Thymeleaf view for all products
    @GetMapping
    public String showProductsPage(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Show the form to add a new product
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product"; // View for adding a new product
    }

    // Handle form submission for adding a product
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/products"; // Redirect to product list after addition
    }

    // Show the form to edit an existing product
    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "edit-product"; // View for editing a product
        }
        return "redirect:/products"; // Redirect if product not found
    }

    // Handle form submission for editing a product
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable("id") Long id, @ModelAttribute Product product) {
        product.setId(id); // Ensure the product ID is set for updating
        productService.updateProduct(product); // Your update logic
        return "redirect:/products"; // Redirect after update
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.findById(id).isPresent()) {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            productService.importProducts(file);
            return ResponseEntity.ok("Upload Successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to import file: " + e.getMessage());
        }
    }

    @GetMapping("/export-to-excel")
    public ResponseEntity<InputStreamResource> exportProducts() {
        ByteArrayInputStream byteArrayInputStream = productService.exportProductsToExcel();
        if (byteArrayInputStream == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=products.xlsx");
        return new ResponseEntity<>(new InputStreamResource(byteArrayInputStream), headers, HttpStatus.OK);
    }

    // New endpoint to convert XML to JSON
    @PostMapping("/convert-xml-to-json")
    public ResponseEntity<String> convertXmlToJson(@RequestBody String xml) {
        try {
            // Convert XML to JSON using the utility method
            String json = XMLToJSONConverter.convertXMLToJSON(xml);
            return ResponseEntity.ok(json); // Return the converted JSON
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to convert XML to JSON: " + e.getMessage());
        }
    }
}

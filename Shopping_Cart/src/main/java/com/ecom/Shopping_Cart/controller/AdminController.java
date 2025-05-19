package com.ecom.Shopping_Cart.controller;


import com.ecom.Shopping_Cart.model.Category;
import com.ecom.Shopping_Cart.model.Product;
import com.ecom.Shopping_Cart.service.CategoryService;
import com.ecom.Shopping_Cart.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String index() {
        return "admin/Index";
    }

    @GetMapping("/load_product")
    public String loadProduct(Model m) {
        List<Category> categories = categoryService.getAllCategory();
        m.addAttribute("categories", categories);
        return "admin/add_product";
    }

    @GetMapping("/category")
    public String category(Model m) {
        m.addAttribute("categorys", categoryService.getAllCategory());
        return "admin/category";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category,
                               @RequestParam("file") MultipartFile file,
                               HttpSession session) {
        try {
            String imageName = (file != null && !file.isEmpty()) ? file.getOriginalFilename() : "default.jpg";
            category.setImageName(imageName);

            Boolean existCategory = categoryService.existCategory(category.getName());
            if (existCategory) {
                session.setAttribute("errorMsg", "Category Name already exists");
            } else {
                Category savedCategory = categoryService.saveCategory(category);

                if (ObjectUtils.isEmpty(savedCategory)) {
                    session.setAttribute("errorMsg", "Not saved! Internal server error");
                } else {
                    if (file != null && !file.isEmpty()) {
                        File saveDir = new ClassPathResource("static/image").getFile();
                        Path imageDirPath = Paths.get(saveDir.getAbsolutePath(), "category_img");

                        // Ensure directory exists
                        if (!Files.exists(imageDirPath)) {
                            Files.createDirectories(imageDirPath);
                        }

                        Path fullPath = imageDirPath.resolve(file.getOriginalFilename());
                        Files.copy(file.getInputStream(), fullPath, StandardCopyOption.REPLACE_EXISTING);
                    }

                    session.setAttribute("succMsg", "Saved Successfully");
                }
            }
        } catch (IOException e) {
            session.setAttribute("errorMsg", "Failed to upload image: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/admin/category";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession session) {
        Boolean deleteCategory = categoryService.deleteCategory(id);
        if (deleteCategory) {
            session.setAttribute("succMsg", "Category deleted successfully");
        } else {
            session.setAttribute("errorMsg", "Something went wrong");
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/loadEditCategory/{id}")
    public String loadEditCategory(@PathVariable int id, Model m) {
        m.addAttribute("category", categoryService.getCategoryById(id));
        return "admin/edit_category";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
                                 HttpSession session) throws IOException {

        Category oldCategory = categoryService.getCategoryById(category.getId());
        String imageName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();

        if (!ObjectUtils.isEmpty(category)) {
            oldCategory.setName(category.getName());
            oldCategory.setIsActive(category.getIsActive());
            oldCategory.setImageName(imageName);
        }
        Category updateCategory = categoryService.saveCategory(oldCategory);
        if (!ObjectUtils.isEmpty(updateCategory)) {
            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/image/").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator +
                        file.getOriginalFilename());

                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }

            session.setAttribute("succMsg", "Category updated successfully");
        } else {
            session.setAttribute("errorMsg", "Something went wrong");
        }
        return "redirect:/admin/loadEditCategory/" + category.getId();
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
                              HttpSession session) throws IOException {

        String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();
        product.setImage(imageName);

        Product saveProduct = productService.saveProduct(product);

        if (!ObjectUtils.isEmpty(saveProduct)) {
            // Ensure the directory exists
            File saveFile = new ClassPathResource("static/image").getFile();
            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img");

            // Create the product_img directory if it does not exist
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            // Copy the file to the directory
            Path filePath = path.resolve(image.getOriginalFilename());
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            session.setAttribute("succMsg", "Product saved successfully");
        } else {
            session.setAttribute("errorMsg", "Something went wrong on the server");
        }

        return "redirect:/admin/load_product";
    }

    @GetMapping("/products")
    public String loadViewProduct(Model m)
    {
        m.addAttribute("products", productService.getAllProducts());
        return "admin/products";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id, HttpSession session) {
        Boolean deleteProduct = productService.deleteProduct(id);
        if (deleteProduct) {
            session.setAttribute("succMsg", "Product delete success");
        } else {
            session.setAttribute("errorMsg", "Something wrong on server");
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable int id ,Model m)
    {
        m.addAttribute("product", productService.getProductById(id));
        m.addAttribute("categories", categoryService.getAllCategory());
        return "admin/edit_product";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
                                HttpSession session, Model m) {

        Product updateProduct = productService.updateProduct(product, image);
        if (!ObjectUtils.isEmpty(updateProduct)) {
            session.setAttribute("succMsg", "Product update success");
        } else {
            session.setAttribute("errorMsg", "Something wrong on server");
        }

        return "redirect:/admin/editProduct/" + product.getId();
    }

}

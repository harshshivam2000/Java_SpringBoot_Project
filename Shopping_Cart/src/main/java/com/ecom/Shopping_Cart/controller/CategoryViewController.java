package com.ecom.Shopping_Cart.controller;

import com.ecom.Shopping_Cart.model.Category;
import com.ecom.Shopping_Cart.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/category")
public class CategoryViewController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/products/{categoryName}")
    public String viewCategoryDetails(@PathVariable String categoryName, Model model) {

        // 1. All categories (for sidebar)
        List<Category> categoryList = categoryRepository.findAll();
        model.addAttribute("categories", categoryList);

        // 2. Set selected category name
        model.addAttribute("selectedCategory", categoryName);

        // 3. Normalize category name for lookup
        String key = categoryName.trim().toLowerCase().replaceAll("\\s", "");

        // 4. Get product details for that category
        List<ProductView> products = getCategoryDetails(key);

        // If product details not found, redirect to error
        if (products.isEmpty()) {
            return "redirect:/error";
        }

        // 5. Add products to model
        model.addAttribute("products", products);

        // 6. Return template
        return "admin/category";
    }

    private List<ProductView> getCategoryDetails(String categoryKey) {
        List<ProductView> details = new ArrayList<>();

        switch (categoryKey) {
            case "skincare":
                details.add(new ProductView("Cleansers", "Remove dirt, oil, makeup, and impurities. Examples: foaming cleanser, gel cleanser, cream cleanser, micellar water.", "/images/skincare/cleansers.png"));
                details.add(new ProductView("Toners", "Balance skin’s pH, tighten pores, and prep skin for serums/moisturizers. Examples: hydrating toner, exfoliating toner, clarifying toner.", "/images/skincare/toner.png"));
                details.add(new ProductView("Exfoliants", "Remove dead skin cells and promote cell turnover. Types: Physical (scrubs), Chemical (AHAs, BHAs).", "/images/skincare/exfoliants.png"));
                details.add(new ProductView("Serums", "Concentrated ingredients: Vitamin C, Hyaluronic acid, Niacinamide, Retinol.", "/images/skincare/serums.png"));
                details.add(new ProductView("Moisturizers", "Hydrate and lock in moisture. Gel (oily skin), Cream (dry skin).", "/images/skincare/moisturizers.png"));
                details.add(new ProductView("Sunscreens", "Protect against UVA/UVB. Types: Physical (mineral), Chemical.", "/images/skincare/sunscreens.png"));
                details.add(new ProductView("Face Masks", "Clay masks, Sheet masks, Overnight masks.", "/images/skincare/masks.png"));
                details.add(new ProductView("Eye Creams", "Target dark circles, puffiness, and fine lines.", "/images/skincare/eyecreams.png"));
                details.add(new ProductView("Treatments & Spot Care", "Examples: salicylic acid gel, benzoyl peroxide, pimple patches.", "/images/skincare/treatments.png"));
                details.add(new ProductView("Lip Care", "Balms, scrubs, masks.", "/images/skincare/lipcare.png"));
                break;

            // Add more categories here...

            default:
                break;
        }

        return details;
    }

    // Inner class to represent product for display
    public static class ProductView {
        private final String name;
        private final String description;
        private final String imageUrl;

        public ProductView(String name, String description, String imageUrl) {
            this.name = name;
            this.description = description;
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}

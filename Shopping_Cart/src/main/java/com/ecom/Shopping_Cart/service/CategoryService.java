package com.ecom.Shopping_Cart.service;
import java.util.List;

import com.ecom.Shopping_Cart.model.Category;
public interface CategoryService  {



        public Category saveCategory(Category category);

        public Boolean existCategory(String name );

        public List<Category> getAllCategory();


        public Boolean deleteCategory(int id);
        //public Category getCategory(int id);

        public Category getCategoryById(int id);
}

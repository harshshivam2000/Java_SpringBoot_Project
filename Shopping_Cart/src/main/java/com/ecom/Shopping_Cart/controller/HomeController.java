package com.ecom.Shopping_Cart.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index()
    {
        return "index";
    }

    @GetMapping("/login")
    public String login()
    {

        return "login";
    }

    @GetMapping("/register")
    public String register()
    {

        return "register";
    }

    @GetMapping("/product")
    public String product()
    {

        return "product";
    }

    @GetMapping("/products")
    public String products()
    {

        return "view_product_details";
    }


}

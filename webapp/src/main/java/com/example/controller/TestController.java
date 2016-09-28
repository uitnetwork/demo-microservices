package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Created by ninhdoan on 8/22/16.
 */
@RestController
public class TestController {

   @RequestMapping("/test")
   public String abc(Principal principal) {
      System.out.println(principal);
      return principal.getName().toString();
   }
}

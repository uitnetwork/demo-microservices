package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Created by ninhdoan on 8/22/16.
 */
@RestController
public class TestController {
   @RequestMapping("/me")
   public Principal abc(Principal hhh) {
      System.out.println("come here!!!!!!!!!!!!!!");
      return hhh;
   }

   @RequestMapping("/what")
   public String abc() {
      return "just something from resource server";
   }
}

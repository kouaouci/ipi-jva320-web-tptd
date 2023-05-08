package com.ipi.jva320.controller;

import com.ipi.jva320.service.SalarieAideADomicileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller// et non @RestController sinon home.html affiche "home" ("réponse rest")
public class HomeController {
    @Autowired
    public SalarieAideADomicileService salairieService;
    // @RequestMapping(value = {"/", "/home", "/home.html"})
    @GetMapping(value = "/")
    public String home(final ModelMap model){
        model.put ( "salarieCount", salairieService.countSalaries ());
        // Afficher le nombre de salairie

        return "home";
    }
    //Message en page d'accueil
}

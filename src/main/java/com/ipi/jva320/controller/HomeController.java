package com.ipi.jva320.controller;

import com.ipi.jva320.model.SalarieAideADomicile;
import com.ipi.jva320.service.SalarieAideADomicileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller// et non @RestController sinon home.html affiche "home" ("réponse rest")
public class HomeController {
    @Autowired
    public SalarieAideADomicileService salairieService;
    @RequestMapping(value = {"/", "/home", "/home.html"})
    @GetMapping(value = "/")
    public String home(final ModelMap model){
        // Afficher le nombre de salairie
        model.put ( "salarieCount", Long.toString (salairieService.countSalaries ()));
        model.put("title","Aide à domicile RH - gestion des salariés");
                 return "home";
    }

}

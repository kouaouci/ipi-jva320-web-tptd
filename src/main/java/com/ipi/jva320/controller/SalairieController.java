package com.ipi.jva320.controller;

import com.ipi.jva320.exception.SalarieException;
import com.ipi.jva320.model.SalarieAideADomicile;
import com.ipi.jva320.service.SalarieAideADomicileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
public class SalairieController {
    @Autowired
    SalarieAideADomicileService salairieService;

    @GetMapping("/salaries/{id}")
    public String getSalairie(final ModelMap model, @PathVariable Long id) throws SalarieException {

        SalarieAideADomicile salairie = salairieService.getSalarie ( id );
        if (salairie == null) {
            return "redirect:/home";
        } else {
            model.put ( "details", salairie );
            model.addAttribute ( "salarie", salairie );
            System.out.println ( salairie );
            return "detail_salarie";
        }
    }
    // add new salarie
    @PostMapping(value = "/salaries/save")
    public String addNewSalarie(@ModelAttribute SalarieAideADomicile salarieAideADomicile, final ModelMap model) throws SalarieException{
        salairieService.creerSalarieAideADomicile ( salarieAideADomicile );
        model.addAttribute("salariees", salairieService.getSalaries());
        return "list";

    }

    // update un salarie
    @PostMapping("/salaries/update")
    public String updateSalarie(@ModelAttribute SalarieAideADomicile salarieAideADomicile, final ModelMap model, @RequestParam(required = false) Long id) throws SalarieException{
        if(id!=null){
            salarieAideADomicile.setId(id);
        }
        System.out.println (salarieAideADomicile.getId ());
        salairieService.updateSalarieAideADomicile ( salarieAideADomicile );
        model.addAttribute("salariees", salairieService.getSalaries());
        return "list";
    }
    // delete
    @GetMapping("/salaries/{id}/delete")
    public String deleteSalarie(@PathVariable Long id) throws SalarieException{
        salairieService.deleteSalarieAideADomicile ( id );
        return "redirect:/home";
    }
}

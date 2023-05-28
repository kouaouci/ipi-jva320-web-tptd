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
    /*
    @RequestMapping("/salaries")
    public String redirectToListWithParams(@RequestParam(required = false, name = "nom") String nom,
                                           @RequestParam(required = false, name = "page", defaultValue = "1") int page,
                                           @RequestParam(required = false, name = "size", defaultValue = "10") int size,
                                           @RequestParam(required = false, name = "sortDirection", defaultValue = "desc") String sortDirection,
                                           @RequestParam(required = false, name = "sortProperty", defaultValue = "id") String sortProperty,
                                           final ModelMap model) {
        Pageable pageable;
        if (Objects.equals(sortDirection, "asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortProperty).descending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortProperty).ascending());
        }
        if (nom == null || nom.length() <= 0) {
            Page itemPages = salarieAideADomicileService.getSalaries(pageable);
            model.addAttribute("salariees", itemPages.getContent());
        } else {
            model.addAttribute("salariees", salarieAideADomicileService.getSalaries(pageable));
        }
        model.put("page", page);
        model.put("sortdir", sortDirection);
        model.put("sortpro", sortProperty);
        if(page == 0) {
            model.put("disable", true);
        } else {
            model.put("disable", false);
        }

        return "list";
    }*/

  /*  @RequestMapping("/salaries/aide/new")
    public String redirectToAdd(final ModelMap model) {
        SalarieAideADomicile aide = new SalarieAideADomicile();
        aide.setId(0L);
        model.put("details", aide);
        model.put("action", "save");
        model.addAttribute("salarie", new SalarieAideADomicile());
        return "detail_Salarie";
    }*/


}

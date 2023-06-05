package com.ipi.jva320.controller;

import com.ipi.jva320.exception.SalarieException;
import com.ipi.jva320.model.SalarieAideADomicile;
import com.ipi.jva320.repository.SalarieAideADomicileRepository;
import com.ipi.jva320.service.SalarieAideADomicileService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.security.Timestamp;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Controller
public class SalairieController {
    @Autowired
    SalarieAideADomicileService salairieService;
    @Autowired
    private SalarieAideADomicileRepository salarieAideADomicileRepository;


    @GetMapping("/salaries/{id}")
    public String getSalairieDetails(final ModelMap model, @PathVariable Long id) {


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


    @PostMapping("/salaries/save")
    public String addNewSalarie(@ModelAttribute SalarieAideADomicile salarie, final ModelMap model) throws SalarieException {

        System.out.println ( "hello" );
        salairieService.creerSalarieAideADomicile ( salarie );
        System.out.println ( salarie );
        model.addAttribute ( "salariees", salairieService.getSalaries () );
      return  "redirect:/salaries/" + salarie.getId();

    }

    @PostMapping("/salaries/update")
    public String updateSalarie(@ModelAttribute SalarieAideADomicile salarie, final ModelMap model, @RequestParam(required = false) Long id) throws SalarieException {
        if (id != null) {
            salarie.setId ( id );
        }

        System.out.println ( salarie.getId () );
        salairieService.updateSalarieAideADomicile ( salarie );
        model.addAttribute ( "salariess", salairieService.getSalaries () );
        return "redirect:/list";
    }


    //Ajouter un salarie
    @GetMapping("/salaries/aide/new")
    public String salarieInputSave( ModelMap model) {
        System.out.println ( "hello" );
        SalarieAideADomicile salarie = new SalarieAideADomicile ();
        salarie.setId ( 0L );
        model.put ( "details", salarie );
        model.put ( "action", "save" );
        model.addAttribute ( "salarie", new SalarieAideADomicile () );
        return "detail_Salarie";

    }

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
            Page itemPages = salairieService.getSalaries(pageable);
            model.addAttribute("salariees", itemPages.getContent());
        } else {
            model.addAttribute("salariees", salairieService.getSalaries(nom));
        }
        model.put("page", page);
        model.put("sortdir", sortDirection);
        model.put("sortpro", sortProperty);
        if (page == 0) {
            model.put("disable", true);
        } else {
            model.put("disable", false);
        }

        return "list";
    }
    // Récuperation de la liste des salaries
    @GetMapping(value="salaries")
    public String getSalaries(final ModelMap model) {
        model.addAttribute("salariees", salairieService.getSalaries());
        return "list";
    }

    @RequestMapping("/salaries/aide/new")
    public String redirectToAdd(final ModelMap model) {
        SalarieAideADomicile salarie = new SalarieAideADomicile();
        salarie.setId(0L);
        model.put("details",salarie );
       model.put("action", "save");
        model.addAttribute("salarie", new SalarieAideADomicile());
        return "detail_Salarie";
    }

    @GetMapping("/salaries/{id}/delete")
    public String deleteSalarie(@PathVariable Long id) throws SalarieException {
        salairieService.deleteSalarieAideADomicile(id);
        return "redirect:/home";
    }



}




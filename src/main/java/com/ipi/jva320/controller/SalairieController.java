package com.ipi.jva320.controller;

import com.ipi.jva320.exception.SalarieException;
import com.ipi.jva320.model.SalarieAideADomicile;
import com.ipi.jva320.repository.SalarieAideADomicileRepository;
import com.ipi.jva320.service.SalarieAideADomicileService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Controller
public class SalairieController {
    @Autowired
    SalarieAideADomicileService salairieService;
    @Autowired
    private SalarieAideADomicileRepository salarieAideADomicileRepository;


    @RequestMapping(value = {"/salaries", "/salaries.html"})
    public String listSalarie(final ModelMap model) {
        model.put ( "salariees", salairieService.getSalaries () );
        return "list";
    }

    @GetMapping(value = {"/salaries/{id}", "/salaries/aide/{id}"})
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

    @GetMapping("/salaries/{id}/delete")
    public String deleteSalarie(@PathVariable Long id,final ModelMap model) throws SalarieException {
        salairieService.deleteSalarieAideADomicile ( Long.valueOf ( id ) );

        return "redirect:/home";

    }

    // afficher le nombre des saliere
    @GetMapping("/home")
    public String countSalaries(final ModelMap model) {
        model.put ( "salarieCount", Long.toString ( salairieService.countSalaries () ) );
        return "home";
    }

    //Ajouter un salarie
    @GetMapping(value = "/salaries/aide/new")
    public String salarieInputSave(final ModelMap model) {
        SalarieAideADomicile salarie = new SalarieAideADomicile();
        model.put("salarie",salarie);
        model.put("title","Gestion des salariés");
        model.put("salarieCount", salairieService.countSalaries());

        return "detail_Salarie";
    }

    @RequestMapping(value="/salaries/save")
    public String addNewSalarie(@ModelAttribute SalarieAideADomicile salarieAideADomicile, final ModelMap model) throws SalarieException {
        salairieService.creerSalarieAideADomicile(salarieAideADomicile);
        model.addAttribute("salariees", salairieService.getSalaries());
        return "list";
    }

    @PostMapping ("/salaries/update")
    public String updateSalarie(@ModelAttribute SalarieAideADomicile salarieAideADomicile, final ModelMap model,@RequestParam(required = false) Long id) throws SalarieException {
        if(id!=null && !Objects.equals(id, salarieAideADomicile.getId())) {
            throw new SalarieException("L'id du salarie ne correspond pas");
        }

        salairieService.updateSalarieAideADomicile(salarieAideADomicile);
        model.addAttribute("salariees", salairieService.getSalaries());
        return "list";
    }


    @GetMapping(value = "/salaries")
    public String salaries(
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortProperty,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(required = false) String nom,
            final ModelMap model) {
        int currentPage;
        int pageSize;
        List<SalarieAideADomicile> listSalaries = Collections.emptyList();
        Page<SalarieAideADomicile> salaries = new PageImpl<> (listSalaries);


        if(page != null){
            currentPage = Integer.parseInt(page);
        }
        else{
            currentPage = 0;
        }
        if(size != null){
            pageSize = Integer.parseInt(size);
        }
        else{
            pageSize = 10;
        }
        if(nom != null){
            listSalaries = salairieService.getSalaries(nom, PageRequest.of(currentPage, pageSize, Sort.by("id")));
        }
        else{
            salaries = salairieService.getSalaries(
                    PageRequest.of(currentPage, pageSize, Sort.by("id")));
        }


        if(Objects.equals(sortProperty, "id")){
            if (Objects.equals(sortDirection, "ASC")) {
                salaries = salairieService.getSalaries(
                        PageRequest.of(currentPage, pageSize, Sort.by("id").ascending())
                );
            } else {
                salaries = salairieService.getSalaries(
                        PageRequest.of(currentPage, pageSize, Sort.by("id").descending())
                );
            }
        }
        else if(Objects.equals(sortProperty, "nom")){
            if (Objects.equals(sortDirection, "ASC")) {
                salaries = salairieService.getSalaries(
                        PageRequest.of(currentPage, pageSize, Sort.by("nom").ascending())
                );
            } else {
                salaries = salairieService.getSalaries(
                        PageRequest.of(currentPage, pageSize, Sort.by("nom").descending())
                );
            }
        }
        model.put("currentPage", currentPage);
        model.put("maxPage", Math.ceil((float) salairieService.countSalaries()/pageSize));
        model.put("salarieCount", salairieService.countSalaries());
        if(nom != null){
            model.put("salaries",listSalaries);
        }
        else{
            model.put("salaries",salaries);
        }
        model.put("title","Gestion des salariés");

        return "list";
    }









    @PostMapping(value = "/salaries/save")
    public String salarieSaveForm(@ModelAttribute SalarieAideADomicile salarieAideADomicile) throws SalarieException {
        if(salarieAideADomicile.getId() != null){
            try{
                if(salarieAideADomicile.getNom().isEmpty()){
                    throw new DataIntegrityViolationException ("Empty name");
                }
                salairieService.updateSalarieAideADomicile(salarieAideADomicile);
                //error = false;
            }
            catch (DataIntegrityViolationException e){
                //error = true;
            }
        }
        else{
            try{
                if(salarieAideADomicile.getNom().isEmpty()){
                    throw new DataIntegrityViolationException("Empty name");
                }
                SalarieAideADomicile _salarieAideADomicile = salairieService.creerSalarieAideADomicile(salarieAideADomicile);
                //error = false;
                return "redirect:/salaries/" + _salarieAideADomicile.getId();
            }
            catch (DataIntegrityViolationException e){
                //error = true;
                return "redirect:/salaries/aide/new";
            }
        }
        return "redirect:/salaries/" + salarieAideADomicile.getId();
    }


    // chercher par nom



}



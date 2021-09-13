package com.ubt.gymms.controllers.administration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ubt.gymms.entities.administration.PersonDTO;
import com.ubt.gymms.services.feignClients.IdentityService;
import feign.FeignException;

@Controller
public class PersonController {

    @Autowired
    private IdentityService identityService;

    @GetMapping("/persons")
    @PreAuthorize("hasAuthority('READ_PERSON')")
    public String persons(Model model){
        model.addAttribute("persons", identityService.persons().getBody());
        return "administration/persons/persons";
    }

    @GetMapping("/addPerson")
    @PreAuthorize("hasAuthority('WRITE_PERSON')")
    public String addPerson(){
        return "administration/persons/addPerson";
    }

    @PostMapping(value = "/addPerson", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('WRITE_PERSON')")
    public ModelAndView addPerson(@ModelAttribute PersonDTO personDTO, @RequestParam("image") MultipartFile image){
        try {
            ResponseEntity<PersonDTO> responseEntity = identityService.addPerson(personDTO, image);
            ModelAndView modelAndView = new ModelAndView("administration/persons/persons");
            modelAndView.addObject("isCreated", responseEntity.getStatusCode() == HttpStatus.OK);
            modelAndView.addObject("persons", identityService.persons().getBody());
            return modelAndView;
        }
        catch (FeignException ex) {
            ModelAndView modelAndView = new ModelAndView("administration/persons/addPerson");
            modelAndView.addObject("personDTO", personDTO);
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/editPerson/{id}")
    @PreAuthorize("hasAuthority('WRITE_PERSON')")
    public String editPerson(@PathVariable Long id, Model model){
        ResponseEntity<PersonDTO> responseEntity = identityService.editPerson(id);
        model.addAttribute("personDTO", responseEntity.getBody());
        model.addAttribute("gender", responseEntity.getBody().getGender().equals("M"));
        return "administration/persons/editPerson";
    }

    @PutMapping(value = "/editPerson", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('WRITE_PERSON')")
    public ModelAndView editPerson(@ModelAttribute PersonDTO personDTO, @RequestParam("image") MultipartFile image){
        try {
            ResponseEntity<PersonDTO> responseEntity = identityService.editPerson(personDTO, image);
            ModelAndView modelAndView = new ModelAndView("administration/persons/persons");
            modelAndView.addObject("isUpdated", responseEntity.getStatusCode() == HttpStatus.OK);
            modelAndView.addObject("persons", identityService.persons().getBody());
            return modelAndView;
        }
        catch (FeignException ex) {
            ModelAndView modelAndView = new ModelAndView("administration/persons/editPerson");
            modelAndView.addObject("personDTO", personDTO);
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/disablePerson/{id}")
    @PreAuthorize("hasAuthority('WRITE_PERSON')")
    public String disablePerson(@PathVariable Long id, Model model){
        try {
            ResponseEntity disabled = identityService.disablePerson(id);
            model.addAttribute("isDisabled", disabled.getStatusCode() == HttpStatus.OK);
            model.addAttribute("persons", identityService.persons().getBody());
            return "administration/persons/persons";
        }
        catch (FeignException ex){
            model.addAttribute("isDisabled", false);
            model.addAttribute("persons", identityService.persons().getBody());
            return "administration/persons/persons";
        }
    }

    @GetMapping("/enablePerson/{id}")
    @PreAuthorize("hasAuthority('WRITE_PERSON')")
    public String enablePerson(@PathVariable Long id, Model model){
        try {
            ResponseEntity enabled = identityService.enablePerson(id);
            model.addAttribute("isEnabled", enabled.getStatusCode() == HttpStatus.OK);
            model.addAttribute("persons", identityService.persons().getBody());
            return "administration/persons/persons";
        }
        catch (FeignException ex){
            model.addAttribute("isEnabled", false);
            model.addAttribute("persons", identityService.persons().getBody());
            return "administration/persons/persons";
        }
    }

    @GetMapping("/person/images/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        return identityService.getImage(id);
    }
}

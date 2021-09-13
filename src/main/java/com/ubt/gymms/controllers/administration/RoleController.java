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
import org.springframework.web.servlet.ModelAndView;

import com.ubt.gymms.entities.administration.RoleDTO;
import com.ubt.gymms.services.feignClients.IdentityService;
import feign.FeignException;

@Controller
public class RoleController {

    @Autowired
    private IdentityService identityService;

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('READ_ROLE')")
    public String roles(Model model){
        model.addAttribute("roles", identityService.roles().getBody());
        return "administration/roles/roles";
    }

    @GetMapping("/addRole")
    @PreAuthorize("hasAuthority('WRITE_ROLE')")
    public String addRole(){
        return "administration/roles/addRole";
    }

    @PostMapping(value = "/addRole", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('WRITE_ROLE')")
    public ModelAndView addRole(@ModelAttribute RoleDTO roleDTO){
        try {
            ResponseEntity<RoleDTO> responseEntity = identityService.addRole(roleDTO);
            ModelAndView modelAndView = new ModelAndView("administration/roles/roles");
            modelAndView.addObject("isCreated", responseEntity.getStatusCode() == HttpStatus.OK);
            modelAndView.addObject("roles", identityService.roles().getBody());
            return modelAndView;
        }
        catch (FeignException ex) {
            ModelAndView modelAndView = new ModelAndView("administration/roles/addRole");
            modelAndView.addObject("roleDTO", roleDTO);
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/editRole/{id}")
    @PreAuthorize("hasAuthority('WRITE_ROLE')")
    public String editRole(@PathVariable Long id, Model model){
        ResponseEntity<RoleDTO> responseEntity = identityService.editRole(id);
        model.addAttribute("roleDTO", responseEntity.getBody());
        return "administration/roles/editRole";
    }

    @PutMapping(value = "/editRole", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('WRITE_ROLE')")
    public ModelAndView editRole(@ModelAttribute RoleDTO roleDTO){
        try {
            ResponseEntity<RoleDTO> responseEntity = identityService.editRole(roleDTO);
            ModelAndView modelAndView = new ModelAndView("administration/roles/roles");
            modelAndView.addObject("isUpdated", responseEntity.getStatusCode() == HttpStatus.OK);
            modelAndView.addObject("roles", identityService.roles().getBody());
            return modelAndView;
        }
        catch (FeignException ex) {
            ModelAndView modelAndView = new ModelAndView("administration/roles/editRole");
            modelAndView.addObject("roleDTO", roleDTO);
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/disableRole/{id}")
    @PreAuthorize("hasAuthority('WRITE_ROLE')")
    public String disableRole(@PathVariable Long id, Model model){
        try {
            ResponseEntity disabled = identityService.disableRole(id);
            model.addAttribute("isDisabled", disabled.getStatusCode() == HttpStatus.OK);
            model.addAttribute("roles", identityService.roles().getBody());
            return "administration/roles/roles";
        }
        catch (FeignException ex){
            model.addAttribute("isDisabled", false);
            model.addAttribute("roles", identityService.roles().getBody());
            return "administration/roles/roles";
        }
    }

    @GetMapping("/enableRole/{id}")
    @PreAuthorize("hasAuthority('WRITE_ROLE')")
    public String enableRole(@PathVariable Long id, Model model){
        try {
            ResponseEntity enabled = identityService.enableRole(id);
            model.addAttribute("isEnabled", enabled.getStatusCode() == HttpStatus.OK);
            model.addAttribute("roles", identityService.roles().getBody());
            return "administration/roles/roles";
        }
        catch (FeignException ex){
            model.addAttribute("isEnabled", false);
            model.addAttribute("roles", identityService.roles().getBody());
            return "administration/roles/roles";
        }
    }
}
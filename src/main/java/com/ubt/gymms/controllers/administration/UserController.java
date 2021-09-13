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

import com.ubt.gymms.entities.administration.UserDTO;
import com.ubt.gymms.services.feignClients.IdentityService;
import feign.FeignException;

@Controller
public class UserController {

    @Autowired
    private IdentityService identityService;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('READ_USER')")
    public String users(Model model){
        model.addAttribute("users", identityService.users().getBody());
        return "administration/users/users";
    }

    @GetMapping("/addUser")
    @PreAuthorize("hasAuthority('WRITE_USER')")
    public String addUser(Model model){
        model.addAttribute("roles", identityService.enabledRoles().getBody());
        model.addAttribute("persons", identityService.enabledWithoutUsers().getBody());
        return "administration/users/addUser";
    }

    @PostMapping(value = "/addUser", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('WRITE_USER')")
    public ModelAndView addUser(@ModelAttribute UserDTO userDTO){

        try {
            ResponseEntity created = identityService.addUser(userDTO);

            ModelAndView modelAndView = new ModelAndView("administration/users/users");
            modelAndView.addObject("isCreated", created.getStatusCode() == HttpStatus.OK);
            modelAndView.addObject("users", identityService.users().getBody());
            return modelAndView;
        }
        catch (FeignException ex) {
            ModelAndView modelAndView = new ModelAndView("administration/users/addUser");
            modelAndView.addObject("userDTO", userDTO);
            modelAndView.addObject("roles", identityService.enabledRoles());
            modelAndView.addObject("persons", identityService.enabledWithoutUsers());
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/editUser/{id}")
    @PreAuthorize("hasAuthority('WRITE_USER')")
    public String editUser(@PathVariable Long id, Model model){
        ResponseEntity<UserDTO> responseEntity = identityService.getUserById(id);
        model.addAttribute("roles", identityService.enabledRoles().getBody());
        model.addAttribute("userDTO", responseEntity.getBody());
        model.addAttribute("persons", identityService.enabledWithoutUsersForEdit(responseEntity.getBody().getPersonId()).getBody());
        return "administration/users/editUser";
    }

    @PutMapping(value = "/editUser", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('WRITE_USER')")
    public ModelAndView editUser(@ModelAttribute UserDTO userDTO){
        try {
            ResponseEntity<UserDTO> responseEntity = identityService.editUser(userDTO);

            ModelAndView modelAndView = new ModelAndView("administration/users/users");
            modelAndView.addObject("isUpdated", responseEntity.getStatusCode() == HttpStatus.OK);
            modelAndView.addObject("users", identityService.users().getBody());
            return modelAndView;
        }
        catch (FeignException ex) {
            ModelAndView modelAndView = new ModelAndView("administration/users/editUser");
            modelAndView.addObject("userDTO", userDTO);
            modelAndView.addObject("roles", identityService.roles().getBody());
            modelAndView.addObject("persons", identityService.enabledWithoutUsers());
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/disableUser/{id}")
    @PreAuthorize("hasAuthority('WRITE_USER')")
    public String disableUser(@PathVariable Long id, Model model){

        ResponseEntity disabled = identityService.disableUser(id);
        model.addAttribute("isDisabled", disabled.getStatusCode() == HttpStatus.OK);
        model.addAttribute("users", identityService.users().getBody());
        return "administration/users/users";
    }

    @GetMapping("/enableUser/{id}")
    @PreAuthorize("hasAuthority('WRITE_USER')")
    public String enableUser(@PathVariable Long id, Model model){

        ResponseEntity enabled = identityService.enableUser(id);
        model.addAttribute("isEnabled", enabled.getStatusCode() == HttpStatus.OK);
        model.addAttribute("users", identityService.users().getBody());
        return "administration/users/users";
    }
}
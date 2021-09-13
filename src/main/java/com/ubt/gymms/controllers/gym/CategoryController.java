package com.ubt.gymms.controllers.gym;

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

import com.ubt.gymms.entities.gym.CategoryDTO;
import com.ubt.gymms.services.feignClients.WorkoutService;
import feign.FeignException;

@Controller
public class CategoryController {

    @Autowired
    private WorkoutService workoutService;

    @GetMapping("/categories")
    @PreAuthorize("hasAuthority('READ_CATEGORY')")
    public String categories(Model model){
        model.addAttribute("categories", workoutService.categories().getBody());
        return "gym/categories/categories";
    }

    @GetMapping("/addCategory")
    @PreAuthorize("hasAuthority('WRITE_CATEGORY')")
    public String addCategory(){
        return "gym/categories/addCategory";
    }

    @PostMapping(value = "/addCategory", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('WRITE_CATEGORY')")
    public ModelAndView addCategory(@ModelAttribute CategoryDTO categoryDTO){
        try {
            ResponseEntity<CategoryDTO> responseEntity = workoutService.addCategory(categoryDTO);
            ModelAndView modelAndView = new ModelAndView("gym/categories/categories");
            modelAndView.addObject("isCreated", responseEntity.getStatusCode() == HttpStatus.OK);
            modelAndView.addObject("categories", workoutService.categories().getBody());
            return modelAndView;
        }
        catch (FeignException ex) {
            ModelAndView modelAndView = new ModelAndView("gym/categories/addCategory");
            modelAndView.addObject("categoryDTO", categoryDTO);
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/editCategory/{id}")
    @PreAuthorize("hasAuthority('WRITE_CATEGORY')")
    public String editCategory(@PathVariable Long id, Model model){
        ResponseEntity<CategoryDTO> responseEntity = workoutService.editCategory(id);
        model.addAttribute("categoryDTO", responseEntity.getBody());
        return "gym/categories/editCategory";
    }

    @PutMapping(value = "/editCategory", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('WRITE_CATEGORY')")
    public ModelAndView editCategory(@ModelAttribute CategoryDTO categoryDTO){
        try {
            ResponseEntity<CategoryDTO> updated = workoutService.editCategory(categoryDTO);
            ModelAndView modelAndView = new ModelAndView("gym/categories/categories");
            modelAndView.addObject("isUpdated", updated.getStatusCode() == HttpStatus.OK);
            modelAndView.addObject("categories", workoutService.categories().getBody());
            return modelAndView;
        }
        catch (FeignException ex) {
            ModelAndView modelAndView = new ModelAndView("gym/categories/editCategory");
            modelAndView.addObject("categoryDTO", categoryDTO);
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/disableCategory/{id}")
    @PreAuthorize("hasAuthority('WRITE_CATEGORY')")
    public String disableCategory(@PathVariable Long id, Model model){
        try {
            ResponseEntity disabled = workoutService.disableCategory(id);
            model.addAttribute("isDisabled", disabled.getStatusCode() == HttpStatus.OK);
            model.addAttribute("categories", workoutService.categories().getBody());
            return "gym/categories/categories";
        }
        catch (FeignException e) {
            model.addAttribute("isDisabled", false);
            model.addAttribute("categories", workoutService.categories().getBody());
            return "gym/categories/categories";
        }
    }

    @GetMapping("/enableCategory/{id}")
    @PreAuthorize("hasAuthority('WRITE_CATEGORY')")
    public String enableCategory(@PathVariable Long id, Model model){
        try {
            ResponseEntity enabled = workoutService.enableCategory(id);
            model.addAttribute("isEnabled", enabled.getStatusCode() == HttpStatus.OK);
            model.addAttribute("categories", workoutService.categories().getBody());
            return "gym/categories/categories";
        }
        catch (FeignException e) {
            model.addAttribute("isEnabled", false);
            model.addAttribute("categories", workoutService.categories().getBody());
            return "gym/categories/categories";
        }
    }
}

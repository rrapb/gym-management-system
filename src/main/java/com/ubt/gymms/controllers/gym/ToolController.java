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

import com.ubt.gymms.entities.gym.ToolDTO;
import com.ubt.gymms.services.feignClients.WorkoutService;
import feign.FeignException;

@Controller
public class ToolController {

    @Autowired
    private WorkoutService workoutService;

    @GetMapping("/tools")
    @PreAuthorize("hasAuthority('READ_TOOL')")
    public String tools(Model model){
        model.addAttribute("tools", workoutService.tools().getBody());
        return "gym/tools/tools";
    }

    @GetMapping("/addTool")
    @PreAuthorize("hasAuthority('WRITE_TOOL')")
    public String addTool(Model model){
        model.addAttribute("categories", workoutService.enabledCategories().getBody());
        return "gym/tools/addTool";
    }

    @PostMapping(value = "/addTool", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('WRITE_TOOL')")
    public ModelAndView addTool(@ModelAttribute ToolDTO toolDTO){
        try {
            ResponseEntity<ToolDTO> responseEntity = workoutService.addTool(toolDTO);
            ModelAndView modelAndView = new ModelAndView("gym/tools/tools");
            modelAndView.addObject("isCreated", responseEntity.getStatusCode() == HttpStatus.OK);
            modelAndView.addObject("tools", workoutService.tools().getBody());
            return modelAndView;
        }
        catch (FeignException ex) {
            ModelAndView modelAndView = new ModelAndView("gym/tools/addTool");
            modelAndView.addObject("toolDTO", toolDTO);
            modelAndView.addObject("categories", workoutService.enabledCategories().getBody());
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/editTool/{id}")
    @PreAuthorize("hasAuthority('WRITE_TOOL')")
    public String editTool(@PathVariable Long id, Model model){
        ResponseEntity<ToolDTO> responseEntity = workoutService.editTool(id);
        model.addAttribute("toolDTO", responseEntity.getBody());
        model.addAttribute("categories", workoutService.enabledCategories().getBody());
        return "gym/tools/editTool";
    }

    @PutMapping(value = "/editTool", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('WRITE_TOOL')")
    public ModelAndView editTool(@ModelAttribute ToolDTO toolDTO){
        try {
            ResponseEntity<ToolDTO> updated = workoutService.editTool(toolDTO);
            ModelAndView modelAndView = new ModelAndView("gym/tools/tools");
            modelAndView.addObject("isUpdated", updated.getStatusCode() == HttpStatus.OK);
            modelAndView.addObject("tools", workoutService.tools().getBody());
            return modelAndView;
        }
        catch (FeignException ex) {
            ModelAndView modelAndView = new ModelAndView("gym/tools/editTool");
            modelAndView.addObject("toolDTO", toolDTO);
            modelAndView.addObject("categories", workoutService.enabledCategories().getBody());
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/disableTool/{id}")
    @PreAuthorize("hasAuthority('WRITE_TOOL')")
    public String disableTool(@PathVariable Long id, Model model){
        try {
            ResponseEntity disabled = workoutService.disableTool(id);
            model.addAttribute("isDisabled", disabled.getStatusCode() == HttpStatus.OK);
            model.addAttribute("tools", workoutService.tools().getBody());
            return "gym/tools/tools";
        }
        catch (FeignException e) {
            model.addAttribute("isDisabled", false);
            model.addAttribute("tools", workoutService.tools().getBody());
            return "gym/tools/tools";
        }
    }

    @GetMapping("/enableTool/{id}")
    @PreAuthorize("hasAuthority('WRITE_TOOL')")
    public String enableTool(@PathVariable Long id, Model model){
        try {
            ResponseEntity disabled = workoutService.enableTool(id);
            model.addAttribute("isDisabled", disabled.getStatusCode() == HttpStatus.OK);
            model.addAttribute("tools", workoutService.tools().getBody());
            return "gym/tools/tools";
        }
        catch (FeignException e) {
            model.addAttribute("isDisabled", false);
            model.addAttribute("tools", workoutService.tools().getBody());
            return "gym/tools/tools";
        }
    }
}

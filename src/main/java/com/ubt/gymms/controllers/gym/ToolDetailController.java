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

import com.ubt.gymms.entities.gym.ToolDetailDTO;
import com.ubt.gymms.services.feignClients.WorkoutService;
import feign.FeignException;

@Controller
public class ToolDetailController {

    @Autowired
    private WorkoutService workoutService;

    @GetMapping("/toolDetails/{id}")
    @PreAuthorize("hasAuthority('READ_TOOL')")
    public String toolDetails(@PathVariable Long id, Model model){
        model.addAttribute("toolDetails", workoutService.toolDetails(id).getBody());
        return "gym/toolDetails/toolDetails";
    }

    @GetMapping("/addToolDetail")
    @PreAuthorize("hasAuthority('WRITE_TOOL')")
    public String addToolDetail(Model model){

        model.addAttribute("tools", workoutService.enabledTools().getBody());
        return "gym/toolDetails/addToolDetail";
    }

    @PostMapping(value = "/addToolDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('WRITE_TOOL')")
    public ModelAndView addToolDetail(@ModelAttribute ToolDetailDTO toolDetailDTO){

        try {
            ResponseEntity<ToolDetailDTO> responseEntity = workoutService.addToolDetail(toolDetailDTO);

            ModelAndView modelAndView = new ModelAndView("gym/toolDetails/toolDetails");
            modelAndView.addObject("isCreated", responseEntity.getStatusCode() == HttpStatus.OK);
            modelAndView.addObject("toolDetails", workoutService.toolDetails(toolDetailDTO.getToolId()).getBody());
            return modelAndView;
        }
        catch (FeignException ex) {
            ModelAndView modelAndView = new ModelAndView("gym/toolDetails/addToolDetail");
            modelAndView.addObject("toolDetailDTO", toolDetailDTO);
            modelAndView.addObject("tools", workoutService.enabledTools().getBody());
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/editToolDetail/{id}")
    @PreAuthorize("hasAuthority('WRITE_TOOL')")
    public String editToolDetail(@PathVariable Long id, Model model){
        ResponseEntity<ToolDetailDTO> responseEntity = workoutService.editToolDetail(id);
        model.addAttribute("toolDetailDTO", responseEntity.getBody());
        model.addAttribute("tools", workoutService.enabledTools().getBody());
        return "gym/toolDetails/editToolDetail";
    }

    @PutMapping(value = "/editToolDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('WRITE_TOOL')")
    public ModelAndView editToolDetail(@ModelAttribute ToolDetailDTO toolDetailDTO){
        try {
            ResponseEntity<ToolDetailDTO> updated = workoutService.editToolDetail(toolDetailDTO);
            ModelAndView modelAndView = new ModelAndView("gym/toolDetails/toolDetails");
            modelAndView.addObject("isUpdated", updated.getStatusCode() == HttpStatus.OK);
            modelAndView.addObject("toolDetails", workoutService.toolDetails(toolDetailDTO.getToolId()).getBody());
            return modelAndView;
        }
        catch (FeignException ex) {
            ModelAndView modelAndView = new ModelAndView("gym/toolDetails/editToolDetail");
            modelAndView.addObject("toolDetailDTO", toolDetailDTO);
            modelAndView.addObject("tools", workoutService.enabledTools().getBody());
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/deleteToolDetail/{id}")
    @PreAuthorize("hasAuthority('WRITE_TOOL')")
    public String deleteToolDetail(@PathVariable Long id, Model model){
        Long toolId = null;
        try {
            toolId = workoutService.getToolDetailById(id).getBody().getToolId();
            ResponseEntity disabled = workoutService.deleteToolDetail(id);
            model.addAttribute("isDeleted", disabled.getStatusCode() == HttpStatus.OK);
            model.addAttribute("toolDetails", workoutService.toolDetails(toolId).getBody());
            return "gym/toolDetails/toolDetails";
        }
        catch (FeignException e) {
            if(toolId != null) {
                model.addAttribute("isDeleted", false);
                model.addAttribute("tools", workoutService.toolDetails(toolId).getBody());
                return "gym/toolDetails/toolDetails";
            }
        }
        return toolDetails(id, model);
    }
}

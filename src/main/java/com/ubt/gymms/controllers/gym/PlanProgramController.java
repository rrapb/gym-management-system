package com.ubt.gymms.controllers.gym;

import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.servlet.ModelAndView;

import com.ubt.gymms.configurations.exceptions.DatabaseException;
import com.ubt.gymms.entities.administration.PersonDTO;
import com.ubt.gymms.entities.gym.PlanProgramDTO;
import com.ubt.gymms.services.feignClients.IdentityService;
import com.ubt.gymms.services.feignClients.WorkoutService;
import feign.FeignException;

@Controller
public class PlanProgramController {

    @Autowired
    private IdentityService identityService;

    @Autowired
    private WorkoutService workoutService;

    @GetMapping("/planPrograms")
    @PreAuthorize("hasAuthority('READ_PLAN_PROGRAM')")
    public String planPrograms(Model model){
        List<PersonDTO> preparedPersons = preparePersons();
        model.addAttribute("anyPerson", !preparedPersons.isEmpty());
        model.addAttribute("persons", preparedPersons);
        model.addAttribute("planPrograms", workoutService.planPrograms().getBody());
        return "gym/planPrograms/planPrograms";
    }

    @GetMapping("/addPlanProgram")
    @PreAuthorize("hasAuthority('WRITE_PLAN_PROGRAM')")
    public String addPlanProgram(Model model){
        model.addAttribute("persons", identityService.enabledPersons().getBody());
        model.addAttribute("categories", workoutService.enabledCategories().getBody());
        return "gym/planPrograms/addPlanProgram";
    }

    @PostMapping(value = "/addPlanProgram", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('WRITE_PLAN_PROGRAM')")
    public ModelAndView addPlanProgram(@ModelAttribute PlanProgramDTO planProgramDTO){
        try {
            ResponseEntity<PersonDTO> personResponseEntity = identityService.getPersonById(planProgramDTO.getPersonId());
            if(personResponseEntity.getStatusCode() == HttpStatus.OK) {
                PersonDTO personDTO = personResponseEntity.getBody();
                planProgramDTO.setPersonFullName(personDTO.getFirstName().concat(" ").concat(personDTO.getLastName()));
                ResponseEntity<PlanProgramDTO> responseEntity = workoutService.addPlanProgram(planProgramDTO);
                List<PersonDTO> preparedPersons = preparePersons();
                ModelAndView modelAndView = new ModelAndView("gym/planPrograms/planPrograms");
                modelAndView.addObject("isCreated", responseEntity.getStatusCode() == HttpStatus.OK);
                modelAndView.addObject("anyPerson", !preparedPersons.isEmpty());
                modelAndView.addObject("persons", preparedPersons);
                modelAndView.addObject("planPrograms", workoutService.planPrograms().getBody());
                return modelAndView;
            }
            else {
                throw new DatabaseException("user not found!");
            }
        }
        catch (FeignException | DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("gym/planPrograms/addPlanProgram");
            modelAndView.addObject("planProgramDTO", planProgramDTO);
            modelAndView.addObject("persons", identityService.enabledPersons().getBody());
            modelAndView.addObject("categories", workoutService.enabledCategories().getBody());
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/editPlanProgram/{id}")
    @PreAuthorize("hasAuthority('WRITE_PLAN_PROGRAM')")
    public String editPlanProgram(@PathVariable Long id, Model model){
        ResponseEntity<PlanProgramDTO> responseEntity = workoutService.editPlanProgram(id);
        model.addAttribute("planProgramDTO", responseEntity.getBody());
        model.addAttribute("persons", identityService.enabledPersons().getBody());
        model.addAttribute("categories", workoutService.enabledCategories().getBody());
        return "gym/planPrograms/editPlanProgram";
    }

    @PutMapping(value = "/editPlanProgram", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('WRITE_PLAN_PROGRAM')")
    public ModelAndView editPlanProgram(@ModelAttribute PlanProgramDTO planProgramDTO){
        try {
            ResponseEntity<PersonDTO> personResponseEntity = identityService.getPersonById(planProgramDTO.getPersonId());
            if(personResponseEntity.getStatusCode() == HttpStatus.OK) {
                PersonDTO personDTO = personResponseEntity.getBody();
                planProgramDTO.setPersonFullName(personDTO.getFirstName().concat(" ").concat(personDTO.getLastName()));
                ResponseEntity<PlanProgramDTO> responseEntity = workoutService.editPlanProgram(planProgramDTO);
                List<PersonDTO> preparedPersons = preparePersons();
                ModelAndView modelAndView = new ModelAndView("gym/planPrograms/planPrograms");
                modelAndView.addObject("isUpdated", responseEntity.getStatusCode() == HttpStatus.OK);
                modelAndView.addObject("anyPerson", !preparedPersons.isEmpty());
                modelAndView.addObject("persons", preparedPersons);
                modelAndView.addObject("planPrograms", workoutService.planPrograms().getBody());
                return modelAndView;
            }
            else {
                throw new DatabaseException("user not found!");
            }
        }
        catch (FeignException | DatabaseException ex) {
            ModelAndView modelAndView = new ModelAndView("gym/planPrograms/editPlanProgram");
            modelAndView.addObject("planProgramDTO", planProgramDTO);
            modelAndView.addObject("persons", identityService.enabledPersons().getBody());
            modelAndView.addObject("categories", workoutService.enabledCategories().getBody());
            modelAndView.addObject("failed", true);
            return modelAndView;
        }
    }

    @GetMapping("/disablePlanProgram/{id}")
    @PreAuthorize("hasAuthority('WRITE_PLAN_PROGRAM')")
    public String disablePlanProgram(@PathVariable Long id, Model model){
        ResponseEntity responseEntity = workoutService.disablePlanProgram(id);
        List<PersonDTO> preparedPersons = preparePersons();
        model.addAttribute("isDisabled", responseEntity.getStatusCode() == HttpStatus.OK);
        model.addAttribute("anyPerson", !preparedPersons.isEmpty());
        model.addAttribute("persons", preparedPersons);
        model.addAttribute("planPrograms", workoutService.planPrograms().getBody());
        return "gym/planPrograms/planPrograms";
    }

    @GetMapping("/enablePlanProgram/{id}")
    @PreAuthorize("hasAuthority('WRITE_PLAN_PROGRAM')")
    public String enablePlanProgram(@PathVariable Long id, Model model){
        ResponseEntity responseEntity = workoutService.enablePlanProgram(id);
        List<PersonDTO> preparedPersons = preparePersons();
        model.addAttribute("isEnabled", responseEntity.getStatusCode() == HttpStatus.OK);
        model.addAttribute("anyPerson", !preparedPersons.isEmpty());
        model.addAttribute("persons", preparedPersons);
        model.addAttribute("planPrograms", workoutService.planPrograms().getBody());
        return "gym/planPrograms/planPrograms";
    }

    @GetMapping("/downloadPlanProgram")
    @PreAuthorize("hasAuthority('READ_PLAN_PROGRAM')")
    public ResponseEntity<byte[]> downloadPlanProgram(@RequestParam String personId){
        return workoutService.downloadPlanProgram(personId);
    }
    
    private List<PersonDTO> preparePersons() {
        ResponseEntity<List<Long>> responseEntity = workoutService.personsWithEnabledPlanProgram();
        List<PersonDTO> enabledPersons = identityService.enabledPersons().getBody();
        List<Long> personIdsWithPlanPrograms = responseEntity.getBody();
        List<PersonDTO> personDTOList = enabledPersons.stream()
                .filter(personDTO -> personIdsWithPlanPrograms.contains(personDTO.getId()))
                .collect(Collectors.toList());
        return personDTOList;
    }
}

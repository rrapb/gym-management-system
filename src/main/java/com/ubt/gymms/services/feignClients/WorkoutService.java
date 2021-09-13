package com.ubt.gymms.services.feignClients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.ubt.gymms.entities.gym.CategoryDTO;
import com.ubt.gymms.entities.gym.PlanProgramDTO;
import com.ubt.gymms.entities.gym.ToolDTO;
import com.ubt.gymms.entities.gym.ToolDetailDTO;

@FeignClient(value = "workout-service")
public interface WorkoutService {

	@GetMapping("/categories/all")
	ResponseEntity<List<CategoryDTO>> categories();

	@GetMapping("/categories/enabled")
	ResponseEntity<List<CategoryDTO>> enabledCategories();

	@GetMapping(value = "/categories/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id);

	@PostMapping(value = "/categories/add", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<CategoryDTO> addCategory(@RequestBody CategoryDTO categoryDTO);

	@GetMapping(value = "/categories/edit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<CategoryDTO> editCategory(@PathVariable Long id);

	@PutMapping(value = "/categories/edit", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<CategoryDTO> editCategory(@RequestBody CategoryDTO categoryDTO);

	@GetMapping("/categories/disable/{id}")
	ResponseEntity disableCategory(@PathVariable Long id);

	@GetMapping("/categories/enable/{id}")
	ResponseEntity enableCategory(@PathVariable Long id);

	@GetMapping("/tools/all")
	ResponseEntity<List<ToolDTO>> tools();

	@GetMapping("/tools/enabled")
	ResponseEntity<List<ToolDTO>> enabledTools();

	@GetMapping(value = "/tools/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ToolDTO> getToolById(@PathVariable Long id);

	@PostMapping(value = "/tools/add", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ToolDTO> addTool(@RequestBody ToolDTO toolDTO);

	@GetMapping(value = "/tools/edit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ToolDTO> editTool(@PathVariable Long id);

	@PutMapping(value = "/tools/edit", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ToolDTO> editTool(@RequestBody ToolDTO toolDTO);

	@GetMapping("/tools/disable/{id}")
	ResponseEntity disableTool(@PathVariable Long id);

	@GetMapping("/tools/enable/{id}")
	ResponseEntity enableTool(@PathVariable Long id);

	@GetMapping("/toolDetails/all/{id}")
	ResponseEntity<List<ToolDetailDTO>> toolDetails(@PathVariable Long id);

	@GetMapping(value = "/toolDetails/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ToolDetailDTO> getToolDetailById(@PathVariable Long id);

	@PostMapping(value = "/toolDetails/add", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ToolDetailDTO> addToolDetail(@RequestBody ToolDetailDTO toolDetailDTO);

	@GetMapping(value = "/toolDetails/edit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ToolDetailDTO> editToolDetail(@PathVariable Long id);

	@PutMapping(value = "/toolDetails/edit", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ToolDetailDTO> editToolDetail(@RequestBody ToolDetailDTO toolDetailDTO);

	@GetMapping("/toolDetails/delete/{id}")
	ResponseEntity deleteToolDetail(@PathVariable Long id);

	@GetMapping("/planPrograms/all")
	ResponseEntity<List<PlanProgramDTO>> planPrograms();

	@GetMapping("/planPrograms/persons")
	ResponseEntity<List<Long>> personsWithEnabledPlanProgram();

	@GetMapping(value = "/planPrograms/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<PlanProgramDTO> getPlanProgramById(@PathVariable Long id);

	@PostMapping(value = "/planPrograms/add", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<PlanProgramDTO> addPlanProgram(@RequestBody PlanProgramDTO toolDTO);

	@GetMapping(value = "/planPrograms/edit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<PlanProgramDTO> editPlanProgram(@PathVariable Long id);

	@PutMapping(value = "/planPrograms/edit", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<PlanProgramDTO> editPlanProgram(@RequestBody PlanProgramDTO toolDTO);

	@GetMapping("/planPrograms/disable/{id}")
	ResponseEntity disablePlanProgram(@PathVariable Long id);

	@GetMapping("/planPrograms/enable/{id}")
	ResponseEntity enablePlanProgram(@PathVariable Long id);

	@GetMapping("/planPrograms/download")
	ResponseEntity<byte[]> downloadPlanProgram(@RequestParam String personId);
}

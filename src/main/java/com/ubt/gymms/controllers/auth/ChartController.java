package com.ubt.gymms.controllers.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ubt.gymms.services.feignClients.IdentityService;
import com.ubt.gymms.services.feignClients.WorkoutService;

@RestController
@RequestMapping("/chart")
public class ChartController
{
	@Autowired
	private IdentityService identityService;
	@Autowired
	private WorkoutService workoutService;

	@PostMapping("/area")
	public Map area(){
		Map<String, String> map = new HashMap<>();
		map.put("Persons", String.valueOf(identityService.enabledPersons().getBody().size()));
		map.put("Users", String.valueOf(identityService.enabledUsers().getBody().size()));
		map.put("Roles", String.valueOf(identityService.enabledRoles().getBody().size()));
		map.put("Categories", String.valueOf(workoutService.enabledCategories().getBody().size()));
		map.put("Tools", String.valueOf(workoutService.enabledTools().getBody().size()));
		map.put("PlanPrograms", String.valueOf(workoutService.enabledPlanPrograms().getBody().size()));
		return map;
	}

	@PostMapping("/pie")
	public Map pie(){
		Map<String, String> map = new HashMap<>();
		map.put("Persons", String.valueOf(identityService.disabledPersons().getBody().size()));
		map.put("Users", String.valueOf(identityService.disabledUsers().getBody().size()));
		map.put("Roles", String.valueOf(identityService.disabledRoles().getBody().size()));
		map.put("Categories", String.valueOf(workoutService.disabledCategories().getBody().size()));
		map.put("Tools", String.valueOf(workoutService.disabledTools().getBody().size()));
		map.put("PlanPrograms", String.valueOf(workoutService.disabledPlanPrograms().getBody().size()));
		return map;
	}
}

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.ubt.gymms.entities.administration.CredentialsDTO;
import com.ubt.gymms.entities.administration.RoleDTO;
import com.ubt.gymms.entities.administration.UserDTO;
import com.ubt.gymms.entities.administration.PersonDTO;

@FeignClient("identity-service")
public interface IdentityService {

	@GetMapping("/persons/all")
	ResponseEntity<List<PersonDTO>> persons();

	@GetMapping("/persons/enabled")
	ResponseEntity<List<PersonDTO>> enabledPersons();

	@GetMapping("/persons/disabled")
	ResponseEntity<List<PersonDTO>> disabledPersons();

	@GetMapping("/persons/enabledWithoutUsers")
	ResponseEntity<List<PersonDTO>> enabledWithoutUsers();

	@GetMapping("/persons/enabledWithoutUsersForEdit/{id}")
	ResponseEntity<List<PersonDTO>> enabledWithoutUsersForEdit(@PathVariable Long id);

	@GetMapping(value = "/persons/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<PersonDTO> getPersonById(@PathVariable Long id);

	@PostMapping(value = "/persons/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<PersonDTO> addPerson(@RequestPart PersonDTO personDTO, @RequestPart("image") MultipartFile image);

	@GetMapping(value = "/persons/edit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<PersonDTO> editPerson(@PathVariable Long id);

	@PutMapping(value = "/persons/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<PersonDTO> editPerson(@RequestPart PersonDTO personDTO, @RequestPart("image") MultipartFile image);

	@GetMapping("/persons/disable/{id}")
	ResponseEntity disablePerson(@PathVariable Long id);

	@GetMapping("/persons/enable/{id}")
	ResponseEntity enablePerson(@PathVariable Long id);

	@GetMapping("/persons/images/{id}")
	ResponseEntity<byte[]> getImage(@PathVariable String id);

	@GetMapping("/roles/all")
	ResponseEntity<List<RoleDTO>> roles();

	@GetMapping("/roles/enabled")
	ResponseEntity<List<RoleDTO>> enabledRoles();

	@GetMapping("/roles/disabled")
	ResponseEntity<List<RoleDTO>> disabledRoles();

	@GetMapping(value = "/roles/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id);

	@PostMapping(value = "/roles/add", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<RoleDTO> addRole(@RequestBody RoleDTO roleDTO);

	@GetMapping(value = "/roles/edit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<RoleDTO> editRole(@PathVariable Long id);

	@PutMapping(value = "/roles/edit", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<RoleDTO> editRole(@RequestBody RoleDTO roleDTO);

	@GetMapping("/roles/disable/{id}")
	ResponseEntity disableRole(@PathVariable Long id);

	@GetMapping("/roles/enable/{id}")
	ResponseEntity enableRole(@PathVariable Long id);

	@GetMapping("/users/all")
	ResponseEntity<List<UserDTO>> users();

	@GetMapping("/users/enabled")
	ResponseEntity<List<UserDTO>> enabledUsers();

	@GetMapping("/users/disabled")
	ResponseEntity<List<UserDTO>> disabledUsers();

	@GetMapping(value = "/users/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<UserDTO> getUserById(@PathVariable Long id);

	@PostMapping(value = "/users/loadUserByEmailAndPassword", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<UserDTO> loadUserByEmailAndPassword(@RequestBody CredentialsDTO credentialsDTO);

	@PostMapping(value = "/users/loadUserByUsername", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<UserDTO> loadUserByUsername(@RequestBody CredentialsDTO credentialsDTO);

	@PostMapping(value = "/users/add", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO);

	@GetMapping(value = "/users/edit/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<UserDTO> editUser(@PathVariable Long id);

	@PutMapping(value = "/users/edit", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<UserDTO> editUser(@RequestBody UserDTO userDTO);

	@GetMapping("/users/disable/{id}")
	ResponseEntity disableUser(@PathVariable Long id);

	@GetMapping("/users/enable/{id}")
	ResponseEntity enableUser(@PathVariable Long id);

	@GetMapping("/users/privileges/{id}")
	ResponseEntity<List<String>> getPrivilegesByUser(@PathVariable Long id);

	@PostMapping("/users/updatePassword")
	ResponseEntity updatePassword(@RequestBody CredentialsDTO credentialsDTO);

	@PostMapping("/users/activateUser")
	ResponseEntity activateUser(@RequestBody CredentialsDTO credentialsDTO);

}

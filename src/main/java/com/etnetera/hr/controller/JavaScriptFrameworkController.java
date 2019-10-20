package com.etnetera.hr.controller;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.dto.JavaScriptFrameworkDto;
import com.etnetera.hr.service.JavascriptFrameworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

/**
 * Simple REST controller for accessing application logic.
 * 
 * @author Etnetera
 *
 */
@RestController
@Validated
public class JavaScriptFrameworkController extends EtnRestController {

	@Autowired
	private JavascriptFrameworkService service;


	@GetMapping("/frameworks")
	public Collection<JavaScriptFrameworkDto> frameworks() {
		return service.findAll();
	}

	@PostMapping("/frameworks")
	public JavaScriptFrameworkDto createFramework(@Valid @RequestBody JavaScriptFrameworkDto framework) {
			return service.createFramework(new JavaScriptFramework(framework));
	}

	@GetMapping("/frameworks/{id}")
	public JavaScriptFrameworkDto findFramework(@PathVariable @NotNull long id) {
		return service.getFramework(id);
	}

	@GetMapping("/frameworks/findByName")
	public Collection<JavaScriptFrameworkDto> findByName(@RequestParam(value="name") @NotBlank @Size(min= 2, max = 30) String name) {
		return service.findByName(name);
	}

	@GetMapping("/frameworks/findDeprecated")
	public Collection<JavaScriptFrameworkDto> findDeprecated(@RequestParam(value = "deprecated", defaultValue = "false") boolean deprecated) {
		return service.findDeprecated(deprecated);
	}

	@PutMapping("/frameworks/{id}")
	public JavaScriptFrameworkDto updateFramework(@Valid @RequestBody JavaScriptFrameworkDto updatedFramework, @PathVariable @NotNull Long id) {

		return service.updateFramework(new JavaScriptFramework(updatedFramework), id);
	}

	@DeleteMapping("/frameworks/{id}")
	public void deleteFramework(@PathVariable @NotNull Long id) {
		service.deleteFramework(id);
	}

}

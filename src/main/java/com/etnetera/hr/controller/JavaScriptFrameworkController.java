package com.etnetera.hr.controller;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Simple REST controller for accessing application logic.
 * 
 * @author Etnetera
 *
 */
@RestController
public class JavaScriptFrameworkController extends EtnRestController {

	private final JavaScriptFrameworkRepository repository;

	@Autowired
	public JavaScriptFrameworkController(JavaScriptFrameworkRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/frameworks")
	public Iterable<JavaScriptFramework> frameworks() {
		return repository.findAll();
	}

	@PostMapping("/frameworks")
	public JavaScriptFramework createFramework(@RequestBody JavaScriptFramework framework) {

			return repository.save(framework);
	}

	@GetMapping("/frameworks/{id}")
	public Optional<JavaScriptFramework> findFramework(@PathVariable Long id) {
		return repository.findById(id);
	}

	@PutMapping("/frameworks/{id}")
	JavaScriptFramework updateFramework(@RequestBody JavaScriptFramework updatedFramework, @PathVariable Long id) {

		return repository.findById(id)
				.map(framework -> {
					framework.setName(updatedFramework.getName());
					framework.setVersion(updatedFramework.getVersion());
					framework.setDeprecationDate(updatedFramework.getDeprecationDate());
					framework.setHypeLevel(updatedFramework.getHypeLevel());
					return repository.save(framework);
				})
				.orElseGet(() -> {
					updatedFramework.setId(id);
					return repository.save(updatedFramework);
				});
	}

	@DeleteMapping("/frameworks/{id}")
	void deleteEmployee(@PathVariable Long id) {
		repository.deleteById(id);
	}

}

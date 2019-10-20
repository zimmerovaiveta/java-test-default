package com.etnetera.hr.service;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.etnetera.hr.rest.NotFoundError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * JavaScriptFramework service layer to connect endpoints and data level and to execute business logic
 */

@Service
public class JavascriptFrameworkService {

    @Autowired
    private JavaScriptFrameworkRepository repository;

    public JavaScriptFramework createFramework(JavaScriptFramework framework) {
        return repository.save(framework);
    }

    public Iterable<JavaScriptFramework> findAll() {
        return repository.findAll();
    }

    public JavaScriptFramework getFramework(long javaScriptFrameworkId) {
        return repository.findById(javaScriptFrameworkId)
                .orElseThrow(() -> new NotFoundError("JavaScriptFramework", javaScriptFrameworkId));
    }

    public Iterable<JavaScriptFramework> findByName(String name) {
        return repository.findByNameIgnoreCaseOrderByVersion(name);
    }

    public Iterable<JavaScriptFramework> findDeprecated( boolean deprecated) {
       return deprecated ? repository.findByDeprecationDateIsNotNullAndDeprecationDateBeforeOrderByDeprecationDateDescNameAscVersionAsc(LocalDate.now())
                : repository.findByDeprecationDateIsNullOrderByNameAscVersionAsc();
    }

    public JavaScriptFramework updateFramework(JavaScriptFramework updatedFramework, long javaScriptFrameworkId) {
        return repository.findById(javaScriptFrameworkId)
                .map(framework -> {
                    framework.setName(updatedFramework.getName());
                    framework.setVersion(updatedFramework.getVersion());
                    framework.setDeprecationDate(updatedFramework.getDeprecationDate());
                    framework.setHypeLevel(updatedFramework.getHypeLevel());
                    return repository.save(framework);
                })
                .orElseGet(() -> {
                    updatedFramework.setId(javaScriptFrameworkId);
                    return repository.save(updatedFramework);
                });
    }

    public void deleteFramework(long javaScriptFrameworkId) {
        repository.deleteById(javaScriptFrameworkId);
    }

}

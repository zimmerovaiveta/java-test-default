package com.etnetera.hr.service;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.dto.JavaScriptFrameworkDto;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.etnetera.hr.rest.NotFoundError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;

import static com.etnetera.hr.dto.JavaScriptFrameworkDto.convertToDto;

/**
 * JavaScriptFramework service layer to connect endpoints and data level and to execute business logic
 */

@Service
public class JavascriptFrameworkService {

    @Autowired
    private JavaScriptFrameworkRepository repository;

    public JavaScriptFrameworkDto createFramework(JavaScriptFramework framework) {
        JavaScriptFramework createdFramework = repository.save(framework);
        return new JavaScriptFrameworkDto(createdFramework);
    }

    public Collection<JavaScriptFrameworkDto> findAll() {
        Iterable<JavaScriptFramework> frameworks = repository.findAll();

        return convertToDto(frameworks);
    }

    public JavaScriptFrameworkDto getFramework(long javaScriptFrameworkId) {
        JavaScriptFramework framework = repository.findById(javaScriptFrameworkId)
                .orElseThrow(() -> new NotFoundError("JavaScriptFramework", javaScriptFrameworkId));
        return new JavaScriptFrameworkDto(framework);
    }

    public Collection<JavaScriptFrameworkDto> findByName(String name) {
        Iterable<JavaScriptFramework> frameworks = repository.findByNameIgnoreCaseOrderByVersion(name);
        return convertToDto(frameworks);
    }

    public Collection<JavaScriptFrameworkDto> findDeprecated( boolean deprecated) {
        Iterable<JavaScriptFramework> frameworks =  deprecated ? repository.findByDeprecationDateIsNotNullAndDeprecationDateBeforeOrderByDeprecationDateDescNameAscVersionAsc(LocalDate.now())
                : repository.findByDeprecationDateIsNullOrderByNameAscVersionAsc();
        return convertToDto(frameworks);
    }

    public JavaScriptFrameworkDto updateFramework(JavaScriptFramework updatedFramework, long javaScriptFrameworkId) {
        JavaScriptFramework framework = repository.findById(javaScriptFrameworkId)
                .map(_framework -> {
                    _framework.setName(updatedFramework.getName());
                    _framework.setVersion(updatedFramework.getVersion());
                    _framework.setDeprecationDate(updatedFramework.getDeprecationDate());
                    _framework.setHypeLevel(updatedFramework.getHypeLevel());
                    return repository.save(_framework);
                })
                .orElseGet(() -> {
                    updatedFramework.setId(javaScriptFrameworkId);
                    return repository.save(updatedFramework);
                });
        return new JavaScriptFrameworkDto(framework);
    }

    public void deleteFramework(long javaScriptFrameworkId) {
        repository.deleteById(javaScriptFrameworkId);
    }

}

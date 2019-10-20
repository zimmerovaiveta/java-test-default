package com.etnetera.hr.repository;

import com.etnetera.hr.data.JavaScriptFramework;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

/**
 * Spring data repository interface used for accessing the data in database.
 * 
 * @author Etnetera
 *
 */
public interface JavaScriptFrameworkRepository extends CrudRepository<JavaScriptFramework, Long> {

    Iterable<JavaScriptFramework> findByNameIgnoreCaseOrderByVersion(String name);
    Iterable<JavaScriptFramework> findByDeprecationDateIsNullOrderByNameAscVersionAsc();
    Iterable<JavaScriptFramework> findByDeprecationDateIsNotNullAndDeprecationDateBeforeOrderByDeprecationDateDescNameAscVersionAsc(LocalDate now);

}

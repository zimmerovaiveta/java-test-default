package com.etnetera.hr.dto;

import com.etnetera.hr.data.JavaScriptFramework;
import com.fasterxml.jackson.core.Version;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

/**
 * DTO object to carry JavascriptFrameworkData
 */
public class JavaScriptFrameworkDto {

    private Long id;
    @Size(min= 2, max = 30)
    @NotEmpty
    private String name;
    @NotNull
    private Version version;
    private LocalDate deprecationDate;
    private Double hypeLevel;

    public JavaScriptFrameworkDto() {
    }

    public JavaScriptFrameworkDto(String name, Version version, LocalDate deprecationDate, Double hypeLevel) {
        this.name = name;
        this.version = version;
        this.deprecationDate = deprecationDate;
        this.hypeLevel = hypeLevel;
    }

    public JavaScriptFrameworkDto(JavaScriptFramework framework) {
        this.id = framework.getId();
        this.name = framework.getName();
        this.version = framework.getVersion();
        this.deprecationDate = framework.getDeprecationDate();
        this.hypeLevel = framework.getHypeLevel();
    }

    public static Collection<JavaScriptFrameworkDto> convertToDto(Iterable<JavaScriptFramework> javaScriptFrameworks){
        Collection<JavaScriptFrameworkDto> ret = new ArrayList<>();
        javaScriptFrameworks.forEach(framework -> ret.add(new JavaScriptFrameworkDto(framework)));

        return ret;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public LocalDate getDeprecationDate() {
        return deprecationDate;
    }

    public void setDeprecationDate(LocalDate deprecationDate) {
        this.deprecationDate = deprecationDate;
    }

    public Double getHypeLevel() {
        return hypeLevel;
    }

    public void setHypeLevel(Double hypeLevel) {
        this.hypeLevel = hypeLevel;
    }
}

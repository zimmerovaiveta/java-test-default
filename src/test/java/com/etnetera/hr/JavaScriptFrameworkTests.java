package com.etnetera.hr;

import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.dto.JavaScriptFrameworkDto;
import com.etnetera.hr.repository.JavaScriptFrameworkRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.RequestDispatcher;
import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Class used for Spring Boot/MVC based tests.
 * 
 * @author Etnetera
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class JavaScriptFrameworkTests {

	private final LocalDate NOW = LocalDate.now();
	private final Version VERSION_1 = new Version(1, 1, 1, "", "", null);
	private final Version VERSION_2 = new Version(2, 2, 2, "", "", null);
	private final Version VERSION_3 = new Version(3, 3, 3, "", "", null);

	private JavaScriptFrameworkDto react_1 = new JavaScriptFrameworkDto("ReactJS", VERSION_1, NOW.minusYears(10), 4.0);
	private JavaScriptFrameworkDto react_2 = new JavaScriptFrameworkDto("ReactJS", VERSION_2, NOW.minusYears(8), 5.0);
	private JavaScriptFrameworkDto react_3 = new JavaScriptFrameworkDto("ReactJS", VERSION_3, null, 9.0);
	private JavaScriptFrameworkDto vue_1 = new JavaScriptFrameworkDto("Vue.js", VERSION_2, NOW.minusYears(5), 4.0);
	private JavaScriptFrameworkDto vue_2 = new JavaScriptFrameworkDto("Vue.js", VERSION_2, NOW.minusYears(2), 6.0);
	private JavaScriptFrameworkDto vue_3 = new JavaScriptFrameworkDto("Vue.js", VERSION_3, null, 8.0);

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper = new ObjectMapper();


	@Autowired
	private JavaScriptFrameworkRepository repository;

	private void prepareSimpleData() throws Exception {
		repository.save(new JavaScriptFramework(react_1));
		repository.save(new JavaScriptFramework(vue_1));
	}

	private void prepareExtendedData() throws Exception {
		repository.save(new JavaScriptFramework(react_1));
		repository.save(new JavaScriptFramework(react_2));
		repository.save(new JavaScriptFramework(react_3));
		repository.save(new JavaScriptFramework(vue_1));
		repository.save(new JavaScriptFramework(vue_2));
		repository.save(new JavaScriptFramework(vue_3));
	}

	// ****************** SAVE **********************

	@Test
	public void addFrameworkValid() throws Exception {

		JavaScriptFramework framework = new JavaScriptFramework("ReactJS", VERSION_1, NOW, 4.0);

		mockMvc.perform(post("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("id", is(1)))
				.andExpect(jsonPath("name", is("ReactJS")))
				.andExpect(jsonPath("version", is(VERSION_1.toString())))
				.andExpect(jsonPath("deprecationDate", is(NOW.toString())))
				.andExpect(jsonPath("hypeLevel", is(4.0)));
	}

	@Test
	public void addFrameworkInvalid() throws JsonProcessingException, Exception {
        JavaScriptFrameworkDto framework = new JavaScriptFrameworkDto("ReactJs", null, null, null);

		mockMvc.perform(post("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors[0].field", is("version")))
				.andExpect(jsonPath("$.errors[0].message", is("NotNull")));

		framework.setName(null);
		framework.setVersion(VERSION_1);
		mockMvc.perform(post("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors[0].field", is("name")))
				.andExpect(jsonPath("$.errors[0].message", is("NotEmpty")));

		framework.setName("verylongnameofthejavascriptframeworkjavaisthebest");
		mockMvc.perform(post("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors[0].field", is("name")))
				.andExpect(jsonPath("$.errors[0].message", is("Size")));

		framework.setName("a");
		mockMvc.perform(post("/frameworks").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(framework)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors[0].field", is("name")))
				.andExpect(jsonPath("$.errors[0].message", is("Size")));

	}

//	*********************** FIND *******************************

	@Test
	public void findValidById() throws Exception {
		prepareSimpleData();

		mockMvc.perform(get("/frameworks/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("id", is(1)))
				.andExpect(jsonPath("name", is("ReactJS")));
	}

	@Test
	public void findInvalidById() throws Exception {
		prepareSimpleData();

		mockMvc.perform(get("/frameworks/3"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("entity", is("JavaScriptFramework")))
				.andExpect(jsonPath("id", is(3)));
	}

	@Test
	public void findValidByName() throws Exception {
		prepareSimpleData();

		mockMvc.perform(get("/frameworks/findByName").param("name", "Vue.js"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id", is(2)))
				.andExpect(jsonPath("$[0].name", is("Vue.js")));

        JavaScriptFrameworkDto framework = new JavaScriptFrameworkDto("ReactJS", VERSION_3, NOW, 4.0);
		repository.save(new JavaScriptFramework(framework));

		mockMvc.perform(get("/frameworks/findByName").param("name", "reactjs"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("ReactJS")))
				.andExpect(jsonPath("$[0].version", is("1.1.1")))
				.andExpect(jsonPath("$[1].id", is(3)))
				.andExpect(jsonPath("$[1].name", is("ReactJS")))
				.andExpect(jsonPath("$[1].version", is("3.3.3")));

		mockMvc.perform(get("/frameworks/findByName").param("name", "notKnownFramework"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	public void findInvalidByName() throws Exception {
		prepareSimpleData();

		mockMvc.perform(get("/frameworks/findByName").param("name", "verylongnameofthejavascriptframeworkjavaisthebest"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors", hasSize(1)))
				.andExpect(jsonPath("$.errors[0].field", is("findByName.name")))
				.andExpect(jsonPath("$.errors[0].message", is("size must be between 2 and 30")));

		mockMvc.perform(get("/frameworks/findByName").param("name", (String) null)
				.requestAttr(RequestDispatcher.ERROR_MESSAGE,
						"Required String parameter 'name' is not present"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void findDeprecated() throws Exception {
		prepareExtendedData();

		mockMvc.perform(get("/frameworks/findDeprecated").param("deprecated", "true"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[0].id", is(5)))
				.andExpect(jsonPath("$[0].name", is("Vue.js")))
				.andExpect(jsonPath("$[0].deprecationDate", is(NOW.minusYears(2).toString())))
				.andExpect(jsonPath("$[2].id", is(2)))
				.andExpect(jsonPath("$[2].name", is("ReactJS")))
				.andExpect(jsonPath("$[2].deprecationDate", is(NOW.minusYears(8).toString())));

		mockMvc.perform(get("/frameworks/findDeprecated").param("deprecated", "false"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is(3)))
				.andExpect(jsonPath("$[0].name", is("ReactJS")));

		mockMvc.perform(get("/frameworks/findDeprecated"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is(3)))
				.andExpect(jsonPath("$[0].name", is("ReactJS")));
	}

//	*********************************** FIND ALL *************************
	@Test
	public void frameworksTest() throws Exception {
		prepareSimpleData();

		mockMvc.perform(get("/frameworks"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("ReactJS")))
				.andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].name", is("Vue.js")));
	}
//	 ***************************** UPDATE **************************
	@Test
	public void updateValid() throws Exception {
		prepareSimpleData();
		vue_1.setName("Angular");

		mockMvc.perform(put("/frameworks/2").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(vue_1)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("name", is("Angular")));
	}

	@Test
	public void updateInvalid() throws Exception {
		prepareSimpleData();

		mockMvc.perform(put("/frameworks/{id}", (Long) null).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(vue_1)))
				.andExpect(status().isMethodNotAllowed());

		vue_1.setName(null);
		mockMvc.perform(put("/frameworks/2").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(vue_1)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0].field", is("name")))
				.andExpect(jsonPath("$.errors[0].message", is("NotEmpty")));

		vue_1.setName("A");
		mockMvc.perform(put("/frameworks/2").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(vue_1)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0].field", is("name")))
				.andExpect(jsonPath("$.errors[0].message", is("Size")));

		vue_1.setName("Vue.js");
		vue_1.setVersion(null);
		mockMvc.perform(put("/frameworks/2").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsBytes(vue_1)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0].field", is("version")))
				.andExpect(jsonPath("$.errors[0].message", is("NotNull")));

	}

//	****************************** DELETE **************************
	@Test
	public void deleteValid() throws Exception {
		prepareSimpleData();

		mockMvc.perform(delete("/frameworks/{id}", 2).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void deleteInvalid() throws Exception {
		prepareSimpleData();

		mockMvc.perform(delete("/frameworks/{id}", (Long) null).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isMethodNotAllowed());
	}

}

package com.teamtreehouse.web.controller;

import com.teamtreehouse.util.WebUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//What you "expect" after performing a request on a MockMvc object is called a ResultMatcher.
// Typically your options will come from the MockMvcResultMatchers class. Some things
// you can examine are the following:
//  content(): check the response body for actual content in the HTTP response
//  flash(): check for certain flash attributes
//  model(): check for model attributes
//  jsonPath(...): check for content in an HTTP response body that is in the JSON format (e.g. for a REST API)
// For a full list of matchers, see the Spring docs for
// org.springframework.test.web.servlet.result.MockMvcResultMatchers
// https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/test/web/servlet/result/MockMvcResultMatchers.html

public class WeatherControllerTest {
    private MockMvc mockMvc;
    private WeatherController controller;

    @Before
    public void setup() {
        controller = new WeatherController();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void home_ShouldRenderDetailView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(view().name("weather/detail"));
    }

    @Test
    public void search_ShouldRedirectWithPathParam() throws Exception {
        mockMvc.perform(get("/search").param("q", "60657"))
                .andExpect(redirectedUrl("/search/60657"));
    }
}

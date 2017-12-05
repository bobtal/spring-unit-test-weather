package com.teamtreehouse.web.controller;

import com.teamtreehouse.domain.Favorite;
import com.teamtreehouse.service.FavoriteNotFoundException;
import com.teamtreehouse.service.FavoriteService;
import com.teamtreehouse.service.WeatherService;
import com.teamtreehouse.service.dto.geocoding.Geometry;
import com.teamtreehouse.service.dto.geocoding.Location;
import com.teamtreehouse.service.dto.geocoding.PlacesResult;
import com.teamtreehouse.service.dto.weather.Weather;
import com.teamtreehouse.service.resttemplate.PlacesService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.teamtreehouse.domain.Favorite.FavoriteBuilder;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class FavoriteControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private FavoriteController controller;

    @Mock
    private FavoriteService favoriteService;

    @Mock
    private PlacesService placesService;

    @Mock
    private WeatherService weatherService;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void index_ShouldIncludeFavoritesInModel() throws Exception {
        // Arrange the mock behavior
        List<Favorite> favorites = Arrays.asList(
                new FavoriteBuilder(1L).withAddress("Chicago").withPlaceId("chicago1").build(),
                new FavoriteBuilder(2L).withAddress("Omaha").withPlaceId("omaha1").build()
        );
        when(favoriteService.findAll()).thenReturn(favorites);

        // Act (perform the MVC request) and Assert results
        mockMvc.perform(get("/favorites"))
                .andExpect(status().isOk())
                .andExpect(view().name("favorite/index"))
                .andExpect(model().attribute("favorites", favorites));
        verify(favoriteService).findAll();
    }

    @Test
    public void add_ShouldRedirectToNewFavorite() throws Exception {
        // Arrange the mock behavior
        long randomId = new Random().nextLong();
        doAnswer(invocation -> {
            Favorite f = (Favorite)invocation.getArguments()[0];
            f.setId(randomId);
            return null;
        }).when(favoriteService).save(any(Favorite.class));

        // Act (perform the MVC request) and Assert results
        mockMvc.perform(
            post("/favorites")
                .param("formattedAddress", "chicago, il")
                .param("placeId", "windycuty")
        ).andExpect(redirectedUrl(String.format("/favorites/%d", randomId)));
        verify(favoriteService).save(any(Favorite.class));
    }

    @Test
    public void detail_ShouldErrorOnNotFound() throws Exception {
        // Arrange the mock behavior
        when(favoriteService.findById(1L)).thenThrow(FavoriteNotFoundException.class);

        // Act (perform the MVC request) and Assert results
        mockMvc.perform(get("/favorites/1"))
            .andExpect(view().name("error"))
            .andExpect(model().attribute("ex", Matchers.instanceOf(FavoriteNotFoundException.class)));
        verify(favoriteService).findById(1L);
    }

    @Test
    public void detail_ShouldShowDetailForFavorite() throws Exception {
        // Arrange the mock behavior
        Favorite fav = new FavoriteBuilder(1L).withPlaceId("chicago1").build();
        Location location = new Location(123.123d, 123.123d);
        Geometry geometry = new Geometry();
        PlacesResult placesResult = new PlacesResult();
        Weather weather = new Weather();

        geometry.setLocation(location);
        placesResult.setPlaceId("chicago1");
        placesResult.setGeometry(geometry);

        when(favoriteService.findById(1L)).thenReturn(fav);
        when(placesService.findByPlaceId("chicago1")).thenReturn(placesResult);
        when(weatherService.findByLocation(location)).thenReturn(weather);

        // Act (perform the MVC request) and Assert results
        mockMvc.perform(
            get("/favorites/1")
        )
                .andExpect(status().isOk())
                .andExpect(model().attribute("favorite", fav))
                .andExpect(model().attribute("weather", weather))
                .andExpect(view().name("favorite/detail"));
        verify(favoriteService).findById(1L);
        verify(placesService).findByPlaceId("chicago1");
        verify(weatherService).findByLocation(location);
    }


}

//    Alternate Approaches to Setting Up Mockito
//
//    Without the MockitoJUnitRunner
//
//        If you want to run your unit tests without the MockitoJUnitRunner, you can delete the @RunWith annotation
//        from the class. Then, you'll need to tell Mockito to initialize the mocks according to your
//        @InjectMocks and @Mock annotations:
//
//public class FavoriteControllerTest {
//    private MockMvc mockMvc;
//
//    @InjectMocks
//    private FavoriteController controller;
//
//    @Mock
//    private FavoriteService favoriteService;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this); // Initialize according to annotations
//        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
//    }
//}
//    Construct Mocks Yourself
//
//        If you'd like to construct and initialize the mocks yourself, you have options.
//        Here is one way to do this:
//
//public class FavoriteControllerTest {
//    private MockMvc mockMvc;
//    private FavoriteController controller;
//    private FavoriteService favoriteService;
//
//    @Before
//    public void setup() {
//        // Construct the favoriteService mock
//        favoriteService = Mockito.mock(FavoriteService.class);
//        controller = new FavoriteController(favoriteService,null,null);
//        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
//    }
//}
//    Of course, this will require a constructor in the FavoriteController class.
//    Because we don't want to break Spring's dependency injection, you can mark this constructor as @Autowired,
//    which means that any parameter values will be injected according to available beans. This is a nice,
//    test-friendly alternative to autowired fields, which require reflection for DI.
//    Here's the relevant part of the FavoriteController class:
//
//@Controller
//public class FavoriteController {
//    // @Autowired annotations removed from fields
//
//    private FavoriteService favoriteService;
//    private PlacesService placesService;
//    private WeatherService weatherService;
//
//    @Autowired // Added for Spring DI
//    public FavoriteController(
//            FavoriteService favoriteService,
//            PlacesService placesService,
//            WeatherService weatherService) {
//        this.favoriteService = favoriteService;
//        this.placesService = placesService;
//        this.weatherService = weatherService;
//    }
//}
//    Overview of Mocking
//
//        Wikipedia has a nice explanation of what mock objects are: https://en.wikipedia.org/wiki/Mock_object
//
//        Here is a nice supplement to our mocking work, still within the context of Spring:
//        https://dzone.com/articles/mocking-in-unit-tests
//
//        Popular Mocking Libraries
//
//        Mockito - http://mockito.org/
//        EasyMock - http://easymock.org/
//        PowerMock - https://github.com/jayway/powermock/wiki/GettingStarted
//        JMockit - http://jmockit.org/
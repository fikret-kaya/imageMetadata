package com.up42.imagemetadata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.up42.imagemetadata.model.Feature;
import com.up42.imagemetadata.service.impl.ImageServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MetadataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ImageServiceImpl service;

    @InjectMocks
    private MetadataController controller;


    private Feature feature;
    private String quicklook;
    private List<Feature> featureList;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        feature = new Feature("ca81d759-0b8c-4b3f-a00a-0908a3ddd655", 1558155123786L,
                1558155123786L, 1558155148785L, "Sentinel-1A");
        String tmpString = "encodedImage";
        quicklook = Base64.getEncoder().encodeToString(tmpString.getBytes());
        featureList = new ArrayList<>();
        featureList.add(feature);
    }

    @AfterEach
    public void tearDown() {
        feature = null;
        featureList = null;
    }

    @Test
    public void fetchFeatures() throws Exception {
        when(service.getListOfFeatures()).thenReturn(featureList);
        mockMvc.perform(get("/features")
                        .content(asJsonString(feature)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void fetchFeaturesNoData() throws Exception {
        when(service.getListOfFeatures()).thenReturn(null);
        mockMvc.perform(get("/features").
                        contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());
        verify(service,times(1)).getListOfFeatures();
    }

    @Test
    public void fetchFeature() throws Exception {
        when(service.getFeature(feature.getId())).thenReturn(feature);
        mockMvc.perform(get("/features/ca81d759-0b8c-4b3f-a00a-0908a3ddd655")
                        .content(asJsonString(feature)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void fetchFeatureWrongId() throws Exception {
        when(service.getFeature(any())).thenReturn(null);
        mockMvc.perform(get("/features/wrongId").
                        contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());
        verify(service,times(1)).getFeature(any());
    }

    @Test
    public void fetchQuicklook() throws Exception {
        when(service.getQuicklook(feature.getId())).thenReturn(quicklook);
        mockMvc.perform(get("/features/ca81d759-0b8c-4b3f-a00a-0908a3ddd655/quicklook")
                        .content(quicklook))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void fetchQuicklookNoImage() throws Exception {
        when(service.getQuicklook(any())).thenReturn(null);
        mockMvc.perform(get("/features/ca81d759-0b8c-4b3f-a00a-0908a3ddd655/quicklook").
                        contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());
        verify(service,times(1)).getQuicklook(any());
    }

    public static String asJsonString(final Object obj){
        try{
            return new ObjectMapper().writeValueAsString(obj);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

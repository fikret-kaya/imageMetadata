package com.up42.imagemetadata.service;

import com.up42.imagemetadata.model.Feature;
import com.up42.imagemetadata.service.impl.ImageServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @Autowired
    @InjectMocks
    private ImageServiceImpl imageService;

    @BeforeEach
    public void setUp() {
        imageService.parseFile();
    }

    @Test
    public void getFeatures() {
        List<Feature> features = imageService.getListOfFeatures();
        assertEquals(14, features.size());
    }

    @Test
    public void getFeaturesInDetail() {
        List<Feature> features = imageService.getListOfFeatures();
        assertEquals("ca81d759-0b8c-4b3f-a00a-0908a3ddd655", features.get(2).getId());
    }

    @Test
    public void getFeaturewithId() {
        Feature feature = imageService.getFeature("ca81d759-0b8c-4b3f-a00a-0908a3ddd655");
        assertNotNull(feature);
        assertEquals(1558155123786L, feature.getTimestamp());
    }

    @Test
    public void checkFeatureMissionName() {
        Feature feature = imageService.getFeature("ca81d759-0b8c-4b3f-a00a-0908a3ddd655");
        assertNotNull(feature);
        assertEquals("Sentinel-1A", feature.getMissionName());
    }

    @Test
    public void checkFeatureBeginViewingDate() {
        Feature feature = imageService.getFeature("ca81d759-0b8c-4b3f-a00a-0908a3ddd655");
        assertNotNull(feature);
        assertEquals(1558155123786L, feature.getBeginViewingDate());
    }

    @Test
    public void checkFeatureEndViewingDate() {
        Feature feature = imageService.getFeature("ca81d759-0b8c-4b3f-a00a-0908a3ddd655");
        assertNotNull(feature);
        assertEquals(1558155148785L, feature.getEndViewingDate());
    }

    @Test
    public void getFeatureWithWrongId() {
        Feature feature = imageService.getFeature("wrongid");
        assertNull(feature);
    }

    @Test
    public void getQuicklook() {
        String quicklook = imageService.getQuicklook("ca81d759-0b8c-4b3f-a00a-0908a3ddd655");
        assertNotNull(quicklook);
        assertFalse(quicklook.isEmpty());
    }

    @Test
    public void getQuicklookInDetail() {
        String quicklook = imageService.getQuicklook("ca81d759-0b8c-4b3f-a00a-0908a3ddd655");
        assertNotNull(quicklook);
        assertFalse(quicklook.isEmpty());

        byte[] decodedBytes = Base64.getDecoder().decode(quicklook);
        assertNotNull(decodedBytes);
        assertTrue(decodedBytes.length > 0);
    }

    @Test
    public void getQuicklookInDetailNoImage() {
        String quicklook = imageService.getQuicklook("b0d3bf6a-ff54-49e0-a4cb-e57dcb68d3b5");
        assertNull(quicklook);
    }

    @Test
    public void getQuicklookWrongId() {
        String quicklook = imageService.getQuicklook("wrongid");
        assertNull(quicklook);
    }

}

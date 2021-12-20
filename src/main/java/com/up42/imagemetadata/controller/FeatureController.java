package com.up42.imagemetadata.controller;

import com.google.gson.Gson;
import com.up42.imagemetadata.model.Feature;
import com.up42.imagemetadata.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FeatureController {
    Logger logger = LoggerFactory.getLogger(FeatureController.class);

    private final ImageService imageService;

    @GetMapping("/features")
    public ResponseEntity<Object> getListOfFeatures() {
        List<Feature> features = imageService.getListOfFeatures();

        if (features != null) {
            return ResponseEntity.ok(features);
        } else {
            logger.error("Image features not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Gson().toJson("Features not found"));
        }
    }

    @GetMapping("/features/{id}")
    public ResponseEntity<Object> getFeature(@PathVariable(value="id") String id) {
        Feature feature = imageService.getFeature(id);

        if (feature != null) {
            return ResponseEntity.ok(feature);
        } else {
            logger.error("Image feature not found for id: " + id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Gson().toJson("Feature not found for id: " + id));
        }
    }

    @GetMapping("/features/{id}/quicklook")
    public ResponseEntity<Object> getQuicklook(@PathVariable(value="id") String id) {
        String quicklook = imageService.getQuicklook(id);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "image/png");

        if (quicklook != null) {
            byte[] decodedBytes = Base64.getDecoder().decode(quicklook);
            return ResponseEntity.ok().headers(responseHeaders).body(decodedBytes);
        } else {
            logger.error("Image feature not found for id: " + id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new Gson().toJson("Feature not found for id: " + id));
        }
    }
}

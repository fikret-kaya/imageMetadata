package com.up42.imagemetadata.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.up42.imagemetadata.model.Feature;
import com.up42.imagemetadata.model.ImageMetadata;
import com.up42.imagemetadata.service.ImageService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {

    Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    // data stored in the hashmap (key:id)
    private Map<String, ImageMetadata> images = new HashMap<>();

    // parsing local json file to fetch related data
    // while parsing and storing the metadata only required fields are fetched and stored
    @PostConstruct
    public void parseFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        JSONParser jsonParser = new JSONParser();

        try {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(
                    new FileReader("data/source-data.json"));

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                JSONArray features = (JSONArray) jsonObject.get("features");
                JSONObject properties = (JSONObject) ((JSONObject) features.get(0)).get("properties");
                JSONObject acquisition = (JSONObject) properties.get("acquisition");

                Feature feature = new Feature(properties.get("id").toString(),
                        (Long) properties.get("timestamp"),
                        (Long) acquisition.get("beginViewingDate"),
                        (Long) acquisition.get("endViewingDate"),
                        acquisition.get("missionName").toString());

                if (!images.containsKey(properties.get("id").toString())) {
                    images.put(properties.get("id").toString(),
                            new ImageMetadata(feature, properties.get("quicklook")));
                }
            }
            logger.info("Parsing json file completed");
        } catch (FileNotFoundException e) {
            logger.error("File not found: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("IO exception: " + e.getMessage());
            e.printStackTrace();
        } catch (ParseException e) {
            logger.error("Parse exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Feature> getListOfFeatures() {
        List<Feature> features = new ArrayList<>();
        for (ImageMetadata imageMetadata : images.values()) {
            features.add(imageMetadata.getFeature());
        }
        return features;
    }

    @Override
    public Feature getFeature(String id) {
        if (id == null || id.isEmpty() || !images.containsKey(id)) {
            return null;
        }
        return images.get(id).getFeature();
    }

    @Override
    public String getQuicklook(String id) {
        if (id == null || id.isEmpty() || !images.containsKey(id)) {
            return null;
        }

        String image = (String) images.get(id).getQuickLook();
        return image;
    }
}

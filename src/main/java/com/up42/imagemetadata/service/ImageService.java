package com.up42.imagemetadata.service;

import com.up42.imagemetadata.model.Feature;

import java.util.List;

public interface ImageService {

    /**
     *
     * @return list of features
     */
    List<Feature> getListOfFeatures();

    /**
     *
     * @param id: image id
     * @return feature of given image
     */
    Feature getFeature(String id);

    /**
     *
     * @param id: image id
     * @return decoded image of the given image
     */
    String getQuicklook(String id);

}

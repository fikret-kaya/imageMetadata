package com.up42.imagemetadata.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ImageMetadata {
    private Feature feature;
    private Object quickLook;
}

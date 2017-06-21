package com.innovation.studio.paintpic.remote;

/**
 * Interfacing Class for visiondb.com API.
 *
 * @author Chaitanya Agrawal
 */
public class VisionDBApi {
    public static final class Config {
        public static final String URL = "https://api.visiondb.com/";
        public static final String IMAGE_URL = "http://img.visiondb.com/";

        public static final String PATH_FILTERS = "filter";
        public static final String PATH_TRENDING = "trending";
    }

    public static final class RESPONSE {
        public static final String VERSION = "1";

        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String THUMBNAIL_URL = "thumbnail_url";
    }
}

package com.kcapp.go4lunch.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GooglePlacesResponse {
    @SerializedName("html_attributions")
    private List<String> htmlAttributions = null;

    @SerializedName("next_page_token")
    private String nextPageToken = null;

    @SerializedName("results")
    private List<Result> results = null;


    /**
     * GETTERS
     */
    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }
    public String getNextPageToken() {
        return nextPageToken;
    }
    public List<Result> getResults() {
        return results;
    }

    /**
     * SETTERS
     */
    public void setHtmlAttributions(List<String> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }
    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }
    public void setResults(List<Result> results) {
        this.results = results;
    }

    public static class Result {
        @SerializedName("geometry")
        private Geometry geometry;

        @SerializedName("id")
        private String id;

        @SerializedName("icon")
        private String icon;

        @SerializedName("name")
        private String name;

        @SerializedName("openingHours")
        private OpeningHours openingHours;

        @SerializedName("photos")
        private List<Photos> photos;

        @SerializedName("place_id")
        private String placeId;

        @SerializedName("plus_code")
        private PlusCode plusCode;

        @SerializedName("rating")
        private double rating;

        @SerializedName("reference")
        private String reference;

        @SerializedName("scope")
        private String scope;

        @SerializedName("types")
        private List<String> types = null;

        @SerializedName("user_ratings_total")
        private Integer UserRatingsTotal;

        @SerializedName("vicinity")
        private String vicinity;

        /**
         * GETTERS
         **/
        public Geometry getGeometry() {
            return geometry;
        }

        public String getId() {
            return id;
        }

        public String getIcon() {
            return icon;
        }

        public String getName() {
            return name;
        }

        public OpeningHours getOpeningHours() {
            return openingHours;
        }

        public List<Photos> getPhotos() {
            return photos;
        }

        public String getPlaceId() {
            return placeId;
        }

        public PlusCode getPlusCode() {
            return plusCode;
        }

        public double getRating() {
            return rating;
        }

        public String getReference() {
            return reference;
        }

        public String getScope() {
            return scope;
        }

        public List<String> getTypes() {
            return types;
        }

        public Integer getUserRatingsTotal() {
            return UserRatingsTotal;
        }

        public String getVicinity() {
            return vicinity;
        }

        /**
         * SETTERS
         **/
        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setOpeningHours(OpeningHours openingHours) {
            this.openingHours = openingHours;
        }

        public void setPhotos(List<Photos> photos) {
            this.photos = photos;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public void setPlusCode(PlusCode plusCode) {
            this.plusCode = plusCode;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public void setUserRatingsTotal(Integer userRatingsTotal) {
            UserRatingsTotal = userRatingsTotal;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }

        //##### CLASS #####//

        /**
         * Class Geometry
         */
        public class Geometry {
            @SerializedName("location")
            private Location location;
            @SerializedName("viewport")
            private Viewport viewport;

            /**
             * GETTERS
             **/
            public Location getLocation() {
                return location;
            }

            public Viewport getViewport() {
                return viewport;
            }

            /**
             * SETTERS
             **/
            public void setLocation(Location location) {
                this.location = location;
            }

            public void setViewport(Viewport viewport) {
                this.viewport = viewport;
            }
        }

        /**
         * Class Location
         */
        public class Location {
            @SerializedName("lat")
            private double lat;
            @SerializedName("lng")
            private double lng;

            /**
             * GETTERS
             **/
            public double getLat() {
                return lat;
            }

            public double getLng() {
                return lng;
            }

            /**
             * SETTERS
             **/
            public void setLat(double lat) {
                this.lat = lat;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }
        }

        /**
         * Class Viewport
         */
        public class Viewport {
            @SerializedName("northeast")
            private Northeast northeast;
            @SerializedName("southwest")
            private Southeast southwest;

            /**
             * GETTERS
             */
            public Northeast getNortheast() {
                return northeast;
            }

            public Southeast getSouthwest() {
                return southwest;
            }

            /**
             * SETTERS
             */
            public void setNortheast(Northeast northeast) {
                this.northeast = northeast;
            }

            public void setSouthwest(Southeast southwest) {
                this.southwest = southwest;
            }
        }

        /**
         * Class Northeast
         */
        public class Northeast {
            @SerializedName("lat")
            private double lat;
            @SerializedName("lng")
            private double lng;

            /**
             * GETTERS
             */
            public double getLat() {
                return lat;
            }

            public double getLng() {
                return lng;
            }

            /**
             * SETTERS
             */
            public void setLat(double lat) {
                this.lat = lat;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }
        }

        /**
         * Class Southeast
         */
        public class Southeast {
            @SerializedName("lat")
            private double lat;
            @SerializedName("lng")
            private double lng;

            /**
             * GETTERS
             */
            public double getLat() {
                return lat;
            }

            public double getLng() {
                return lng;
            }

            /**
             * SETTERS
             */
            public void setLat(double lat) {
                this.lat = lat;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }
        }

        /**
         * Class OpeningHours
         */
        public class OpeningHours {
            @SerializedName("open_now")
            private String openNow;

            /**
             * GETTERS
             **/
            public String getOpenNow() {
                return openNow;
            }

            /**
             * SETTERS
             **/
            public void setOpenNow(String openNow) {
                this.openNow = openNow;
            }
        }

        /**
         * Class Photos
         */
        public class Photos {
            @SerializedName("height")
            private double height;

            @SerializedName("html_attributions")
            private List<String> htmlAttributions = null;

            @SerializedName("photo_reference")
            private String photoReference;

            @SerializedName("width")
            private double width;

            /**
             * GETTERS
             **/
            public double getHeight() {
                return height;
            }

            public List<String> getHtmlAttributions() {
                return htmlAttributions;
            }

            public String getPhotoReference() {
                return photoReference;
            }

            public double getWidth() {
                return width;
            }

            /**
             * SETTERS
             **/
            public void setHeight(double height) {
                this.height = height;
            }

            public void setHtmlAttributions(List<String> htmlAttributions) {
                this.htmlAttributions = htmlAttributions;
            }

            public void setPhotoReference(String photoReference) {
                this.photoReference = photoReference;
            }

            public void setWidth(double width) {
                this.width = width;
            }
        }

        /**
         * Class PlusCode
         */
        public class PlusCode {
            @SerializedName("compound_code")
            private String compoundCode;

            @SerializedName("global_code")
            private String globalCode;

            /**
             * GETTERS
             */
            public String getCompoundCode() {
                return compoundCode;
            }

            public String getGlobalCode() {
                return globalCode;
            }

            /**
             * SETTERS
             */
            public void setCompoundCode(String compoundCode) {
                this.compoundCode = compoundCode;
            }

            public void setGlobalCode(String globalCode) {
                this.globalCode = globalCode;
            }
        }

    }
}

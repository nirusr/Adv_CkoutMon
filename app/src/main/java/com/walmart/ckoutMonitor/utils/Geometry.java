
package com.walmart.ckoutMonitor.utils;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Geometry {

    private Location location;
    private String locationType;
    private Viewport viewport;

    /**
     * 
     * @return
     *     The location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * 
     * @param location
     *     The location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * 
     * @return
     *     The locationType
     */
    public String getLocationType() {
        return locationType;
    }

    /**
     * 
     * @param locationType
     *     The location_type
     */
    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    /**
     * 
     * @return
     *     The viewport
     */
    public Viewport getViewport() {
        return viewport;
    }

    /**
     * 
     * @param viewport
     *     The viewport
     */
    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

}

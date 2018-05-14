package com.conveyal.r5.analyst.cluster;

import com.conveyal.r5.analyst.GridCache;
import com.conveyal.r5.analyst.PointSet;
import com.conveyal.r5.transit.TransportNetwork;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single point, interactive task coming from the Analysis UI and returning a surface of travel
 * times to each destination (several travel times to each destination are returned, representing the percentiles
 * of travel time from the chosen origin to that destination.
 */
public class TravelTimeSurfaceTask extends AnalysisTask {
    @Override
    public Type getType() {
        return Type.TRAVEL_TIME_SURFACE;
    }

    @Override
    public boolean isHighPriority() {
        return true;
    }

    @JsonIgnoreProperties(ignoreUnknown=true)

    /** Whether to download as a Conveyal flat binary file for display in analysis-ui, or a geotiff */
    public enum Format {
        /** Flat binary grid format */
        GRID,
        /** GeoTIFF file for download and use in GIS */
        GEOTIFF
    }

    /** Default format is a Conveyal flat binary file */
    private Format format = Format.GRID;

    public void setFormat(Format format){
        this.format = format;
    }

    public Format getFormat(){
        return format;
    }

    /**
     * Get the list of point destinations to route to.
     * This may be an arbitrary list of points or the bounds of a grid we'll
     * generate points within.
     * Travel time results may be combined with many different grids, so we don't want to limit their geographic extent
     * to that of any one grid. Instead we use the extents supplied in the request.
     * The UI only sends these if the user has changed them to something other than "full region".
     * If "full region" is selected, the UI sends nothing and the backend fills in the bounds of the region.
     *
     * FIXME: the request bounds indicate either origin bounds or destination bounds depending on the request type.
     * We need to specify these separately as we merge all the request types.
     */
    @Override
    public List<PointSet> getDestinations(TransportNetwork network, GridCache gridCache) {
        List<PointSet> pointSets = new ArrayList<>();
        // Use TransportNetwork pointSet as base to avoid relinking
        pointSets.add(pointSetCache.get(this.zoom, this.west, this.north, this.width, this.height, network.pointSet));
        return pointSets;
    }
}

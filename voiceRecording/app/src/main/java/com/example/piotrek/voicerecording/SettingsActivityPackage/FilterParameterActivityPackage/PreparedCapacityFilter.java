package com.example.piotrek.voicerecording.SettingsActivityPackage.FilterParameterActivityPackage;

import com.example.piotrek.voicerecording.Tools.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piotrek on 2015-11-04.
 */
public class PreparedCapacityFilter {
    private List<Point> capacityPoints = null;
    private String name = null;

    public void addCapacityPoint(Point p)
    {
        if (capacityPoints == null)
            capacityPoints = new ArrayList<Point>();
        capacityPoints.add(p);
    }

    public PreparedCapacityFilter(String name)
    {
        this.name = name;

    }
    public PreparedCapacityFilter() {}

    @Override
    public String toString()
    {
        return this.name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Point> getCapacityPoints() {
        return capacityPoints;
    }
}

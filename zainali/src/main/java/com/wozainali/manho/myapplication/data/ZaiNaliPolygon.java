package com.wozainali.manho.myapplication.data;

import android.util.Log;

import java.util.ArrayList;

public class ZaiNaliPolygon {

    public boolean PointIsInRegion(double x, double y, ArrayList<ZaiNaliLatLng> thePath) {
        int crossings = 0;

        ZaiNaliLatLng point = new ZaiNaliLatLng(x, y);
        int count = thePath.size();

        // for each edge
        for (int i = 0; i < count; i++) {
            ZaiNaliLatLng a = thePath.get(i);

            int j = i + 1;
            if (j >= count) {
                j = 0;
            }
            ZaiNaliLatLng b = thePath.get(j);
            if (RayCrossesSegment(point, a, b)) {
                crossings++;
            }
        }
        // odd number of crossings?
        return (crossings % 2 == 1);
    }

    boolean RayCrossesSegment(ZaiNaliLatLng point, ZaiNaliLatLng a, ZaiNaliLatLng b) {
        double px = point.Longitude;
        double py = point.Latitude;
        double ax = a.Longitude;
        double ay = a.Latitude;
        double bx = b.Longitude;
        double by = b.Latitude;
        if (ay > by) {
            ax = b.Longitude;
            ay = b.Latitude;
            bx = a.Longitude;
            by = a.Latitude;
        }
        // alter longitude to cater for 180 degree crossings
        if (px < 0) {
            px += 360;
        }
        ;
        if (ax < 0) {
            ax += 360;
        }
        ;
        if (bx < 0) {
            bx += 360;
        }
        ;

        if (py == ay || py == by) py += 0.00000001;
        if ((py > by || py < ay) || (px > Math.max(ax, bx))) return false;
        if (px < Math.min(ax, bx)) return true;

        double red = (ax != bx) ? ((by - ay) / (bx - ax)) : Float.MAX_VALUE;
        double blue = (ax != px) ? ((py - ay) / (px - ax)) : Float.MAX_VALUE;
        return (blue >= red);
    }
}

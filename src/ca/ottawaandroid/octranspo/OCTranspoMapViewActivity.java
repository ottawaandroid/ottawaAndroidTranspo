package ca.ottawaandroid.octranspo;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class OCTranspoMapViewActivity extends MapActivity {
    MyMapItemizedOverlay mItemizedMapOverlays;
    private MapView mMapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_map);
        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.setBuiltInZoomControls(true);

        // TODO get current gps position

        // TODO get nearby bus stops from service

        // for testing mock list of GeoPoints
        showNearbyStops(mockStops());

        // for testing to center on test pin
        MapController mc = mMapView.getController();
        mc.setCenter(new GeoPoint(45425003, -75721633));
        mc.setZoom(18);
    }

    private void showNearbyStops(ArrayList<GeoPoint> stops) {
        // Bus stop pin
        Drawable busStopPin = this.getResources().getDrawable(R.drawable.ic_pin_stop);
        buildOverlay(busStopPin, stops);

    }

    private void showBusses(ArrayList<GeoPoint> busses) {
        // Bus pin
        Drawable busPin = this.getResources().getDrawable(R.drawable.ic_pin_bus);
        buildOverlay(busPin, busses);

    }

    private void buildOverlay(Drawable marker, ArrayList<GeoPoint> points) {
        List<Overlay> mapOverlays = mMapView.getOverlays();
        MyMapItemizedOverlay itemizedoverlay = new MyMapItemizedOverlay(marker, this);

        for (GeoPoint point : points) {
            OverlayItem overlayitem = new OverlayItem(point, "Stop ID: 5718", "8 27 40 95 96 105");
            overlayitem.setMarker(marker);
            itemizedoverlay.addOverlay(overlayitem);
            mapOverlays.add(itemizedoverlay);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private class MyMapItemizedOverlay extends ItemizedOverlay<OverlayItem> {
        private ArrayList<OverlayItem> icmOverlays = new ArrayList<OverlayItem>();
        private Context icmContext;

        public MyMapItemizedOverlay(Drawable marker, Context c) {
            super(boundCenterBottom(marker));
            icmContext = c;
        }

        public void clear() {
            icmOverlays.clear();
        }

        public void addOverlay(OverlayItem overlay) {
            super.boundCenterBottom(overlay.getMarker(0));
            icmOverlays.add(overlay);
            populate();
        }

        public void refresh() {
            populate();
        }

        @Override
        protected OverlayItem createItem(int i) {
            return icmOverlays.get(i);
        }

        @Override
        public int size() {
            return icmOverlays.size();
        }

        @Override
        protected boolean onTap(int index) {
            // show a view here that lets you select your bus stop
            // On bus stop selection, add nearby bus markers to overlay
            OverlayItem item = icmOverlays.get(index);
            AlertDialog.Builder dialog = new AlertDialog.Builder(icmContext);
            dialog.setTitle(item.getTitle());
            dialog.setMessage(item.getSnippet());
            dialog.show();
            return true;
        }
    }

    private ArrayList<GeoPoint> mockStops() {
        ArrayList<GeoPoint> stops = new ArrayList<GeoPoint>();
        stops.add(new GeoPoint(45425003, -75721633));
        return stops;
    }
}
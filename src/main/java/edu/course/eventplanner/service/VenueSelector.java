package edu.course.eventplanner.service;

import edu.course.eventplanner.model.Venue;
import java.util.*;

public class VenueSelector {
    private final List<Venue> venues;

    public VenueSelector(List<Venue> venues) {
        this.venues = venues;
    }

    public Venue selectVenue(double budget, int guestCount) {
        List<Venue> meetCriteria = new ArrayList<>();

        for (Venue v : venues) {
            if (v.getCapacity() >= guestCount && v.getCost() <= budget) {
                meetCriteria.add(v);
            }
        }
        if (meetCriteria.isEmpty()) {
            return null;
        }
        Collections.sort(meetCriteria, Comparator.comparing(Venue::getCost).thenComparing(Venue::getCost));
        return venues.get(0);

    }
}
package edu.course.eventplanner.service;

import edu.course.eventplanner.model.*;

import java.util.*;

public class SeatingPlanner {
    private final Venue venue;

    public SeatingPlanner(Venue venue) {
        this.venue = venue;
    }

    public Map<Integer, List<Guest>> generateSeating(List<Guest> guests) {
        if (guests == null || guests.isEmpty()) {
            return Map.of();
        }

        final int tableSeats = venue.getSeatsPerTable();
        final int tableCount = venue.getTables();

        // tableId -> guests
        Map<Integer, List<Guest>> seatingMap = new HashMap<>();

        // remainingSeats -> tableIds
        TreeMap<Integer, List<Integer>> leftoverTables = new TreeMap<>();

        // initialize tables
        for (int t = 0; t < tableCount; t++) {
            seatingMap.put(t, new ArrayList<>());
            leftoverTables
                    .computeIfAbsent(tableSeats, k -> new LinkedList<>())
                    .add(t);
        }

        // group guests
        Map<String, Queue<Guest>> guestGroups = new HashMap<>();
        for (Guest g : guests) {
            guestGroups
                    .computeIfAbsent(g.getGroupTag(), k -> new LinkedList<>())
                    .add(g);
        }

        // sort groups largest -> smallest
        List<Queue<Guest>> groups = guestGroups.values()
                .stream()
                .sorted((a, b) -> Integer.compare(b.size(), a.size()))
                .toList();

        // seat groups
        for (Queue<Guest> group : groups) {
            while (!group.isEmpty()) {

                // find smallest table that can fit remaining group
                Integer space = leftoverTables.ceilingKey(group.size());
                if (space == null) {
                    // otherwise take the table with the most space
                    space = leftoverTables.lastKey();
                }

                List<Integer> tables = leftoverTables.get(space);
                int tableId = tables.remove(0);

                if (tables.isEmpty()) {
                    leftoverTables.remove(space);
                }

                int seatsToFill = Math.min(space, group.size());

                for (int i = 0; i < seatsToFill; i++) {
                    seatingMap.get(tableId).add(group.poll());
                }

                int newSpace = space - seatsToFill;
                if (newSpace > 0) {
                    leftoverTables
                            .computeIfAbsent(newSpace, k -> new LinkedList<>())
                            .add(tableId);
                }
            }
        }

        return seatingMap;
    }
}

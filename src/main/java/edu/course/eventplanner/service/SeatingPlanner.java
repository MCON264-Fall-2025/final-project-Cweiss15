package edu.course.eventplanner.service;

import edu.course.eventplanner.model.*;

import java.util.*;

public class SeatingPlanner {
    private final Venue venue;

    public SeatingPlanner(Venue venue) {
        this.venue = venue;
    }

    public Map<Integer, List<Guest>> generateSeating(List<Guest> guests) {
        final int tableSeats = venue.getSeatsPerTable();
        final List<Integer> tables =  new ArrayList<>();

        if (guests.isEmpty()) {
            return null;
        }
        for (int i = 0; i < venue.getTables(); i++) tables.add(i);
        Map<Integer, List<Guest>> seatingMap = new HashMap<>();
        Map<String, Queue<Guest>> guestGroups = new HashMap<>();
        for (Guest guest : guests) {
            String tag = guest.getGroupTag();
            if (guestGroups.containsKey(tag)) {
                guestGroups.get(tag).add(guest);
            } else {
                guestGroups.put(tag, new LinkedList<>());
                guestGroups.get(tag).add(guest);
            }
        }
        int ctr = 0;
        for (Map.Entry<String, Queue<Guest>> group : guestGroups.entrySet()) {
            Queue<Guest> value = group.getValue();
            String key = group.getKey();
            if (value.size() == tableSeats) {
                seatingMap.put(tables.get(ctr), new LinkedList<>());
                seatingMap.get(tables.get(ctr)).addAll(guestGroups.get(key));
                ctr++;
                guestGroups.remove(key);
                tables.remove(ctr);
            }
            if (value.size() > tableSeats) {
                seatingMap.put(ctr, new LinkedList<>());
                for (int i = 0; i < tableSeats; i++) {
                    seatingMap.get(tables.get(ctr)).add(value.poll());
                    ctr++;
                    tables.remove(ctr);
                }
            }
        }
        //make a binary search tree with leftover seats at each table and leftover people in groups
        List<Map.Entry<String, Queue<Guest>>> sortedGroups =
                guestGroups.entrySet().stream().sorted((a, b) ->
                        Integer.compare(b.getValue().size(), a.getValue().size())).toList();

        TreeMap<Integer, List<Integer>> leftoverTables = new TreeMap<>();
        int groupSize;
        int space;
        for (Map.Entry<String, Queue<Guest>> sortedGroup : sortedGroups) {
            groupSize = sortedGroup.getValue().size();
            space = leftoverTables.ceilingKey(groupSize);
            List<Integer> bestFit = leftoverTables.get(space);
            List<Guest> seatedNow = new ArrayList<>();
            int i = 0;
            while (!sortedGroup.getValue().isEmpty() && i < space) {
                seatedNow.add(sortedGroup.getValue().poll());
                i++;
            }
            seatingMap.put(bestFit.get(0), seatedNow);
            leftoverTables.get(space).remove(0);
            if (leftoverTables.get(space).isEmpty())
                leftoverTables.remove(space);
            if (bestFit.isEmpty()) {
                leftoverTables.remove(space);
            }
            int newSpace = space - seatedNow.size();
            if (newSpace > 0) {
                if (!leftoverTables.containsKey(newSpace)) {
                    leftoverTables.put(newSpace, new LinkedList<>());
                }
                leftoverTables.get(newSpace).add(leftoverTables.get(space).get(0));
            }

        }
        return seatingMap;
    }
}

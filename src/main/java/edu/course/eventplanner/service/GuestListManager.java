package edu.course.eventplanner.service;

import edu.course.eventplanner.model.Guest;
import java.util.*;

public class GuestListManager {
    private final LinkedList<Guest> guests = new LinkedList<>();
    private final Map<String, Guest> guestByName = new HashMap<>();
    public void addGuest(Guest guest) {
        guests.add(guest);
    }
    public boolean removeGuest(Guest guest) { guests.remove(guest);
        for (Guest g : guests) {
            if (g.equals(guest)) {
                guests.remove(guest);
                return true;
            }
        }
        return false;
    }
    public Guest findGuest(String guestName) { return null; }
    public int getGuestCount() { return guests.size(); }
    public List<Guest> getAllGuests() { return guests; }
}

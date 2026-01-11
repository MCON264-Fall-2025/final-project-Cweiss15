package edu.course.eventplanner;

import edu.course.eventplanner.model.Guest;
import edu.course.eventplanner.model.Task;
import edu.course.eventplanner.model.Venue;
import edu.course.eventplanner.service.GuestListManager;
import edu.course.eventplanner.service.SeatingPlanner;
import edu.course.eventplanner.service.TaskManager;
import edu.course.eventplanner.service.VenueSelector;
import edu.course.eventplanner.util.Generators;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventPlannerTests {
    @Test
    public void testAddGuest() {
        GuestListManager guestListManager = new GuestListManager();
        Guest guest = new Guest("Jack", "Friend");
        guestListManager.addGuest(guest);
        List<Guest> guests = guestListManager.getAllGuests();
        assertEquals(1, guests.size());
        assertEquals(guest, guests.get(0));
    }

    @Test
    public void testRemoveGuest() {
        GuestListManager guestListManager = new GuestListManager();
        Guest guest = new Guest("Jack", "Friend");
        guestListManager.addGuest(guest);
        List<Guest> guests = guestListManager.getAllGuests();
        assertEquals(1, guests.size());
        guestListManager.removeGuest("Jack");
        assertTrue(guests.isEmpty());
    }

    @Test
    public void testAddTask() {
        TaskManager  taskManager = new TaskManager();
        Task task = new Task("Make guest list");
        Task task2 = new Task("Make seating chart");
        taskManager.addTask(task);
        taskManager.addTask(task2);
        Queue<Task> tasks = taskManager.getUpcomingTasks();
        assertEquals(2, tasks.size());
        assertEquals(task, tasks.poll());
        assertEquals(task2, tasks.poll());
    }

    @Test
    public void executeTask() {
        TaskManager  taskManager = new TaskManager();
        Task task = new Task("Make guest list");
        Task task2 = new Task("Make seating chart");
        taskManager.addTask(task);
        taskManager.addTask(task2);
        Queue<Task> tasks = taskManager.getUpcomingTasks();
        assertEquals(2, tasks.size());
        taskManager.executeNextTask();
        assertEquals(1, tasks.size());
        assertEquals(task2, tasks.poll());
        Stack<Task> done = taskManager.getCompletedTasks();
        assertEquals(1, done.size());
        assertEquals(task,  done.peek());
    }

    @Test
    public void undoTask() {
        TaskManager  taskManager = new TaskManager();
        Task task = new Task("Make guest list");
        Task task2 = new Task("Make seating chart");
        taskManager.addTask(task);
        taskManager.addTask(task2);
        Queue<Task> tasks = taskManager.getUpcomingTasks();
        assertEquals(2, tasks.size());
        taskManager.executeNextTask();
        assertEquals(1, tasks.size());
        Stack<Task> done = taskManager.getCompletedTasks();
        assertEquals(1, done.size());
        taskManager.undoLastTask();
        assertEquals(2, tasks.size());
        assertTrue(done.isEmpty());
        assertEquals(task, tasks.poll());
    }

    @Test
    public void bestVenueTest() {
        int budget = 2500;
        int guests = 55;
        VenueSelector venueSelector = new VenueSelector(Generators.generateVenues());
        Venue venue = venueSelector.selectVenue(budget, guests);
        assertEquals("Garden Hall", venue.getName());
    }

    @Test
    public void noBestVenueTest() {
        int budget = 1500;
        int guests = 55;
        VenueSelector venueSelector = new VenueSelector(Generators.generateVenues());
        Venue venue = venueSelector.selectVenue(budget, guests);
        assertTrue(venue==null);
    }

    @Test
    public void seatingTwoPartlyFullTableTest() {
        Venue venue = new Venue("Community Hall",1500,40,5,8);
        List<Guest> guests = new LinkedList<>();
        guests.add(new Guest("Jill", "Family"));
        guests.add(new Guest("Jill", "Family"));
        guests.add(new Guest("Jill", "Family"));
        guests.add(new Guest("Jill", "Family"));
        guests.add(new Guest("Jane", "Friend"));
        guests.add(new Guest("Jane", "Friend"));
        guests.add(new Guest("Jane", "Friend"));
        guests.add(new Guest("Jane", "Friend"));
        guests.add(new Guest("Jane", "Friend"));
        SeatingPlanner seatingPlanner = new SeatingPlanner(venue);
        Map<Integer, List<Guest>> seatingMap = seatingPlanner.generateSeating(guests);
        assertEquals(5, seatingMap.get(0).size());
        assertEquals("Friend", seatingMap.get(0).get(0).getGroupTag());
        assertEquals(4, seatingMap.get(1).size());
        assertEquals("Family", seatingMap.get(1).get(0).getGroupTag());
        assertEquals(0, seatingMap.get(3).size());
    }

    @Test
    public void seatingOneTableDifferentTagsTest() {
        Venue venue = new Venue("Community Hall",1500,40,5,8);
        List<Guest> guests = new LinkedList<>();
        guests.add(new Guest("Jill", "HisFamily"));
        guests.add(new Guest("Jack", "HerFriends"));
        guests.add(new Guest("Max", "HisFriends"));
        guests.add(new Guest("John", "HerFamily"));
        guests.add(new Guest("Jake", "HisFamilyFriend"));
        guests.add(new Guest("Jason", "HerFamilyFriend"));
        SeatingPlanner seatingPlanner = new SeatingPlanner(venue);
        Map<Integer, List<Guest>> seatingMap = seatingPlanner.generateSeating(guests);
        assertEquals(6, seatingMap.get(0).size());
        assertEquals(0, seatingMap.get(1).size());
    }
}


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

import static org.junit.jupiter.api.Assertions.*;

public class EventPlannerTests {
    //done
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
        assertEquals(1, tasks.size());
        assertTrue(done.isEmpty());
        assertEquals(task2, tasks.poll());
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

    @Test
    public void seatingTwoTablesSameTagsTest() {
        Venue venue = new Venue("Community Hall",1500,40,5,8);
        List<Guest> guests = new LinkedList<>();
        guests.add(new Guest("Jane", "Friend"));
        guests.add(new Guest("Jane", "Friend"));
        guests.add(new Guest("Jane", "Friend"));
        guests.add(new Guest("Jane", "Friend"));
        guests.add(new Guest("Jane", "Friend"));
        guests.add(new Guest("Jane", "Friend"));
        guests.add(new Guest("Jane", "Friend"));
        guests.add(new Guest("Jane", "Friend"));
        guests.add(new Guest("Jane", "Friend"));
        guests.add(new Guest("Jane", "Friend"));
        SeatingPlanner seatingPlanner = new SeatingPlanner(venue);
        Map<Integer, List<Guest>> seatingMap = seatingPlanner.generateSeating(guests);
        assertEquals(8, seatingMap.get(0).size());
        assertEquals(2, seatingMap.get(1).size());
    }

    @Test
    public void generateVenuesTest() {
        Generators generator = new Generators();
        List <Venue> venues = generator.generateVenues();
        assertEquals(3, venues.size());
    }

    @Test
    public void generateGuestsTest() {
        int guestCount = 45;
        Generators generator = new Generators();
        List<Guest> guests = Generators.GenerateGuests(guestCount);
        assertEquals(45, guests.size());
    }

    @Test
    public void undoWithNoCompletedTasks() {
        TaskManager taskManager = new TaskManager();
        Task undone = taskManager.undoLastTask();
        assertTrue(taskManager.getCompletedTasks().isEmpty());
        assertTrue(taskManager.getUpcomingTasks().isEmpty());
        assertEquals(null, undone);
    }

    @Test
    public void executeWithNoTasksTest() {
        TaskManager taskManager = new TaskManager();
        Task executed = taskManager.executeNextTask();
        assertEquals(null, executed);
        assertTrue(taskManager.getCompletedTasks().isEmpty());
    }

    @Test
    public void removeNonExistentGuestTests() {
        GuestListManager guestListManager = new GuestListManager();
        guestListManager.addGuest(new Guest("Jack", "Friend"));
        guestListManager.removeGuest("Jill");
        assertEquals(1, guestListManager.getAllGuests().size());
    }

    @Test
    public void seatingEmptyGuestListTest() {
        Venue venue = new Venue("Community Hall",1500,40,5,8);
        SeatingPlanner seatingPlanner = new SeatingPlanner(venue);
        Map<Integer, List<Guest>> seating = seatingPlanner.generateSeating(new ArrayList<>());
        assertTrue(seating.isEmpty());
    }

    @Test
    public void menuExitOption() {
        Scanner scanner = new Scanner("-1\n");
        int result = Main.menu(scanner);
        assertEquals(-1, result);
    }
        @Test
        public void removeGuestFound() {
            GuestListManager manager = new GuestListManager();
            manager.addGuest(new Guest("Bob", "Family"));

            Scanner scanner = new Scanner("Bob\n");
            Main.removeGuest(manager, scanner);

            assertTrue(manager.getAllGuests().isEmpty());
        }

    @Test
    public void addTaskAddsTask() {
        TaskManager manager = new TaskManager();
        Scanner scanner = new Scanner("Book venue\n");

        Main.addTask(scanner, manager);

        assertEquals(1, manager.getUpcomingTasks().size());
    }

    @Test
    public void MainUndoWithNoCompletedTasks() {
        TaskManager taskManager = new TaskManager();
        Task task = new Task("task");
        taskManager.addTask(task);
        Main.undoTask(taskManager);
        List<Task> tasks = taskManager.getCompletedTasks();
        assertTrue(taskManager.getCompletedTasks().isEmpty());
        assertTrue(taskManager.getUpcomingTasks().size()==1);
    }
    @Test
    public void MainLoadSampleGuestsTest() {
        List<Guest> guests = Main.loadSampleGuests(45);
        assertEquals(45, guests.size());
    }

    @Test
    public void MainSeatingMapCustomGuestListOverridesTest() {
        Venue venue = new Venue("Community Hall",1500,40,5,8);
        GuestListManager guestListManager = new GuestListManager();
        guestListManager.addGuest(new Guest("Jack", "Friend"));
        guestListManager.addGuest(new Guest("Bob", "Family"));
        guestListManager.addGuest(new Guest("Bob", "Family"));
        guestListManager.addGuest(new Guest("Bob", "Family"));
        guestListManager.addGuest(new Guest("Bob", "Family"));
        List<Guest> guests = Main.loadSampleGuests(45);
        assertEquals(45, guests.size());
        Map<Integer, List<Guest>> seatingMap = Main.createSeatingMap(venue, guestListManager, guests);
        assertEquals(5, seatingMap.get(0).size());
        assertTrue(seatingMap.get(1).isEmpty());
        assertEquals("Bob", seatingMap.get(0).getFirst().getName());
    }
    @Test
    public void MainSeatingMapGeneratedListUsedIfNoGuestListTest() {
        Venue venue = new Venue("Community Hall",1500,40,5,8);
        GuestListManager guestListManager = new GuestListManager();
        List<Guest> guests = Main.loadSampleGuests(40);
        assertEquals(40, guests.size());
        Map<Integer, List<Guest>> seatingMap = Main.createSeatingMap(venue, guestListManager, guests);
        assertFalse(seatingMap.isEmpty());
        assertEquals(8, seatingMap.get(0).size());
        assertEquals(8, seatingMap.get(4).size());
    }

    }






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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
    @Test
    public void MainSelectVenueTest() {
        int budget = 2500;
        int guests = 55;
        VenueSelector venueSelector = new VenueSelector(Generators.generateVenues());
        Venue venue = Main.selectVenue(venueSelector, budget, guests);
        assertEquals("Garden Hall", venue.getName());
    }

    @Test
    public void MainExecuteTask() {
        TaskManager  taskManager = new TaskManager();
        Task task = new Task("Make guest list");
        Task task2 = new Task("Make seating chart");
        taskManager.addTask(task);
        taskManager.addTask(task2);
        Queue<Task> tasks = taskManager.getUpcomingTasks();
        assertEquals(2, tasks.size());
        Main.completeTask(taskManager);
        assertEquals(1, tasks.size());
        assertEquals(task2, tasks.poll());
        Stack<Task> done = taskManager.getCompletedTasks();
        assertEquals(1, done.size());
        assertEquals(task,  done.peek());
    }
    @Test
    public void mainAddGuestCapturesGuestInManager() {
        GuestListManager manager = new GuestListManager();
        String inputData = "Alice\nFriend\n";
        Scanner scanner = new Scanner(inputData);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Main.addGuest(manager, scanner);

        System.setOut(originalOut);
        String output = outContent.toString();

        assertEquals(1, manager.getAllGuests().size());
        assertEquals("Alice", manager.getAllGuests().get(0).getName());
        assertEquals("friend", manager.getAllGuests().get(0).getGroupTag());
        assertTrue(output.contains("Enter guest name"));
        assertTrue(output.contains("Enter guest tag"));
    }

    @Test
    public void mainRemoveGuestNotFoundPrintsMessage() {
        GuestListManager manager = new GuestListManager();
        manager.addGuest(new Guest("Bob", "Family"));
        Scanner scanner = new Scanner("Alice\n");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Main.removeGuest(manager, scanner);

        System.setOut(originalOut);
        String output = outContent.toString();

        assertEquals(1, manager.getAllGuests().size());
        assertTrue(output.contains("Guest not found"));
    }

    @Test
    public void mainUndoTaskNoCompletedTasksPrintsMessage() {
        TaskManager manager = new TaskManager();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Main.undoTask(manager);

        System.setOut(originalOut);
        String output = outContent.toString();

        assertTrue(output.contains("There are no tasks currently completed"));
        assertTrue(manager.getUpcomingTasks().isEmpty());
        assertTrue(manager.getCompletedTasks().isEmpty());
    }
    @Test
    public void mainLoadSampleGuestsPrintsMessage() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        List<Guest> guests = Main.loadSampleGuests(5);

        System.setOut(originalOut);
        String output = outContent.toString();

        assertEquals(5, guests.size());
        assertTrue(output.contains("5\"dummy\" guests have been created"));
    }

    @Test
    public void mainCompleteTaskNoTasksPrintsMessage() {
        TaskManager manager = new TaskManager();

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Main.completeTask(manager);

        System.setOut(originalOut);
        String output = outContent.toString();

        assertTrue(output.contains("Your to do list is currently empty"));
        assertTrue(manager.getUpcomingTasks().isEmpty());
        assertTrue(manager.getCompletedTasks().isEmpty());
    }

    @Test
    public void mainCreateSeatingMapNoVenuePrintsMessage() {
        GuestListManager manager = new GuestListManager();
        List<Guest> guests = new ArrayList<>();
        guests.add(new Guest("Bob", "Family"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Map<Integer, List<Guest>> seating = Main.createSeatingMap(null, manager, guests);

        System.setOut(originalOut);
        String output = outContent.toString();

        assertNull(seating);
        assertTrue(output.contains("There is no venue selected"));
    }
    @Test
    public void seatingMapWithNoGuestsAndNoGuestList() {
        Venue venue = new Venue("Tiny Hall", 1000, 10, 1, 10);
        GuestListManager manager = new GuestListManager();
        List<Guest> guests = new ArrayList<>();

        Map<Integer, List<Guest>> seatingMap = Main.createSeatingMap(venue, manager, guests);

        assertNull(seatingMap);
    }

    @Test
    public void mainSelectVenueNoOptionsWithinBudget() {
        VenueSelector venueSelector = new VenueSelector(Generators.generateVenues());
        Venue venue = Main.selectVenue(venueSelector, 100, 100); // impossible budget/guests

        assertNull(venue);
    }

    @Test
    public void mainPrintStatsFullData() {
        GuestListManager guestListManager = new GuestListManager();
        guestListManager.addGuest(new Guest("Alice", "Friend"));
        guestListManager.addGuest(new Guest("Bob", "Family"));

        TaskManager taskManager = new TaskManager();
        taskManager.addTask(new Task("Task 1"));
        taskManager.executeNextTask(); // move to completed

        Venue venue = new Venue("Grand Hall", 3000, 50, 5, 10);
        Map<Integer, List<Guest>> seatingMap = new HashMap<>();
        seatingMap.put(0, Arrays.asList(new Guest("Alice", "Friend"), new Guest("Bob", "Family")));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        Main.printStats(2, 5000, taskManager, guestListManager, venue, seatingMap);

        System.setOut(originalOut);
        String output = outContent.toString();

        assertTrue(output.contains("Guest count: 2"));
        assertTrue(output.contains("Budget: 5000"));
        assertTrue(output.contains("Completed tasks list"));
        assertTrue(output.contains("Guest list:"));
        assertTrue(output.contains("Alice"));
        assertTrue(output.contains("Venue: Grand Hall"));
        assertTrue(output.contains("Seating map:"));
    }

    @Test
    public void mainPrintStatsEmptyData() {
        GuestListManager guestListManager = new GuestListManager();
        TaskManager taskManager = new TaskManager();
        Venue venue = null;
        Map<Integer, List<Guest>> seatingMap = null;

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.printStats(0, 0, taskManager, guestListManager, venue, seatingMap);

        System.setOut(System.out);
        String output = outContent.toString();

        assertTrue(output.contains("Guest count: 0"));
        assertTrue(output.contains("Budget: 0"));
        assertFalse(output.contains("Completed tasks list"));
        assertFalse(output.contains("Guest list:"));
    }

    @Test
    public void menuOptionLoadSampleData() {
        Scanner scanner = new Scanner("1\n");
        int choice = Main.menu(scanner);
        assertEquals(1, choice);
    }

    @Test
    public void menuOptionAddGuest() {
        Scanner scanner = new Scanner("2\n");
        int choice = Main.menu(scanner);
        assertEquals(2, choice);
    }

    @Test
    public void menuOptionRemoveGuest() {
        Scanner scanner = new Scanner("3\n");
        int choice = Main.menu(scanner);
        assertEquals(3, choice);
    }

    @Test
    public void menuInvalidThenValidInput() {
        Scanner scanner = new Scanner("12\n5\n");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        int choice = Main.menu(scanner);

        System.setOut(System.out);
        String output = outContent.toString();


        assertTrue(output.contains("Invalid option"));
        assertEquals(5, choice);
    }
    }






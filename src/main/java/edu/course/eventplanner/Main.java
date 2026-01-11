package edu.course.eventplanner;

import edu.course.eventplanner.model.Guest;
import edu.course.eventplanner.model.Task;
import edu.course.eventplanner.model.Venue;
import edu.course.eventplanner.service.GuestListManager;
import edu.course.eventplanner.service.SeatingPlanner;
import edu.course.eventplanner.service.TaskManager;
import edu.course.eventplanner.service.VenueSelector;
import edu.course.eventplanner.util.Generators;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Venue venue = null;
        Map<Integer, List<Guest>> seatingMap = null;
        GuestListManager guestListManager = new GuestListManager();
        TaskManager taskManager = new TaskManager();
        List<Venue> venues = Generators.generateVenues();
        VenueSelector venueSelector = new VenueSelector(venues);
        int choice=0;

        System.out.println("Please enter how many guests will be attending you event: ");
        int guestCount = input.nextInt();
        input.nextLine();
        System.out.println("Please enter your budget for your event: ");
        double budget = input.nextDouble();
        input.nextLine();

        while (choice!=-1) {
        choice = menu(input);
        switch (choice) {
            case 1: {
                loadSampleData(input, guestCount);
                break;
            }
            case 2: {
                addGuest(guestListManager, input);
                break;
            }
            case 3: {
                removeGuest(guestListManager, input);
                break;
            }
            case 4: {
                venue = selectVenue(venueSelector, budget, guestCount);
                break;
            }
            case 5: {
                seatingMap = createSeatingMap(venue, guestListManager);
                break;
            }
            case 6: {
                addTask(input, taskManager);
                break;
            }
            case 7: {
                completeTask(taskManager);
                break;
            }
            case 8: {
                undoTask(taskManager);
                break;
            }
            case 9: {
                printStats(guestCount, budget, taskManager, guestListManager, venue, seatingMap);
                break;
            }
            default: {
                System.out.println("Goodbye!");
                break;
            }
        }
        }

    }

    private static void printStats(int guestCount, double budget, TaskManager taskManager, GuestListManager guestListManager, Venue venue, Map<Integer, List<Guest>> seatingMap) {
        System.out.println("Here are you current event stats!");
        System.out.println("Guest count: " + guestCount);
        System.out.println("Budget: " + budget);
        if (!taskManager.getUpcomingTasks().isEmpty()) {
            System.out.println("To do list:\n" + taskManager.getUpcomingTasks());
        }
        if (!taskManager.getCompletedTasks().isEmpty()) {
            System.out.println("Completed tasks list:\n" + taskManager.getCompletedTasks());
        }
        if (!guestListManager.getAllGuests().isEmpty())
            System.out.println("Guest list: \n" + guestListManager.getAllGuests());
        if (venue != null) {
            System.out.println("Venue: " + venue.getName() + "\n\tCost: " + venue.getCost() + "\n\tCapacity: " + venue.getCapacity() + "\n\tNumber of tables: " + venue.getTables() + "\n\tSeats per table: " + venue.getSeatsPerTable());
        }
        if (seatingMap != null) {
            System.out.println("Seating map: \n" + seatingMap.toString());
        }
    }

    private static Map<Integer, List<Guest>> createSeatingMap(Venue venue, GuestListManager guestListManager) {

        if (venue == null) {
            System.out.println("There is no venue selected. Please select option 4 to find your optimal venue first.");
        } else {
            SeatingPlanner seatingPlanner = new SeatingPlanner(venue);
            Map<Integer, List<Guest>> seatingMap = seatingPlanner.generateSeating(guestListManager.getAllGuests());
            System.out.println(seatingMap);
            return seatingMap;
        }
        return null;
    }

    private static Venue selectVenue(VenueSelector venueSelector, double budget, int guestCount) {
        Venue optimal = venueSelector.selectVenue(budget, guestCount);
        if (optimal == null) {
            System.out.println("We're sorry, there is no venue large enough for your guest list and in your budget.");
        } else
            System.out.println("The best venue for your budget and guest list is " + optimal.getName() + ", its capacity is " + optimal.getCapacity() + " and the total cost is " + optimal.getCost() + ".");
        return optimal;
    }

    public static void undoTask(TaskManager taskManager) {
        Task task = taskManager.undoLastTask();
        if (task == null)
            System.out.println("There are no tasks currently completed.");
        System.out.println(task + " has been marked as undone and moved back from completed to your to do list.\nYou got this!");
    }

    private static void completeTask(TaskManager taskManager) {
        Task task = taskManager.executeNextTask();
        if (task == null) {
            System.out.println("Your to do list is currently empty. Hurray!");
        }
        System.out.println(task + " has been successfully moved from to do to completed list. Nice job!");
    }

    public static void addTask(Scanner input, TaskManager taskManager) {
        System.out.println("Enter task description to add to task list: ");
        String task = input.nextLine();
        Task newTask = new Task(task);
        taskManager.addTask(newTask);
    }

    public static int menu(Scanner input) {
        System.out.println("1. Load sample data\n" +
                "2. Add guest\n" +
                "3. Remove guest\n" +
                "4. Select venue\n" +
                "5. Generate seating chart\n" +
                "6. Add preparation task\n" +
                "7. Execute next task\n" +
                "8. Undo last task\n" +
                "9. Print event summary");
        System.out.println("Enter an option from the menu or -1 to exit: ");
        int choice = input.nextInt();
        input.nextLine();
        while ((choice < 1 || choice > 9) && choice != -1) {
            System.out.println("Invalid option. Try again: ");
            choice = input.nextInt();
            input.nextLine();
        }
        return choice;
    }


    public static void loadSampleData(Scanner input, int guestCount) {
        Generators.GenerateGuests(guestCount);
    }

    public static void addGuest(GuestListManager guestListManager, Scanner input) {
        System.out.println("Enter guest name:");
        String guestName = input.nextLine();
        System.out.println("Enter guest tag: ");
        String guestTag = input.nextLine().toLowerCase();
        Guest guest = new Guest(guestName, guestTag);
        guestListManager.addGuest(guest);
    }

    public static void removeGuest(GuestListManager guestListManager, Scanner input) {
        System.out.println("Enter guest name:");
        String guestName = input.nextLine();
        for (Guest g : guestListManager.getAllGuests()) {
            if (g.getName().equals(guestName)) {
                guestListManager.removeGuest(g.getName());
                System.out.println("Guest removed.");
                return;
            }
        }
        System.out.println("Guest not found.");
    }
}


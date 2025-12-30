package edu.course.eventplanner;

import edu.course.eventplanner.model.Guest;
import edu.course.eventplanner.service.GuestListManager;
import edu.course.eventplanner.util.Generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        GuestListManager guestListManager = new GuestListManager();

        int choice = menu(input);
        switch (choice) {
            case 1: {
                loadSampleData(input);
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
                break;
            }
            case 5: {
                break;
            }
            case 6: {
                break;
            }
            case 7: {
                break;
            }
            case 8: {
                break;
            }
            case 9: {
                break;
            }
        }

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
        while ((choice < 1 || choice > 9) && choice != -1) {
            System.out.println("Invalid option. Try again: ");
            choice = input.nextInt();
        }
        return choice;
    }

    public static int getGuestCnt(Scanner input) {
        System.out.println("Enter the number of guests your event will have: ");
        return input.nextInt();
    }

    public static void loadSampleData(Scanner input) {
        Generators.GenerateGuests(getGuestCnt(input));
        Generators.generateVenues();
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
        System.out.println("Enter guest tag:");
        String guestTag = input.nextLine();
        boolean found = false;
        while (!found) {
            for (Guest g : guestListManager.getAllGuests()) {
                if (g.getName().equals(guestName) && g.getGroupTag().equals(guestTag)) {
                    guestListManager.removeGuest(g);
                    found = true;
                }
            }
        }
    }
}

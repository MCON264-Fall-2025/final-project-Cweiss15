# Event Planner Mini

This project demonstrates practical use of data structures:
linked lists, stacks, queues, maps, trees, sorting, and searching.

## What You Must Do
- Implement all TODO methods
- Write JUnit 5 tests for core logic
- Pass instructor autograding tests
- Explain your design choices in this README

## Design Choices Explained
For this menu-driven event planner assistant I used a switch to run the main program. I kept my main class neat by making each case its own method.
There are different menu options to assist with making guest lists, keeping track of to-do lists, helping you find the perfect venue, and seat your guests so everyone is comfortable!

To choose a venue I used java's Collections.sort because it efficiently sorts through the list of venues and compares to find something under your budget with enough seats for your guests.
To make it even more efficient I first selected from the venues only the venues that work with the user's constraints of budget and capacity before sorting to find the cheapest of the viable options.

For the seating planner I first made a map of a queue of guests with their group's tag as the key. I then ordered the groups from largest to smallest.
I then made a treemap where the number of seats left open at the table is the key to an array of the table numbers matching in available seats.
I used the tree to efficiently go through the guest groups (largest to smallest) and seat them at the first available table that's closest in number of remaining seats to their group size.
This design enabled me to start with the largest groups so I can split up the groups as little as possible amongst different tables, and also ensured that when there was just a few stragglers left
they would generally be seated at a table with others and not by themselves. This also still ensured tables were filled in order, just not always to full capacity if a group could fit better elsewhere.


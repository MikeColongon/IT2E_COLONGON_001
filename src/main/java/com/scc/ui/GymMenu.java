package com.scc.ui;

import com.scc.dao.MemberDao;
import com.scc.model.Member;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GymMenu {

    private static final String LINE = "=".repeat(54);

    private final MemberDao memberDao;
    private final Scanner sc;

    public GymMenu(MemberDao memberDao, Scanner sc) {
        this.memberDao = memberDao;
        this.sc = sc;
    }

    // ---------------- MENU LOOP ----------------
    public void display() {
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> addMember();
                case 2 -> viewAllMembers();
                case 3 -> {
                    System.out.println("\nExiting system... Thank you!");
                    running = false;
                }
                default -> System.out.println("\nERROR: Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n" + LINE);
        System.out.println("          GYM MEMBERSHIP MANAGEMENT");
        System.out.println(LINE);
        System.out.println("[1] Add New Member");
        System.out.println("[2] View All Members");
        System.out.println("[3] Exit System");
        System.out.println(LINE);
    }

    // ---------------- ADD MEMBER ----------------
    private void addMember() {
        System.out.println("\n--- ADD NEW MEMBER ---");

        String name = readNonEmpty("Enter Full Name: ");
        String tier = readOption("Enter Tier (BASIC / PREMIUM / VIP): ",
                "BASIC", "PREMIUM", "VIP");
        double fee = readFee("Enter Monthly Fee: ");
        String status = readOption("Enter Status (ACTIVE / INACTIVE): ",
                "ACTIVE", "INACTIVE");

        if (memberDao.addMember(name, tier, fee, status)) {
            System.out.println("\nSUCCESS: Member record saved to database!");
        } else {
            System.out.println("\nERROR: Could not save member record.");
        }
    }

    // ---------------- VIEW ALL MEMBERS ----------------
    private void viewAllMembers() {
        List<Member> members = memberDao.getAllMembers();

        System.out.println("\n" + "=".repeat(80));
        System.out.println("                              ALL GYM MEMBERS");
        System.out.println("=".repeat(80));

        if (members.isEmpty()) {
            System.out.println("-> No records found.");
            System.out.println("Total Records Displayed: 0");
            return;
        }

        // Dynamic column widths: widest of header or data
        int wId = "ID".length();
        int wName = "FULL NAME".length();
        int wTier = "TIER".length();
        int wFee = "MONTHLY FEE".length();
        int wStatus = "STATUS".length();

        for (Member m : members) {
            wId = Math.max(wId, String.valueOf(m.id()).length());
            wName = Math.max(wName, m.fullName().length());
            wTier = Math.max(wTier, m.tier().length());
            wFee = Math.max(wFee, String.format("%.2f", m.monthlyFee()).length());
            wStatus = Math.max(wStatus, m.status().length());
        }

        String border = "+" + "-".repeat(wId + 2)
                + "+" + "-".repeat(wName + 2)
                + "+" + "-".repeat(wTier + 2)
                + "+" + "-".repeat(wFee + 2)
                + "+" + "-".repeat(wStatus + 2) + "+";

        String rowFormat = "| %-" + wId + "s | %-" + wName + "s | %-" + wTier
                + "s | %-" + wFee + "s | %-" + wStatus + "s |%n";

        System.out.println(border);
        System.out.printf(rowFormat, "ID", "FULL NAME", "TIER", "MONTHLY FEE", "STATUS");
        System.out.println(border);

        for (Member m : members) {
            System.out.printf(rowFormat,
                    m.id(),
                    m.fullName(),
                    m.tier(),
                    String.format("%.2f", m.monthlyFee()),
                    m.status());
        }

        System.out.println(border);
        System.out.println("Total Records Displayed: " + members.size());
    }

    // ---------------- INPUT HELPERS ----------------

    /** nextInt() followed by nextLine() to consume the leftover newline. */
    private int readInt(String prompt) {
        System.out.print(prompt);
        try {
            int value = sc.nextInt();
            sc.nextLine(); // consume leftover newline
            return value;
        } catch (InputMismatchException e) {
            sc.nextLine(); // discard the invalid input
            return -1;     // falls into the "invalid choice" branch
        }
    }

    /** nextDouble() followed by nextLine(), repeated until valid. */
    private double readFee(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double fee = sc.nextDouble();
                sc.nextLine(); // consume leftover newline
                if (fee < 0) {
                    System.out.println("ERROR: Fee cannot be negative.");
                    continue;
                }
                return fee;
            } catch (InputMismatchException e) {
                sc.nextLine(); // discard the invalid input
                System.out.println("ERROR: Please enter a valid number (e.g., 2500.00).");
            }
        }
    }

    private String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("ERROR: This field cannot be empty.");
        }
    }

    private String readOption(String prompt, String... allowed) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim().toUpperCase();
            for (String option : allowed) {
                if (option.equals(input)) {
                    return input;
                }
            }
            System.out.println("ERROR: Invalid value. Choose one of: " + String.join(", ", allowed));
        }
    }
}

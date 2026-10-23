package com.scc;

import com.scc.dao.DatabaseInitializer;
import com.scc.dao.MemberDao;
import com.scc.ui.GymMenu;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        DatabaseInitializer.initialize();

        Scanner scanner = new Scanner(System.in);
        new GymMenu(new MemberDao(), scanner).display();
        scanner.close();
    }
}

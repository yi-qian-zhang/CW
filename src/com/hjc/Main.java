package com.hjc;

import java.io.*;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Please enter the database you want to operate: ");
            String dbName = scanner.next();

            System.out.print("Please enter the name of the saved file: ");
            String readName = scanner.next();

            System.out.print("Please enter the name of the backup database: ");
            String copyName = scanner.next();

            scanner.close();

            CW cw = new CW(dbName, readName, copyName);

            cw.getData();
            cw.copyDB();
            CW.stat.close();
            CW.con.close();
        }
    }
}

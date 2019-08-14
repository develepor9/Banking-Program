import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class Main {
    private static Scanner scan = new Scanner (System.in);
    private static Scanner readFile;
    private static PrintWriter toFile;

    public static void main(String[] args) {
        boolean isInteracting = true;
        System.out.println("\nWelcome to Knab Bank's computer banking application!\nTo select a menu item" +
                " enter the number stated to the left of the item.");
           /*
           All the scan.nextLines and scan.nexts are to move the scanner to the next line and to 'reset' the
           scanner respectively, in order to minimize the times that the program will give the user the same
           message if the input contains spaces. Nothing crashes without them it just makes the ux more
           pleasant.
           */
        while (isInteracting)
        {
            try
            {
                File folder = new File ("src/Users");
                File [] listOfUsers = folder.listFiles();
                System.out.println("\n1. Login\n2. Sign up\n3. Exit");
                int input = scan.nextInt();

                switch (input)
                {
                    case 1: // login
                        System.out.println("\nEnter your username:");
                        String username = scan.next(); scan.nextLine();
                        System.out.println("\nEnter your password:");
                        String password = scan.next(); scan.nextLine();

                        boolean exists = false;
                        for (File user : listOfUsers)
                        {
                            if ((username + ".txt").equals(user.getName()))
                            {
                                exists = true;
                                boolean match = false;
                                try
                                {
                                    readFile = new Scanner(new File(user.getPath()));
                                    if (readFile.next().equals(password))
                                    {
                                        match = true;
                                    }
                                } catch (FileNotFoundException e)
                                {
                                    System.out.println("\nCould not find the user's file.");
                                }
                                if (match)
                                {
                                    System.out.println("\nWelcome back, "+username+".");
                                    userMenu(user);
                                } else
                                {
                                    System.out.println("\nThe password you entered is incorrect.");
                                }
                            }
                        }
                        if (!exists)
                        {
                            System.out.println("\nA user with this username does not exist.");
                        }
                        break;

                    case 2: // sign up
                        System.out.println("\n** You username or password may not contain spaces. **");
                        System.out.println("Enter the username you would like to use:");
                        username = scan.next(); scan.nextLine();
                        boolean isUnique = true;

                        for (File user : listOfUsers)
                        {
                            if ((username + ".txt").equals(user.getName()))
                            {
                                System.out.println("\nThis username is already in use. Please use a " +
                                        "different one.");
                                isUnique = false;
                            }
                        }

                        if (isUnique)
                        {
                            System.out.println("\nConfirm your username:");

                            if ((scan.next()).equals(username))
                            {
                                scan.nextLine();
                                System.out.println("\nEnter the password you would like to use:");
                                password = scan.next(); scan.nextLine();
                                System.out.println("\nConfirm your password:");
                                if (scan.next().equals(password))
                                {
                                    scan.nextLine();
                                    try {
                                        toFile = new PrintWriter("src/Users/" + username + ".txt");
                                        toFile.print(password+"\n=0");
                                        toFile.close();
                                        System.out.println("\nAccount created.");
                                    } catch (Exception e) {
                                        System.out.println("\nCould not find the file or IO exception.");
                                    }
                                } else {
                                    System.out.println("\nYour password does not match what you have entered " +
                                            "or contains a space.");
                                }
                            } else {
                                System.out.println("\nYour username does not match what you have entered or " +
                                        "contains a space.");
                            }
                        }
                        break;
                    case 3: // exit
                        isInteracting = false;
                        break;
                    default:
                        System.out.println("\nThat option does not exist! Please try again.");
                        break;
                }
            }
            catch (java.util.InputMismatchException e) {
                System.out.println("\nPlease enter whole numbers only.");
                scan.next();
            }
        }
    }

    private static void userMenu (File _user)
    {
        boolean isLoggedIn = true;
        while (isLoggedIn)
        {
            try {
                readFile = new Scanner(new File(_user.getPath()));
                ArrayList <String> fileContents = new ArrayList<>();
                while (readFile.hasNext()) {
                    fileContents.add(readFile.nextLine());
                }

                System.out.println("\n1. Deposit\n2. Withdraw\n3. Logout");
                int input = scan.nextInt(); scan.nextLine();
                float amount;

                switch (input) {
                    case 1:
                    case 2:
                        String sTotal = fileContents.get(fileContents.size()-1);
                        char [] caTotal = new char[sTotal.length()-1];
                        for (int i = 0; i < caTotal.length; i++) {
                            caTotal[i] = sTotal.charAt(i+1);
                        }
                        float newTotal = Float.parseFloat(new String(caTotal));

                        if (input == 1) {
                            System.out.println("\nEnter the amount you would like to deposit:");
                            amount = scan.nextFloat(); scan.nextLine();
                            newTotal += amount;
                            fileContents.add("+"+amount+" has been deposited.");
                            System.out.println("Success! $"+amount+" has been deposited.");
                        } else {
                            System.out.println("\nEnter the amount you would like to withdraw:");
                            amount = scan.nextFloat(); scan.nextLine();
                            newTotal -= amount;
                            fileContents.add("-"+amount+" has been withdrawn.");
                            System.out.println("Success! $"+amount+" has been withdrawn.");
                        }

                        fileContents.remove(fileContents.size()-2);
                        fileContents.add("="+newTotal);

                        toFile = new PrintWriter(new File(_user.getPath()));
                        for (String line: fileContents) {
                            toFile.println(line);
                        }
                        toFile.close();
                        break;
                    case 3:
                        isLoggedIn = false;
                        break;
                    default:
                        System.out.println("\nThat option does not exist! Please try again.");
                        break;
                }
            } catch (java.io.FileNotFoundException e) {
                System.out.println("\nCould not find your file.");
                scan.next();
            } catch (java.util.InputMismatchException e) {
                System.out.println("\nPlease enter whole numbers only.");
                scan.next();
            }
        }
    }
}
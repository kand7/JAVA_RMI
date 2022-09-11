import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.net.InetAddress;

/* ΒΙΚΤΩΡ ΡΟΜΑΝΙΟΥΚ*/

public class Client extends UnicastRemoteObject implements Listener {

    public void roomsAvailable(int rooms) {
        System.out.println("New Rooms available : " + rooms);
    }

    // The serializable class needs to have a final serialVersionUID
    private static final long serialVersionUID = -1113582265865921787L;

    public Client() throws RemoteException {
        super();// this keyword refers to parent class object
    }

    // void function to print the availability
    public void availableRooms(String notice) throws RemoteException {
        System.out.println(notice);
        System.exit(0);
    }

    public static void main(String[] args) throws InterruptedException, UnknownHostException {

        // store the hostname of the client to a variable named hostname
        String hostname = InetAddress.getLocalHost().getHostName();

        // when the client do not use any arguments the usage is printed
        if (args.length == 0) {
            System.out.println("Usage: java HRClient option argument(s)");
            System.out.println("option= list argument= hostname");
            System.out.println("option= book arguments= hostname type number name");
            System.out.println("option= guests argument= hostname");
            System.out.println("option= cancel arguments= hostname type number name");

            System.exit(0);
        }
        // when the client chooses option list and argument hostname
        else if (args[0].equals("list") && args[1].equals(hostname)) {
            try {
                Interface var = (Interface) Naming.lookup("rmi://localhost/Book");
                // getting all available rooms
                System.out.println(var.availableRooms());

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
        // when the client gives option book and arguments hostname type number name
        else if (args[0].equals("book") && args[1].equals(hostname)) {
            try {

                // searching for RMI
                Interface var = (Interface) Naming.lookup("rmi://localhost/Book");
                String book = var.bookRoom(args[2], args[3], args[4]);
                System.out.println(book);
                // two bools if booking was successful
                // bookRoom returns a String
                // book.contains returns true if the string contains the specific sequence of
                // characters
                boolean newBook = book.contains("Your booking was successful");
                boolean newBook2 = book.contains("No rooms available.");

                if (!newBook && !newBook2) {
                    // the two assignments below are used to manipulate the number of rooms
                    // available
                    book = book.replace("There are only ", "");
                    book = book.replace(" rooms available.Do you want to book the existing ones [Y/N]?", "");
                    // now book has the number of max rooms. We use replace. to filter the string
                    // and leave only the number.
                    // scan user input
                    Scanner scan = new Scanner(System.in);
                    char option = scan.next().charAt(0);

                    // check user's option
                    if (option == 'Y' || option == 'y') {
                        System.out.println(var.bookRoom(args[2], book, args[4]));
                    }
                    System.out.println("Do you wanna get notified when a room's available?");
                    option = scan.next().charAt(0);
                    if (option == 'Y' || option == 'y') {
                        Client hrClient = new Client();
                        System.out.println("Adding you to the list!");
                        if (args[2].equals("A")) {
                            var.addListenerA(hrClient);
                        } else if (args[2].equals("B")) {
                            var.addListenerB(hrClient);
                        } else if (args[2].equals("C")) {
                            var.addListenerC(hrClient);
                        } else if (args[2].equals("D")) {
                            var.addListenerD(hrClient);
                        } else if (args[2].equals("E")) {
                            var.addListenerE(hrClient);
                        } else
                            System.out.println("Failed to Add");
                        System.out.println("You've been added successfully!");

                    }
                    scan.close();

                }
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
        // when user chooses guests with argument hostname just print guests
        else if (args[0].equals("guests") && args[1].equals(hostname))

        {
            try {
                Interface var = (Interface) Naming.lookup("rmi://localhost/Book");
                System.out.println(var.guests());

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
        // when the user wants to cancel a room
        else if (args[0].equals("cancel") && args[1].equals(hostname)) {
            try {
                Interface var = (Interface) Naming.lookup("rmi://localhost/Book");
                System.out.println(var.cancelRoom(args[2], args[3], args[4]));

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
        // int any other case, meaning wrong input print usage
        else {
            System.out.println("Usage: java HRClient option argument(s)");
            System.out.println("option= list argument= hostname");
            System.out.println("option= book arguments= hostname type number name");
            System.out.println("option= guests argument= hostname");
            System.out.println("option= cancel arguments= hostname type number name");

            System.exit(0);
        }
    }

    @Override
    public void someoneCancelledA() throws RemoteException {

        System.out.println("Someone Cancelled a room A");

    }

    @Override
    public void someoneCancelledB() throws RemoteException {

        System.out.println("Someone Cancelled a room B");

    }

    @Override
    public void someoneCancelledC() throws RemoteException {

        System.out.println("Someone Cancelled a room C");

    }

    @Override
    public void someoneCancelledD() throws RemoteException {

        System.out.println("Someone Cancelled a room D");

    }

    @Override
    public void someoneCancelledE() throws RemoteException {

        System.out.println("Someone Cancelled a room E");

    }

}

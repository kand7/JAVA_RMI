import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
/* ΒΙΚΤΩΡ ΡΟΜΑΝΙΟΥΚ*/

public class Impl extends UnicastRemoteObject implements Interface {

    // The serializable class needs to have a final serialVersionUID
    private static final long serialVersionUID = -1113582265865921787L;

    // Lists of Listeners
    public List<Listener> listenersForA = new ArrayList<>();
    public List<Listener> listenersForB = new ArrayList<>();
    public List<Listener> listenersForC = new ArrayList<>();
    public List<Listener> listenersForD = new ArrayList<>();
    public List<Listener> listenersForE = new ArrayList<>();

    // 2D Array with Number and Price [number][price]
    public int[][] availableRooms = {
            { 30, 50 },
            { 45, 70 },
            { 25, 80 },
            { 10, 120 },
            { 5, 150 }
    };

    // 2D Array with Type and Description [type][desc]
    public String[][] roomType = {
            { "A", "single rooms" },
            { "B", "double rooms" },
            { "C", "twin rooms" },
            { "D", "triple rooms" },
            { "E", "quad rooms" }
    };

    public List<String> waiting;

    // a dynamically allocated 2d array responsible for the reservations
    // 115 is the sum of all rooms available
    // and 3 are the arguments that can be passed to its room
    // arguments are reservations[0][0] == type
    // arguments are reservations[0][1] == number of rooms
    // arguments are reservations[0][2] == name
    public String[][] reservations = new String[115][3];
    int resIndex = 0;// helpful variable to check whether there was an entry or not

    public Impl() throws RemoteException {
        super();
    }

    // availableRooms
    @Override
    public String availableRooms() throws RemoteException {
        // We return a string with all available rooms
        String available = "";
        for (int i = 0; i < availableRooms.length; i++) {
            available += availableRooms[i][0] + " rooms of type " + roomType[i][0]
                    + "(" + roomType[i][1] + ") are available, with price of: " + availableRooms[i][1]
                    + " euros per night.\n";
        }
        return available;
    }

    // bookRoom
    @Override
    public String bookRoom(String type, String number, String name) throws RemoteException {
        String total = "";
        boolean book = false;
        int numOfRooms = Integer.parseInt(number); // string to int

        for (int i = 0; i < roomType.length; i++) {

            if (roomType[i][0].equals(type)) {
                // if we have more available rooms than client needs
                if (availableRooms[i][0] >= numOfRooms) {
                    for (int j = 0; j < resIndex; j++) {
                        // if client already has same type rooms reserved
                        if (reservations[j][0].equals(type) && reservations[j][2].equals(name)) {
                            int numOfResRooms = Integer.parseInt(reservations[j][1]) + numOfRooms;
                            reservations[j][1] = String.valueOf(numOfResRooms);
                            availableRooms[i][0] -= numOfRooms;
                            total = "Your booking was successful.\nThe total price is : "
                                    + availableRooms[i][1] * numOfRooms
                                    + "euros.";

                            book = true;
                        }

                    }
                    // if first time booking a room
                    if (!book) {
                        reservations[resIndex][0] = type;
                        reservations[resIndex][1] = number;
                        reservations[resIndex][2] = name;

                        resIndex++;

                        availableRooms[i][0] -= numOfRooms;

                        total = "Your booking was successful.\nThe total price is : "
                                + availableRooms[i][1] * numOfRooms
                                + " euros.";
                    }
                    // if available rooms less than rooms client wants, but not 0
                } else if (availableRooms[i][0] < numOfRooms && availableRooms[i][0] != 0) {
                    total = "There are only " + availableRooms[i][0]
                            + " rooms available.Do you want to book the existing ones [Y/N]?";

                } else if (availableRooms[i][0] == 0) {
                    total = "No rooms available.";
                }
            }
        }
        return total;
    }

    // implementation of function guests
    @Override
    public String guests() throws RemoteException {
        String guests = "";

        if (resIndex == 0) {
            guests = "There are no confirmed reservations at the moment";
        } else {
            guests = "The list of reservation(s) is presented below:\n";

            for (int i = 0; i < resIndex; i++) {
                guests += reservations[i][2] + " has "
                        + reservations[i][1] + " rooms of type "
                        + reservations[i][0] + ".\n";
            }
        }
        return guests;
    }

    // implementation of function cancelRooms
    @Override
    public String cancelRoom(String type, String number, String name) throws RemoteException {
        String total = "There was a problem at finding your reservation.";

        if (resIndex == 0) {
            total = "There are no reservations to be canceled.";
        }

        else {

            int numOfRooms = Integer.parseInt(number);

            for (int i = 0; i < resIndex; i++) {
                // check if there's a reservation with parameters given
                if (reservations[i][0].equals(type) && reservations[i][2].equals(name)) {
                    int numOfResRooms = Integer.parseInt(reservations[i][1]);
                    if (numOfResRooms == numOfRooms) {
                        for (int j = 0; j < roomType.length; j++) {

                            if (roomType[j][0].equals(type)) {
                                availableRooms[j][0] += numOfRooms;
                            }
                        }
                        total = "You have canceled all rooms of type " +
                                type + " for the client " + name + ".";

                        if (type.equals("A")) {
                            notifyListenersA();
                        } else if (type.equals("B")) {
                            notifyListenersB();

                        } else if (type.equals("C")) {
                            notifyListenersC();

                        } else if (type.equals("D")) {
                            notifyListenersD();

                        } else if (type.equals("E")) {
                            notifyListenersE();

                        } else
                            System.out.println("Error Notifying Listeners");

                        resIndex--;

                    }

                    else if (numOfResRooms > numOfRooms) {
                        numOfResRooms -= numOfRooms;
                        reservations[i][1] = String.valueOf(numOfResRooms);

                        for (int j = 0; j < roomType.length; j++) {

                            if (roomType[j][0].equals(type)) {
                                availableRooms[j][0] += numOfRooms;
                            }
                        }

                        total = "You have canceled " + numOfRooms + " rooms of type " +
                                type + " for the client " + name + ".\nThe client now has " + numOfResRooms
                                + " room(s) in his reservation.";
                        if (type.equals("A")) {
                            notifyListenersA();
                        } else if (type.equals("B")) {
                            notifyListenersB();

                        } else if (type.equals("C")) {
                            notifyListenersC();

                        } else if (type.equals("D")) {
                            notifyListenersD();

                        } else if (type.equals("E")) {
                            notifyListenersE();

                        } else
                            System.out.println("Error Notifying Listeners");

                    } else
                        total = "Error! You are trying to cancel more rooms reservations than you have booked.";
                }

            }

        }

        return total;
    }

    @Override
    public void addListenerA(Listener hrListener) throws RemoteException {
        listenersForA.add(hrListener);
    }

    @Override
    public void addListenerB(Listener hrListener) throws RemoteException {
        listenersForB.add(hrListener);
    }

    @Override
    public void addListenerC(Listener hrListener) throws RemoteException {
        listenersForC.add(hrListener);
    }

    @Override
    public void addListenerD(Listener hrListener) throws RemoteException {
        listenersForD.add(hrListener);
    }

    @Override
    public void addListenerE(Listener hrListener) throws RemoteException {
        listenersForE.add(hrListener);
    }

    @Override
    public void removeListenerA(Listener hrListener) throws RemoteException {
        listenersForA.remove(hrListener);
    }

    @Override
    public void removeListenerB(Listener hrListener) throws RemoteException {
        listenersForB.remove(hrListener);
    }

    @Override
    public void removeListenerC(Listener hrListener) throws RemoteException {
        listenersForC.remove(hrListener);
    }

    @Override
    public void removeListenerD(Listener hrListener) throws RemoteException {
        listenersForD.remove(hrListener);
    }

    @Override
    public void removeListenerE(Listener hrListener) throws RemoteException {
        listenersForE.remove(hrListener);
    }

    public void notifyListenersA() {
        for (Listener listeners : listenersForA) {
            try {
                listeners.someoneCancelledA();
            } catch (RemoteException e) {
                listenersForA.remove(listeners);
            }

        }
    }

    public void notifyListenersB() {
        for (Listener listeners : listenersForB) {
            try {
                listeners.someoneCancelledB();
            } catch (RemoteException e) {
                listenersForB.remove(listeners);
            }

        }
    }

    public void notifyListenersC() {
        for (Listener listeners : listenersForC) {
            try {
                listeners.someoneCancelledC();
            } catch (RemoteException e) {
                listenersForC.remove(listeners);
            }

        }
    }

    public void notifyListenersD() {
        for (Listener listeners : listenersForD) {
            try {
                listeners.someoneCancelledD();
            } catch (RemoteException e) {
                listenersForD.remove(listeners);
            }

        }
    }

    public void notifyListenersE() {
        for (Listener listeners : listenersForE) {
            try {
                listeners.someoneCancelledE();
            } catch (RemoteException e) {
                listenersForE.remove(listeners);
            }

        }
    }

    public void getListeners() {
        for (int i = 0; i < listenersForA.size(); i++) {
            System.out.println("Listener : " + listenersForA.get(i));
        }
    }
}

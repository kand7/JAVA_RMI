import java.rmi.*;
/* ΒΙΚΤΩΡ ΡΟΜΑΝΙΟΥΚ*/
public interface Interface extends Remote {

    // method to depict available rooms which returns a string
    public String availableRooms() throws RemoteException;

    // method used to book a room with 3 parameters which returns a string
    public String bookRoom(String type, String number, String name) throws RemoteException;

    // method to display guests which also returns a string
    public String guests() throws RemoteException;

    // method to cancel rooms with 3 parameters which returns a string object
    public String cancelRoom(String type, String number, String name) throws RemoteException;

    public void addListenerA(Listener addListenerA) throws RemoteException;

    public void addListenerB(Listener addListenerB) throws RemoteException;

    public void addListenerC(Listener addListenerC) throws RemoteException;

    public void addListenerD(Listener addListenerD) throws RemoteException;

    public void addListenerE(Listener addListenerE) throws RemoteException;

    public void removeListenerA(Listener addListenerA) throws RemoteException;

    public void removeListenerB(Listener addListenerB) throws RemoteException;

    public void removeListenerC(Listener addListenerC) throws RemoteException;

    public void removeListenerD(Listener addListenerD) throws RemoteException;

    public void removeListenerE(Listener addListenerE) throws RemoteException;

}

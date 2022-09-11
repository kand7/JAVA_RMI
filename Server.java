import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
/* ΒΙΚΤΩΡ ΡΟΜΑΝΙΟΥΚ*/

public class Server {

    public static void main(String[] args) {
        try {
            // try to create a registry in port 1099
            LocateRegistry.createRegistry(1099);
            System.out.println("Java RMI registry created.");
        } catch (RemoteException e) {
            // if something goes wrong
            System.out.println("Java RMI registry already exists.");
        }

        // remote object associated with name so that remote callers can look it up
        Impl book;
        try {
            book = new Impl();
            Naming.rebind("rmi://localhost/Book", book);

        } catch (RemoteException e) {
            System.out.println("HRSServer: " + e.getMessage());
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("HRSServer: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception aInE) {
            System.out.println("Remote error- " + aInE);
        }
    }
}

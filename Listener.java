import java.rmi.Remote;
import java.rmi.RemoteException;
/* ΒΙΚΤΩΡ ΡΟΜΑΝΙΟΥΚ*/
public interface Listener extends Remote {

    void someoneCancelledA() throws RemoteException;

    void someoneCancelledB() throws RemoteException;

    void someoneCancelledC() throws RemoteException;

    void someoneCancelledD() throws RemoteException;

    void someoneCancelledE() throws RemoteException;

}

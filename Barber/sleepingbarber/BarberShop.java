package sleepingbarber;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author studente
 */

// oggetto condiviso fra il thread Barbiere ed i thread cliente
public class BarberShop {
    // attributi funzionali
    // costante che definisce il numero di sedie
    private static final int NUM_SEATS = 5;
    // variabile che conta i posti disponibili
    private int freeSeats;
    // attributi di sincronizzazione
    
    // a guadia di freeSeats
    private ReentrantLock mutex;
    // semaforo per sospendere il barbiere
    private Semaphore  newCustomers;
    // semaforo per sospendere i clienti
    private Semaphore barberAvailable;
    
    // costruttore dell'oggetto
    public BarberShop(){
         // attributi funzionali
         this.freeSeats       = BarberShop.NUM_SEATS;
         // attributi di sincronizzazione
         this.mutex           = new ReentrantLock();
         this.newCustomers    = new Semaphore(0); // solo un thread non serve FIFO
         this.barberAvailable = new Semaphore(0, true); // FIFO 
         
    }// end costruttore
    
     // metodo utilizzato dai clienti per
     // cercare posto nel negozio ed eventualmente attendere
     // il taglio di capelli
     public boolean lookAndWait(Customer c){
         boolean ret = false;
         // controllo se ci sono posti per l'attesa
         // INIZIO SEZIONE CRITICA
         this.mutex.lock();
         if(this.freeSeats > 0){
             // posso fermarmi in attesa del taglio
             this.freeSeats--;
             // sveglio il barbiere se dorme
             this.newCustomers.release();
             //System.out.println(this.newCustomers);
             // mi devo metter in attesa per il taglio dei capelli
             // rilascio il lock
             this.mutex.unlock();
             // ora posso attendere sul semaforo
             try{
                this.barberAvailable.acquire();
                // mi sono stati tagliati i capelli
                ret = true;
                System.out.println(c.getName() + " ha tagliato i capelli!!");
             }catch(InterruptedException e){
                 System.out.println(e);
             }
         }else {
             // non c'è posto torno a casa
             // rilascio il lock della mutua esclusione
             System.out.println("Il cliente " + c.getName()+
                     " non ha potuto tagliare i capelli!");
             this.mutex.unlock();
             
         }
         return ret;
    } // end metodo lookAndWait
         
    // metodo invocato dal barbiere per tagliare i capelli
    public void cut() throws InterruptedException {
        // si sospenderà subito in attesa dei clienti
        this.newCustomers.acquire();
        // se sono qua significa che almeno un cliente
        // è arrivato
        // incremento il numero di sedie disponibili
        this.mutex.lock();
        try{
            this.freeSeats++;
            System.out.println("Il barbiere taglia i capelli!!!");
        }finally{
            this.mutex.unlock();
        }
        // simulo il tempo necessario a tagliare i capelli con una sleep
        Thread.sleep(25); // tempo necessario 25ms
        // libero dalla coda di attesa il prossimo cliente
        // in ordine FIFO
        this.barberAvailable.release();  
        
    }// end metodo cut()
         
    
}// end classe
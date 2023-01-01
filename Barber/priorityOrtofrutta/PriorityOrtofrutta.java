/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package priorityOrtofrutta;

/**
 *
 * @author studente
 */
public class PriorityOrtofrutta {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creo l'oggetto da condividere
        OrtoShop shop    = new OrtoShop();
        Gestore  gestore = new Gestore(shop);
        gestore.start();
        
        // creo 50 clienti
        
        /* creo 30 clienti che vendono insalata */
        Cliente clienti[] = new Cliente[50];
        for(int i = 0; i < 30; i++){
           clienti[i] = new Cliente(shop, 
        		                    "Customer_"+i,
        		                    "INSALATA");
           clienti[i].start();
        }
        
        /* creo 20 clienti che vendono pomodori */
        for(int i = 30; i < 50; i++){
            clienti[i] = new Cliente(shop, 
         		                    "Customer_"+i,
         		                    "POMODORO");
            clienti[i].start();
         }
        
        try{
            // mi  metto in attesa della terminazione dei soli clienti
            for(int i = 0; i < clienti.length; i++){
               clienti[i].join();
            }
               // tutti i clenti sono terminati
               // invio l'interrupt al gestore
               gestore.interrupt();
               // attendo la terminazione del gestore
               gestore.join();
            
        }catch(InterruptedException e){
                System.out.println(e);
                                       
        }
        System.out.println("Simulazione terminata!!");
    }// end metodo main()
    
}// end classe

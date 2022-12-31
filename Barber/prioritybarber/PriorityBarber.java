/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prioritybarber;

/**
 *
 * @author studente
 */
public class PriorityBarber {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creo l'oggetto da condividere
        BarberShop shop = new BarberShop();
        Barber barber   = new Barber(shop);
        barber.start();
        
        // creo 10 clienti
        Customer customers[] = new Customer[10];
        for(int i = 0; i < customers.length; i++){
        customers[i] = new Customer(shop, "Customer_"+i);
        customers[i].start();
    }
        
        try{
            // mi  metto in attesa della terminazione dei soli clienti
            for(int i = 0; i < customers.length; i++){
               customers[i].join();
            }
               // tutti i clenti sono terminati
               // invio l'interrupt al barbiere
               barber.interrupt();
               // attendo la terminazione del barbiere
               barber.join();
            
        }catch(InterruptedException e){
                System.out.println(e);
                
                           
        }
        System.out.println("Simulazione terminata!!");
    }// end metodo main()
    
}// end classe

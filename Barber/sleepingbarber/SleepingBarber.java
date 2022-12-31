/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sleepingbarber;

/**
 *
 * @author studente
 */
public class SleepingBarber {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       // creo l'oggetto da condividere
       BarberShop shop      = new BarberShop();
       // creo il barbiere
       Barber barber        = new Barber(shop);
       // lancio il barbiere
       barber.start();
       // creo dieci clienti
       Customer customers[] = new Customer[10];
       for(int i = 0; i < customers.length; i++){
           customers[i] = new Customer(shop, "Customer_" + i);
           customers[i].start();
       }
       // ora il regista si mette in attesa SOLO per i clienti
       try{
           for(int i = 0; i < customers.length; i++){
                customers[i].join();
            }
       }catch(InterruptedException e){
           System.out.println(e);
               
       }
       // ora so che tutti i clienti sono terminati
       // posso terminare il barbiere
       // invio un interrupt al barbiere
       barber.interrupt();
       try{ 
           barber.join();     
       }catch(InterruptedException e){
           System.out.println(e);
               
       }
       // se sono qua, anche il barbiere è terminato
       System.out.println("Simulzione terminata!!");
       
    }// end main()
    
}// end classe

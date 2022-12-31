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
public class Gestore extends Thread{
    private OrtoShop myShop;
    
    public Gestore(OrtoShop o){
        super("Gestore");
        this.myShop = o;
    }
    
    // comportamento del thread
    // deve essere in terminazione deferita
    @Override
    public void run(){
        boolean isAlive = true;
        while(isAlive){
            try{
                this.myShop.formulaOrdini();
            }catch(InterruptedException e){
                System.out.println(e);
                isAlive = false;
                           
            }
        }// end while
        System.out.println(super.getName() +" termina!!!");
    }// end etodo run()
}// end classe

/*
This class will allow you to store product sales history. 
Products (Item class in this case) have a name and number sold
Sales history is sorted according to the date of sales and will allow for easy additions as records become available
*/

import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

/**
 *
 * @author Vincent Tereso
 */
public class SalesHistory {
    //Item class just to simplify parameters
    static class Item{
        String name;
        int numberSold;
        
        Item(String name, int numberSold){
            this.name=name;
            this.numberSold=numberSold;
        }
    }
    
    TreeMap<Date,HashMap<String,Integer>> sales;
            
    public SalesHistory(){
        this.sales=new TreeMap<>();
    }
    
    public void getHistory(Date date){
        HashMap<String,Integer> daySales=sales.get(date);
        if(daySales==null)System.out.println("No records for this date");
        else{
            System.out.printf("Date: %s\n",date);
            for(String key:daySales.keySet())System.out.printf("Item: %-10s Sold: %d\n",key,daySales.get(key));
        }
        System.out.println();
    }
    
    public void getFullHistory(){
        sales.keySet().forEach(this::getHistory);
    }
    
    public void addToRecord(Date date, Item item){
        HashMap<String,Integer> daySales=sales.get(date);
        if(daySales==null){
            daySales=new HashMap<>();
            sales.put(date, daySales);
        }
        daySales.merge(item.name,item.numberSold,Integer::sum);
    }
    
    public static void main(String[]args){
        SalesHistory history=new SalesHistory();
        //This Date constructor is deprecated, but just for example purposes
        //Demo data
        history.addToRecord(new Date(2000,12,10),new Item("Apples",200));
        history.addToRecord(new Date(2000,12,10),new Item("Oranges",200));
        history.addToRecord(new Date(2000,12,10),new Item("Apples",200));
        history.addToRecord(new Date(2018,3,16),new Item("Nuts",5));
        history.addToRecord(new Date(2000,12,11),new Item("Apples",201));
        history.addToRecord(new Date(2000,12,12),new Item("Apples",202));
        history.getFullHistory();
    }
    
    
}

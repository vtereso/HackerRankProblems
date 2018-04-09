package practice;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @author Vincent De Sousa Tereso
 */


/**
*   Extend/modify Functor enum as needed
* 
*   Functors MUST:
*       implement/override the toString method (otherwise defaulted to Functor name)
*       specify a precendence level through constructor (otherwise defaulted to 1)
*       specify an evaluate method in the form of a BiFunction <T,T> - > <T>
* 
*   NOTE:
*       Precedence will be checked from the top level to 1
*       Ensure that there are no holes in the precedence levels to improve performance
*       Enum instances are ordered by precedence for ease of use
*/
enum Functor{
    ADD(1){
        @Override
        public String toString(){
        return " + ";
        }
        
        @Override
        public int evaluate(int val1, int val2){
            return val1+val2;
        }
    },
    SUB(1){
        @Override
        public String toString(){
        return " - ";
        }
        
        @Override
        public int evaluate(int val1, int val2){
            return val1-val2;
        }
    },
    MULTI(2){
        @Override
        public String toString(){
        return " * ";
        }
        
        @Override
        public int evaluate(int val1, int val2){
            return val1*val2;
        }
    },
    DIV(2){
        @Override
        public String toString(){
        return " / ";
        }
        
        @Override
        public int evaluate(int val1, int val2){
            return val1/val2;
        }
    },
    //Ensure that this is the top precedence level by itself!!!
    BIND(3){
        @Override
        public String toString(){
        return "";
        }
        
        @Override
        public int evaluate(int val1, int val2){
            return (val1*10)+val2;
        }
    };
    //This is a special rule and should be at the top of the chain
    public static int maxPrecedence=Functor.BIND.precedence;
    public int precedence=1;
    
    Functor(int precedence){
        this.precedence=precedence;
    }
    //MUST override
    public int evaluate(int total, int value){
        return -1;
    }
}
//This class will permute numbers 1 up to 9 using the specified FunctorList and determine if they evaluate to the sum specified
//Will return a null pointer if the range is not within the correct range
public class SumFinder {
    
    //class to contain state while traversing over all permutations
    public static class Permutation{
        ArrayList<Integer> values;
        ArrayList<Functor> functors;
        
        //For the purposes of this class in the scope of SumFinder:
        //Simply a pointer to the data is fine
        //Instances only to be used by toString
        public Permutation(ArrayList<Integer> values, ArrayList<Functor> functors){
            this.values=values;
            this.functors=functors;
        }
        
        @Override
        public String toString(){
            StringBuilder sb=new StringBuilder();
            for(int i=0;i<values.size();i++){
                sb.append(String.format("%d%s",
                          values.get(i),
                         (i>=functors.size())?"":functors.get(i)));
            }
            return sb.toString();
        }
        
        //Evaluate a Permutation against check sum
        public static boolean evaluate(ArrayList<Integer> values, ArrayList<Functor> functorList, int sum){
            //run from the top precedence rule down
            //evaluating like an AST, without creating one
            ArrayList<Integer> valueCopy=new ArrayList<>(values);
            ArrayList<Functor> functorCopy=new ArrayList<>(functorList);
            for(int currentPrecedence=Functor.maxPrecedence;currentPrecedence>0;currentPrecedence--){
                int ref=0;
                Iterator<Functor> it=functorCopy.iterator();
                while(it.hasNext()){
                    Functor f=it.next();
                    if(f.precedence==currentPrecedence){
                        valueCopy.set(ref,f.evaluate(valueCopy.get(ref),valueCopy.get(ref+1)));
                        valueCopy.remove(ref+1);
                        it.remove();
                    }
                    else ref++;
                }
                if(functorList.isEmpty())break;
            }
            //if the evaluation is equal to the check sum
            return valueCopy.get(0)==sum;
        }
    }
    
    //Public API of SumFinder class
    //Finds permutations using ALL Functors in the Functor enum
    public static ArrayList<Permutation> computeSum(int rangeEnd, int sum){
        //invalid parameter values
        if(sum<1 || rangeEnd>9 || rangeEnd<1)return null;
        
        ArrayList<Integer> values=new ArrayList<>();
        for(int i=1;i<=rangeEnd;i++)values.add(i);
        return computeSum(values,
                          new ArrayList<>(Arrays.asList(Functor.values())), 
                          sum);
    }
    
    //Public API of SumFinder class
    //Finds permutations using ALL Functors specified
    public static ArrayList<Permutation> computeSum(int rangeEnd, int sum, ArrayList<Functor> functors){
        //invalid parameter values
        if(sum<1 || rangeEnd>9 || rangeEnd<1)return null;
        
        ArrayList<Integer> values=new ArrayList<>();
        for(int i=1;i<=rangeEnd;i++)values.add(i);
        return computeSum(values,
                          functors, 
                          sum);
    }
    
    //Private helper with core logic
    private static ArrayList<Permutation> computeSum(ArrayList<Integer> values, ArrayList<Functor> functors, int sum){
        ArrayList<ArrayList<Functor>> results=new ArrayList<>();
        results.add(new ArrayList<>());
        int listSize=values.size();
        for(int i=0;i<(listSize-1);i++){
            ArrayList<ArrayList<Functor>> newResults=new ArrayList<>();
            results.forEach(p->{
                functors.forEach(f->{
                    ArrayList<Functor> newFunctors=new ArrayList<>(p);
                    newFunctors.add(f);
                    newResults.add(newFunctors);
                });
            });
            results=newResults;
        }
        return results.stream()
               .filter(p->Permutation.evaluate(values,p,sum))
               .map(p->new Permutation(values,p))
               .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    //Example usage below
    public static void main(String[]args){
        //Specify Functor list
//        ArrayList<Functor> functors=new ArrayList<>(Arrays.asList(new Functor[]{Functor.ADD}));
//        ArrayList<Permutation> results=SumFinder.computeSum(9,6750,functors);
        
        //Defaultes to using all functions in Functor enum
        ArrayList<Permutation> results=SumFinder.computeSum(9,6750);
        if(results==null || results.isEmpty())System.out.println("No matches found");
        else results.forEach(System.out::println);
    }
}

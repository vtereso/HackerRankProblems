//NP implementation
//Solution could have been O(n/2)
//determine the maximal size subset where the sum of any two elements cannot be divisible by k
public static void main(String[] args)
    {
        Scanner in=new Scanner(System.in);
        int size=in.nextInt();
        int k=in.nextInt();
        int addToEnd=0;
        //group numbers by remainders key:remainder value:count
        HashMap<Integer,Integer>remainderMap=new HashMap<Integer,Integer>();
        for(int i=0;i<size;i++)
        {
            int remainder=in.nextInt()%k;
            Integer count=remainderMap.get(remainder);
            if(count==null)count=0;
            count++;
            remainderMap.put(remainder,count);
        }
        //remove "single" elements
        //two values of remainder 0 would be invalid-add single value
        if(remainderMap.remove(0)!=null)addToEnd++;
        //two values of remainder k/2 would be invalid-add single value
        if(k%2==0 && remainderMap.remove((k/2))!=null)addToEnd++;
        //find all elements with no collisions, remove and add to accum
        Iterator entries = remainderMap.entrySet().iterator();
        while(entries.hasNext())
        {
            Map.Entry entry = (Map.Entry) entries.next();
            Integer key = (Integer)entry.getKey();
            Integer value = (Integer)entry.getValue();
            if(remainderMap.get(k-key)==null)
            {
                addToEnd+=remainderMap.get(key);
                //entries.remove doesn't return the value like hashmap remove
                entries.remove();
            }
        }
        //find biggest subset with collisons
        int subsetSize=0;
        ArrayList<HashSet<Integer>> weaknessList=new ArrayList<HashSet<Integer>>();
        ArrayList<Integer> sums=new ArrayList<Integer>();
        for(Integer remainder1:remainderMap.keySet())
        {
            HashSet<Integer> weakSet=new HashSet<Integer>();
            weakSet.add(remainder1);
            weakSet.add(k-remainder1);
            weaknessList.add(weakSet);
            //new sum
            sums.add(remainderMap.get(remainder1));


            int currentSize=weaknessList.size();
            for(int i=0;i<currentSize;i++)
            {
                HashSet<Integer> weaknessSet=weaknessList.get(i);
                if(!weaknessSet.contains(remainder1))
                {
                    HashSet<Integer> newWeakSet=new HashSet<Integer>(weaknessSet);
                    newWeakSet.add(k-remainder1);
                    newWeakSet.add(remainder1);
                    weaknessList.add(newWeakSet);
                    //add new sum to sum list
                    sums.add(sums.get(i)+remainderMap.get(remainder1));
                }
            }
        }
        for(int i=0;i<sums.size();i++)
        {
            Integer val=sums.get(i);
            if(val>subsetSize)subsetSize=val;
        }
        subsetSize+=addToEnd;
        System.out.println(subsetSize);
    }

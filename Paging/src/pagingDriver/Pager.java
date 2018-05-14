package pagingDriver;

import java.io.*;
import java.util.*;
/**
 * Created by jeffersonvivanco on 11/28/16.
 */
public class Pager {

    public static void main(String[] args) throws IOException{
        //Creating new class to run main
        Pager pager = new Pager();
        //Creating queue to hold all numbers
        Queue<Integer> randomNumbers = new LinkedList<Integer>();

        /*
        Reading random-number text file and adding it to the queue to be used
         */
        FileInputStream input = null;
        BufferedReader br = null;
        //Scanner inputScan = new Scanner("./Paging/random_numbers_file");
        try{


            input = new FileInputStream("random_numbers_file.txt");
            br = new BufferedReader(new InputStreamReader(input));
        }catch (Exception e){
            System.err.println("The random numbers file could not be found, please make sure the random number file\n" +
                    "is in the project directory in the Paging folder, the file should \nbe called" +
                    "\"randome_numbers_file.txt\"");
            System.exit(0);
        }
        String line = null;
        while((line = br.readLine())!= null){
            randomNumbers.add(Integer.parseInt(line));
        }


        //Assigning input to variables
        int machineSize = Integer.parseInt(args[0]);
        int pageSize = Integer.parseInt(args[1]);
        int processSize = Integer.parseInt(args[2]);
        int jobMixNumber = Integer.parseInt(args[3]);
        int numOfRefPerProc = Integer.parseInt(args[4]);
        String replacementAlgo = args[5];

        pager.pagingSimulator(machineSize, pageSize,processSize,jobMixNumber,numOfRefPerProc,replacementAlgo, randomNumbers);


    }
    public void pagingSimulator(int machineSize, int pageSize, int processSize, int jobMixNumber, int numOfRefPerProc,
                                String replacementAlgo, Queue<Integer> randomNumbers){

        System.out.println("The machine size is "+machineSize);
        System.out.println("The page size is "+pageSize);
        System.out.println("The process size is "+processSize);
        System.out.println("The job mix number is "+jobMixNumber);
        System.out.println("The number of references per process is "+numOfRefPerProc);
        System.out.println("The replacement algorithm is "+replacementAlgo+"\n\n");

        /*
        Assigning A,B, & C based on job mix number and also creating number of processes.
         */
        HashMap<Integer,Process> processHashMap = new HashMap<Integer,Process>();
        switch (jobMixNumber){
            case 1:
                Process process = new Process(1,1,0,0,numOfRefPerProc);
                processHashMap.put(1, process);
                break;
            case 2:
                for(int i=1; i<=4; i++){
                    Process proce = new Process(i,1,0,0,numOfRefPerProc);
                    processHashMap.put(i,proce);
                }
                break;
            case 3:
                for(int i=1; i<=4; i++){
                    Process proc = new Process(i,0,0,0,numOfRefPerProc);
                    processHashMap.put(i, proc);
                }
                break;
            case 4:
                Process process1 = new Process(1,.75,.25,0,numOfRefPerProc);
                Process process2 = new Process(2,.75,0,.25,numOfRefPerProc);
                Process process3 = new Process(3, .75,.125,.125,numOfRefPerProc);
                Process process4 = new Process(4,.5,.125,.125,numOfRefPerProc);
                processHashMap.put(1, process1);
                processHashMap.put(2,process2);
                processHashMap.put(3,process3);
                processHashMap.put(4,process4);
                break;
        }


        /* Queue for lru replacement algorithm */
        Queue<Page> pageQueue = new LinkedList<>();
        /* Stack for lifo replacement algorithm */
        Stack<Page> pageStack = new Stack<Page>();

        /* Array that represents frame table */
        Page[] frameTable = new Page[machineSize/pageSize];

        /*  Simulating paging */
        int numOfProcesses = processHashMap.size();
        int q = 3;
        Arrays.fill(frameTable, null);
        int totalFaults = 0;
        Evictions totalEvictions = new Evictions();
        for(int time=1; time<=numOfRefPerProc*numOfProcesses;){

            /* Iterating through each process to run the number of references for each process */
            Set set  = processHashMap.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()){
                Map.Entry mentry = (Map.Entry)iterator.next();
                Process currentP = (Process)mentry.getValue();
                for(int ref=0; ref<3 && time<=numOfRefPerProc*numOfProcesses && currentP.getNumOfReferencesLeft()>0; ref++){
                    currentP.decrementNumOfReferences();
                    /* Getting ref word number */
                    int refWord =0;
                    if(currentP.isFirstReference()){
                        refWord = (111*currentP.getProcessNum())%processSize;
                        currentP.passedFirstReference();
                        currentP.setW(refWord);
                    }
                    else{
                        if(Math.abs(currentP.getW())>processSize){
                            currentP.setW(currentP.getW()+processSize);
                        }
                        refWord = (currentP.getW()+processSize )% processSize;
                    }
                    int pageNum = refWord/pageSize;
                    Page currentPage = new Page(pageNum, currentP.getProcessNum());
                    int hitNum = hit(frameTable,currentPage,replacementAlgo,pageQueue);
                    if(hitNum>=0){
                        System.out.println(currentP.getProcessNum()+" references word "+refWord+" page("+pageNum+")" +
                                "at a time "+time+": Hit in frame "+hitNum);
                    }else{
                        currentP.pageFault();
                        totalFaults++;
                        int frameUsedOrEvicted = add(frameTable,currentPage,replacementAlgo,pageQueue,currentP, time, totalEvictions, pageStack, processHashMap, randomNumbers);
                        if(frameUsedOrEvicted>=0){
                            System.out.println(currentP.getProcessNum()+ " references word "+refWord+
                                    " page("+pageNum+") at time "+time+ ": Fault using free frame "+frameUsedOrEvicted);
                        }
                    }
                    int r = randomNumbers.poll();
//                    System.out.println("Random number USED FOR Y: "+r);
                    double y = r/(Integer.MAX_VALUE + 1d);
                    if(y<currentP.getA()){
                        currentP.incrementWByA();
                        currentP.WIsNotRandom();
                    }
                    else if(y < (currentP.getA()+currentP.getB())){
                        currentP.doProbabilityBToW();
                        currentP.WIsNotRandom();
                    }
                    else if(y < (currentP.getA()+currentP.getB()+currentP.getC())){
                        currentP.doProbabilityCToW();
                        currentP.WIsNotRandom();
                    }
                    else if(y >= (currentP.getA()+currentP.getB()+currentP.getC())){
                        //Not sure what he means by each with probability (1-A-B-C)/S
                        int r2 = randomNumbers.poll();
//                        System.out.println("Random number used if y is fourth case:" +r2);
                        currentP.WIsRandom();
                        currentP.setW(r2);
                    }
                    else{
                        //do nothing, none of the probabilities is satisfied, probably an error
                    }
                    time++;
                }
            }
        }
        Set set2 = processHashMap.entrySet();
        Iterator iterator2 = set2.iterator();
        double totalAvgResTime = 0;
        while(iterator2.hasNext()){
            Map.Entry mentry = (Map.Entry)iterator2.next();
            Process p = (Process)mentry.getValue();

            if(p.getNumOfEvictions() != 0){
                System.out.printf("\nProcess %d had %d faults and %.1f average residency. \n",p.getProcessNum(),p.getNumOfPageFaults(),p.getAvgResTime());
                totalAvgResTime += p.getResTime();
            }
            else{
                System.out.printf("\nProcess %d had %d faults with no evictions, the average residence is undefined. \n",p.getProcessNum(),p.getNumOfPageFaults());
            }
        }
        if(totalEvictions.getNumOfEvictions() !=0){
            System.out.printf("\nThe total number of faults is %d and the overall average residency is %.1f. \n",totalFaults,totalAvgResTime/totalEvictions.getNumOfEvictions());

        }
        else{
            System.out.printf("\nThe total number of faults is %d with no evictions, the overall average residence is undefined. \n",totalFaults);
        }
    }

    public int hit(Page[] frameTable, Page currentPage, String replacementAlgo, Queue<Page> pageQueue){
        int index=-1;
        for(int i=0; i<frameTable.length; i++){
            if(frameTable[i] != null){
                if(currentPage.equals(frameTable[i])){
                    currentPage.setLoadedTime(frameTable[i].getLoadedTime());
                    if(replacementAlgo.equalsIgnoreCase("lru")){
                        if(pageQueue.contains(currentPage)){
                            Iterator<Page> iterator3 = pageQueue.iterator();
                            while (iterator3.hasNext()){
                                Page p = iterator3.next();
                                if(currentPage.equals(p)){
                                    iterator3.remove();
                                }
                            }
                            pageQueue.add(currentPage);
                        }
                        else {
                            pageQueue.add(currentPage);
                        }
                    }
                    return i;
                }
            }
        }
        return index;
    }
    public int add(Page[] frameTable, Page currentPage, String replacementAlgo, Queue<Page> pageQueue, Process process, int time, Evictions totalEvictions, Stack<Page> pageStack, HashMap<Integer,Process> processHashMap,
                   Queue<Integer> randomNumbers){

        for(int i=frameTable.length -1; i>=0; i--){
            if(frameTable[i]== null){
                frameTable[i]=currentPage;
                currentPage.setLoadedTime(time);
                if(replacementAlgo.equalsIgnoreCase("lifo")){
                    pageStack.add(currentPage);
                }
                if(replacementAlgo.equalsIgnoreCase("lru")){
                    if(pageQueue.contains(currentPage)){
                        Iterator<Page> iterator = pageQueue.iterator();
                        while (iterator.hasNext()){
                            Page p = iterator.next();
                            if(currentPage.equals(p)){
                                iterator.remove();
                            }
                        }
                        pageQueue.add(currentPage);
                    }
                    else {
                        pageQueue.add(currentPage);
                    }
                }
                return i;
            }
        }
        return evict(frameTable,currentPage,replacementAlgo,pageQueue,process, time, totalEvictions, pageStack, processHashMap, randomNumbers);
    }
    public int evict(Page[] frameTable, Page currentPage, String replacementAlgo, Queue<Page> pageQueue, Process process, int time, Evictions totalEvictions, Stack<Page>pageStack, HashMap<Integer,Process> processHashMap,
                     Queue<Integer>randomNumbers){
        Page removed = null;
        Process wherePageWasRemoved = null;
        switch (replacementAlgo){
            case "lru":
                removed = pageQueue.poll();
                wherePageWasRemoved = processHashMap.get(removed.getProcessNum());
                break;
            case "lifo":
                removed = pageStack.pop();
                wherePageWasRemoved = processHashMap.get(removed.getProcessNum());
                break;
            case "random":
                int removedNum = randomNumbers.poll();
//                System.out.println("Random number used in evict: "+removedNum);
                int removeIndex = (removedNum)%frameTable.length;
                removed = frameTable[removeIndex];
                wherePageWasRemoved = processHashMap.get(removed.getProcessNum());
                break;

        }
        for(int i=0; i<frameTable.length; i++){
            if(removed.equals(frameTable[i])){
                wherePageWasRemoved.addResTime(time - removed.getLoadedTime());
                wherePageWasRemoved.incrementEvict();
                totalEvictions.incEvictions();
                currentPage.setLoadedTime(time);
                frameTable[i] = currentPage;
                if(replacementAlgo.equalsIgnoreCase("lru")){
                    if(pageQueue.contains(currentPage)){
                        Iterator<Page> iterator = pageQueue.iterator();
                        while (iterator.hasNext()){
                            Page p = iterator.next();
                            if(currentPage.equals(p)){
                                iterator.remove();
                            }
                        }
                        pageQueue.add(currentPage);
                    }
                    else {
                        pageQueue.add(currentPage);
                    }
                }
                if(replacementAlgo.equalsIgnoreCase("lifo")){
                    pageStack.add(currentPage);
                }
                return i;
            }
        }
        return -1;
    }
}
/*
Eviction class that wraps an int to keep track of the total number of evictions that occur.
 */
class Evictions {
    private int numOfEvictions;

    public Evictions(){
        numOfEvictions = 0;
    }
    public void incEvictions(){
        this.numOfEvictions++;
    }
    public double getNumOfEvictions(){
        return this.numOfEvictions*1.0;
    }
}

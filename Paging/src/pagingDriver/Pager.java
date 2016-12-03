package pagingDriver;
import java.util.*;
/**
 * Created by jeffersonvivanco on 11/28/16.
 */
public class Pager {

    public static void main(String[] args){
        //Creating new class to run main
        Pager pager = new Pager();

        //Assigning input to variables
        int machineSize = Integer.parseInt(args[0]);
        int pageSize = Integer.parseInt(args[1]);
        int processSize = Integer.parseInt(args[2]);
        int jobMixNumber = Integer.parseInt(args[3]);
        int numOfRefPerProc = Integer.parseInt(args[4]);
        String replacementAlgo = args[5];

        pager.pagingSimulator(machineSize, pageSize,processSize,jobMixNumber,numOfRefPerProc,replacementAlgo);


    }
    public void pagingSimulator(int machineSize, int pageSize, int processSize, int jobMixNumber, int numOfRefPerProc,
                                String replacementAlgo){

        System.out.println("The machine size is "+machineSize);
        System.out.println("The page size is "+pageSize);
        System.out.println("The process size is "+processSize);
        System.out.println("The job mix number is "+jobMixNumber);
        System.out.println("The number of references per process is "+numOfRefPerProc);
        System.out.println("The replacement algorithm is "+replacementAlgo+"\n\n");

        /* Queue for lru replacement algorithm */
        Queue<Page> pageQueue = new LinkedList<>();
        /* Stack for lifo replacement algorithm */
        Stack<Page> pageStack = new Stack<Page>();

        /* Array that represents frame table */
        Page[] frameTable = new Page[machineSize/pageSize];

        if(jobMixNumber==1){
            Arrays.fill(frameTable, null);
            Process process = new Process(1);
            int finishTime = 0;
            int totalFaults = 0;
            Evictions totalEvictions = new Evictions();
            for(int i=1; i<=numOfRefPerProc; i++){
                int refWord = (111*process.getProcessNum()+(i-1))%processSize;
                int pageNum = refWord/pageSize;
                Page currentPage = new Page(pageNum,process.getProcessNum());
                int hitNum = hit(frameTable,currentPage);
                if(hitNum>=0){
                    System.out.println(process.getProcessNum()+ " references word "+refWord+" page("+pageNum+")"+
                    " at time "+i+": Hit in frame "+ hitNum);
                }
                else{
                    process.pageFault();
                    totalFaults++;
                    if(replacementAlgo.equalsIgnoreCase("lru")){
                        pageQueue.add(currentPage);
                    }

                    int frameUsedOrEvicted = add(frameTable,currentPage,replacementAlgo,pageQueue,process, i, totalEvictions, pageStack);
                    if(frameUsedOrEvicted >= 0){
                        System.out.println(process.getProcessNum()+ " references word "+refWord+
                                " page("+pageNum+") at time "+i+ ": Fault using free frame "+frameUsedOrEvicted);
                    }
                }
                finishTime = i;

            }
            if(totalEvictions.getNumOfEvictions() != 0){
                System.out.printf("\nProcess %d had %d faults and %.1f average residency. \n",process.getProcessNum(),process.getNumOfPageFaults(),process.getAvgResTime());
                System.out.printf("\nThe total number of faults is %d and the overall average residency is %.1f. \n",totalFaults,process.getResTime()/totalEvictions.getNumOfEvictions());
            }
            else{
                System.out.printf("\nProcess %d had %d faults with no evictions, the average residence is undefined. \n",process.getProcessNum(),process.getNumOfPageFaults());
                System.out.printf("\nThe total number of faults is %d with no evictions, the overall average residence is undefined. \n",totalFaults);
            }

        }

    }

    public int hit(Page[] frameTable, Page currentPage){
        int index=-1;
        for(int i=0; i<frameTable.length; i++){
            if(frameTable[i] != null){
                if(currentPage.equals(frameTable[i])){
                    currentPage.setLoadedTime(frameTable[i].getLoadedTime());
                    return i;
                }
            }
        }
        return index;
    }
    public int add(Page[] frameTable, Page currentPage, String replacementAlgo, Queue<Page> pageQueue, Process process, int time, Evictions totalEvictions, Stack<Page> pageStack){

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
        return evict(frameTable,currentPage,replacementAlgo,pageQueue,process, time, totalEvictions, pageStack);

    }
    public int evict(Page[] frameTable, Page currentPage, String replacementAlgo, Queue<Page> pageQueue, Process process, int time, Evictions totalEvictions, Stack<Page>pageStack){
        Page removed = null;
        switch (replacementAlgo){
            case "lru":
                removed = pageQueue.poll();
                break;
            case "lifo":
                removed = pageStack.pop();
                break;
        }
        for(int i=0; i<frameTable.length; i++){
            if(removed.equals(frameTable[i])){
                process.addResTime(time - removed.getLoadedTime());
                process.incrementEvict();
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

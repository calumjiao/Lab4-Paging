package pagingDriver;

import java.util.HashMap;

/**
 * Created by jeffersonvivanco on 11/28/16.
 */
public class Process {

    private int processNum;
    private int numOfPageFaults;
    private int waitingTime;
    private int loadingTime;

    public Process(int processNum){
        this.processNum = processNum;
        this.numOfPageFaults = 0;
        this.waitingTime = 0;
        this.loadingTime = 0;

    }
    public int getProcessNum(){
        return processNum;
    }
    public void pageFault(){
        this.numOfPageFaults++;
    }
}

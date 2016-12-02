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
    private double resTime;
    private int numOfEvictions;

    public Process(int processNum){
        this.processNum = processNum;
        this.numOfPageFaults = 0;
        this.resTime = 0.0;
        this.numOfEvictions = 0;

    }
    public int getProcessNum(){
        return processNum;
    }
    public void pageFault(){
        this.numOfPageFaults++;
    }
    public int getNumOfPageFaults(){
        return this.numOfPageFaults;
    }
    public void addResTime(double rt){
        this.resTime = this.resTime + rt;
    }
    public double getResTime(){
        return this.resTime;
    }
    public void incrementEvict(){
        this.numOfEvictions++;
    }
    public double getAvgResTime(){
        return this.resTime/this.numOfEvictions;
    }
    @Override
    public String toString(){
        return "processNum: "+this.processNum+" resTime: "+this.resTime;
    }
}

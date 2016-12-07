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
    private double A;
    private double B;
    private double C;
    private int nextWordRef;
    private int w;
    private int numOfReferences;
    private boolean wIsRandom;
    private boolean isFirstReference;

    public Process(int processNum){
        this.processNum = processNum;
        this.numOfPageFaults = 0;
        this.resTime = 0.0;
        this.numOfEvictions = 0;
        this.nextWordRef = 1;

    }
    public Process(int processNum, double A, double B, double C, int numOfReferences){
        this.processNum = processNum;
        this.numOfPageFaults = 0;
        this.resTime = 0.0;
        this.numOfEvictions = 0;
        this.A = A;
        this.B = B;
        this.C = C;
        this.nextWordRef = 1;
        this.w = 0;
        this.numOfReferences = numOfReferences;
        this.wIsRandom = false;
        this.isFirstReference = true;
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

    public int getNumOfEvictions() {
        return numOfEvictions;
    }

    public double getAvgResTime(){
        return this.resTime/this.numOfEvictions;
    }
    public double getA() {
        return A;
    }
    public double getB() {
        return B;
    }
    public double getC() {
        return C;
    }
    public int getNextWordRef() {
        return nextWordRef;
    }

    public void setNextWordRef(int nextWordRef) {
        this.nextWordRef = nextWordRef;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }
    public void incrementWByA(){
        this.w++;
    }
    public void doProbabilityBToW(){
        this.w  = this.w - 5;
    }
    public void doProbabilityCToW(){
        this.w = this.w +4;
    }
    public int getNumOfReferencesLeft() {
        return numOfReferences;
    }
    public void decrementNumOfReferences(){
        this.numOfReferences--;
    }
    public void WIsRandom(){
        this.wIsRandom = true;
    }
    public void WIsNotRandom(){
        this.wIsRandom = false;
    }
    public boolean getWIsRandom(){
        return this.wIsRandom;
    }
    public boolean isFirstReference() {
        return isFirstReference;
    }
    public void passedFirstReference(){
        this.isFirstReference = false;
    }
    @Override
    public String toString(){
        return "processNum: "+this.processNum+" resTime: "+this.resTime;
    }
}

package pagingDriver;
import java.util.*;
import java.io.*;
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
        System.out.println("The replacement algorithm is "+replacementAlgo);

        int[] frameTable = new int[machineSize/pageSize];
        if(jobMixNumber==1){
            Arrays.fill(frameTable, -1);
            Process process = new Process(1);
            for(int i=1; i<=numOfRefPerProc; i++){
                int refWord = (111*process.getProcessNum()+(i-1))%processSize;
                int pageNum = refWord/pageSize;
                int hitNum = hit(frameTable,pageNum);
                if(hitNum>=0){
                    System.out.println(process.getProcessNum()+ "references word "+refWord+"("+pageNum+")"+
                    " at time "+i+": Hit in frame"+ hitNum);
                }
                else{

                }

            }
        }

    }

    public int hit(int[] frameTable, int pageNum){
        int index=-1;
        for(int i=0; i<frameTable.length; i++){
            if(pageNum ==i){
                index = i;
            }
        }
        return index;
    }


}

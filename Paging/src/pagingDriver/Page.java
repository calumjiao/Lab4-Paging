package pagingDriver;

/**
 * Created by jeffersonvivanco on 11/30/16.
 */
public class Page {

    private int pageNum;
    private int processNum;
    private int loadedTime;

    public Page(int pageNum,int processNum){
        this.pageNum = pageNum;
        this.processNum = processNum;
    }

    int getPageNum(){
        return pageNum;
    }
    int getProcessNum(){
        return processNum;
    }

    public void setLoadedTime(int l){
        this.loadedTime = l;
    }
    public int getLoadedTime(){
        return this.loadedTime;
    }
    @Override
    public boolean equals(Object object){
        Page other  = (Page)object;
        if(this.pageNum == other.getPageNum() && this.processNum == other.getProcessNum()){
            return true;
        }
        else{
            return false;
        }
    }
    @Override
    public String toString(){
        return "Process num: "+this.processNum+" page num: "+pageNum;
    }
}

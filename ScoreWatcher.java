import org.apache.zookeeper.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.io.IOException;

public class ScoreWatcher implements Watcher, Runnable{
    private static ZooKeeper zk;
    private static int count;
    private static BuildResult bres;
    public void process(WatchedEvent we){
        if(we.getType() == Event.EventType.NodeChildrenChanged || we.getState() == Event.KeeperState.SyncConnected){
            getChildren();
        }
    }

    AsyncCallback.ChildrenCallback getChildrenCallBack = new AsyncCallback.ChildrenCallback(){
        public void processResult(int rc, String path, Object ctx, List<String> nodes){
            if(nodes != null || (!nodes.isEmpty())){
                bres.getList(nodes);
                bres.buildHighScores();
                bres.buildRecent();
            }
        }
    };

    public void getChildren(){
        zk.getChildren("/data", this, getChildrenCallBack, null);
    }

    public void run(){
        try{
            synchronized(this){
                while(true){
                    wait();
                }
            }
        }
        catch(InterruptedException e){
        }

    }

    public static void watcherMain(String[] args) throws IOException, InterruptedException, KeeperException{
        if(args.length != 3){
            System.out.println("Enter the right number of arguments!");
            System.exit(1);
        }

        String active_base = "/active_nodes";
        String data_base = "/data";

        ScoreWatcher sw = new ScoreWatcher();
        String ip = args[1];
        count = Integer.parseInt(args[2]);
        zk = new ZooKeeper(ip, 5000, sw);
        bres = new BuildResult(zk, count);

        if(zk.exists(active_base, false) == null)
            zk.create(active_base, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        if(zk.exists(data_base, false) == null)
            zk.create(data_base, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        sw.getChildren();
        sw.run();
    }
}


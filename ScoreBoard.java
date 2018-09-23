import org.apache.zookeeper.KeeperException;

import java.io.IOException;

public class ScoreBoard {
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException{
        if(args[0].equals("player")){
            new Player().playerMain(args);
        }
        if(args[0].equals("watcher")){
            new ScoreWatcher().watcherMain(args);
        }
    }
}

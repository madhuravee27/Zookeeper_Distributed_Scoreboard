import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ConnectToZk {
    private ZooKeeper zk;
    private CountDownLatch connSignal = new CountDownLatch(1);

    public ZooKeeper connect(String host) throws IllegalStateException, IOException, InterruptedException{
        zk = new ZooKeeper(host, 5000, new Watcher(){
            public void process(WatchedEvent we){
                if(we.getState() == Event.KeeperState.SyncConnected){
                    connSignal.countDown();
                }
            }
        });
        connSignal.await();

        return zk;
    }

    public void close() throws InterruptedException{
        zk.close();
    }
}

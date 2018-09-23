import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Player {
    private static ZooKeeper zk;
    protected static ConnectToZk zkConnect;

    public static void playerMain(String[] args) throws IOException, InterruptedException, KeeperException {

        if(!(args.length == 3 || args.length == 6)){
            System.out.println("Enter the right number of arguments!");
            System.exit(1);
        }

        zkConnect = new ConnectToZk();
        Random rand = new Random();
        String ip = args[1];
        String name = args[2];
        String store_name = name;

        zk = zkConnect.connect(ip);
        String active_base = "/active_nodes";
        String data_base = "/data";

        if(zk.exists(active_base, false) == null)
            zk.create(active_base, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        if(zk.exists(data_base, false) == null)
            zk.create(data_base, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        if(args.length == 3){
            if(name.split(" ").length == 2){
                String []name_tmp = name.split(" ");
                name = name_tmp[0] + "_" + name_tmp[1];
            }

            String active_name = active_base + "/" + name;
            String data_name = data_base + "/" + name;
            if(zk.exists(active_name, false) == null){
                zk.create(active_name, name.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                zk.create(data_name, name.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            }
            else{
                System.out.println("Player already exists");
                System.exit(1);
            }
        }

        if(args.length == 6){
            int count = Integer.parseInt(args[3]);
            int u_delay = Integer.parseInt(args[4]);
            int u_score = Integer.parseInt(args[5]);
            if(count < 0 || u_delay < 0 || u_score < 0){
                System.out.println("Enter positive values!");
                System.exit(1);
            }

            count = Math.min(Integer.MAX_VALUE, count);
            u_delay = Math.min(Integer.MAX_VALUE, u_delay);
            u_score = Math.min(Integer.MAX_VALUE - 3, u_score);

            if(name.split(" ").length == 2){
                String []name_tmp = name.split(" ");
                name = name_tmp[0] + "_" + name_tmp[1];
            }

            String active_name = active_base + "/" + name;
            String data_name = data_base + "/" + name;
            if(zk.exists(active_name, false) == null){
                zk.create(active_name, name.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                zk.create(data_name, name.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            }
            else{
                System.out.println("Player already exists");
                System.exit(1);
            }

            for(int i = 1; i <= count; i++){
                int score = (int)(Math.abs(rand.nextGaussian()*3 + u_score));
                String data  = String.valueOf(score);
                zk.create(data_base +"/"+ name +"_"+ LocalDateTime.now(), (name +" " + data +" " + LocalDateTime.now()).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                TimeUnit.SECONDS.sleep(u_delay);
            }
        }
		
		while(true){
		}

        /*
        while(true){
            System.out.println("Enter score");
            String data = br.readLine();
            zk.create(data_base +"/"+ name +"_"+ LocalDateTime.now(), (name +" " + data +" " + LocalDateTime.now()).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        */
    }
}

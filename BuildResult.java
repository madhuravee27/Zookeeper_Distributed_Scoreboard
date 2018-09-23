import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class BuildResult {
    private List<NodeData> zNodes;
    private ZooKeeper zk;
    private int count;

    BuildResult(ZooKeeper zk, int count){
        this.zk = zk;
        this.count = count;
    }

    public void getList(List <String> nodes){
        zNodes = new ArrayList<>();
        String []nodeData;
        String name;
        LocalDateTime time;
        int data;
        for(String node : nodes){
            try{
                Stat details = zk.exists("/data/" + node, false);
                if(details.getEphemeralOwner() == 0){
                    nodeData = new String(zk.getData("/data/" + node, false, zk.exists("/data/" + node, false)), "UTF-8").split(" ");
                    if(nodeData!=null && nodeData.length != 0) {
                        //Arrays.toString(nodeData);
                        name = nodeData[0];
                        data = Integer.parseInt(nodeData[1]);
                        time = LocalDateTime.parse(nodeData[2]);
                        zNodes.add(new NodeData(name, time, data));
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (KeeperException k) {
                k.printStackTrace();
            }catch (UnsupportedEncodingException u){
                u.printStackTrace();
            }
        }
    }

    public void buildRecent(){
        Collections.sort(zNodes, new TimeComparator());
        System.out.println(">>>>>>>>>>>>>>>>>");
        System.out.println("Recent scores: ");
        System.out.println(">>>>>>>>>>>>>>>>>");
        organizeAndPrint(zNodes);
    }

    public void buildHighScores(){
        Collections.sort(zNodes, new DataComparator());
        System.out.println(">>>>>>>>>>>>>>>>>");
        System.out.println("Top scores: ");
        System.out.println(">>>>>>>>>>>>>>>>>");
        organizeAndPrint(zNodes);
    }

    public void organizeAndPrint(List<NodeData> zNodes){
        if(zNodes.size() <= count) {
            for (NodeData n : zNodes) {
                printData(n);
            }
        }
        else{
            int ptr = 0;
            for (int i = count; i > 0; i--){
                NodeData nd = zNodes.get(ptr++);
                printData(nd);
            }
        }
    }

    public void printData(NodeData nd){
        try {
            String active = zk.exists("/active_nodes/" + nd.name, false) != null ? "**" : "";
            System.out.println(nd.name + "\t" + nd.data + "\t" + active);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (KeeperException k) {
            k.printStackTrace();
        }
    }
}


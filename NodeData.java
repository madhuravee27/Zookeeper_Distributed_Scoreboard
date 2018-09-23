import java.time.LocalDateTime;
import java.util.Comparator;

class NodeData{
    String name;
    LocalDateTime time;
    int data;
    NodeData(String name, LocalDateTime time, int data){
        this.name = name;
        this.time = time;
        this.data = data;
    }
}

class DataComparator implements Comparator<NodeData> {
    @Override
    public int compare(NodeData obj1, NodeData obj2) {
        return obj2.data - obj1.data;
    }
}

class TimeComparator implements Comparator<NodeData> {
    @Override
    public int compare(NodeData obj1, NodeData obj2) {
        if((obj1.time).isBefore(obj2.time))
            return 1;
        else
            return -1;
    }
}
package nextus.naeilro.model;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

/**
 * Created by chosw on 2017-01-05.
 */

public class TrainStation implements SortedListAdapter.ViewModel {

    private String nodeID;
    private String nodeName;

    public TrainStation(){}

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}

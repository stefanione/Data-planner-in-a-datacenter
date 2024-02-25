/* Implement this class. */

import java.util.ArrayList;
import java.util.List;

public class MyDispatcher extends Dispatcher {

    private int last_index = -1;
    private int index = 0;
    private int index_1 = 0;

    List<Host> minim_sizes = new ArrayList<>();
    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
    }

    @Override
    public void addTask(Task task) {
        if(algorithm.equals(SchedulingAlgorithm.ROUND_ROBIN)){
            last_index = (last_index + 1) % hosts.size();
            hosts.get(last_index).addTask(task);
        }

        if(algorithm.equals(SchedulingAlgorithm.SIZE_INTERVAL_TASK_ASSIGNMENT)){
            if(task.getType().equals(TaskType.SHORT)){
                hosts.get(0).addTask(task);
            } else if(task.getType().equals(TaskType.MEDIUM)){
                hosts.get(1).addTask(task);
            } else if(task.getType().equals(TaskType.LONG)){
                hosts.get(2).addTask(task);
            }
        }
        if(algorithm.equals(SchedulingAlgorithm.LEAST_WORK_LEFT)){
            long min = Long.MAX_VALUE;
            for(int i = 0; i < hosts.size(); i++){
                if(hosts.get(i).getWorkLeft() < min){
                    min = hosts.get(i).getWorkLeft();
                    index = i;
                }
            }
            hosts.get(index).addTask(task);
        }

        if(algorithm.equals(SchedulingAlgorithm.SHORTEST_QUEUE)){
            long min = Long.MAX_VALUE;
            for (Host value : hosts) {
                if (value.getQueueSize() < min) {
                    min = value.getQueueSize();
                }
            }

            for (Host host : hosts) {
                if (host.getQueueSize() == min) {
                    minim_sizes.add(host);
                }
            }

            long min_2 = Long.MAX_VALUE;
            if(minim_sizes.size() > 1){
                for(int j = 0; j < minim_sizes.size(); j++){
                    if(minim_sizes.get(j).getWorkLeft() < min_2){
                        min_2 = minim_sizes.get(j).getWorkLeft();
                        index_1 = j;
                    }
                }
                minim_sizes.get(index_1).addTask(task);
            } else if(minim_sizes.size() == 1) {
                minim_sizes.get(0).addTask(task);
            }
        }

    }
}

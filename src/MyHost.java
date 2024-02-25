/* Implement this class. */

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MyHost extends Host {
    volatile boolean stop = true;
    public int remember = 0;
    final BlockingQueue<Task> tasks = new LinkedBlockingQueue<>();
    BlockingQueue<Task> prioritizedTasks = new LinkedBlockingQueue<>();
    @Override
    public void run() {
        while(stop){
            synchronized (tasks){
                if(!tasks.isEmpty()) {
                    Task task = tasks.poll();
                    try {
                        for(int i = 0; i < (task.getDuration() / 1000) * 2; i++){
                            Thread.sleep(500);
                            remember -= 500;
                        }
                        task.finish();

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    @Override
    public void addTask(Task task) {
        Task head = tasks.peek();
        if(head != null){
            if(task.getPriority() > head.getPriority()){
                prioritizedTasks.add(task);
                while(!tasks.isEmpty()){
                    prioritizedTasks.add(tasks.poll());
                }
                while(!prioritizedTasks.isEmpty()){
                    tasks.add(prioritizedTasks.poll());
                }
            } else {
                tasks.add(task);
            }
        } else {
            tasks.add(task);
        }
        remember += task.getDuration();
    }

    @Override
    public int getQueueSize() {
        return tasks.size();
    }

    @Override
    public long getWorkLeft() {
        return remember;
    }

    @Override
    public void shutdown() {
        stop = false;
    }
}

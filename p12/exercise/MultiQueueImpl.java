package p12.exercise;

import java.security.Key;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class MultiQueueImpl<T, Q> implements MultiQueue<T, Q>{

    private Map<Q,Queue<T>> queues = new HashMap<>();

    @Override
    public Set<Q> availableQueues() {
        return queues.keySet();
    }

    @Override
    public void openNewQueue(Q queue) {
        if (queues.containsKey(queue)) {
            throw new IllegalArgumentException("Queue is already available.");
        }
        else{
            queues.put(queue,new LinkedList<>());
        }
    }

    @Override
    public boolean isQueueEmpty(Q queue) {
        if(availableQueues().contains(queue)){
            return queues.get(queue).isEmpty();
        }
        else{
            throw new IllegalArgumentException("Queue is not available.");
        }
    }

    @Override
    public void enqueue(T elem, Q queue) {
        if(availableQueues().contains(queue)){
            queues.get(queue).add(elem);
        }
        else{
            throw new IllegalArgumentException("Queue is not available.");
        }
    }

    @Override
    public T dequeue(Q queue) {
        if(availableQueues().contains(queue)){
            if (queues.get(queue).isEmpty()) {
                return null;
            } else {
                return queues.get(queue).remove();
            }
        }
        else{
            throw new IllegalArgumentException("Queue is not available.");
        }
    }

    @Override
    public Map<Q, T> dequeueOneFromAllQueues() {
        Map<Q, T> dequeueMap = new HashMap<>();

        for (Q queue : queues.keySet()) {
            dequeueMap.put(queue, dequeue(queue));
        }

        return dequeueMap;
    }

    @Override
    public Set<T> allEnqueuedElements() {
        Set<T> enqueuedElements = new HashSet<>();
        
        for (Q queue : queues.keySet()) {
            for (T element : queues.get(queue)) {
                enqueuedElements.add(element);
            }
        }

        return enqueuedElements;
    }

    @Override
    public List<T> dequeueAllFromQueue(Q queue) {
        if (!(availableQueues().contains(queue))) {
            throw new IllegalArgumentException("Queue is not available.");
        }

        LinkedList<T> dequeuedElements = new LinkedList<>();
        while (!(queues.get(queue).isEmpty())) {
            dequeuedElements.add(dequeue(queue));
        }
        
        return dequeuedElements;
    }

    @Override
    public void closeQueueAndReallocate(Q queue) {
        if (!(availableQueues().contains(queue))) {
            throw new IllegalArgumentException("Queue is not available.");
        }

        if (availableQueues().size() == 1) {
            throw new IllegalStateException("No queue available for reallocation.");
        }
        
        List<T> elements = dequeueAllFromQueue(queue);
        queues.remove(queue);

        for (Q q : queues.keySet()) {
            if(availableQueues().contains(q)){
                for (T t : elements) {
                    queues.get(q).add(t);
                }
            }
        }
    }

}

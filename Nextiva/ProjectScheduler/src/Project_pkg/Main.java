package Project_pkg;
import java.util.*;

class Task 
{
    String name;
    int duration;
    List<String> dependencies;
    int est, eft, lst, lft;

    Task(String name, int duration, List<String> dependencies) {
        this.name = name;
        this.duration = duration;
        this.dependencies = dependencies;
        this.est = 0;
        this.eft = 0;
        this.lst = Integer.MAX_VALUE;
        this.lft = Integer.MAX_VALUE;
    }
}

public class Main {

    static void calculateEstEft(Map<String, Task> tasks) {
        for (Task task : tasks.values()) {
            if (task.dependencies.isEmpty()) {
                task.est = 0;
            } else {
                task.est = task.dependencies.stream()
                        .mapToInt(dep -> tasks.get(dep).eft)
                        .max()
                        .orElse(0);
            }
            task.eft = task.est + task.duration;
        }
    }

    static void calculateLstLft(Map<String, Task> tasks) {
        int maxEft = tasks.values().stream().mapToInt(task -> task.eft).max().orElse(0);
        List<Task> reversedTasks = new ArrayList<>(tasks.values());
        Collections.reverse(reversedTasks);
        
        for (Task task : reversedTasks) {
            if (tasks.values().stream().noneMatch(t -> t.dependencies.contains(task.name))) {
                task.lft = maxEft;
            } else {
                task.lft = tasks.values().stream()
                        .filter(t -> t.dependencies.contains(task.name))
                        .mapToInt(t -> t.lst)
                        .min()
                        .orElse(maxEft);
            }
            task.lst = task.lft - task.duration;
        }
    }
    public static void main(String[] args) {
        Map<String, Task> tasks = new LinkedHashMap<>();
        
        tasks.put("Event_Planning_Start", new Task("Event_Planning_Start", 0, Collections.emptyList()));
        tasks.put("Book_Venue", new Task("Book_Venue", 4, Collections.singletonList("Event_Planning_Start")));
        tasks.put("Send_Invitations", new Task("Send_Invitations", 2, Collections.singletonList("Book_Venue")));
        
        tasks.put("Order Cake", new Task("Order Cake", 3, Collections.singletonList("Book_Venue")));
        tasks.put("Arrange_Catering", new Task("Arrange_Catering", 3, Collections.singletonList("Book_Venue")));
        tasks.put("Prepare Food", new Task("Prepare Food", 2, Collections.singletonList("Send_Invitations")));
        
        tasks.put("Final Touches", new Task("Final Touches", 1, Arrays.asList("Prepare Food","Arrange_Catering", "Send_Invitations")));

        
        calculateEstEft(tasks);
        calculateLstLft(tasks);

        System.out.printf("%-30s%-10s%-10s%-10s%-10s%-10s\n", "Task", "Duration", "EST", "EFT", "LST", "LFT");
        for (Task task : tasks.values()) {
            System.out.printf("%-30s%-10d%-10d%-10d%-10d%-10d\n",
                    task.name, task.duration, task.est, task.eft, task.lst, task.lft);
        }

        int earliestCompletion = tasks.values().stream().mapToInt(task -> task.eft).max().orElse(0);
        int latestCompletion = tasks.values().stream().mapToInt(task -> task.lft).max().orElse(0);

        System.out.println("Earliest time all tasks will be completed: " + earliestCompletion);
        System.out.println("Latest time all tasks will be completed: " + latestCompletion);
    }
}

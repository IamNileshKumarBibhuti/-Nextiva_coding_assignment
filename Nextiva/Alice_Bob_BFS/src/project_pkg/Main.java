package project_pkg;

import java.util.*;

public class Main {
    private static Map<String, List<String>> friendsMap = new HashMap<>();
/*
 * Alice -- Bob -- David -- Janice
    |       
    |
  Charlie -- Eve

 */
    public static void main(String[] args) {
        // Initializing the friends network
        addFriendship("Alice", "Bob");
        addFriendship("Alice", "Charlie");
        addFriendship("Bob", "David");
        addFriendship("Charlie", "Eve");
        addFriendship("David", "Janice");

        Scanner scanner = new Scanner(System.in);

        System.out.println("Friends of Alice: " + getFriends("Alice"));
        System.out.println("Friends of Bob: " + getFriends("Bob"));
        System.out.println("Common friends of Alice and Bob: " + getCommonFriends("Alice", "Bob"));

        System.out.println("Enter the names of two friends to find their nth connection (comma-separated):");
        String input = scanner.nextLine();
        String[] friends = input.split(",");
        if (friends.length == 2) {
            String friend1 = friends[0].trim();
            String friend2 = friends[1].trim();
            int connectionLevel = findNthConnection(friend1, friend2);
            System.out.println("Connection level between " + friend1 + " and " + friend2 + " is: " + connectionLevel);
        } else {
            System.out.println("Invalid input. Please enter exactly two names.");
        }

        scanner.close();
    }

    private static void addFriendship(String friend1, String friend2) {
        friendsMap.computeIfAbsent(friend1, k -> new ArrayList<>()).add(friend2);
        friendsMap.computeIfAbsent(friend2, k -> new ArrayList<>()).add(friend1);
    }

    private static List<String> getFriends(String friend) {
        return friendsMap.getOrDefault(friend, Collections.emptyList());
    }

    private static List<String> getCommonFriends(String friend1, String friend2) {
        List<String> friends1 = getFriends(friend1);
        List<String> friends2 = getFriends(friend2);
        List<String> commonFriends = new ArrayList<>(friends1);
        commonFriends.retainAll(friends2);
        return commonFriends;
    }

    private static int findNthConnection(String friend1, String friend2) {
        if (friend1.equals(friend2)) return 0;

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, Integer> distance = new HashMap<>();

        queue.add(friend1);
        visited.add(friend1);
        distance.put(friend1, 0);

        while (!queue.isEmpty()) {
            String currentFriend = queue.poll();
            int currentDistance = distance.get(currentFriend);

            for (String neighbor : getFriends(currentFriend)) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    distance.put(neighbor, currentDistance + 1);

                    if (neighbor.equals(friend2)) {
                        return currentDistance + 1;
                    }
                }
            }
        }

        return -1; // No connection found
    }
}

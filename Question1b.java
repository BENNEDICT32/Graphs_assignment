//Bennedict Mkhonto
//4226279
//CSC 212 T4,P1
//Question 1b


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Question1b {
    
    
    //Creates a private variable(adjacency_list) to store the graph's adjacencylist
    private List<Node> adjacency_list;

    public Question1b(String filename) throws FileNotFoundException { //Reads the file and throws an exception if the file is not found
        adjacency_list = new ArrayList<>();  //Initializes the adjacency_list as an empty arraylist
        readAdjacency_list(filename); //Calls the readAdjacency_List method to read the adjacency list in the text fil
    }


    private void readAdjacency_list(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            String node = parts[0];
            List<String> neighbors = new ArrayList<>();
            for (int i = 1; i < parts.length; i++) {
                neighbors.add(parts[i]);
            }
            adjacency_list.add(new Node(node, neighbors));
        }
        scanner.close();
    }

    public List<List<String>> findPaths(String start, String end, String via) {
        List<List<String>> allPaths = new ArrayList<>();

        // Find paths from start to via
        List<List<String>> startViaPaths = dfs(start, via, new boolean[adjacency_list.size()], new ArrayList<>());

        // Find paths from via to end
        List<List<String>> viaEndPaths = dfs(via, end, new boolean[adjacency_list.size()], new ArrayList<>());

        // Combine paths
        for (List<String> startViaPath : startViaPaths) {
            for (List<String> viaEndPath : viaEndPaths) {
                List<String> fullPath = new ArrayList<>(startViaPath);
                fullPath.addAll(viaEndPath.subList(1, viaEndPath.size())); // Avoid duplicating the 'via' node
                allPaths.add(fullPath);
            }
        }
        return allPaths;
    }

    private List<List<String>> dfs(String currentNode, String end, boolean[] visited, List<String> path) {
        List<List<String>> allPaths = new ArrayList<>();
        Node current = findNode(currentNode);
        
        if (current == null) {
            return allPaths; // Node not found
        }

        visited[adjacency_list.indexOf(current)] = true;
        path.add(currentNode);

        if (currentNode.equals(end)) {
            allPaths.add(new ArrayList<>(path));
        } else {
            for (String neighbor : current.getNeighbors()) {
                Node neighborNode = findNode(neighbor);
                if (neighborNode != null && !visited[adjacency_list.indexOf(neighborNode)]) {
                    allPaths.addAll(dfs(neighbor, end, visited, path));
                }
            }
        }

        path.remove(path.size() - 1);
        visited[adjacency_list.indexOf(current)] = false;
        return allPaths;
    }

    private Node findNode(String node) {
        for (Node n : adjacency_list) {
            if (n.getNode().equals(node)) {
                return n;
            }
        }
        return null;
    }

    private static class Node {
        private String node;
        private List<String> neighbors;

        public Node(String node, List<String> neighbors) {
            this.node = node;
            this.neighbors = neighbors;
        }

        public String getNode() {
            return node;
        }

        public List<String> getNeighbors() {
            return neighbors;
        }
    }

    public static void main(String[] args) {
        try (FileWriter writer = new FileWriter("output2.txt")) {
            Question1b pathFinder = new Question1b("graph2_input.txt");
            List<List<String>> allPaths = pathFinder.findPaths("L", "K", "M");

            if (!allPaths.isEmpty()) {
                writer.write("True there is a path from start to finish through M \n");
            } else {
                writer.write("False there is no path from start to finish through M \n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

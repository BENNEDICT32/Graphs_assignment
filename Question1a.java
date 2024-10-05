//Bennedict Mkhonto
//4226279
//CSC 212 T4,P1
//Question 1a

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Question1a {

    //Creates a private variable(adjacency_list) to store the graph's adjacencylist
    private List<Node>adjacency_list;


    public Question1a(String filename) throws FileNotFoundException { //Reads the file and throws an exception if the file is not found
        adjacency_list = new ArrayList<>();  //Initializes the adjacency_list as an empty arraylist
        readAdjacency_list(filename); //Calls the readAdjacency_List method to read the adjacency list in the text fil
    }

    private void readAdjacency_list(String filename) throws FileNotFoundException {
        File file =new File(filename);
        Scanner Reader = new Scanner(file);//We use a scanner to read the text file

        while (Reader.hasNextLine()) {
            String line = Reader.nextLine(); //Stores the line of the text file
            String[] parts = line.split(","); //Separates every element by a comma and store it in an array of parts
            String node = parts[0]; //The first element of the text file is stored on the variable named node
            String[] nextnodes = new String[parts.length - 1];  //Creates an array called named nextnodes with a size of the array of parts-1
        
            
            System.arraycopy(parts, 1, nextnodes, 0, parts.length - 1); //Copies the neighbouring nodes from parts array to the nextnodes array
            adjacency_list.add(new Node(node, nextnodes));// Add a new node object to the adjaceny_list with node name and neighbouring nodes
        }

        Reader.close(); //Closes the reader
    }

    //Creates a public method that takes start and end node names parameters and return a list of lists of strings
    public List<List<String>> Availablepaths(String start, String end) { 
        List<List<String>> allPaths = new ArrayList<>(); //creates an empty list to store a list 
        boolean[] visited = new boolean[adjacency_list.size()];//Creates an array that will keep track of the visited nodes

        dfs(start, end, visited, new ArrayList<>(), allPaths); //calls the depth first method
        return allPaths;
    }

    //Creates a private depth first search method that takes in the currentNode,end node,an array of visited Nodes and a list of path,and a list of list allpaths
    private void dfs(String currentNode, String end, boolean[] visited, List<String> path, List<List<String>> allPaths) {
        Node current = findNode(currentNode); //Call the method to find the currentNode 
        visited[adjacency_list.indexOf(current)] = true; //Marks the current node as visited and place it in the array that keeps track of the visited nodes
        path.add(currentNode); //the currend node is now placed in the list of path

        if (currentNode.equals(end)) { //Check if the currentNode is not the end node
            allPaths.add(new ArrayList<>(path)); // If it is the end node adds the current path to the list of all paths
        } else { //If the currentNode is not the endNode then we will check the Neighbours of the currentNode 
            for (String neighbor : current.getNeighbors()) {
                Node neighborNode = findNode(neighbor); //Store the neighbour of the currentNode as neighbourNode
                if (!visited[adjacency_list.indexOf(neighborNode)]) { // We check the neighbour if it's visited in the arra of visitedNodes
                    dfs(neighbor, end, visited, path, allPaths); //Recursively do the depth first method
                }
            }
        }

        path.remove(path.size() - 1); //removes the path on the arraylist and decreases the arraylist size by 1 
        visited[adjacency_list.indexOf(current)] = false; //marks the currentNode as not visites
    }
    
    //This is a method of finding the Node 
    private Node findNode(String node) { 
        for (Node n : adjacency_list) { //searches the Node in the ajacency_list
            if (n.getNode().equals(node)) { //Return the node if it is found otherwise null
                return n;
            }
        }

        return null;
    }

    private static class Node {
        private String node;
        private String[] neighbors;

        public Node(String node, String[] neighbors) {
            this.node = node;
            this.neighbors = neighbors;
        }

        public String getNode() {
            return node;
        }

        public String[] getNeighbors() {
            return neighbors;
        }
    }

    public static void main(String[] args) {
        try {
            FileWriter writer=new FileWriter("output1.txt"); //Creates a file named output1.txt
            Question1a all_Paths_finder= new Question1a("graph1_input.txt"); 
            List<List<String>> allPaths = all_Paths_finder.Availablepaths("L", "K"); //Calls the Availablepaths and stores all the paths on the lists Allpaths

            if (!allPaths.isEmpty()) { //Searches the all  the paths in the allPaths list and displays it if it found
                writer.write("These are all the possible paths: "+"\n");
                for (int i = 0; i < allPaths.size(); i++) {
                    String results="Path " + (i + 1) + ": " + allPaths.get(i);
                    writer.write(results+ "\n"); //Writes the output to the text file we created
                }
            } else { //Otherwise displays no path found
                writer.write("No paths found");
                
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

       
import java.util.*;
import java.io.*;
public class routing {
	// create a tree BinaryTreeHeap
    private static BinaryTreeHeap heap ;
    // create a Edegs
    private static int[][] Edegs;
    // for save the path of shortest path to hashtable
    private static Hashtable<Integer, Integer> listVertexofPath;
    // create class of tree
    class BinaryTreeHeap {
        private int[] weight;
        private int[] index;
        public int[] FinalCost;
        private int size;
        // construct of fibonacciHeap with n
        public BinaryTreeHeap(int n) {
            weight = new int[n];
            index = new int[n];
            FinalCost = new int[n];

            for (int i = 0; i < n; i++) {
                index[i] = -1;
                FinalCost[i] = -1;
            }

            size = 0;
        }
        // check if BinaryTreeHeap is empty will be true
        public boolean isEmpty() {
            return (size == 0);
        }
        // excute for shiftUp 
        private void shiftUp(int i) {
            int j;
            while (i > 0) {
                j = (i - 1) / 2;
                //if the FinalCost of weight i is less than the FinalCost of weight j
                if (FinalCost[weight[i]] < FinalCost[weight[j]]) {
                    // swap here
                    int temp = index[weight[i]];
                    index[weight[i]] = index[weight[j]];
                    index[weight[j]] = temp;
                    // swap here
                    temp = weight[i];
                    weight[i] = weight[j];
                    weight[j] = temp;
                    i = j;
                } else
                    break;
            }
        }
        // method of shiftDown
        private void shiftDown(int i) {
            int j, k;
            while (2 * i + 1 < size) {
                j = 2 * i + 1;
                k = j + 1;
                if (k < size && FinalCost[weight[k]] < FinalCost[weight[j]]
                        && FinalCost[weight[k]] < FinalCost[weight[i]]) {
                    // swap here
                    int temp = index[weight[k]];
                    index[weight[k]] = index[weight[i]];
                    index[weight[i]] = temp;
                    // swap here
                    temp = weight[k];
                    weight[k] = weight[i];
                    weight[i] = temp;

                    i = k;
                } else if (FinalCost[weight[j]] < FinalCost[weight[i]]) {
                    // swap here
                    int temp = index[weight[j]];
                    index[weight[j]] = index[weight[i]];
                    index[weight[i]] = temp;
                    // swap here
                    temp = weight[j];
                    weight[j] = weight[i];
                    weight[i] = temp;

                    i = j;
                } else
                    break;
            }
        }
        // get the element at top of Heap
        public int pop() {
            int res = weight[0];
            weight[0] = weight[size - 1];
            index[weight[0]] = 0;
            size--;
            shiftDown(0);
            return res;
        }
        // insert a element into Heap
        public void push(int x, int c) {
            if (index[x] == -1) {
                FinalCost[x] = c;
                weight[size] = x;
                index[x] = size;
                size++;
                shiftUp(index[x]);
            } else {
                if (c < FinalCost[x]) {
                    FinalCost[x] = c;
                    shiftUp(index[x]);
                    shiftDown(index[x]);
                }
            }
        }
    }
   
    // use Dijkstar to find the shortest path
    public routing(String inputFile) 
    {
    	//read file
    	File file = new File(inputFile);

        try {
        	// use readfileanner to read each of line
            Scanner readfile = new Scanner(file);
            //read a new line
            String getLine=readfile.nextLine();
            // split it to array
            String[] weights=getLine.split(" ");
            //get the number or vertexs
            int vts=Integer.parseInt(weights[0]);
            // get the number of edges
            int edges=Integer.parseInt(weights[1]);
            
            // init a Edegs
            Edegs = new int[vts][vts];
            //init a heap
            heap=new BinaryTreeHeap(vts);
            
            for(int i=0;i<vts;i++)
            	for(int j=0;j<vts;j++)Edegs[i][j]=0;
            
            //loop until 
            while(readfile.hasNextLine())
            {
            	try
            	{
            		// get the line and save to line
	                String line=readfile.nextLine();
	                // ssplit the line
	                weights=line.split(" ");
	                //get the edge
	                int edge1=Integer.parseInt(weights[0]);
	                int edge2=Integer.parseInt(weights[1]);
	                int weight=Integer.parseInt(weights[2]);
	                //save weight to Edegs
	                Edegs[edge1][edge2]=Edegs[edge2][edge1]=weight;
	                
            	}
            	catch(Exception ae){}
            }
            readfile.close();
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public int countVertex()
    {
    	return Edegs.length;
    }
    public static String toBinary(int number)
    {
    	String binary="";
    	while(number>0)
    	{
    		binary=number%2+binary;
    		number=number/2;
    	}
    	int n=8-binary.length();
    	for(int i=0;i<n;i++)
    	{ 
    		binary="0"+binary;
    	}
    	return binary;
    }
    public static Hashtable<Integer,String> readFile(String filename)
    {
    	Hashtable<Integer,String> result=new Hashtable<Integer,String>();
    	int ids=0;
    	File file = new File(filename);
        try {

            Scanner sc = new Scanner(file);
            while(sc.hasNextLine())
            {
	            String Line=sc.nextLine();
	            if(Line.trim().length()>2)
	            {
		            result.put(ids, Line);
		            ids++;
	            }
            }
            sc.close();
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    	return result;
    }
    public int findPath(int numOfNodes, int source, int dest) 
    {
    	// insert a vertex into the list of shortest path
    	listVertexofPath=new Hashtable<Integer, Integer>();
    	// push a vertex into heap
        heap.push(source, 0);
        while (!heap.isEmpty()) 
        {
        	// get the minimum of weight in the top of heap
            int u = heap.pop();
            if(!listVertexofPath.containsKey(u))listVertexofPath.put(u, -1);
            if (u == dest)
                return heap.FinalCost[dest];
            for (int i = 0; i < numOfNodes; i++) 
            {
                if (Edegs[u][i] > 0)
                {
                	if(heap.FinalCost[i]>0)
                	{
                		// use dijkstra to update the next vertex
                		if(heap.FinalCost[i]>heap.FinalCost[u] + Edegs[u][i])
                		{
                			heap.push(i, heap.FinalCost[u] + Edegs[u][i]);
                			listVertexofPath.put(i, u);
                		}
                	}
                	else
                		{
                			heap.push(i, heap.FinalCost[u] + Edegs[u][i]);
                			listVertexofPath.put(i, u);
                		}
                }
            }
        }
        return -1;
    }
    
    
    
   public static void main(String[] args) 
    {
    	
    	if(args.length<4)
    	{
    		System.out.println("Vaid for input!");
    		return;
    	}
    	String inputgraph=args[0];// the argsment for input graph
    	String inputip=args[1];// file of input ip
    	int source_node=Integer.parseInt(args[2]);// source node
        int destination_node=Integer.parseInt(args[3]); // destination node
        // create a Dijkstar for get the shortest path
        routing dij = new routing(inputgraph);
        
        // Source is node A (node 0) and destination is node F (node 5)
        
        // use hashtable to save the ip in file input ip
        Hashtable<Integer, String> lstIPs=readFile(inputip);
        
        // find the shortest 
        int FinalCost =dij.findPath(dij.countVertex(), source_node, destination_node);
        // create a bianry tree for get prefix of ip
        BinaryTree tree=new BinaryTree();
        // create a root
        tree.insertBST("*");
        try {
        	
        	 
 
			File file = new File("d:\\output.txt");
 
			if (!file.exists()) {
				file.createNewFile();
			}
			// create a file for write output
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(FinalCost+"");
			System.out.println(FinalCost);
			
			bw.newLine();
			// get the result of the shortest path and save to hashtable
			Hashtable<Integer, Integer> result=routing.listVertexofPath;
			
			int vertex=destination_node;
			
			String pathofGraph=vertex+""; 
			while(vertex!=source_node)
			{
				vertex=result.get(vertex);
				pathofGraph=vertex+" "+pathofGraph;
				
			}
			String[] listVertex=pathofGraph.split(" ");
			String binarysource_node=lstIPs.get(source_node);
			String binarydestination_node=lstIPs.get(destination_node);
			//for each of vertex
			for(int i=1;i<listVertex.length;i++)
			{
				int nvertex=Integer.parseInt(listVertex[i]);
				String IPvertex=lstIPs.get(nvertex);
				String[] ips=IPvertex.split("\\.");
				
				//System.out.println(nvertex);
				// get the binary string of this ip
				int nip=Integer.parseInt(ips[0]);
				String binaryvertex=toBinary(nip);
				binaryvertex=binaryvertex+toBinary(Integer.parseInt(ips[1]));
				binaryvertex=binaryvertex+toBinary(Integer.parseInt(ips[2]));
				binaryvertex=binaryvertex+toBinary(Integer.parseInt(ips[3]));
				//System.out.println(binaryvertex);
				
				// insert this ip into binary tree
				tree.insertBST(binaryvertex);
			}
			// the next for each of vertex in the shortest path
			for(int i=1;i<listVertex.length;i++)
			{
				int vertexip=Integer.parseInt(listVertex[i]);
				
				if(vertexip!=-1)
				{
					String IPvertex=lstIPs.get(vertexip);
					String[] ips=IPvertex.split("\\.");
					//System.out.println(ips.length);
					//System.out.println(ips[0]);
					int nip=Integer.parseInt(ips[0]);
					String binaryvertex=toBinary(nip);
					//find the prefix of this ip
					binaryvertex=binaryvertex+toBinary(Integer.parseInt(ips[1]));
					binaryvertex=binaryvertex+toBinary(Integer.parseInt(ips[2]));
					binaryvertex=binaryvertex+toBinary(Integer.parseInt(ips[3]));
					bw.write(tree.getString(binaryvertex)+" ");
					System.out.println(tree.getString(binaryvertex)+" ");// and write the prefix
				}
			}
			bw.close();
 
			//System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    
    
   
}
class Node {

    public Node leftNode,  rightNode; // the nodes
    public String data; //the AnyClass objext

    public Node(String data ) {//constructor
        this.data= data;
        this.leftNode = null;
        this.rightNode = null;
    }

    public void show() {
        //calls the show method of the AnyClass
        System.out.print(data.toString());
    }
}

class BinaryTree {
    Node theBTRootNode;

    public BinaryTree() // constructor
    {
        theBTRootNode = null;
    }

    // ------------------ Addition of the node to the BST-------------------
    protected Node insertAB(Node currentNode, Node newNode,int index) 
    {
    	if(currentNode==null)currentNode=newNode;
    	else
    	// if current node is the leaf
    	if(currentNode.leftNode==null&&currentNode.rightNode==null)
    	{
    		Node emptyBTNode = new Node(currentNode.data);
    		
    		// check the current bit
    		if(currentNode.data.charAt(index)=='1')
    		{
    			// change the data of current node is *
    			currentNode.rightNode=emptyBTNode;
    			currentNode.data="*";
    			// check the current bit of newNode
    			if(newNode.data.charAt(index)=='0')currentNode.leftNode=newNode;
    			else
    				// call insert this node
    				insertAB(emptyBTNode,newNode,index+1);
    		}
    		else if(currentNode.data.charAt(index)=='0')
    		{
    			currentNode.leftNode=emptyBTNode;
    			currentNode.data="*";
    			if(newNode.data.charAt(index)=='1')currentNode.rightNode=newNode;
    			else
    				insertAB(emptyBTNode,newNode,index+1);
    		}
    		else currentNode=newNode;
    	}
        else
        {
        	
        	
        	if (newNode.data.charAt(index)=='0') {
		            currentNode.leftNode = insertAB(currentNode.leftNode, newNode,index+1);
		        } else {
		            // else if bigger appends to the right
		            currentNode.rightNode = 
		               insertAB(currentNode.rightNode, newNode,index+1);
		        }
        }
        return currentNode;
    }

    public String getString(String data) {
        Node anyClassBTNode = new Node(data);
        //calls insert above
        return getString(theBTRootNode, anyClassBTNode,0);
    }
    // get the string is the prefix of ip
	protected String getString(Node currentNode, Node newNode,int index) {
	    	
		if(currentNode.leftNode==null&&currentNode.rightNode==null)return "";
    	else
        {
        	
        	
        	if (newNode.data.charAt(index)=='0') {
		            return  "0"+getString(currentNode.leftNode, newNode,index+1);
		        } else {
		            // else if bigger appends to the right
		        	return  "1"+getString(currentNode.rightNode, newNode,index+1);
		        }
        }
         
	 }

    public void insertBST(String data) {
        Node anyClassBTNode = new Node(data);
        //calls insert above
        theBTRootNode = insertAB(theBTRootNode, anyClassBTNode,0);
    }

    // ------------------ InOrder traversal-------------------
    protected void inorder(Node currentNode) {
        if (currentNode != null) {
            inorder(currentNode.leftNode);
            currentNode.show();
            inorder(currentNode.rightNode);
        }
    }

    //calls the method to do in order
    public void inorderBST() {
        inorder(theBTRootNode);
    }

    
    protected String postOrderBST (Node root)
    {
     
      if(root == null) return "";
      System.out.println("null");
      return root.data+" "+postOrderBST( root.leftNode )+" "+ postOrderBST( root.rightNode );  

    }
    public String postOrderBST() {
    	return postOrderBST(theBTRootNode);
    }
    // ----- Search for key name and  returns ref. 
    //              to Node or null if not found--------
    protected Node search(Node currentNode, String keyName,int index) {
        //if the root is null returns null
        if (currentNode == null) {
            return null;
        } else {
            //checks if they are equal
            if (keyName==currentNode.data)  {
                return currentNode;
            //checks id the key is smaller than the current
            //record  if smaller traverses to the left
            } else if (keyName.charAt(index)=='0') {
                return search(currentNode.leftNode, keyName,index+1);
            } else {
                // if bigger traverses to the left
                return search(currentNode.rightNode, keyName,index+1);
            }
        }
    }

    //returns null if no result else returns 
    //the AnyClass object matched with the keyName
    public String searchBST(String keyName) {
        Node temp = search(theBTRootNode, keyName,0);
        if (temp == null) {
      //noresults found
           return null;
        } else {
         //result found
           return temp.data;
        }
    }
    
}
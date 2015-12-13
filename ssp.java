import java.util.*;
import java.util.Hashtable;
import java.io.*;
public class ssp {
	// create a tree Tree_Fibonacy_Heap
    private static Tree_Fibonacy_Heap heap ;
    // create a IpGraph
    private static int[][] IpGraph;
    // for save the path of shortest path to hashtable
    private static Hashtable<Integer, Integer> setofVertex;
    // create class of tree
    class Tree_Fibonacy_Heap {
        private int[] weight;
        private int[] index;
        public int[] FinalCost;
        private int size;
        // construct of fibonacciHeap with n
        public Tree_Fibonacy_Heap(int n) {
            weight = new int[n];
            index = new int[n];
            FinalCost = new int[n];

            for (int i = 0; i < n; i++) {
                index[i] = -1;
                FinalCost[i] = -1;
            }

            size = 0;
        }
        // check if Tree_Fibonacy_Heap is empty will be true
        public boolean isEmpty() {
            return (size == 0);
        }
        // excute for shiftON 
        private void shiftON(int i) {
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
        // method of shiftOFF
        private void shiftOFF(int i) {
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
            shiftOFF(0);
            return res;
        }
        // insert a element into Heap
        public void push(int x, int c) {
            if (index[x] == -1) {
                FinalCost[x] = c;
                weight[size] = x;
                index[x] = size;
                size++;
                shiftON(index[x]);
            } else {
                if (c < FinalCost[x]) {
                    FinalCost[x] = c;
                    shiftON(index[x]);
                    shiftOFF(index[x]);
                }
            }
        }
    }
   
    // use Dijkstar to find the shortest path
    public ssp(String inputFile) 
    {
    	//read file
    	File file = new File(inputFile);

        try {
        	// use readDataanner to read each of line
            Scanner readData = new Scanner(file);
            //read a new line
            String getLine=readData.nextLine();
            // split it to array
            String[] weights=getLine.split(" ");
            //get the number or vertexs
            int vts=Integer.parseInt(weights[0]);
            // get the number of edges
            int edges=Integer.parseInt(weights[1]);
            
            // init a IpGraph
            IpGraph = new int[vts][vts];
            //init a heap
            heap=new Tree_Fibonacy_Heap(vts);
            
            for(int i=0;i<vts;i++)
            	for(int j=0;j<vts;j++)IpGraph[i][j]=0;
            
            //loop until 
            while(readData.hasNextLine())
            {
            	try
            	{
            		// get the line and save to line
	                String line=readData.nextLine();
	                // ssplit the line
	                weights=line.split(" ");
	                //get the edge
	                int edge1=Integer.parseInt(weights[0]);
	                int edge2=Integer.parseInt(weights[1]);
	                int weight=Integer.parseInt(weights[2]);
	                //save weight to IpGraph
	                IpGraph[edge1][edge2]=IpGraph[edge2][edge1]=weight;
	                
            	}
            	catch(Exception ae){}
            }
            readData.close();
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public int countVertex()
    {
    	return IpGraph.length;
    }
    public int getListPath(int numOfNodes, int source, int dest) 
    {
    	// insert a vertex into the list of shortest path
    	setofVertex=new Hashtable<Integer, Integer>();
    	// push a vertex into heap
        heap.push(source, 0);
        while (!heap.isEmpty()) 
        {
        	// get the minimum of weight in the top of heap
            int u = heap.pop();
            if(!setofVertex.containsKey(u))setofVertex.put(u, -1);
            if (u == dest)
                return heap.FinalCost[dest];
            for (int i = 0; i < numOfNodes; i++) 
            {
                if (IpGraph[u][i] > 0)
                {
                	if(heap.FinalCost[i]>0)
                	{
                		// use dijkstra to update the next vertex
                		if(heap.FinalCost[i]>heap.FinalCost[u] + IpGraph[u][i])
                		{
                			heap.push(i, heap.FinalCost[u] + IpGraph[u][i]);
                			setofVertex.put(i, u);
                		}
                	}
                	else
                		{
                			heap.push(i, heap.FinalCost[u] + IpGraph[u][i]);
                			setofVertex.put(i, u);
                		}
                }
            }
        }
        return -1;
    }
    /*
     * How to run it?
     * choose run-> run confign at agrumment you need put like:
     * input_IpGraphsmall_part1.txt 0 999
     * see the setofVertex in output file in current folder of project
     * 
     */
    public static void main(String[] args) {
    	
        ssp dij = new ssp(args[0]);
        int source_node=Integer.parseInt(args[1]);
        int destination_node=Integer.parseInt(args[2]); 
        // Source is node A (node 0) and destination is node F (node 5)
        int FinalCost =dij.getListPath(dij.countVertex(), source_node, destination_node);
        try {
        	 
 
	//		File file = new File("d:\\output.txt");
 
			// if file doesnt exists, then create it
	//		if (!file.exists()) {
	//			file.createNewFile();
	//		}
			// open file for write the setofVertex
	//		FileWriter fw = new FileWriter(file.getAbsoluteFile());
	//		BufferedWriter bw = new BufferedWriter(fw);
			// writhe the minimum weight of shortest path
	//		bw.write(FinalCost+"");
	//		bw.newLine();
			
                     System.out.println(FinalCost+"");
			// all vertex in the shortest path is save in setofVertex
			Hashtable<Integer, Integer> setofVertex=ssp.setofVertex;
			// start from destination
			int vertex=destination_node;
			//loop and get the before vertex that start from it to this vertex
			String VertexinPath=vertex+""; 
			while(vertex!=source_node)
			{
				vertex=setofVertex.get(vertex);
				// add to setofVertex
				VertexinPath=vertex+" "+VertexinPath;
				
			}
			// write the setofVertex to output
		//bw.write(VertexinPath);
		System.out.println(VertexinPath);
			//bw.close();
 
			
 
		} 	finally{}
  	//		catch (IOException e) {
	//		e.printStackTrace();
	//	}
    }

    
   
}


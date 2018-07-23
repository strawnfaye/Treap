// S. Faye Strawn
// Sa914922

// Original BST code by Sean Szumlanski


import java.util.HashSet;

// a generic Node object to store value, priority,
// and references to left and right children
class Node<AnyType extends Comparable<AnyType>>
{
	AnyType data;
        int priority;
	Node<AnyType> left, right;

	Node(AnyType data, int priority)
	{
		this.data = data;
                this.priority = priority;
	}
}


public class Treap<AnyType extends Comparable<AnyType>>
{
    private Node<AnyType> root;
    HashSet<Integer> myHash = new HashSet<Integer>();
    
    // takes a value to be added to the treap as input, generates a random
    // priority for this value, and inserts the value and priority into the treap
    // as a new node
    public void add(AnyType data)
    {             
        if(!contains(data))
        {
            int priority = (int)(Math.random() * Integer.MAX_VALUE) + 1;
            System.out.println("priority = " + priority);
        
            // store priority in hash set to assure no duplicates
            while(myHash.add(priority) == false)
            {
                priority = (int)(Math.random() * Integer.MAX_VALUE) + 1;
                System.out.println("loop priority = " + priority);
            }
        
            // insert value BST-style and percolate according to priority
            root = insert(root, data, priority);
        }
    }
    

    // takes a value to be added the treap and a priority to accompany it as 
    // input and inserts value and priority into treap as a new node
    public void add(AnyType data, int priority)
    {   
        if(!contains(data))
        {
            root = insert(root, data, priority);
            myHash.add(priority);
        }        
    }
    
    // takes the treap's root, the value to be inserted, and the priority of that 
    // value as input. Inserts new node into treap by traversing BST-style, then 
    // percolating back up according to the priority.
    private Node<AnyType> insert(Node<AnyType> root, AnyType data, int priority)
    {
        // when an empty spot is found, insert data and priority as a new node
        if (root == null)
      	{
            return new Node<AnyType>(data, priority);
	}
        
        // if input data is less than the current node's data, recursively 
        // traverse left subtree
        else if (data.compareTo(root.data) < 0)
	{
            root.left = insert(root.left, data, priority);
            
            // if input priority is less than current node's priority, perform
            // rotations to restore heapiness
            if(priority < root.priority)
            {
                // rotate right - left child moves up to take parent's place
                root = rotateRight(root);                       
            }	
        }
        
        // if input data is greater than the current node's data, recursively 
        // traverse right subtree
        else if (data.compareTo(root.data) > 0)
        {
            root.right = insert(root.right, data, priority);
            
            // if input priority is less than current node's priority, perform
            // rotations to restore heapiness
            if(priority < root.priority)
            {
                // rotate left - right child moves up to take parent's place
                root = rotateLeft(root);
            }
        }
        else
        {
            // explicitly state disallowance of duplicate values
            ;
        }

        return root;
    }
    
    // rotate right - left child moves up to take parent's place
    private Node<AnyType> rotateRight(Node<AnyType> root)
    {            
        Node<AnyType> temp = root;
        root = root.left;
        temp.left = root.right;
        root.right = temp;
            	
        return root;         
    }
    
    // rotate left - right child moves up to take parent's place
    private Node<AnyType> rotateLeft(Node<AnyType> root)
    {
        Node<AnyType> temp = root;
        root = root.right;
        temp.right = root.left;
        root.left = temp;
        
        return root;
    }
    
    // takes data to be removed as input and deletes node containing this data
    public void remove(AnyType data)
    {
        root = remove(root, data);
    }
    
    // takes the treap's root and the data to be removed as input, traverses
    // treap to find node containing this data, percolates this node down until 
    // it's a leaf, then deletes it from the treap
    private Node<AnyType> remove(Node<AnyType> root, AnyType data)
    {   
        // if treap is empty, there's nothing to delete
        if(root == null)
        {
            return null;
        }
        // if input data is less than current node's data, recursively traverse
        // left subtree
        else if(data.compareTo(root.data) < 0)
        {
            root.left = remove(root.left, data);
        }
        // if input data is greater than current node's data, recursively 
        // traverse right subtree
        else if(data.compareTo(root.data) > 0)
        {
            root.right = remove(root.right, data);
        }
        // if input data is equal to the data contained in this node
        else
        {
           // if this node is a leaf
            if(root.left == null && root.right == null)
            {
                myHash.remove(root.priority);
                return null;
            }
            // if this node has only a left child
            else if(root.left != null && (root.right == null || root.left.priority < root.right.priority) )
            {           
                root = rotateRight(root);
                root.right = remove(root.right, data);
            }
            // if this node has only a right child
            else if(root.right != null && (root.left == null || root.right.priority < root.left.priority))
            {
                root = rotateLeft(root);
                root.left = remove(root.left, data);
            }
       
        }
        
        return root;   
    }
   
    // takes data of questionable existence as input, returns true if found
    // and false otherwise
    public boolean contains(AnyType data)
    {           
        return contains(root, data);       
    }
    
    // takes root of treap and data of questionable existence as input,
    // traverses treap to find node containing this data, then returns true
    // if found and false otherwise
    private boolean contains(Node<AnyType> root, AnyType data)
    {      
        if(root == null)
            return false;
        
        else if(data.compareTo(root.data) < 0)
            return contains(root.left, data);
        
        else if(data.compareTo(root.data) > 0)
            return contains(root.right, data);
        
        else
            return true;       
    }
    

    // returns num elements in treap by accessing number of priorities
    // stored in hash set
    public int size()
    {
       return myHash.size();
    }
    
    // returns height of treap
    public int height()
    {
        return height(root);
    }
    
    // return height of treap by traversing to the bottom of the longest
    // subtree
    private int height(Node<AnyType> root)
    {
        if(root == null)
        {
            // an empty tree has a height of -1
            return -1;
        }
        else if(root.left != null && root.right != null)
        {
            // find longest subtree
            int leftHeight = height(root.left) + 1;
            int rightHeight = height(root.right) + 1;
            if(leftHeight > rightHeight)
            {
                return leftHeight;
            }
            else
            {
                return rightHeight;
            }
        }
        else if(root.left != null && root.right == null)
        {
            return height(root.left) + 1;
        }
        else if(root.right != null && root.left == null)
        {
            return height(root.right) + 1;
        }
        else
        {
            // a tree with a single node has a height of 0
            return 0;
        }
        
    }
    
    // returns a rating of assignment difficulty on a scale of 1.0 to 5.0
    public static double difficultyRating()
    {
        // I felt that this assignment was fairly challenging,
        // particularly wrapping my head around recursive rotations.
        return 4.0;       
    }
    
    // returns estimate of time spent on assignment
    public static double hoursSpent()
    {      
        return 6.0;       
    }
    
    // returns reference to root node
    public Node<AnyType> getRoot()
    {
        return root;
    }

}

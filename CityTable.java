import java.util.ArrayList;
import java.io.*;
import java.util.*;
import java.util.HashSet;
import java.io.Serializable;

public class CityTable implements Serializable {
    // attributes
    private Hash24 hashFunction;    
    private int maxNumCollisions;
    private int maxHashIndex;
    private int primaryTableSize;
    public int numElements;
    public NodePrimary[] primaryTable;
    
    // constructor
    public CityTable(String fileName, int primaryTableSize) {
        // initialize max number of collisons for tracking
        this.maxNumCollisions = 0;
        // set primary table size
        this.setPrimaryTableSize(primaryTableSize);
        // read in data from the file and store it in the primary table
        this.readFile(fileName);
        // dump the hash function used for primary table
        System.out.println("Primary Hash Table Function:");
        this.getHashFunction().dump();
        // output primary hash table statistics
        System.out.println("\nPrimary Hash Table Statistics: ");
        // output number of cities read in
        System.out.println("\tNumber of cities read in: " + this.getNumElements());
        // output primary hash table size
        System.out.println("\tSize of primary table: " + this.getPrimaryTableSize());
        // output max number of collisons at a bucket in primary table
        System.out.println("\tMaximum number of collisions: " + this.getMaxNumCollisions());
        // print collision stats:
        this.printCollisionStats();
        // print all cities in the bucket with the most collisions
        this.printMaxCollisionCities();
        // generate all secondary hash tables
        this.generateSecondaryTables();
        // print hash function statistics
        this.printHashFunctionStats();
        // print average number of hash functions needed for collision slots
        this.printAverageHashFunctions();
    }

    // getters and setters
    public void setMaxCollisionHashIndex(int maxIndex) { this.maxHashIndex = maxIndex; }
    public int getMaxCollisionIndex() { return this.maxHashIndex; }
    public Hash24 getHashFunction() { return this.hashFunction; }
    public void setHashFunction(Hash24 hashFunction) { this.hashFunction = hashFunction; }
    public int getPrimaryTableSize() { return this.primaryTableSize; }
    public void setPrimaryTableSize(int primaryTableSize) { this.primaryTableSize = primaryTableSize; }
    public int getNumElements() { return this.numElements; }
    public void setNumElements(int numElements) { this.numElements = numElements; }
    public NodePrimary[] getPrimaryTable() { return this.primaryTable; }
    public void setPrimaryTable(NodePrimary[] primaryTable) { this.primaryTable = primaryTable; }
    public int getMaxNumCollisions() { return this.maxNumCollisions; }

    @Override
    public String toString() {
        return "{" +
            " hashFunction='" + getHashFunction() + "'" +
            ", primaryTableSize='" + getPrimaryTableSize() + "'" +
            ", numElements='" + getNumElements() + "'" +
            ", primaryTable='" + getPrimaryTable() + "'" +
            "}";
    }

    public void readFile(String fileName) {
        // create scanner to read in the data from file
		Scanner infile = null;
        // try to open file
		try {
			infile = new Scanner(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
			System.exit(0);
		}
        // generate primary table from data
        this.primaryTable = new NodePrimary[getPrimaryTableSize()];
        // initialize all the elements (NodePrimary)
        for (int i = 0; i < this.primaryTable.length; i++) {
			this.primaryTable[i] = new NodePrimary();
            this.primaryTable[i].setNumHashFunctions(1);
            this.primaryTable[i].bucket = new ArrayList<NodeSecondary>();
		}
        // create Hash24 object for finding hash values
        Hash24 hasher = new Hash24();
        this.setHashFunction(hasher);
        // go thru and get the data from the file
        while (infile.hasNextLine()) {
            String key = infile.nextLine();
            String data = infile.nextLine();
            // get hash index
            int hashIndex = hasher.hash(key) % this.primaryTableSize;
            // increment count at index
            this.primaryTable[hashIndex].count++;
            // check if the maxNum of collisions is at that index
            if (this.primaryTable[hashIndex].getCount() > maxNumCollisions) {
                this.maxNumCollisions = this.primaryTable[hashIndex].getCount();
                this.setMaxCollisionHashIndex(hashIndex);
            }
            // set hash function 
            this.primaryTable[hashIndex].setHashFunction(hasher);
            // make new node secondary and add to bucket that will hold
            // all elements at that index and be used to handle collisions
            NodeSecondary node = new NodeSecondary(key, data);
            // add to the bucket in current node
            this.primaryTable[hashIndex].bucket.add(node);
            // increase number of total elements (cities)
            this.numElements++;
        }
        infile.close();
    }
    void printMaxCollisionCities() {
        int index = this.getMaxCollisionIndex();
        System.out.println("\n***** Cities in the slot with the most collisions ******");
        for (int i = 0; i < primaryTable[index].bucket.size(); i++) {
            System.out.println("\t" + primaryTable[index].bucket.get(i).getKey() + ", " + 
                               primaryTable[index].bucket.get(i).getData());
        }
    }

    /* for testing purpose */
    public ArrayList<NodeSecondary> getSameHashBucket() {
        ArrayList<NodeSecondary> duplicateHashes = new ArrayList<>();
        for (int i = 0; i < primaryTable.length; i++) {
            if (primaryTable[i].bucket.size() == 5) {
                duplicateHashes = primaryTable[i].bucket;
            }
        }
        return duplicateHashes;
    }
    // this function receieves an array from the a primary table node that had collisions
    // - it will find a hash function that returns a unique value for each element
	public Hash24 allUnique(ArrayList<NodeSecondary> allSameHash, int secondTableSize, NodePrimary node) {
		boolean allUnique = false;
		// create hash object
        Hash24 hasher = new Hash24();
        // set a counter to keep track of how many hash functions were needed to find a unique one
        int hashFunctionsNeeded = 0;
		// loop until you find a new hash function that produces unique hashes
        while (!allUnique) {
			hasher = new Hash24();
            hashFunctionsNeeded++;
            // create an array of integers and loop thru the array and get new hash values
			ArrayList<Integer> hashes = new ArrayList<Integer>();
			for (int i = 0; i < allSameHash.size(); i++) {
				Integer hash = hasher.hash(allSameHash.get(i).getKey()) % secondTableSize;
				hashes.add(hash);
			}
            allUnique = true;
            // use a set to check for duplicates
			Set<Integer> store = new HashSet<>();
			for (Integer num: hashes) {
				// if we try to add an already existing number to the set then the result will
                // be false so we would set all unique to false
                if (store.add(num) == false) {
					allUnique = false;
					break;
				}
			}
		}
        // set the number of hash functions that were needed for that node
        node.setNumHashFunctions(hashFunctionsNeeded);
		return hasher;
	}
    // function to generate the secondary hash tables wherever there was collisions
    public void generateSecondaryTables() {
        // loop thru the primary table and search for any index where collisions occurred
        for (int i = 0; i < primaryTable.length; i++) {
            // if count is greater than one than collisions occurred
            if (primaryTable[i].count > 1) {
                // get unique hash function for that bucket
                Hash24 newHasher = this.allUnique(primaryTable[i].getBucket(), primaryTable[i].getCount(), primaryTable[i]);
                // store the new hash function in the node
                primaryTable[i].setHashFunction(newHasher);
                // set size for the secondary array - if t elements, new size is t**2
                int secondarySize = primaryTable[i].getCount() * primaryTable[i].getCount();
                primaryTable[i].secondTableSize = secondarySize;
                // initialize the secondary table for the node
                primaryTable[i].secondTable = new NodeSecondary[secondarySize];
                // initialize everything
                for (int j = 0; j < secondarySize; j++) {
                    primaryTable[i].secondTable[j] = new NodeSecondary();
                }
                // loop thru current bucket and get new hash index for each one and then put in array
                for (int j = 0; j < primaryTable[i].bucket.size(); j++) {
                    // get the new hash index
                    int hashIndex = newHasher.hash(primaryTable[i].bucket.get(j).getKey()) % secondarySize;
                    // create the node for the secondary table and insert into table
                    NodeSecondary node = new NodeSecondary(primaryTable[i].bucket.get(j).getKey(), primaryTable[i].bucket.get(j).getData());
                    primaryTable[i].secondTable[hashIndex] = node;
                }
            }
        }
    }

    // function to print collision statistics: for each i between 0 and 24 inclusive, print the number
    // of primary slots that have i collisions
    public void printCollisionStats() {
        // from 0 to 24
        for (int i = 0; i < 25; i++) {
            int counter = 0;
            for (int j = 0; j < primaryTable.length; j++) {
                if (primaryTable[j].getCount()  == i) 
                    counter++;
            }
            System.out.println("# of primary slots with " + i + " cities = " + counter);
        }
    }
    
    // function to print the hash function statistics
    void printHashFunctionStats() {
        System.out.println("\nSecondary hash table statistics: ");
        // from 0 to 21
        for (int i = 1; i < 21; i++) {
            int counter = 0;
            for (int j = 0; j < primaryTable.length; j++) {
                if (primaryTable[j].getNumHashFunctions()  == i && primaryTable[j].getCount() > 1) 
                    counter++;
            }
            System.out.println("\t# of secondary hash tables trying " + i + " hash functions = " + counter);
        }
    }

    // function to print average number of hash functions used for any slot where collision happened
    public void printAverageHashFunctions() {
        // set tracker variables
        double count = 0;
        double numHashFunctions = 0;
        // loop thru the primary table and check if collision took place
        for (int i = 0; i < primaryTable.length; i++) {
            if (primaryTable[i].getCount() > 1) {
                count++;
                numHashFunctions += primaryTable[i].getNumHashFunctions();
            }
        } // print out statistics
        System.out.println("\nNumber of secondary hash tables with more than one item: " + count);
        System.out.println("Average # of hash functions tried = " + (numHashFunctions / count));
    }

    // helper function to split string
    public float[] splitData(String data) {
        String[] arr = data.split(" ");
        float firstVal = Float.parseFloat(arr[0]);
        float secondVal = Float.parseFloat(arr[1]);
        float[] vals = new float[2];
        vals[0] = firstVal;
        vals[1] = secondVal;
        return vals;
    }

    public City find(String key) {
        // compute the first hash index using the stored hash function
        int firstHashIndex = this.hashFunction.hash(key) % this.primaryTableSize;
        // check primary table and see if that slot is empty -> if empty return null
        if (primaryTable[firstHashIndex].getCount() == 0) {
            return null;
        } // check if count is one -> if so check the bucket which would contain only one entry and compare
        else if (primaryTable[firstHashIndex].getCount() == 1) {
            // check if key matches
            if (key.equalsIgnoreCase(primaryTable[firstHashIndex].bucket.get(0).getKey())) {
                float[] data = this.splitData(primaryTable[firstHashIndex].bucket.get(0).getData());
                String cityName = primaryTable[firstHashIndex].bucket.get(0).getKey();
                City city = new City(cityName, data[0], data[1]);
                return city;
            } // if not a match return null
            else return null;
        } // if count is greater than one -> compute second hash index and then check that slot
        else if (primaryTable[firstHashIndex].getCount() > 1) {
            // get the second hash index using that nodes hash function
            int secondHashIndex = primaryTable[firstHashIndex].hashFunction.hash(key) % primaryTable[firstHashIndex].secondTableSize;
            // check that slot in the primary table -> if not empty then compare with that value
            if (!primaryTable[firstHashIndex].secondTable[secondHashIndex].getKey().isEmpty()) {
                // check if the key matches the key at that index
                if (key.equalsIgnoreCase(primaryTable[firstHashIndex].secondTable[secondHashIndex].getKey())) {
                    float[] data = this.splitData(primaryTable[firstHashIndex].secondTable[secondHashIndex].getData());
                    String cityName = primaryTable[firstHashIndex].secondTable[secondHashIndex].getKey();
                    City city = new City(cityName, data[0], data[1]);
                    return city;
                }
            }
        }
        return null;
    }
    
    // method to store the hash table in a file with the given filename using Javas writeObject method
    public void writeToFile(String fName) {
        try {
            // create a new file with ObjectOutputStream
            FileOutputStream out = new FileOutputStream(fName);
            ObjectOutputStream oout = new ObjectOutputStream(out);
            // write to the file
            oout.writeObject(this);
            // close the stream
            oout.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // method to read CityTable back from a file using javas readObject() method
    public static CityTable readFromFile(String fileName) {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            CityTable city = (CityTable) ois.readObject();
            return city;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

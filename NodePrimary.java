
import java.util.ArrayList;
import java.io.Serializable;

public class NodePrimary implements Serializable {
	// attributes
	public int count;
	public ArrayList<NodeSecondary> bucket;
	public NodeSecondary secondTable[];
	public Hash24 hashFunction;
	public int numHashFuncs;
	public int secondTableSize;
	
	// constructors
	public NodePrimary() {}
	public NodePrimary(int count, ArrayList<NodeSecondary> bucket, NodeSecondary[] nodeSecondaries, Hash24 hashFunction) {
		this.count = count;
		this.bucket = bucket;
		this.hashFunction = hashFunction;
	}
	// getters and setters
	public int getCount() { return this.count; }
	public void setCount(int count) { this.count = count; }
	public ArrayList<NodeSecondary> getBucket() { return this.bucket; }
	public void setBucket(ArrayList<NodeSecondary> bucket) { this.bucket = bucket; }
	public void setNumHashFunctions(int numHashes) { this.numHashFuncs = numHashes; }
	public int getNumHashFunctions() { return this.numHashFuncs; }
	public Hash24 getHashFunction() { return this.hashFunction;}
	public void setHashFunction(Hash24 hashFunction) { this.hashFunction = hashFunction; }

	@Override
	public String toString() {
		return "{" +
			" count='" + getCount() + "'" +
			", Bucket='" + getBucket() + "'" +
			", hashFunction='" + getHashFunction() + "'" +
			"}";
	}
	
}

	
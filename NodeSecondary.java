import java.util.Objects;
import java.io.Serializable;

public class NodeSecondary implements Serializable {
    // attributes
    private String key;
    private String data;

    // constructors
    public NodeSecondary() {
        this.key = "";
        this.data = "";
    }
    public NodeSecondary(String key, String data) {
        this.key = key;
        this.data = data;
    }

    // getters and setters
    public String getKey() { return this.key;  }
    public void setKey(String key) { this.key = key; }
    public String getData() { return this.data; }
    public void setData(String data) { this.data = data; }
    public NodeSecondary key(String key) {
        setKey(key);
        return this;
    }
    public NodeSecondary data(String data) {
        setData(data);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof NodeSecondary)) {
            return false;
        }
        NodeSecondary nodeSecondary = (NodeSecondary) o;
        return Objects.equals(key, nodeSecondary.key) && Objects.equals(data, nodeSecondary.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, data);
    }

    @Override
    public String toString() {
        return "{" +
            " key='" + getKey() + "'" +
            ", data='" + getData() + "'" +
            "}";
    }
}

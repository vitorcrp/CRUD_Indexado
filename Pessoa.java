import java.io.*;

public class Pessoa implements Registro {
	protected int id;
    protected String name;
    protected String email;
    protected int age;
    
    public Pessoa() {
        id = -1;
        name = "";
        email = "";
        age = -1;
    }
    
    public Pessoa(int i, String n, String e, int a) {
        id = i;
        name = n;
        email = e;
        age = a;
    }
    
    public int getID() {
        return id;
    }
    
    public void setID(int id) {
        this.id = id;
    }
    
    public byte[] toByteArray() throws IOException { 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeUTF(name);
        dos.writeUTF(email);
        dos.writeInt(age);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException { 
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        id = dis.readInt();
        name = dis.readUTF();
        email = dis.readUTF();
        age = dis.readInt();
    }
      
    public String toString() {
      return "\nID: " + id + 
             "\nname: " + name + 
             "\nemail: " + email + 
             "\nage: " + age;
    }

}
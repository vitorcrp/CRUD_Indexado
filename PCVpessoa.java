import java.io.*;

public class PCVpessoa implements RegistroHashExtensivel<PCVpessoa> {

  
  private int id;     
  private long position;
  private short size = 12;
 
  public PCVpessoa() {
    this(-1, -1);
  }

  public PCVpessoa(int i, long p) {
    try {
      this.id = i ;
      this.position = p;
      if (id < -1)
        throw new Exception("Id inválido");
    } catch (Exception ec) {
      ec.printStackTrace();
    }
  }

  @Override
  public int hashCode() {
    return this.id;
  }
  
  public long getPosition() {
	  return position;
  }

  public short size() {
    return this.size;
  }

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(id);
    dos.writeLong(position);
    byte[] bs = baos.toByteArray();
    byte[] bs2 = new byte[size];
    for (int i = 0; i < size; i++)
      bs2[i] = ' ';
    for (int i = 0; i < bs.length && i < size; i++)
      bs2[i] = bs[i];
    return bs2;
  }

  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    this.id = dis.readInt();
    this.position = dis.readLong();
  }

}


import java.io.*;
import java.lang.reflect.Constructor;


public class CRUD<T extends Registro> {

	RandomAccessFile access; 
	Constructor<T> constructor;
	String fileName;
	HashExtensivel<PCVpessoa> he;

	public CRUD(Constructor<T> c, String file) throws Exception {
		this.fileName = file;
		this.constructor = c;
		this.access = new RandomAccessFile(file, "rw");
		he = new HashExtensivel<>(PCVpessoa.class.getConstructor(), 4, "pessoas.hash_d.db",
		          "pessoas.hash_c.db");
	}

	public int create(T obj) throws Exception { //Metodo que cria novos registros
		int lastID = 0; 
		access.seek(0); //posiciona o ponteiro no inicio do arquivo
		if (access.length() == 0) { 
			access.writeInt(1); //escreve o cabeçalho como 1
			obj.setID(1); 
		} else {
			lastID = access.readInt(); 
			obj.setID(lastID + 1); 
			access.seek(0); 
			access.writeInt(obj.getID()); //escreve o cabeçalho como o respectivo id
		}
		byte[] mByte = obj.toByteArray();  //cria um vetor de bytes para o respectivo registro e escreve seus atributos
		long pos = access.length(); 
		access.seek(pos);
		pos = access.getFilePointer();
		access.writeBoolean(false); 
		access.writeShort(mByte.length);  
    	access.write(mByte);
    	he.create(new PCVpessoa(obj.getID(), pos));
    	return obj.getID();
	}

	public T read(int id) throws Exception { //Método que recupera os registros existentes
		T obj = null;
		long pos;
		boolean lap;
		short size;
		long lapPosition;
		int objID;
		pos = he.read(id).getPosition();
		if (pos != -1) {
			access.seek(pos);
			lap = access.readBoolean();
			size = access.readShort();
			lapPosition = access.getFilePointer();
			objID = access.readInt();
			if(!lap &&  id == objID) {
				obj = constructor.newInstance();
				byte[] mByte = new byte[size];
				access.seek(lapPosition);
		        access.read(mByte);
		        obj.fromByteArray(mByte);
			}	
		}
		return obj; //retorna o objeto caso encontrado ou retorna null caso não tenha sido encontrado
	}
  
	public boolean update(T newObj) throws Exception {
		boolean returnUpdt = false;
		boolean flag = false;
		byte[] newByte = newObj.toByteArray();
		if(newObj.getID() < 0) System.out.println("Registro Inválido"); 
		else {
			long pos;
			long newPos;
            boolean lap;
            short size;
            pos = he.read(newObj.getID()).getPosition();
            if (pos != -1) {
            	while (!flag) { //Lê o registro, verifica se a lapide é verdadeira e o registro tem o mesmo id passado para o método
	            	access.seek(pos);
            		lap = access.readBoolean(); 
	            	size = access.readShort();
	            	byte[] mByte = new byte[size]; 
	            	access.read(mByte);
	            	T obj = constructor.newInstance();
	            	obj.fromByteArray(mByte);
	            	
	            	if(!lap && newObj.getID() == obj.getID()){ 
	            		access.seek(pos);                    
	            		access.read(mByte); 
	            		if(newByte.length <= mByte.length){ //verifica se o novo registro  tem um tamanho menor ou igual ao registro que vai ser atualizado
	            			access.seek(pos);
	            			access.writeBoolean(false); 
	            			access.writeShort(mByte.length); 
		                	access.write(newByte);
						} else {
							access.seek(pos); //posiciona o ponteiro na posicao do lapide
							access.writeBoolean(true); //escreve o lapide como falso (excluido)
							access.seek(access.length()); //posiciona o ponteiro no final do arquivo
							newPos = access.getFilePointer(); //identificar a nova posição do registro
							access.writeBoolean(false); //escreve o lapide como verdadeiro e em seguida escreve o registro
							access.writeShort(newByte.length); 
							access.write(newByte); 
							he.update(new PCVpessoa(newObj.getID(), newPos));
						} 
	            		flag = true;
	            		returnUpdt = true;
	            	}
	            }
            }  
		}
		return returnUpdt;
  }
  
	public boolean delete(int id) throws Exception {
		boolean returnDel = false;
		if(id <= 0) System.out.println("\nREGISTRO INVÁLIDO"); 
		else {
			long pos;
            boolean lap;
            long currentLap;
            boolean flag = false;
            short size;
            int objID;
            pos = he.read(id).getPosition();
            if (pos != -1) {
	            while(!flag){ //lê o registro, verifica se a lapide é verdadeira e o registro tem o mesmo id passado para o método
	                access.seek(pos);
	                lap = access.readBoolean();
	                size = access.readShort();
	                currentLap = access.getFilePointer();
	                objID = access.readInt(); 
	                access.seek(currentLap + size); //posiciona o ponteiro nas lapides dos próximos registros 
	                if(access.getFilePointer()>=access.length()) flag = true; 
	                    if(!lap && id == objID) { 
	                        flag = true;
	                        access.seek(pos); 
	                        access.writeBoolean(true); 
	                        returnDel = true; 
	                    }
	            }   
            }
		}
        return returnDel;
	}

}
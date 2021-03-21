import java.io.*;

public class Main {
  public static void main(String[] args) {

    CRUD<Pessoa> fileP;

    Pessoa p1 = new Pessoa(-1, "Lorena", "lorena@email.com", 18);
    Pessoa p2 = new Pessoa(-1, "Vítor", "vitor@email.com", 19);
    Pessoa p3 = new Pessoa(-1, "Gabriel", "gabriel@email.com", 20);
    int id1, id2, id3; // ID dos registros
    boolean ok;

    try {
      new File("pessoas.db").delete();
      new File("pessoas.hash_c.db").delete();
      new File("pessoas.hash_d.db").delete();
      // Apaga os arquivos anteriores, para criar novos
      fileP = new CRUD<>(Pessoa.class.getConstructor(), "pessoas.db");

      id1 = fileP.create(p1);
      id2 = fileP.create(p2);
      id3 = fileP.create(p3);
      System.out.println("\nRegistros criados com sucesso");

      System.out.println("\nProcurando registros:"); // Procura os registros e os printa na tela
      System.out.println(fileP.read(id1));
      System.out.println(fileP.read(id2));
      System.out.println(fileP.read(id3));

      System.out.println("\nAtualizando registro:");
      p1.name = "Nanda";
      p1.email = "fernanda@email.com";

      System.out.println("\nRegistro pré atualizado:");
      System.out.println(fileP.read(id1));
      ok = fileP.update(p1);
      System.out.println("\nRegistro pós atualizado:");
      System.out.println(fileP.read(id1));

      System.out.println("\nDeletando registro:");
      ok = fileP.delete(id2);
      if (ok)
        System.out.println("\nRegistro excluido.");
      Pessoa p = fileP.read(id2);
      if (p == null)
        System.out.println("\nRegistro não encontrado.");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

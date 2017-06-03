package mirrlis;

import mirrlis.reflection.Cube;
import mirrlis.reflection.Square;
import mirrlis.rotation.Quadrate;

//Рассвет сознанья поутру
//есть чудо, а не малость...
//отбрось ленивую хандру,
//да рассветает радость

public class Main {

  public static void main(String[] args) {
      Cube c = new Cube(3, 3, 2);
      //Quadrate c = new Quadrate(6);
      //Square c = new Square(9,4);
      try { 
          c.generateRandomKey();
          
          c.encryptFile("files/open2.txt");
          c.printCipherText();

          System.out.println("\n-------------------------------------------");
          c.encryptFile("files/close2.txt");
          c.printCipherText();
          
          c.printKey();
      } catch(Exception e) {
          e.printStackTrace();
      }
  }
}

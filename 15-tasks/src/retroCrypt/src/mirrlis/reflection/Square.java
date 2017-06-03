package mirrlis.reflection;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Square {
    class Point implements Comparator<Point> {
        @Override  public int compare(Point p1, Point p2) {
            if      (p1.i > p2.i) return 1;
            else if (p1.i < p2.i) return -1;
            else if (p1.j > p2.j) return 1;
            else if (p1.j < p2.j) return -1;
            return 0;
        }
        
        int i, j;
        public Point(int i, int j) {
            this.i = i; this.j = j; 
        }
    };
    
    
    int m; //высота решетки (решетка - 1/4 часть всего поля) 
    int n; //ширина решетки (решетка - 1/4 часть всего поля)
    int cipherText[][]; 
    int key[]; //ключ есть четверичное число... разрядности m*n
    int reflectionOrder[] = new int[] {0, 1, 2, 3}; //порядок отражений
    
    static int X_MIRROR = 0x00000001;
    static int Y_MIRROR = 0x00000002;
    

    private Point getPoint(int keyPos, int key4xDigit) throws IllegalArgumentException {
        if ((keyPos >= m*n) || (keyPos < 0)) 
            throw new IllegalArgumentException("keyPos");
        
        int x = (keyPos / n) + 1;
        int y = (keyPos % n) + 1;
        
        if ((key4xDigit & X_MIRROR) != 0) 
            y = -y;
        else
        	y--;
       
        if ((key4xDigit & Y_MIRROR) != 0) 
            x = -x;
        else
        	x--;
        
        return new Point(m+x, n+y);
    }
    
    public Square(int n, int m) {
        this.m = m; 
        this.n = n;
        cipherText = new int[2*m][2*n];
        for (int i=0; i<2*m; i++)
        	for (int j=0; j<2*n; j++)
        		cipherText[i][j] = '#';
        key = new int[m*n];        
    }
    
    public void generateRandomKey() {
        Random r = new Random();
        for (int i = 0; i<m*n; i++)
            key[i] = r.nextInt(4);
    }

    public void setKey(int key[]) {
        for (int i = 0; i<m*n; i++)
            this.key[i] = key[i];
    }
    
    public int encrypt(char text[], int offset) {
        int length = text.length;
        if (length < 0) throw new IllegalArgumentException("length");
        
        Point points[] = new Point[m*n];
        int k = offset;
        for (int i = 0; i<4; i++) {
            for (int j = 0; j<m*n; j++) 
                points[j] = getPoint(j, key[j] ^ reflectionOrder[i]);
            Arrays.sort(points, points[0]);
            for (int j = 0; j<m*n; j++, k++) {
                char c = '*';
                if (k < length)
                    c = text[k];
                cipherText[points[j].i][points[j].j] = c;
            }
            //printCipherText();
            //System.out.print('\n');
        }
        return k;
    }
    
    private String getStringFromReader(Reader reader, int maxLength) throws Exception {
        int offset = 0; 
        int nr = 0;
        char buf[] = new char[maxLength];
        while (true) {
            if (maxLength-offset <= 0) break;
            nr = reader.read(buf, offset, maxLength-offset);
            if (nr < 0) break;
            if (nr == 0) continue;
            offset += nr;
        } 
        return new String(buf, 0, offset); 
    }
    
    public void encryptFile(String fileName) throws Exception {
        FileInputStream fis = new FileInputStream(fileName);
        InputStreamReader isr = new InputStreamReader(fis, "CP1251");
        String content = getStringFromReader(isr, m*n*4);
        encrypt(content.toCharArray(), 0);
    }
    
    public void printCipherText() {
        for (int i=0; i<2*m; i++) { 
            for (int j=0; j<2*n; j++) {
                System.out.print((char)cipherText[i][j]);
            }
            System.out.print('\n');
        }
    }
    
    public void printKey() {
    	System.out.print("int key[] = new int[] {");
    	int i;
    	for (i=0; i<m*n-1; i++)
    		System.out.print(" " + key[i] + ",");
		System.out.print(" " + key[i] + " };");
    }
}

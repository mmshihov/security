package mirrlis.reflection;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Cube {
    class Point implements Comparator<Point> {
        @Override  public int compare(Point p1, Point p2) {
        	
            if      (p1.k > p2.k) return 1;
            else if (p1.k < p2.k) return -1;
            else if (p1.i > p2.i) return 1;
            else if (p1.i < p2.i) return -1;
            else if (p1.j > p2.j) return 1;
            else if (p1.j < p2.j) return -1;
            return 0;
        }
        
        int i, j, k;
        public Point(int i, int j, int k) {
            this.i = i; 
            this.j = j;
            this.k = k;
        }
    };
    
    
    int m; //высота  
    int n; //ширина 
    int l; //глубина
    int cipherText[][][]; 
    int key[]; //ключ есть восьмиричное число... разрядности m*n*l
    int reflectionOrder[] = new int[] {0, 1, 2, 3, 4, 5, 6, 7}; //порядок отражений
    
    static int XZ_MIRROR = 0x00000001;
    static int YZ_MIRROR = 0x00000002;
    static int XY_MIRROR = 0x00000004;

    private Point getPoint(int keyPos, int key8xDigit) throws IllegalArgumentException {
        if ((keyPos >= m*n*l) || (keyPos < 0)) 
            throw new IllegalArgumentException("keyPos");
        
        int z = (keyPos / (m*n)) + 1;
        int xy = (keyPos % (n*m));
        int x = (xy / n) + 1;
        int y = (xy % n) + 1;
        
        if ((key8xDigit & XZ_MIRROR) != 0) 
            y = -y;
        else
        	y--;
       
        if ((key8xDigit & YZ_MIRROR) != 0) 
            x = -x;
        else
        	x--;
        
        if ((key8xDigit & XY_MIRROR) != 0) 
            z = -z;
        else
        	z--;
        
        return new Point(m+x, n+y, l+z);
    }
    
    public Cube(int n, int m, int l) {
        this.m = m; 
        this.n = n;
        this.l = l;
        cipherText = new int[2*m][2*n][2*l];
        clearCipherText();
        key = new int[m*n*l];        
    }
    
    void clearCipherText() {
        for (int i=0; i<2*m; i++)
            for (int j=0; j<2*n; j++)
                for (int k=0; k<2*l; k++)
                    cipherText[i][j][k] = '#';        
    }
    
    public void generateRandomKey() {
        Random r = new Random();
        for (int i = 0; i<m*n*l; i++)
            key[i] = r.nextInt(8);
    }

    public void setKey(int key[]) {
        for (int i = 0; i<m*n*l; i++)
            this.key[i] = key[i];
    }
    
    public int encrypt(char text[], int offset) {
        clearCipherText();
        
        int length = text.length;
        if (length < 0) throw new IllegalArgumentException("length");
        
        Point points[] = new Point[m*n*l];
        int k = offset;
        for (int i = 0; i<8; i++) {
            for (int j = 0; j<m*n*l; j++) 
                points[j] = getPoint(j, key[j] ^ reflectionOrder[i]);
            Arrays.sort(points, points[0]);
            for (int j = 0; j<m*n*l; j++, k++) {
                char c = '*';
                if (k < length)
                    c = text[k];
                cipherText[points[j].i][points[j].j][points[j].k] = c;
            }
            printCipherText();
            System.out.print('\n');
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
        String content = getStringFromReader(isr, m*n*l*8);
        encrypt(content.toCharArray(), 0);
    }
    
    public void printCipherText() {
    	for (int k=0; k<2*l; k++) {
    		System.out.print("[" + k + "]:\n");
    		for (int i=0; i<2*m; i++) { 
    			for (int j=0; j<2*n; j++) {
    				System.out.print((char)cipherText[i][j][k]);
    			}
        		System.out.print('\n');
    		}
    		System.out.print('\n');
    	}
    }
    
    public void printKey() {
    	System.out.print("int key[] = new int[] {");
    	int i;
    	for (i=0; i<m*n*l-1; i++)
    		System.out.print(" " + key[i] + ",");
		System.out.print(" " + key[i] + " };");
    }
}

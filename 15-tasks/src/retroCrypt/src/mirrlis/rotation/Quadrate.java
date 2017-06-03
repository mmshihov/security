package mirrlis.rotation;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;


public class Quadrate {
    
    //индексы квадратной матрицы будем хранить таком формате
    //так как будет массив, то наследуем от интерфейса IComparable,
    //чтобы иметь возможность сортировать их
    public static class TIndexes implements Comparator<TIndexes> {
        public int i, j;

        //определяем интерфейсный метод для возможности сортировок
        @Override  public int compare(TIndexes p1, TIndexes p2) {
            if (p1.i>p2.i) return 1;
            else if (p1.i<p2.i) return -1;
            else
                if (p1.j>p2.j) return 1;
                else if (p1.j<p2.j) return -1;
                else return 0;
        }
    }

    private int    N;      //квадратняая решетка с размерностью (2N x 2N) 
    private byte[] RDigit; //решетка кодируется числом в 4-ичной системе счисления с 
                           //количеством разрядов равным (N x N)
    private byte[] RotationOrder = new byte[]{0, 3, 1, 2}; //порядок поворотов решетки
                                                           //по умолчанию - обычный...

    private char[][] M; //матрица решетки (хранит символы...)
    private TIndexes[][] R = new TIndexes[4][]; //упорядоченные массивы индексов
                                                //для ускорения процесса шифровки-дешифровки

    //конструкторы
    public Quadrate(int n) {
        N      = n;         
        RDigit = new byte[N*N];         
        R[0]   = new TIndexes[N*N];
        R[1]   = new TIndexes[N*N];
        R[2]   = new TIndexes[N*N];
        R[3]   = new TIndexes[N*N];
        for (int i=0; i<R.length; ++i) {
            for (int j=0; j<R[i].length; ++j) {
                R[i][j] = new TIndexes();
            }
        }
        M      = new char[2*N][2*N]; //определим решетку
    }

    
    //может понадобится для повторения процесса шифрации с тем же ключом
    public Quadrate(int n, byte[] array) {
        this(n);
        for (int i=0; i<N*N; i++) RDigit[i] = array[i];
        ToIndexes();
    }

    
    //может понадобится для повторения процесса шифрации с тем же ключом
    //и тем же порядком вращений
    public Quadrate(int n, byte[] rarray, byte[] rorder) {
        this(n, rarray);
        for (int i=0; i<4; i++) RotationOrder[i] = rorder[i];
    }

    
    //случайная инициализация массива пробоев
    public void generateRandomKey() {
        Random r = new Random();	      
        for (int i=0; i<N*N; i++) RDigit[i]=(byte)r.nextInt(4);	      
        ToIndexes();
    }
    
    
    //вывод матрицы
    private void MOut() {
        int i, j;
        for (i=0; i<2*N; i++) {
            System.out.println();
            for (j=0; j<2*N; j++) 
                System.out.print(M[i][j]);
        }
    }

    
    //очистка матрицы...
    public void MClear() {
        for (int i=0; i<2*N; i++) 
            for (int j=0; j<2*N; j++) M[i][j] = ' ';
    }

    
    //выводим массив пробоев...
    public void PrintRDigit() {
        int i;
        
        System.out.print("byte[] RDigit=new byte[" + (N*N) + "] {");
        for (i=0; i<N*N-1; i++)
            System.out.print(RDigit[i] + ", ");
        System.out.print(RDigit[i]);
        System.out.print("}");
    }

    
    //выводим массив порядка вращений
    public void PrintRotationOrder() {
        int i;
        System.out.print("byte[] RotationOrder=new byte[] {");
        for (i=0; i<3; i++)
            System.out.print(RotationOrder[i] + ", ");
        System.out.print(RotationOrder[i]);
        System.out.print("}");
    }

    
    //криптуем строку...
    public void EncryptString(String msg) {
        int l=0, i, j, k;

            MClear();
            for (i=0; i<4; i++) {
                for (j=0; j<N*N; j++) {
                    char c = '*';
                    if (l<msg.length()) {
                        c = msg.charAt(l++);
                    } 
                    k = RotationOrder[i];
                    M[R[k][j].i][R[k][j].j] = c; 
                }
                MOut(); System.out.println();
                DecryptToConsole(); System.out.println();
            }      
        MOut(); System.out.println();
        DecryptToConsole(); System.out.println();
    }

    
    //декриптуем матрицу
    public void DecryptToConsole() 
    {         
        int i, j, k;

        for (i=0; i<4; i++) {
            for (j=0; j<N*N; j++) {
                k = RotationOrder[i];
                System.out.print(M[R[k][j].i][R[k][j].j]);
            }
        }
        System.out.println();
    }

    
    public void encryptFile(String fileName) throws Exception {
        FileInputStream fis = new FileInputStream(fileName);
        InputStreamReader isr = new InputStreamReader(fis, "CP1251");
        String content = getStringFromReader(isr, N*N*4);
        EncryptString(content);
    }
    
    public void printCipherText() {
        MOut();
        System.out.println();
    }
    
    
    public void printKey() {
        PrintRDigit();
        PrintRotationOrder();
    }

    
    //инициализирует массивы индексов
    private void ToIndexes() {
        int r, i, j;
        for (r=0; r<N*N; r++) {
            i = r/N;
            j = r%N;
            //определим в какой квадрат ставить индекс и на скоько градусов надо 
            //довернуть матирцу...
            switch (RDigit[r]) {
                case 0: R[0][r].i = i;       R[0][r].j = j;       break;
                case 1: R[0][r].i = j;       R[0][r].j = N+N-1-i; break;
                case 2: R[0][r].i = N+N-1-i; R[0][r].j = N+N-1-j; break;
                case 3: R[0][r].i = N+N-1-j; R[0][r].j = i;       break;
                default: throw new RuntimeException();
            }
            //поворот на 90 градусов...
            R[1][r].i = R[0][r].j; R[1][r].j = N+N-1-R[0][r].i;
            R[2][r].i = R[1][r].j; R[2][r].j = N+N-1-R[1][r].i;
            R[3][r].i = R[2][r].j; R[3][r].j = N+N-1-R[2][r].i;
        }
        Arrays.sort(R[0],R[0][0]); 
        Arrays.sort(R[1],R[1][0]);
        Arrays.sort(R[2],R[2][0]); 
        Arrays.sort(R[3],R[3][0]);  
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
    
    

}

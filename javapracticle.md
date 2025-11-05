# Java Practicals 1 to 15

## 1. Factorial of a Number

```java
import java.util.*;
class Factorial {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), fact = 1;
        for(int i=1;i<=n;i++) fact*=i;
        System.out.println("Factorial: "+fact);
    }
}
```

## 2. Display First 50 Prime Numbers

```java
class First50Primes {
    public static void main(String[] args) {
        int count=0, num=2;
        while(count<50){
            boolean prime=true;
            for(int i=2;i*i<=num;i++)
                if(num%i==0){prime=false;break;}
            if(prime){System.out.print(num+" ");count++;}
            num++;
        }
    }
}
```

## 3. Find Sum and Average of N Numbers

```java
import java.util.*;
class SumAvg {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n=sc.nextInt(), sum=0;
        for(int i=0;i<n;i++) sum+=sc.nextInt();
        System.out.println("Sum="+sum+" Avg="+(sum/(float)n));
    }
}
```

## 4. Simple Calculator

```java
import java.util.*;
class Calculator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double a=sc.nextDouble(), b=sc.nextDouble();
        char op=sc.next().charAt(0);
        double r = switch(op){
            case '+': yield a+b;
            case '-': yield a-b;
            case '*': yield a*b;
            case '/': yield b!=0?a/b:Double.NaN;
            default: yield 0;
        };
        System.out.println("Result: "+r);
    }
}
```

## 5. Method and Constructor Overloading

```java
class OverloadDemo {
    int a, b;
    OverloadDemo(){ a = b = 0; }
    OverloadDemo(int x){ a = b = x; }
    OverloadDemo(int x, int y){ a = x; b = y; }

    void show(){ System.out.println(a+" "+b); }
    void show(int x){ System.out.println("x="+x); }
    void show(int x, int y){ System.out.println("Sum="+(x+y)); }
    public static void main(String[] args) {
        OverloadDemo ob1 = new OverloadDemo();
        OverloadDemo ob2 = new OverloadDemo(5);
        OverloadDemo ob3 = new OverloadDemo(3,7);
        ob1.show();
        ob2.show(10);
        ob3.show(4,6);
    }
}
```

## 6. Rectangle Comparison (Area & Color)

```java
class Rectangle {
    int width, length, area;
    String color;

    Rectangle(int w, int l, String c) {
        width = w; length = l; color = c;
        area = w * l;
    }

    boolean isMatching(Rectangle r) {
        return area == r.area && color.equalsIgnoreCase(r.color);
    }

    public static void main(String[] args) {
        Rectangle r1 = new Rectangle(5, 4, "Red");
        Rectangle r2 = new Rectangle(4, 5, "red");
        if(r1.isMatching(r2))
            System.out.println("Matching Rectangle");
        else
            System.out.println("Non-Matching Rectangle");
    }
}
```

## 7. Sort List of Integers

```java
import java.util.*;
class SortIntegers {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        ArrayList<Integer> list = new ArrayList<>();
        for(int i=0;i<n;i++) list.add(sc.nextInt());
        Collections.sort(list);
        System.out.println("Sorted: "+list);
    }
}
```

## 8. Sort List of Names

```java
import java.util.*;
class SortNames {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        ArrayList<String> names = new ArrayList<>();
        sc.nextLine();
        for(int i=0;i<n;i++) names.add(sc.nextLine());
        Collections.sort(names);
        System.out.println("Sorted: "+names);
    }
}
```

## 9. Add Two Matrices

```java
import java.util.*;
class AddMatrix {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int r = sc.nextInt(), c = sc.nextInt();
        int a[][] = new int[r][c], b[][] = new int[r][c], sum[][] = new int[r][c];
        for(int i=0;i<r;i++) for(int j=0;j<c;j++) a[i][j]=sc.nextInt();
        for(int i=0;i<r;i++) for(int j=0;j<c;j++) b[i][j]=sc.nextInt();
        for(int i=0;i<r;i++){
            for(int j=0;j<c;j++){
                sum[i][j]=a[i][j]+b[i][j];
                System.out.print(sum[i][j]+" ");
            }
            System.out.println();
        }
    }
}
```

## 10. Implementing Interface

```java
interface Shape {
    double area();
}

class Circle implements Shape {
    double r;
    Circle(double r){ this.r=r; }
    public double area(){ return Math.PI*r*r; }
}

public class Main {
    public static void main(String[] args) {
        Shape s = new Circle(5);
        System.out.println("Area: "+s.area());
    }
}
```

## 11. Player Class with Inheritance

```java
class Player {
    String name;
    Player(String n){ name = n; }
    void display(){ System.out.println("Player: "+name); }
}

class CricketPlayer extends Player {
    CricketPlayer(String n){ super(n); }
    void show(){ System.out.println(name+" is a Cricket Player"); }
}

class FootballPlayer extends Player {
    FootballPlayer(String n){ super(n); }
    void show(){ System.out.println(name+" is a Football Player"); }
}

class HockeyPlayer extends Player {
    HockeyPlayer(String n){ super(n); }
    void show(){ System.out.println(name+" is a Hockey Player"); }
}

public class Main {
    public static void main(String[] args) {
        CricketPlayer c = new CricketPlayer("Virat");
        FootballPlayer f = new FootballPlayer("Messi");
        HockeyPlayer h = new HockeyPlayer("Dhyan Chand");
        c.display(); c.show();
        f.display(); f.show();
        h.display(); h.show();
    }
}
```

## 12. Exception Handling

```java
public class Main {
    public static void main(String[] args) {
        try {
            int a = 10, b = 0;
            int c = a / b;
            System.out.println(c);
        } catch (ArithmeticException e) {
            System.out.println("Error: Division by zero");
        }
    }
}
```

## 13. Graphics Drawing

```java
import java.awt.*;
import javax.swing.*;
public class Main extends JPanel {
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect(30, 30, 100, 60);
        g.drawOval(160, 30, 100, 60);
        g.drawLine(30, 120, 260, 120);
        g.drawString("Graphics Demo", 90, 160);
    }
    public static void main(String[] args) {
        JFrame f = new JFrame("Graphics Example");
        f.add(new Main());
        f.setSize(300, 250);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
```

## 14. File Read and Write

```java
import java.io.*;
public class Main {
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"));
             BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {
            String line;
            while((line = br.readLine()) != null)
                bw.write(line + "\n");
            System.out.println("File copied successfully.");
        } catch(IOException e) {
            System.out.println("Error: " + e);
        }
    }
}
```

## 15. Creating Two Threads

```java
class Task extends Thread {
    public void run() {
        for(int i=1;i<=5;i++)
            System.out.println(getName()+" → "+i);
    }
}
public class Main {
    public static void main(String[] args) {
        Task t1 = new Task();
        Task t2 = new Task();
        t1.start();
        t2.start();
    }
}
```

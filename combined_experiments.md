# Experiments 8, 9, and 10 (Combined)

## Experiment 8  
**Name:** Sanket Gadekar  
**Roll No:** 204A039  

```matlab
clear;
p_x = [2.25 4 1.42];
x = length(p_x);
disp("Number of rows in P(X): " ,x);
Hx=0;
for i = 1:x
        Hx = Hx + p_x(i) * log2(1 / p_x(i));
end

disp("Entropy of input message H(X) is: ");
disp(Hx);

p_y = [1.39 2.60 4.17];
y = length(p_y);
disp("Number of rows in P(Y): " , y);

Hy = 0;
for j = 1:y
        Hy = Hy + p_y(j) * log2(1 / p_y(j));
end

disp("Entropy of output message:");
disp(Hy);

P_xy = [0.75 0.25 1.25;
        0.5 1.5 2.5;
        0.14 0.8 0.42];

[m, n] = size(P_xy);
Hxy = 0;

for i = 1:m
    for j = 1:n
        if P_xy(i,j) > 0
            Hxy = Hxy + P_xy(i,j) * log2(1 / P_xy(i,j));
        end
    end
end

disp("Entropy of joint probability matrix H(X,Y) is: ");
disp(Hxy);

Hx_y = Hxy - Hy;   
disp("Conditional entropy H(X|Y) when Y is transmitted: ");
disp(Hx_y);

Hy_x = Hxy - Hx;  
disp("Conditional entropy H(Y|X) when X is transmitted: ");
disp(Hy_x);

I_xy = Hx - Hx_y;
disp("Mutual information I(X;Y) is: ");
disp(I_xy);
```

---

## Experiment 9  
**Name:** Shubham Ambre  
**Roll No:** 304A038  

```matlab
clc;
clear;

m = [0 1 1 1];
p = [0 0 1; 1 0 0; 1 1 0; 0 1 1];
k = length(m);
Ik = eye(k, k);
G = [Ik p];
disp("Generator matrix is:");
disp(G);

C = modulo(m * G, 2);
disp("Code word is:");
disp(C);

r = input('Enter received code vector: ');
e = input('Enter received error: ');
n = length(C);
In_k = eye(n - k, n - k);
H = [p' In_k];
disp("Parity check matrix is:");
disp(H);

S1 = modulo(r * H', 2);
S2 = modulo(e * H', 2);
disp("Syndrome1 is:");
disp(S1);
disp("Syndrome2 is:");
disp(S2);

HT = H';
if (S1 == 0 && S2 == 0)
    disp("Correct");
else
    disp("Received code vector is with error");
    for i = 1:n
        if (S1 == HT(i, :))
            e = [zeros(1, i-1) 1 zeros(1, n-i)];
        end
    end
end

disp("Correct word is:");
CC = modulo(r + e, 2);
disp(CC);
```

---

## Experiment 10  
**Name:** Smit Gudadhe  
**Roll No:** 304A043  

```matlab
clc;
clear all;

m = [1 1 0 1];
k = length(m);
n = 7;
x = poly(0, 'x');
m_x = m(1)*x^3 + m(2)*x^2 + m(3)*x + m(4);
disp("Message polynomial m(x):");
disp(m_x);

g_x = x^3 + x + 1;
disp("Generator polynomial g(x):");
disp(g_x);

w_x = m_x * x^(n - k);
disp("Word polynomial w(x):");
disp(w_x);

[q, r_x] = pdiv(w_x, g_x);
disp("Remainder r(x):");
disp(r_x);

c_x = w_x + r_x;
disp("Codeword polynomial c(x):");
disp(c_x);

CS = modulo(coeff(c_x), 2);
disp("Codeword bits:");
disp(CS);

E = [1 0 0 0 0 0 0];
E_x = E(1)*x^6 + E(2)*x^5 + E(3)*x^4 + E(4)*x^3 + E(5)*x^2 + E(6)*x + E(7);
disp("Error polynomial E(x):");
disp(E_x);

R_x = c_x + E_x;
disp("Received polynomial R(x):");
disp(R_x);

[q1, r1] = pdiv(R_x, g_x);
S1 = modulo(coeff(r1), 2);
disp("Syndrome S1:");
disp(S1);

[q2, r2] = pdiv(E_x, g_x);
S2 = modulo(coeff(r2), 2);
disp("Syndrome S2:");
disp(S2);

if sum(S1) == 0 & sum(S2) == 0 then
    disp("Received code word is correct (no error)");
else
    disp("Received code word has an error");
end

CC = modulo(coeff(R_x) + coeff(E_x), 2);
disp("Corrected code word bits:");
disp(CC);
```

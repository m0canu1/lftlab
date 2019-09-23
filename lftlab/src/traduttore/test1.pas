//test test
/*prova prova*/
/*
x:=10;
print(5*5+2/2); //26
read(y);
print (x/y+2); //
while(x>0){//while(y<x){
print(x);
x:=x-1;
};
n:=2;
case
when (n>2) print(1)
when (x>0) print(2)
when (y>0) {print(y); print(3)}
else print(5);*/

read(x_1);
read(_y2);

while(x_1<>_y2){
case
when (x_1<_y2) {_y2:=_y2-x_1; print(_y2)}
else {x_1:=x_1-_y2; print(x_1)}
};
print(x_1)

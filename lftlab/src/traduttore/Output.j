 header 

 ldc 10
 istore 0
 ldc 5
 ldc 5
 imul 
 ldc 2
 ldc 2
 idiv 
 iadd 
 invokestatic Output/print(I)V
 invokestatic Output/read()I
 istore 1
 iload 0
 iload 1
 idiv 
 ldc 2
 iadd 
 invokestatic Output/print(I)V
 goto L1
L1:
 iload 0
 ldc 0
 if_icmpgt L2
 goto L3
L2:
 iload 0
 invokestatic Output/print(I)V
 iload 0
 ldc 1
 isub 
 istore 0
 goto L1
L3:
 ldc 2
 istore 2
 iload 2
 ldc 2
 if_icmpgt L4
 goto L5
L4:
 ldc 1
 invokestatic Output/print(I)V
 goto L0
L5:
 iload 0
 ldc 0
 if_icmpgt L6
 goto L7
L6:
 ldc 2
 invokestatic Output/print(I)V
 goto L0
L7:
 iload 1
 ldc 0
 if_icmpgt L8
 goto L9
L8:
 iload 1
 invokestatic Output/print(I)V
 ldc 3
 invokestatic Output/print(I)V
 goto L0
L9:
 ldc 5
 invokestatic Output/print(I)V
L0:
L0:
 return
.end method

.method public static main([Ljava/lang/String;)V
 invokestatic Output/run()V
 return
.end method


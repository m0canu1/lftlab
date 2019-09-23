 header 

 invokestatic Output/read()I
 istore 0
 invokestatic Output/read()I
 istore 1
 goto L1
L1:
 iload 0
 iload 1
 if_icmpne L2
 goto L3
L2:
 iload 0
 iload 1
 if_icmplt L4
 goto L5
L4:
 iload 1
 iload 0
 isub 
 istore 1
 iload 1
 invokestatic Output/print(I)V
 goto L1
L5:
 iload 0
 iload 1
 isub 
 istore 0
 iload 0
 invokestatic Output/print(I)V
L1:
 goto L1
L3:
 iload 0
 invokestatic Output/print(I)V
L0:
 return
.end method

.method public static main([Ljava/lang/String;)V
 invokestatic Output/run()V
 return
.end method


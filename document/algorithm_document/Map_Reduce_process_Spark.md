##Map_Reduce 过程

####1.map task处理数据量大小
1.1个map task对应了1个split，1个split的大小由split max， split min， block size三个参数起决

2.根据切分公式一般取中间值，为block size，一般一个split的大小对应block size数值

3.hadoop有默认的block size(128MB)，map task数量乘以block size就是整个job处理的数据量

4.一个split数据如果不够block size大小，则按照block size来算，所以pdf中的数据为 3 X 128MB 到 4 X 128MB

5.block块默认为3份，每份要在三个不同的节点内

####2.map task 过程

1.input split进入到map函数中处理，如果input输入一个文本，map函数调用的次数等于文本的行数

2.map函数处理完之后，会将数据放到内存缓冲区内，100MB，达到缓冲区80%时，会将数据溢写（溢写时需要缓冲区空间接收map函数的输出，所以不是100%）

3.map函数在将数据存入buffer in memory之前，会进行partition分区，分区算法：key求hash后，，对reduce的个数取模（3个reduce）

4.buffer in memory溢写的过程中，会进行sort排序（归并排序，插入排序），溢写到disk上，三次溢写，merge on disk,将溢写的结果合并

5，合并的过程中同样要进行排序（归并排序，插入排序），最终将大量的小文件合并成三大块，落地到disk内，map task结束

####3.reduce task 过程

1.每个reduce task， fetch获取一块map task的结果；每个reduce task不断拉取对应编号的处理区域数据

2.reduce task获得文件后，	将相同编号的数据，进行merge合并，同时进行sort排序(归并排序，插入排序)

3.merge合并结束后，进行group分组，reduce函数被调用次数与分了多少组相同

4.从reduce函数之前，到map函数之后，称为shuffle，洗牌

####4.影响map reduce过程效率的原因
1.map reduce过程慢，主要由于过多的disk磁盘IO

>包括map task内的溢写到磁盘，merge合并进行磁盘IO，reduce task，fetch拉取数据到磁盘，merge合并过程有大量的disk磁盘IO，reduce函数处理完之后，结束hadoop计算过程输出进行磁盘IO

2.大量的排序
>为了分区，必须要强制进行大量的排序，否则无法分组，进入reduce函数之前已经排好序了，在map task内进行sort排序，减小reduce函数之前排序的压力

>归并排序复杂度nlogn, 插入排序为n的平方

3.额外的复制，序列化，shuffle过程避免不了磁盘的IO，包括spark，虽然spark是基于内存计算
### 启动背景

想要更快地解决50万级别的数据插入

todo:

1. 整合日志框架

### 为什么选择easyexcel

poi存在问题：非常消耗内存，数据操作麻烦（workbook、row、cell）

easyexcel遇到在大的excel都不会出现内存溢出问题，此外数据读取是Java对象，操作友好，**以使用简单，节省内存著称**

### easyexcel拟解决的问题

1. excel读写时内存溢出
2. excel格式解析比较简单

### 工作原理

Excel解析过程：xlsx文件解压缩 -> SAX模式读取xml文件 -> 模型转换 -> 返回给调用者

poi ：内存 内存 workbook 全部内容

easyexcel ：磁盘 磁盘 java对象 一行一行返回对象

![img](https://img2020.cnblogs.com/blog/1905053/202005/1905053-20200514150645200-356748885.png)
